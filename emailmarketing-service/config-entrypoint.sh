#!/bin/sh
#while ! nc -z config-server 8888 ; do
#    echo "Waiting for upcoming Config Server"
#    sleep 2
#done
#while ! nc -z registry-service 8761 ; do
#    echo "Waiting for upcoming Registry Server"
#    sleep 2
#done
#while ! nc -z gateway-service 8083 ; do
#    echo "Waiting for upcoming Gateway Server"
#    sleep 2
#done
java -jar app.jar