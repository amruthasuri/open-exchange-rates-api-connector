# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.fx-data-api.connectors.openexchagerates' is invalid and this project uses 'com.fx_data_api.connectors.openexchagerates' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.1/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.1/maven-plugin/build-image.html)
* [Spring Integration Test Module Reference Guide](https://docs.spring.io/spring-integration/reference/testing.html)
* [Spring Integration Apache Kafka Module Reference Guide](https://docs.spring.io/spring-integration/reference/kafka.html)
* [Spring Integration](https://docs.spring.io/spring-boot/3.4.1/reference/messaging/spring-integration.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.4.1/reference/actuator/index.html)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/3.4.1/reference/messaging/kafka.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Integrating Data](https://spring.io/guides/gs/integration/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

