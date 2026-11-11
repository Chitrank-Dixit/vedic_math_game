package com.ank.sutrasaga.config

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Unit tests validating that the Gradle build configuration and CI pipelines
 * strictly enforce secure release signing and prevent accidental debug fallback.
 */
class ReleaseSigningConfigurationTest {

    private val rootDir: File by lazy {
        var current: File = File(".").canonicalFile
        while (current.parentFile != null && !File(current, "app/build.gradle.kts").exists()) {
            current = current.parentFile!!
        }
        current
    }

    private val buildGradleFile: File by lazy {
        File(rootDir, "app/build.gradle.kts")
    }

    private val gitignoreFile: File by lazy {
        File(rootDir, ".gitignore")
    }

    private val ciWorkflowFile: File by lazy {
        File(rootDir, ".github/workflows/android_ci.yml")
    }

    @Test
    fun releaseBuildType_doesNotFallBackToDebugSigning() {
        assertTrue("app/build.gradle.kts must exist", buildGradleFile.exists())
        val content = buildGradleFile.readText()

        // Release buildType must explicitly bind release signing config
        assertTrue(
            "Release buildType must assign release signingConfig",
            content.contains("signingConfig = signingConfigs.getByName(\"release\")")
        )

        // Must never assign debug signingConfig to release buildType
        val releaseBlockRegex = Regex("""release\s*\{([^}]+)\}""", RegexOption.DOT_MATCHES_ALL)
        val match = releaseBlockRegex.find(content)
        assertTrue("release buildType block must exist", match != null)

        val releaseBlock = match!!.groupValues[1]
        assertFalse(
            "release buildType must never assign debug signingConfig",
            releaseBlock.contains("""signingConfigs.getByName("debug")""")
        )
    }

    @Test
    fun gradleBuild_validatesRequiredSigningPropertiesOnReleaseTasks() {
        val content = buildGradleFile.readText()

        assertTrue(
            "Build must validate task graph for release artifact packaging tasks",
            content.contains("gradle.taskGraph.whenReady")
        )
        assertTrue(
            "Build must inspect bundleRelease task",
            content.contains("bundleRelease")
        )
        assertTrue(
            "Build must fail with GradleException when release signing parameters are missing",
            content.contains("RELEASE SIGNING ERROR:")
        )
        assertTrue(
            "Build must support standard keystore path property",
            content.contains("KEYSTORE_PATH")
        )
        assertTrue(
            "Build must support ANDROID_KEYSTORE_PATH fallback property",
            content.contains("ANDROID_KEYSTORE_PATH")
        )
    }

    @Test
    fun gitignore_blocksKeystoreFiles() {
        assertTrue(".gitignore must exist", gitignoreFile.exists())
        val content = gitignoreFile.readText()

        assertTrue(
            ".gitignore must ignore .jks files",
            content.lines().any { it.trim() == "*.jks" }
        )
        assertTrue(
            ".gitignore must ignore .keystore files",
            content.lines().any { it.trim() == "*.keystore" }
        )
    }

    @Test
    fun ciWorkflow_usesSecureReleaseSigningWithoutExposingSecrets() {
        assertTrue("android_ci.yml must exist", ciWorkflowFile.exists())
        val content = ciWorkflowFile.readText()

        assertTrue(
            "CI must have a release job for signing artifacts",
            content.contains("Build & Sign Release Bundle")
        )
        assertTrue(
            "CI release job must require base64 keystore secret",
            content.contains("secrets.RELEASE_KEYSTORE_BASE64")
        )
        assertTrue(
            "CI release job must clean up temporary keystore file",
            content.contains("Secure Cleanup of Keystore")
        )

        // Ensure secrets are never echoed in workflow commands
        assertFalse("CI must not echo keystore passwords", content.contains("echo \${{ secrets.RELEASE_KEYSTORE_PASSWORD }}"))
        assertFalse("CI must not echo key passwords", content.contains("echo \${{ secrets.RELEASE_KEY_PASSWORD }}"))
    }

    @Test
    fun ciWorkflow_validatesProductionReleaseArtifactAndBuildsAab() {
        assertTrue("android_ci.yml must exist", ciWorkflowFile.exists())
        val content = ciWorkflowFile.readText()

        // Debug job must run tests, lint, and assembleDebug
        assertTrue("CI debug job must execute testDebugUnitTest", content.contains("testDebugUnitTest"))
        assertTrue("CI debug job must execute lintDebug", content.contains("lintDebug"))
        assertTrue("CI debug job must execute assembleDebug", content.contains("assembleDebug"))

        // Release job must run lintRelease and bundleRelease
        assertTrue("CI release job must execute lintRelease", content.contains("lintRelease"))
        assertTrue("CI release job must execute bundleRelease", content.contains("bundleRelease"))

        // Must perform comprehensive artifact verification on the produced AAB
        assertTrue("CI must have a Verify Release Artifact step", content.contains("Verify Release Artifact"))
        assertTrue("CI verification must check AAB existence", content.contains("app-release.aab"))
        assertTrue("CI verification must check archive integrity with unzip", content.contains("unzip -t"))
        assertTrue("CI verification must check base AndroidManifest", content.contains("base/manifest/AndroidManifest.xml"))
        assertTrue("CI verification must check classes.dex", content.contains("base/dex/classes.dex"))
        assertTrue("CI verification must check resources.pb", content.contains("base/resources.pb"))
        assertTrue("CI verification must check signature using jarsigner", content.contains("jarsigner -verify"))
        assertTrue("CI verification must enforce applicationId", content.contains("com.ank.sutrasaga"))

        // Must upload the release AAB as an artifact
        assertTrue("CI must upload release bundle artifact", content.contains("sutrasaga-release-aab"))

        // Must NOT publish to Google Play automatically
        assertFalse("CI must not automatically publish to Google Play", content.contains("r0adkll/upload-google-play"))
        assertFalse("CI must not contain play store credentials", content.contains("PLAY_CONFIG_JSON"))
        assertFalse("CI must not contain service account key", content.contains("SERVICE_ACCOUNT_JSON"))
    }
}

