# Integrating a Spring boot Application using OIDC Protocol with OKTA

We will integrate a custom OIDC Application based on Spring boot with OKTA.
First we will create an OIDC application in Okta IDaaS portal and then we will use the Client ID and Client Secret obtained from the portal in our Spring boot application.

## Prerequisite learning

OKTA IDaaS,
Java,
Springboot,
Thymeleaf.

##  OAuth 2.0 and OpenID Connect (OIDC)
### OAuth 2.0
OAuth2.0 is a standard used to provide access to the client applications. It enables delegated authorization, thus controlling authorization to access a protected resources.
The Oauth 2.0 has four components
- **Authorization Server** - Authorization server issues the accesss token. Here Okta is our authorization server.
- **Resource Owner** - The users of our application who tries to get access to the resource are the resource owner are called resource owner.
- **Client** - The web browser or any other application with which we are trying to access the application are clients. Clients requests access token from the authorization server.
- **Resource Server** - Resource server is the application which validates the access token and grants access of the resources to the user/resource owner.

### OpenID Connect (OIDC)
Open ID Connect (OIDC) protocol is an authentication standard which is built using OAuth 2.0. It adds ID token which is technically a JSON Web Token (JWT) and this token includes identifiable information about the user like name and email address.

#### OpenID Connect Flows
Open ID flows describes how Identity Provider handles the user authentication.
There are four OIDC flows:
- **Authorization Code**
- **Implicit Code**
- **Resource Owner Credentials**
- **Client Credentials**

Here we have used **Authorization Code Flow**.

#### Authorization Code Flow
Authorization Code flow is used in server-side web applications where the source-code is not exposed publicly. Here the **Authorization** code is exchanged for a token. During this code exchange the client secret must be passed. This client secret should be stored securely in the client.

#### Workflow
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/Authorization%20Code%20Flow.png)
- The user tries to access the application through client.
- It automatically redirects the user to the OKTA Authorization Server.
- The Authorization server redirects the user to the login page to authenticate and authorize.
- User authenticates and tries to log in.
- After getting authenticated, OKTA Authorization server generates an authentication code which is fed to the client.
- The client now sends the authentication code + client id + client secret to the authorization server.
- Authorization server validates it and sends ID and Access token to the client.
- Client requests application for the user data with the access token.
- Application now responds with the requested data to the client.
## Configuring Okta
- If we already have access to **Okta** portal we can continue with the next steps
    - Else we can get a [trail version](https://www.okta.com/free-trial/) or we can create a [developer account](https://developer.okta.com/). 
        
- We will sign in to our **Okta** portal

- Go to **Applications** on the left side menu.

- Select **Create App Integration**
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si2.png)

- Next select **OIDC - OpenID Connect** as **Sign-in method**
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si3.png)

- Under **Application type** select **Web Application**
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si4.png)

- Give a **Name** to the application

- Provide a **Logo** and select **Authorization Code** below
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si5.png)

- Next provide the **Sign-in redirect URIs** : http://localhost:8080/login/oauth2/code/okta

- Fill the **Sign-out redirect URIs** : https://<Okta-tenant>/login/login.html
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si6.png)

- We can skip **group assignment** for now. **Save** the changes.
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si7.png)

- Next copy the **Client-ID** and **Clinet-secret** and paste those in the **application.properties** file of the spring boot application.
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si8.png)
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si9.png)

- We can now check the application for the configurations we did.
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si12.png)

- Next moving to the **Assignements** tab we can click on the **Assign** button and assign it to the user.
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si13.png)

## Setting up the Spring boot application

- Navigate to the [Spring Initializr](https://start.spring.io/) or you can use the Spring Initializr plugin from your IDE.
![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/si1.png)
- Selct the java version
- Select **Maven** or **Gradle**
- Give name to the project
- Then add the following dependencies Spring
    - *Spring Web*
    - *Spring Security*
    - *Sprin Boot DevTools*
    - *Thymeleaf*
    - *Okta*
- After adding the following dependencies click on the option **Generate**.
- A zip file of the project will get downloaded.
- Extract it and open the project in your favourite IDE.
After adding the following dependencies the **pom.xml** will be -
## pom.xml



```bash
  <?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.7.5</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.example.okta</groupId>
	<artifactId>OIDCIntegration</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>OIDCIntegration</name>
	<description>Demo project for OIDC integration with Spring Boot</description>
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>com.okta.spring</groupId>
			<artifactId>okta-spring-boot-starter</artifactId>
			<version>2.1.6</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
		<groupId>com.okta.spring</groupId>
		<artifactId>okta-spring-boot-starter</artifactId>
		<version>2.1.6</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>

```


## Step - 1

We will now create a **controller** class with the following code.

## src/main/java/com.example.okta.OIDCIntegration/contoller/control

```bash
package com.example.okta.OIDCIntegration.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class control {

    @GetMapping("/")
    public ModelAndView getMessage(@AuthenticationPrincipal OidcUser user) {
        ModelAndView mav=new ModelAndView("welcome");
        String msg = "Hi "+user.getFullName() + " welcome home. Good to see you back!";
        String email= "Email id: "+user.getEmail();
        String phone_number= "Phone number: "+user.getPhoneNumber();
        mav.addObject("message", msg);
        mav.addObject("mail", email);
        return mav;
    }
}

```


## Step - 2

We will now create a **welcome.html** file using **Thymeleaf**

## src/main/resources/templates/welcome.html

```bash
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org/">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>OIDC Integration</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
    <style>
    	.center {
      		text-align: center;
      		font-family: 'Times New Roman', Times, serif;
    	}
    	.design {
      		font-size: 20px;
      		font-weight: bold;
    	}
  </style>
</head>

<body>
    <div>
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-3 px-2">
            <a class="navbar-brand">Spring boot integration with OIDC</a>
            <div class="ms-auto mb-2 mb-lg-0 pull-right">
                <div><a href="/logout"><button type="button" class="btn btn-light">Logout</button></a></div>
            </div>
        </nav>
	
	<br><br><br><br><br>

        <div class="center">
            <div class="design" th:text="${message}"></div><br>
            <div th:text="${mail}"></div><br><br>
        </div>
    </div>
</body>

</html>
```
## Step - 3

Next we need to add some properties in the **application.properties**
- Here we will need the **Okta tenant**
- Next we will need the **client-id** and **client-secret**. We will get it once we configure our application in **Okta**
- Then we need to specify a **post-logout-uri** where it will get redirected post logout.
- We have used **thymeleaf.cache = false** so that we don't need to restart our server in order to see the changes.
## src/main/resources/application.properties
```bash
okta.oauth2.issuer = https://<okta-tenant>/oauth2/default
okta.oauth2.client-id = <client-id>
okta.oauth2.client-secret = <client-secret>
okta.oauth2.post-logout-redirect-uri=https://<okta-tenant>/login/login.htm
#for thymeleaf
spring.thymeleaf.cache = false
```
## Step - 4

The main springboot application file was already created. We don't need any modification. We will run this file.
## src/main/java/com.example.okta.OIDCIntegration/OidcIntegrationApplication 
```bash
package com.example.okta.OIDCIntegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OidcIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OidcIntegrationApplication.class, args);
	}

}
```

## src/main/java/com.example.okta.OIDCIntegration/contoller/control

```bash
package com.example.okta.OIDCIntegration.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class control {

    @GetMapping("/")
    public ModelAndView getMessage(@AuthenticationPrincipal OidcUser user) {
        ModelAndView mav=new ModelAndView("welcome");
        String msg = "Hi "+user.getFullName() + " welcome home. Good to see you back!";
        String email= "Email id: "+user.getEmail();
        String phone_number= "Phone number: "+user.getPhoneNumber();
        mav.addObject("message", msg);
        mav.addObject("mail", email);
        return mav;
    }
}

```


## src/main/java/com.example.okta.OIDCIntegration/contoller/control

```bash
package com.example.okta.OIDCIntegration.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class control {

    @GetMapping("/")
    public ModelAndView getMessage(@AuthenticationPrincipal OidcUser user) {
        ModelAndView mav=new ModelAndView("welcome");
        String msg = "Hi "+user.getFullName() + " welcome home. Good to see you back!";
        String email= "Email id: "+user.getEmail();
        String phone_number= "Phone number: "+user.getPhoneNumber();
        mav.addObject("message", msg);
        mav.addObject("mail", email);
        return mav;
    }
}

```


## src/main/java/com.example.okta.OIDCIntegration/contoller/control

```bash
package com.example.okta.OIDCIntegration.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class control {

    @GetMapping("/")
    public ModelAndView getMessage(@AuthenticationPrincipal OidcUser user) {
        ModelAndView mav=new ModelAndView("welcome");
        String msg = "Hi "+user.getFullName() + " welcome home. Good to see you back!";
        String email= "Email id: "+user.getEmail();
        String phone_number= "Phone number: "+user.getPhoneNumber();
        mav.addObject("message", msg);
        mav.addObject("mail", email);
        return mav;
    }
}

```


## src/main/java/com.example.okta.OIDCIntegration/contoller/control

```bash
package com.example.okta.OIDCIntegration.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class control {

    @GetMapping("/")
    public ModelAndView getMessage(@AuthenticationPrincipal OidcUser user) {
        ModelAndView mav=new ModelAndView("welcome");
        String msg = "Hi "+user.getFullName() + " welcome home. Good to see you back!";
        String email= "Email id: "+user.getEmail();
        String phone_number= "Phone number: "+user.getPhoneNumber();
        mav.addObject("message", msg);
        mav.addObject("mail", email);
        return mav;
    }
}

```


## src/main/java/com.example.okta.OIDCIntegration/contoller/control

```bash
package com.example.okta.OIDCIntegration.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class control {

    @GetMapping("/")
    public ModelAndView getMessage(@AuthenticationPrincipal OidcUser user) {
        ModelAndView mav=new ModelAndView("welcome");
        String msg = "Hi "+user.getFullName() + " welcome home. Good to see you back!";
        String email= "Email id: "+user.getEmail();
        String phone_number= "Phone number: "+user.getPhoneNumber();
        mav.addObject("message", msg);
        mav.addObject("mail", email);
        return mav;
    }
}

```


##  Verifying the connection

We will now verify the connection. 

- We will open a browser and try to access http://localhost:8080.

![](https://github.com/rcRounak/OIDCIntegrationImages/blob/main/Demo.gif)


- We can see our connection got redirected to the **Okta login page**.

- After authentication we saw that our application is running and is showing our **name** and **email address**.

- We also checked the **logout** functionality

- When clicked on the **logout button** we got redirected back to the **OKTA login page**.


## Lessons Learned

How to integrate an **OIDC Application** with **OKTA IdaaS** using **Autorization Code flow**.

## Authors

- *[Rounak Roy Chowdhury](https://github.com/rcRounak)*

## Feedback

If you have any feedback, please reach out to me at rounakroyc23@gmail.com

