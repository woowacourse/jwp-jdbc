dependencies {
    api(project(":nextstep-mvc"))
    api(project(":nextstep-jdbc"))

    val springVersion = rootProject.extra.get("springVersion")
    val tomcatVersion = rootProject.extra.get("tomcatVersion")

    implementation("org.springframework:spring-jdbc:$springVersion")
    implementation("org.springframework:spring-web:$springVersion")

    implementation("org.apache.commons:commons-dbcp2:2.6.0")

    runtimeOnly("com.h2database:h2:1.4.199")
    runtimeOnly("mysql:mysql-connector-java:8.0.17")

    implementation("org.apache.tomcat.embed:tomcat-embed-core:$tomcatVersion")
    implementation("org.apache.tomcat.embed:tomcat-embed-logging-juli:8.5.2")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:$tomcatVersion")
    
    implementation("org.reflections:reflections:0.9.11")

    api("com.fasterxml.jackson.core:jackson-databind:2.9.9.1")
    implementation("org.apache.ant:ant:1.10.6")
}
