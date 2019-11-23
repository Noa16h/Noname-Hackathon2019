plugins {
    id("application")
    id("java")
}

group = "noname"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.bytedeco:javacv-platform:1.5.2")
}

application {
    mainClassName = "noname.Application"
}

java {
    sourceCompatibility = JavaVersion.VERSION_13
}