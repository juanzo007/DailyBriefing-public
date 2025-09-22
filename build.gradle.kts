/*
 * Root build.gradle.kts
 * Plays a sound after successful builds on Windows (PowerShell).
 */

val buildSuccessSound = tasks.register(
    "buildSuccessSound",
    org.gradle.api.tasks.Exec::class
) {
    doFirst { println("✅ Build complete — pumping INXS low end now...") }
    commandLine("powershell", "-c", "[console]::Beep(100,500)")
}

listOf(
    "build",
    "assemble",
    "assembleDebug",
    "assembleRelease",
    "bundleDebug",
    "bundleRelease",
    "test",
    "testDebugUnitTest",
    "testReleaseUnitTest"
).forEach { t ->
    tasks.matching { it.name == t }.configureEach {
        finalizedBy(buildSuccessSound)
    }
}
