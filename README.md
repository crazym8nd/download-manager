### Guide to run app
Here is [API Documentation](src/main/resources/api-docs.yaml)

To run this application you need to install docker (https://docs.docker.com/get-docker/).
1. Run docker.
2. Clone the repository to your local machine.
3. Run docker compose up from root directory.
You can also use this application with your existing AWS S3 account, to do that - you need to change credentials in [application.yaml](src/main/resources/application.yaml)

If something goes wrong:
1. Run docker-compose up -d localstack. ( manually creating localstack for s3 storage)
2. Then run ./gradlew test
Those tests can show you what is wrong with app.

### Project goals
Implement REST API with
* interaction with Amazon AWS S3 file storage
* allows user to upload files to S3
* provides user with the ability to view history of uploads
* requires JWT
* provides role-based access

### Requirements
Interaction with AWS S3 must be implemented using AWS SDK v2. Application must be Docker-ready.
The following entities with mandatory properties must be used:
* File: String name, String location, Status status
* User: String username, Status status, List<Event> events
* Event: User user, File file, Status status

Access levels and rights:
* USER: upload file, read history of own uploads only, read own user data only
* MODERATOR: USER + read/edit/delete all events, read/edit/delete all files
* ADMIN: full access

### Tech. stack

* Java 17
* Gradle
* Spring
    * Boot
    * Security
    * Reactive Data
    * WebFlux
* MySQL
* Flyway
* AWS SDK
* Amazon AWS S3
* Docker
* LocalStack


