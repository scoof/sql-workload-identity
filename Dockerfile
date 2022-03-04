FROM maven:3.8.4-jdk-11 as builder
WORKDIR /app
COPY pom.xml .
RUN mvn -e -B dependency:resolve
COPY src ./src
RUN mvn -e -B package

FROM gcr.io/distroless/java:11-nonroot
COPY --from=builder /app/target/msal-java-1.0-SNAPSHOT.jar /app.jar
# Kubernetes runAsNonRoot requires USER to be numeric
USER 65532:65532
CMD ["/app.jar"]
