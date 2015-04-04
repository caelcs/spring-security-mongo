#Spring Security Mongo

[![Build Status](https://travis-ci.org/caelwinner/spring-security-mongo.svg?branch=master)](https://travis-ci.org/caelwinner/spring-security-mongo)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/uk.co.caeldev/spring-security-mongo/badge.png?style=flat)](http://search.maven.org/#search|ga|1|g%3A%22uk.co.caeldev%22%20AND%20a%3A%22spring-security-mongo%22)

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