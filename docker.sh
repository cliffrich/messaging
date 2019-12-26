#!/bin/sh

set -e

[[ -z "$DEBUG" ]] || set -x

build_jar() {
  mvn clean install -DskipTests
}

build_docker_image() {
    echo -e "\e[31m===>\e[37m Building docker image for 'messaging' application...\e[0m"
    docker build . -t messaging:latest
}

run_application_via_docker() {
    echo -e "\e[31m===>\e[37m Running 'messaging' application via docker\e[0m"
    docker-compose up -d
}

show_application_logs_if_requested() {
    if [[ "$1" == "logs" ]]; then
        echo -e "\e[31m===>\e[37m Showing logs for docker container\e[0m"
        docker-compose logs -f messaging
    fi
}

main() {
  echo -e "\e[31m===>\e[37m Building application and docker image...\e[0m"
  build_jar
  build_docker_image
#  run_application_via_docker
  show_application_logs_if_requested "$1"
}
#Calling the above function
main "$1"