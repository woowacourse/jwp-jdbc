dependencies {
    api(project(":nextstep-jdbc"))

    implementation("org.reflections:reflections:0.9.11")

    api("com.fasterxml.jackson.core:jackson-databind:2.9.9.1")
    implementation("org.apache.ant:ant:1.10.6")

    implementation("org.apache.commons:commons-dbcp2:2.6.0")

    runtimeOnly("com.h2database:h2:1.4.199")
    runtimeOnly("mysql:mysql-connector-java:8.0.17")
}
