## Password Generator
Create a random password to match a given policy. Built with Spring Boot.

### How to run:
To run locally:
```
$ ./gradlew clean build
$ ./gradlew bootRun
```
visit: http://localhost:8080/password

To run the tests:
```
$ ./gradlew test
```

### Deployment process:
Using a [Digital Ocean Droplet](https://www.digitalocean.com/products/droplets/):

##### Create a subdomain
Point your domain's A/AAAA records at your droplet's IP address(es).<br>
Name: `droplet`

[Install Java](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-20-04)
on the droplet.
```
$ sudo apt install openjdk-11-jre-headless
```
Verify
```
$ java -version
```
You may need the Java Development Kit (JDK) in addition to the JRE in order to compile and run some specific 
Java-based software. To install the JDK, execute the following command, which will also install the JRE:
```
$ sudo apt install default-jre
```
Verify:
```
$ javac -version
```
Setting the `JAVA_HOME` Environment Variable.
```
$ sudo update-alternatives --config java

There is only one alternative in link group java (providing /usr/bin/java): /usr/lib/jvm/java-11-openjdk-amd64/bin/java
Nothing to configure.
```
Copy the path from your preferred installation. Then open `/etc/environment`:
```
$ sudo nano /etc/environment
```
At the end of this file, add the following line, making sure to replace the highlighted path with your own copied 
path, but do not include the `bin/` portion of the path:
```
JAVA_HOME="/usr/lib/jvm/java-11-openjdk-amd64"
```
Now reload this file to apply the changes to your current session:
```
$ source /etc/environment
```
Verify:
```
$ echo $JAVA_HOME
```
Other users will need to execute the command source /etc/environment or log out and log back in to apply this setting.

##### Web server
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
$ sudo nano /etc/caddy/Caddyfile
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
Caddy will keep your certificates renewed while it serves your sites, and it even redirects HTTP requests to HTTPS.<br>
Certs are located: `$ /var/lib/caddy/.local/share/caddy/`

Visit droplet.ericbalawejder.com/password to verify installation.

##### Clone spring boot app onto droplet
```
$ cd home/
$ git clone https://github.com/ericbalawejder/password-generator.git
```
Edit the `@CrossOrigin()` annotation arguments in `PasswordController.java` on the `showPassword()` method.
We will be accessing the app from `"https://ericbalawejder.com"` and from the droplet machine
`"https://droplet.ericbalawejder.com"` for testing. This is to allow access to the application 
running on our droplet from a foreign server or else we will get `blocked by CORS policy` error.
```java
    @CrossOrigin(origins = {"https://ericbalawejder.com", "https://droplet.ericbalawejder.com"})
    @PostMapping(path = "/show", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordResponse> showPassword(
            @ModelAttribute("generator") PasswordGenerator generator) {
        final PasswordResponse passwordResponse = new PasswordResponse(generator);

        return new ResponseEntity<>(passwordResponse, HttpStatus.OK);
    }
```

#### Systemd service
The application needs to be packaged as a jar file. Gradle can handle that for us by running the following
command in the project root directory:
```
$ ./gradlew clean build
```
Running the application can now be achieved directly from the jar:
```
$ java -jar build/libs/password-generator-0.0.1-SNAPSHOT.jar 
```
Create a user to run the service:
```
$ sudo useradd password-generator-user
```
Check that the user was created:
```
$ getent passwd | grep password-generator-user
password-generator-user:x:1000:1000::/home/password-generator-user:/bin/sh
```
We are changing the owner of the deployed file to be the just created user: password-generator-user.
```
$ sudo chown password-generator-user:password-generator-user /home/password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar
```
As a last part, we are allowing that user to read and execute the deployed file, but totally withdraw 
access to the deployed jar from the group or anyone else on the system.
```
$ sudo chmod 500 /home/password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar
```
This way access to the system is restricted if someone is able to access the file system through our 
deployed application.

For the systemd service setup, we need to create a script named `password-generator.service` and put
it in `/etc/systemd/system` directory:

```
$ nano etc/systemd/system/password-generator.service
```

```
[Unit]
Description=A Spring Boot Application for Password Generation
After=syslog.target

[Service]
Type=simple
User=password-generator-user
ExecStart=/usr/bin/java -jar /home/password-generator/build/libs/password-generator-0.0.1-SNAPSHOT.jar
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
$ sudo systemctl status password-generator
```
To stop and start the service:
```
$ sudo systemctl stop password-generator
$ sudo systemctl start password-generator
```
If changes are made to the service file, we will get a warning to reload the daemon.
```
$ systemctl daemon-reload
```

#### TODO:
* Logs

* Return character array and use jQuery to iterate through array for display.

* Make field hidden until response is returned.

* Check for known bad passwords using:
  * https://haveibeenpwned.com/Passwords

* [Handle form field BindException properly](https://stackoverflow.com/questions/48786173/spring-boot-handle-exception-wrapped-with-bindexception).
