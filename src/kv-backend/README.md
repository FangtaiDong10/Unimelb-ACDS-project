# KV-Backend

This is the backend of Project VIOLET.

## Requirements

- JDK8
- Maven

## Config

See **kv-backend/src/main/resources/application-prod.yaml**

## Build

```shell
$ cd kv-backend
$ mvn clean package -Dmaven.test.skip=true
$ cd target
```

The name of the generated jar file is **kv-backend-{version}.jar**

## Deploy

```shell
$ java -jar kv-backend-{version}.jar --spring.profiles.active=prod
```
