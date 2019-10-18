plugins {
    idea
}

idea {
    module {
        inheritOutputDirs = false
        outputDir = file("webapp/WEB-INF/classes")
    }
}

dependencies {
    api(project(":nextstep-mvc"))
    api(project(":nextstep-jdbc"))

    val springVersion = rootProject.extra.get("springVersion")
    val tomcatVersion = rootProject.extra.get("tomcatVersion")
    implementation("org.springframework:spring-jdbc:$springVersion")
    implementation("org.springframework:spring-web:$springVersion")
    
    implementation("org.apache.tomcat.embed:tomcat-embed-core:$tomcatVersion")
    implementation("org.apache.tomcat.embed:tomcat-embed-logging-juli:8.5.2")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:$tomcatVersion")
}
