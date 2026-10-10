plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "io.github.adamian.pagelin"

    compileSdk = 36

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "io.github.adamian-code"
                artifactId = "pagelin"
                version = "1.0.3"

                pom {
                    name.set("Pagelin")
                    description.set("A Compose-first PDF generation library for Android.")
                    url.set("https://github.com/adamian-code/pagelin")

                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("adamian-code")
                            name.set("Alexandros Damianakis")
                            url.set("https://github.com/adamian-code")
                        }
                    }

                    scm {
                        url.set("https://github.com/adamian-code/pagelin")
                        connection.set("scm:git:git://github.com/adamian-code/pagelin.git")
                        developerConnection.set("scm:git:ssh://git@github.com/adamian-code/pagelin.git")
                    }
                }
            }
        }
        repositories {
            maven {
                name = "Bundle"
                url = uri(layout.buildDirectory.dir("central-bundle"))
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey"),
        System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword")
    )

    sign(publishing.publications)
}
