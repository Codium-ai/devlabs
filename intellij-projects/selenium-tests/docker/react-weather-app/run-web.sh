#!/bin/bash


docker run --name weather-example -d -p 4000:4000 -v /Users/davidparry/code/devlabs/intellij-projects/selenium-tests/docker/react-weather-app:/usr/src/app -w /usr/src/app node:18-buster-slim tail -f /dev/null

docker exec -it weather-example sh
