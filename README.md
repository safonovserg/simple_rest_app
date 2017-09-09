# simple_rest_app
It is REST Java app that is packaged in executable jar.

Used stack: jdk 8, Jersey 2, embedded Tomcat 8, Guice 4, Gson 2

To run it execute `mvn compile package`

next go to target dir and run `java -jar money-app-1.0-SNAPSHOT.jar`

(optionally you could specify  `-Dport=12345`, default is 8080)