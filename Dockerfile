FROM java:8

# Copy the source.
COPY . /source
WORKDIR /source

# Build.
ENV GRADLE_USER_HOME /source/.gradle
RUN ./gradlew build

# Make app dir and copy the artifact.
RUN mkdir -p /opt/api-demo
RUN cp build/libs/api-demo.jar /opt/api-demo/api-demo.jar

# Remove sources.
RUN rm -rf /source

# Start the service.
WORKDIR /opt/api-demo

EXPOSE 4567

ENTRYPOINT java -jar api-demo.jar