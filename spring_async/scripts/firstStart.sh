#!/usr/bin/env bash

# run this script from the root project but not from `scripts` folder
  mvn clean install && \
  docker compose up
