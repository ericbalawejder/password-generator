#!/bin/bash

# Gradle commands, upon completion, cause the shell session to exit. The commands
# must be in one long string. This behavior happens in a HereDoc and a shell script.
# echo 'starting gradle'
# ./gradlew clean build
# echo 'finished' <- never gets executed

jar='/home/password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar'
owner='password-generator-user'

ssh droplet "cd /home/password-generator; \
git pull; \
./gradlew clean build; \
chown $owner:$owner $jar; \
chmod 500 $jar; \
systemctl restart password-generator; \
echo 'finished deployment!'"
