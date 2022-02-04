#!/bin/sh
set -e
echo "-------------Starting the expert mock app-------------"
echo $PWD
cd ./application-quote-and-buy-expert/
nohup mvn spring-boot:run -Dspring-boot.run.profiles=mock -Dspring-boot.run.arguments=--server.port=8080 &
spring_boot_pid=$!
echo $spring_boot_pid
echo "sleep 20 seconds"
sleep 20
#kill -9 $spring_boot_pid
echo "-------------Starting the integration tests on mock-------------"
cd ./../integration-tests/quote-and-buy-expert/
echo "changed folder to integration-tests----------------"
mvn verify -P integration-tests -DPROPERTIES_FILE_PATH=src/test/resources/application-local-mock.properties
kill -9 $spring_boot_pid || echo "failed to kill $spring_boot_pid"
echo "stopped"
cd ./../../
#echo "sleep 10 seconds"
#sleep 10
#echo "-------------Starting the expert app-------------"
#echo $PWD
#cd ./application-quote-and-buy-expert
#nohup mvn spring-boot:run -Dspring-boot.run.profiles=jenkins -Dspring-boot.run.arguments=--server.port=8080 &
#spring_boot_pid=$!
#echo $spring_boot_pid
#echo "sleep 10 seconds"
#sleep 10
##kill -9 $spring_boot_pid
#echo "-------------Starting the integration tests-------------"
#cd ./../integration-tests/quote-and-buy-expert/
#echo "changed folder to integration-tests----------------"
#mvn verify -P integration-tests -DPROPERTIES_FILE_PATH=src/test/resources/application-local.properties
#kill -9 $spring_boot_pid || echo "failed to kill $spring_boot_pid"
#echo "stopped"
#cd ./../../
