plugins {
    kotlin("jvm") version "1.4.21"
}

group = "brys.dev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.dv8tion:JDA:4.2.0_227")
    implementation("com.sedmelluq:lavaplayer:1.3.66")
    implementation("log4j:log4j:1.2.17")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.google.apis:google-api-services-youtube:v3-rev212-1.25.0")
}
