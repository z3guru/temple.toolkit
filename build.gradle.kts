plugins {
    id("java-library") // 'java' 대신 'java-library'를 사용하면 API와 구현을 더 잘 관리할 수 있습니다.
    id("maven-publish")
    id("signing")
}

group = "guru.z3"
version = "0.1.2"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.apache.logging.log4j:log4j-core:2.25.4")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "guru.z3"
            artifactId = "toolkit"
            version = project.version.toString()

            pom {
                name.set("toolkit")
                description.set("A concise description of your library.")
                url.set("https://github.com/your-username/your-repo") // TODO: Update with your project's URL

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("z3guru")
                        name.set("ZCUBE LC")
                        email.set("zeze@z3.guru")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/z3guru/temple.toolkit.git")
                    developerConnection.set("scm:git:ssh://git@github.com:z3guru/temple.toolkit.git")
                    url.set("https://github.com/z3guru/temple.toolkit")
                }
            }
        }
    }

    repositories {
        maven {
//            name = "sonatype"
//            // URL을 새로운 Central Portal 방식의 릴리스 저장소 주소로 변경합니다.
//            url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
            name = "MavenCentral"
            // URL을 새로운 Central Portal 방식의 릴리스 저장소 주소로 변경합니다.
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            //url = uri("https://central.sonatype.com/api/v1/publisher/deployments/maven")
            credentials {
                username = project.findProperty("ossrhUsername") as String?
                password = project.findProperty("ossrhPassword") as String?
            }
        }
    }
}

signing {
    // gradle.properties 또는 환경 변수에 GPG 키 정보가 설정되어 있어야 합니다.
    // (signing.keyId, signing.password, signing.secretKeyRingFile)
    sign(publishing.publications["mavenJava"])
}

tasks.register<Zip>("makeBundle") {
    dependsOn("publishMavenJavaPublicationToMavenLocal") // 로컬 빌드 선행

    // 1. ZIP 파일 이름을 'bundle.zip'으로 설정
    archiveFileName.set("bundle.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))

    // 2. 중요: Central Portal이 요구하는 '정확한 폴더 구조'대로 ZIP 내부에 배치
    // 발급받은 namespace의 점(.)을 슬래시(/)로 바꿉니다. (예: com.example -> com/example)
    val packagePath = group.toString().replace(".", "/")
    val artifactId = "toolkit" // 내 프로젝트 이름(artifact id)
    val versionStr = version.toString()

    // 3. .m2 로컬 저장소에 생성된 파일들을 올바른 구조로 복사
    from(file("${System.getProperty("user.home")}/.m2/repository/$packagePath/$artifactId/$versionStr")) {
        into("$packagePath/$artifactId/$versionStr")
    }
}