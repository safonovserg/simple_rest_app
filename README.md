# simple_rest_app
It is REST Java app to transfer money between accounts. Packaged in executable jar.

TODO: money thansfer is valid only for single vm, because use synchronized blocks. Need to enhance solution with smth like distributed locks log, see article example https://engineering.linkedin.com/distributed-systems/log-what-every-software-engineer-should-know-about-real-time-datas-unifying

Used stack: jdk 8, Jersey 2, embedded Tomcat 8, Guice 4, Gson 2

1. To run it execute `mvn compile package`
2. Next go to target dir and run `java -jar money-app-1.0-SNAPSHOT.jar`
(optionally you could specify  `-Dport=12345`, default is 8080)
3. To run integration tests execute `mvn verify`

Now `http://localhost:8080/api/ping ` should be accessible from browser
