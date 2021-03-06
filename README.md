# Demo API ![GitHub Logo](https://travis-ci.org/mrolla/demo-api.svg?branch=master) [![Docker Repository on Quay](https://quay.io/repository/mrolla/demo-api/status "Docker Repository on Quay")](https://quay.io/repository/mrolla/demo-api)

## Building from Source
The project uses a [Gradle][]-based build system. In the instructions
below, [`./gradlew`][] is invoked from the root of the source tree and serves as
a cross-platform, self-contained bootstrap mechanism for the build.

### Prerequisites

[Git][] and [Java JDK 8][JDK8 build]

Be sure that your `JAVA_HOME` environment variable points to the `jdk1.8.0` folder
extracted from the JDK download.

Also make sure you are using the correct java version by issuing in a terminal the following
command

    java -version

### Build everything
    ./gradlew build

### Run the test suite
    ./gradlew test

### Run
    ./gradlew run

... and discover more commands with `./gradlew tasks`.

### Run the Docker service
Be sure to have [Docker][] installed and then simply issue:

    docker run -d --name demo-api -p 4567:4567 quay.io/mrolla/demo-api

[Docker]: https://www.docker.com/
[Gradle]: http://gradle.org
[`./gradlew`]: http://vimeo.com/34436402
[Git]: http://help.github.com/set-up-git-redirect
[JDK8 build]: http://www.oracle.com/technetwork/java/javase/downloads