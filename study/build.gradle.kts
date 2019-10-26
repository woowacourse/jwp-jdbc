dependencies {
    api(project(":nextstep-jdbc"))

    implementation("org.reflections:reflections:0.9.11")

    api("com.fasterxml.jackson.core:jackson-databind:2.9.9.1")
    implementation("org.apache.ant:ant:1.10.6")

    testImplementation("org.apache.commons:commons-dbcp2:2.6.0")
    testImplementation("org.junit.platform:junit-platform-launcher:1.5.1")
    testImplementation("mysql:mysql-connector-java:8.0.17")
}
