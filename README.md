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

### Step 1
Add as dependency to your project and then use the beans in your Spring Oauth2 Configuration

### Step 2
Add this annotation to your configuration class:

```java
@Configuration
@EnableSecurityMongo
public class MongoSecurityConfiguration {

}
```
Having this annotation will define in your spring context all the necessary to use this library.

### Step 3
define the following properties in your app if you want to use the default Mongo client. 
If you want to use your own version just DO NOT ADD these properties.

```
mongo.host=localhost
mongo.port=27017
mongo.database=testdb
mongo.username=testuser
mongo.password=testpassword
```

## Creating users manually in Mongo DB

You can produce the json to create in your mongo instance the users by executing some of the integration tests or just insert this json:

Mongo User
```javascript
{
    "_id" : "testuser",
    "_class" : "uk.co.caeldev.springsecuritymongo.domain.User",
    "password" : "testpassword",
    "userUUID" : LUUID("03479d48-93cf-5e55-974f-842eb0200ca8"),
    "authorities" : [ 
        {
            "role" : "ROLE_USER",
            "_class" : "org.springframework.security.core.authority.SimpleGrantedAuthority"
        }
    ],
    "accountNonExpired" : true,
    "accountNonLocked" : true,
    "credentialsNonExpired" : true,
    "enabled" : true
}
```

Mongo Client Detail

```javascript
{
    "_id" : "testclient",
    "_class" : "uk.co.caeldev.springsecuritymongo.domain.MongoClientDetails",
    "clientSecret" : "testclientsecret",
    "scope" : [ 
        "read"
    ],
    "resourceIds" : [ 
        "oauth2-resource"
    ],
    "authorizedGrantTypes" : [ 
        "authorization_code", 
        "implicit"
    ],
    "registeredRedirectUris" : [ 
        "http://www.google.co.uk"
    ],
    "authorities" : [ 
        {
            "role" : "ROLE_CLIENT",
            "_class" : "org.springframework.security.core.authority.SimpleGrantedAuthority"
        }
    ],
    "accessTokenValiditySeconds" : 30000.0000000000000000,
    "refreshTokenValiditySeconds" : 30000.0000000000000000,
    "additionalInformation" : {},
    "autoApproveScopes" : [ 
        ""
    ]
}
```


