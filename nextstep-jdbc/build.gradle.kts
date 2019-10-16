dependencies {
    val springVersion = rootProject.extra.get("springVersion")

    testImplementation("org.springframework:spring-jdbc:$springVersion")
    testImplementation("com.h2database:h2:1.4.199")
    testImplementation("org.apache.commons:commons-dbcp2:2.6.0")
}
