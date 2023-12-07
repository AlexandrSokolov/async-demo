#!/usr/bin/env bash

# run this script from the root project but not from `scripts` folder
docker container rm sav-dev-app && \
  docker image rm sav-dev-demo-spring-async:latest && \
  mvn clean install && \
  docker compose up
