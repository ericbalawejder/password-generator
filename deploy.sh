#!/bin/bash

#ssh droplet "cd /home/password-generator; git pull; ./gradlew build; chown password-generator-user:password-generator-user /home/password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar; chmod 500 /home/password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar; systemctl restart password-generator"

jar='/home/password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar'
app_root='/home/password-generator'
build='./gradlew build'
change_owner='chown password-generator-user:password-generator-user'
change_permission='chmod 500'
restart_systemd='systemctl restart password-generator'

#ssh root@192.168.1.1 'bash -s' < script.sh


#ssh droplet << EOF
  #cd $app_root;
  #git pull;
  #$build;
  #$change_permission $jar;
  #$restart_systemd;
#EOF

ssh droplet << EOF
  set +x;
  cd /home/energy-server;
  pwd;
  git pull;
  ./gradlew build
  $change_permission $jar;
  echo 'finished!';
EOF
