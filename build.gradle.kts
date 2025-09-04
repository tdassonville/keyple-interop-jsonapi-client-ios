///////////////////////////////////////////////////////////////////////////////
// GRADLE CONFIGURATION
///////////////////////////////////////////////////////////////////////////////

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
  signing
  `maven-publish`
}

tasks {
  spotless {
    kotlinGradle {
      target("**/*.kts")
      ktfmt()
    }
  }
}

kotlin {
  if (System.getProperty("os.name").lowercase().contains("mac")) {
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
      iosTarget.binaries.framework {
        baseName = rootProject.name
        isStatic = true
        export(libs.keypleInteropJsonapiClientKmpLib)
        export(libs.keypleInteropLocalreaderNfcmobileKmpLib)
        // Configure Info.plist
        binaryOption("bundleId", rootProject.name)
        binaryOption("bundleVersion", "2")
        binaryOption("bundleShortVersionString", version.toString())
      }
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.kotlin.stdlib)
        implementation(libs.napier)
        implementation(libs.keypleInteropJsonapiClientKmpLib)
        implementation(libs.keypleInteropLocalreaderNfcmobileKmpLib)
      }
    }

    if (System.getProperty("os.name").lowercase().contains("mac")) {
      iosMain {
        dependencies {
          api(libs.keypleInteropJsonapiClientKmpLib)
          api(libs.keypleInteropLocalreaderNfcmobileKmpLib)
        }
      }
    }
  }

}