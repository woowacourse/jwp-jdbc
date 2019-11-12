dependencies {
    implementation("org.reflections:reflections:0.9.11")
    api(project(":nextstep-jdbc"))
    api(project(":nextstep-mvc"))

    api("com.fasterxml.jackson.core:jackson-databind:2.9.9.1")
    implementation("org.apache.ant:ant:1.10.6")
}
