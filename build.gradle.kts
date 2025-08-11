plugins {
  `java-library`
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "xyz.jungha.job"
version = "1.0.5"

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
  mavenCentral()
  maven {
    url = uri("https://jitpack.io")
  }
  maven {
    url = uri("https://repo.papermc.io/repository/maven-public/")
  }
  maven {
    url = uri("https://repo.momirealms.net/releases/")
  }
}


dependencies {
  compileOnly("org.projectlombok:lombok:1.18.30")
  annotationProcessor("org.projectlombok:lombok:1.18.30")

  compileOnly("net.momirealms:custom-crops:3.6.40")
  compileOnly("net.momirealms:custom-fishing:2.3.12")

  compileOnlyApi(files("libs/AddCook-3.7.8.jar"))
  compileOnlyApi(files("libs/AddCrop-3.4.9.jar"))

  compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks {
  compileJava {
    options.release = 21
  }

  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }

  shadowJar {
    archiveFileName.set("Job-${version}.jar")
    destinationDirectory.set(File("C:\\Users\\sjh05\\Documents\\서버\\1.21.4\\plugins"))
  }

  build {
    dependsOn(shadowJar)
  }
}
