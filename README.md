### Password Generator
Create a policy to generate a random password. Built with Spring Boot and Thymeleaf.

### How to run:
To run locally:
```
$ cd password-generator
$ ./gradlew bootRun
```
visit:<br>
http://localhost:8080/password

### Deployment process:
Using a [Digital Ocean Droplet](https://www.digitalocean.com/products/droplets/):

Install Java
```
$ apt install openjdk-11-jre-headless
```

Install [Caddy](https://caddyserver.com/docs/install)
```
$ sudo apt install -y debian-keyring debian-archive-keyring apt-transport-https
$ curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/gpg.key' | sudo tee /etc/apt/trusted.gpg.d/caddy-stable.asc
$ curl -1sLf 'https://dl.cloudsmith.io/public/caddy/stable/debian.deb.txt' | sudo tee /etc/apt/sources.list.d/caddy-stable.list
$ sudo apt update
$ sudo apt install caddy
```
Installing this package automatically starts and runs Caddy as a systemd service named caddy, and also comes 
with a caddy-api service which is not enabled by default, should you need it. Digital Ocean also offers this 
as a preinstalled [service](https://marketplace.digitalocean.com/apps/caddy).

Configure caddy file:
```
$ nano /etc/caddy/Caddyfile
```
```
# The Caddyfile is an easy way to configure your Caddy web server.
#
# Unless the file starts with a global options block, the first
# uncommented line is always the address of your site.
#
# To use your own domain name (with automatic HTTPS), first make
# sure your domain's A/AAAA DNS records are properly pointed to
# this machine's public IP, then replace ":80" below with your
# domain name.

droplet.ericbalawejder.com {
        # Another common task is to set up a reverse proxy:
        reverse_proxy localhost:8080

        # Or serve a PHP site through php-fpm:
        # php_fastcgi localhost:9000
}

# Refer to the Caddy docs for more information:
# https://caddyserver.com/docs/caddyfile
```
It will keep your certificates renewed while it serves your sites, and it even redirects HTTP requests to HTTPS.<br>
Certs are located: `$ /var/lib/caddy/.local/share/caddy/`

Point your domain's A/AAAA records at your droplet's IP address(es).
droplet
Visit: droplet.ericbalawejder.com/password

Clone spring boot app onto droplet
```
$ git clone https://github.com/ericbalawejder/password-generator.git
```
Edit PasswordController.java `@CrossOrigin()`

password.html url:
url: https://droplet.ericbalawejder.com/show


#### Systemd service
The application needs to be packaged as a jar file. Gradle can handle that for us by running the following
command in the project root directory:
```
$ ./gradlew bootJar
```
Running the application can now be achieved directly from the jar:
```
$ java -jar build/libs/password-generator-0.0.1-SNAPSHOT.jar 
```
Create a user to run the service:
```
$ sudo useradd password-generator-user
```
We are changing the owner of the deployed file (via CI / CD or direct upload) to be the just created 
user: password-generator-user.
```
$ sudo chown password-generator-user:password-generator-user password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar
```
As a last part, we are allowing that user to read and execute the deployed file, but totally withdraw 
access to the deployed jar from the group or anyone else on the system.
```
$ sudo chmod 500 password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar
```
This way access to the system is restricted if someone is able to access the file system through our 
deployed application.

For the systemd service setup, we need to create a script named `password-generator.service` using the 
following example and put it in /etc/systemd/system directory:

nano etc/systemd/system/password-generator.service
```
[Unit]
Description=A Spring Boot Application for Password Generation
After=syslog.target

[Service]
User=password-generator-user
ExecStart=/root/password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```
To enable our configuration, we can execute:
```
$ sudo systemctl enable password-generator.service
Created symlink /etc/systemd/system/multi-user.target.wants/password-generator.service → /etc/systemd/system/password-generator.service.
```
We should be able to check the status of our application with the following command:
```
$ sudo systemctl status password-generator.service
```


password-generator.service has default permissions of 644. Changed them to 744...tried 777?
```
$ /etc/systemd/system# chmod 744 password-generator.service
```



If changes are made to the service file, we will get a warning to reload the daemon.
```
$ systemctl daemon-reload
```


#### TODO:
return character array
use jQuery to iterate through array for display

Place protections on policy:
handle spring boot exceptions in controller
backend
form

Explicit special symbols in generator algorithm
place instructions on form of /password

escape html special characters for display
html and css for form design
