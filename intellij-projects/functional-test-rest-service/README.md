# Spring Boot Functional Testing

## Overview

This Java Spring Boot project serves as a live coding demonstration for showcasing the capabilities of Codiumate
plugin in Functional Testing a OpenAPI 3.0.1 Restful Service.

## Features



## Usage

1. Clone this repository to your local development environment.

   ```shell
   git clone https://github.com/yourusername/devlabs.git
   ```

2. Set up your development environment with Codiumate and the necessary Java development tools.
3. Demonstrate the endpoints for Profile component:
      ``` 
      curl --location 'http://localhost:8585/profile/1234' 
      ```
      ``` 
      curl --location 'http://localhost:8585/profile/' --header 'Content-Type: application/json' --data '{"id":1,"name":"Mike Meyers"}' 
      ```
      ``` 
      curl --location 'http://localhost:8585/profile/users' 
      ```
4. Focus on Application.java
   - Add the api-docs.json to the additional context
   - Add application.properties
   - FunctionalSpec.groovy
   - ProfileFunctionalSpec.groovy
   
5. Prompt:
   ```
   Add to this Spock Framework integration tests in ProfileFunctionalSpec extending the FunctionalSpec using the "when-where-then" format of the testing DSL. The tests should interact with a running
   API application that is described in this OAS 3.0 standard in api-docs.json file , sending requests with dynamic data specified in the "where" data table.
   ```

## Resources
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Martin Fowloer Refactoring](https://refactoring.com)
- [Spockframework](https://spockframework.org)


