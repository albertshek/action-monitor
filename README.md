## Getting Started
The application allow two users to communicate between themselves pushing the messages to the destination user. All the messages are stored in database.

## Pre Requirements
* IntelliJ IDE
* Maven
* JDK 17
* Docker
* Docker Compose

## How to Run
1. Clone the project and open it with IntelliJ
2. Navigate to the project folder and run the Active MQ, Postgres and PG Admin by executing the command docker compose up -d
3. Create Database with below command:

* docker exec -it postgres bash
* psql -U admin
* create database action_monitor;

4. Run the class ActionMonitorApplication
5. Access the Client page at http://localhost:8081


## How to Test
1. Open 2 client pages at http://localhost:8081
2. In Client page #1, input WebSocket Id (e.g. Client001) and connect
3. In Client page #2, input WebSocket Id (e.g. Client002) and connect
4. In Client page #1, input User id (e.g. Client002) and input Text Message (e.g. Hello Client002). The Text Message could be displayed in Client page #2.
5. In Client page #2, input User id (e.g. Client001) and input Text Message (e.g. Hello Client001). The Text Message could be displayed in Client page #1.


## Test Stretegy
In this project, classic and mockist unit test strategy have been used.

When testing application layer classes (e.g. WebSocketService) which do more coordinating work and behavior verification , mocking is needed (e.g. repository) and mockist strategy is used.

When testing other classes (e.g. WebSocketChannelInterceptor) which is more on state or expected value verification , classic strategy is used.