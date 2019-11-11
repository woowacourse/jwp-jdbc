dependencies {
    val springVersion = rootProject.extra.get("springVersion")

    implementation("org.springframework:spring-jdbc:$springVersion")

    implementation("org.apache.commons:commons-dbcp2:2.6.0")
}
