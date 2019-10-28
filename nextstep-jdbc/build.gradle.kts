dependencies {
    val springVersion = rootProject.extra.get("springVersion")
    val tomcatVersion = rootProject.extra.get("tomcatVersion")

    implementation("org.apache.commons:commons-dbcp2:2.6.0")

    testImplementation("org.springframework:spring-jdbc:$springVersion")
    testImplementation("org.springframework:spring-web:$springVersion")

    runtimeOnly("com.h2database:h2:1.4.199")
    testImplementation("org.apache.commons:commons-dbcp2:2.6.0")
}
