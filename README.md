# online-market-rest-api

##### RUN

- mvn clean package
- java -jar online-market-rest-api.jar

#### MySQL database must be configured on your PC

##### GET AUTH TOKEN

###### For User:
- POST: localhost:8080/oauth/token?grant_type=password&username=user&password=user,
  Header: Authorization Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0

##### CHECK API

- GET: localhost:8080/users , Header: Authorization Bearer <auth-token>
- GET: localhost:8080/ads, Header: Authorization Bearer <auth-token>
- POST: localhost:8080/users , Header: Authorization Bearer <auth-token>, Body: [user information]
- POST: localhost:8080/ads , Header: Authorization Bearer <auth-token>, Body: [ad information]



Spring docs used: https://projects.spring.io/spring-security-oauth/docs/oauth2.html

===========

<i>(с) Sergiy Balukh</i>
