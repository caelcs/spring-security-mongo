#Spring Security Mongo

[![Build Status](https://travis-ci.org/caelwinner/spring-security-mongo.svg?branch=master)](https://travis-ci.org/caelwinner/spring-security-mongo)

Library to provide full implementation of all the repositories
and provider necessary to have all the security persisted in MongoDB.

* ApprovalStore
* ClientDetailsService
* ClientRegistrationService
* TokenStore
* UserDetailsManager
* ClientTokenServices

##How to use it

Add as dependency to your project and then use the beans in your Spring Oauth2 Configuration

```
<dependency>
    <groupId>uk.co.caeldev</groupId>
    <artifactId>spring-security-mongo</artifactId>
    <version>0.1.6</version>
</dependency>
```
