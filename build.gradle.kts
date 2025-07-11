plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "xyz.jungha.job"
version = "1.0.0"

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
  mavenCentral()
  maven {
    url = uri("https://jitpack.io")
    url = uri("https://repo.extendedclip.com/releases/")
  }
}

dependencies {
  compileOnly("org.projectlombok:lombok:1.18.30")
  annotationProcessor("org.projectlombok:lombok:1.18.30")

  compileOnly("me.clip:placeholderapi:2.11.6")

  paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

tasks {
  compileJava {
    options.release = 21
  }

  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }

  shadowJar {
    archiveFileName.set("Job.jar")
    destinationDirectory.set(File("C:\\Users\\sjh05\\Documents\\서버\\1.21.4\\plugins"))
  }

  build {
    dependsOn(shadowJar)
  }
}
