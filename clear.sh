#!/bin/sh

set -e

main() {
#    remove_dangling_images
    remove_messaging_images
    remove_volumes
}

remove_dangling_images() {
    if [[ "$(docker images --filter dangling=true -q | wc -l)" != "0" ]]; then
        echo -e "\e[31m===>\e[37m Removing dangling images...\e[0m"
        docker rmi $(docker images --filter dangling=true -q)
    fi
}

remove_messaging_images() {
    if [[ "$(docker images messaging -q | wc -l)" != "0" ]]; then
        echo -e "\e[31m===>\e[37m Removing messaging images...\e[0m"
        docker rmi -f $(docker images messaging -q)
    fi
}

remove_volumes() {
    if [[ "$(docker volume ls -q | wc -l)" != "0" ]]; then
        echo -e "\e[31m===>\e[37m Removing volumes...\e[0m"
        docker volume rm $(docker volume ls -q)
    fi
}

main
