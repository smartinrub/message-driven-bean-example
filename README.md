# Message Driven Bean Example

This is a project to show how to use Message Driven Beans (MDBs) from Java EE.

## Technologies

- Maven
- Java EE (MDB)

## Getting Started

Both, MDBs and client, can be deployed in a web server like Glassfish, Payara or WildFly.

### Deployment on Payara

1. Install Payara (MacOS):

    ```
    brew install payara
    ```

2. Start Payara:

    ```
    asadmin start-domain --verbose domain
    ```
      
3. Go to Payara administration console at `localhost:8080`. The first time it will ask you to set a password for the admin user.                                                                                                             
4. Build EJB beans and client application:

    ```
    mvn clean install
    ```

5. Deploy MDBs and client: go to Applications>Deploy... and choose the file 
 `my-mdb/target/my-mdb-1.0.jar` to deploy the beans and `mdb-client/target/mdb-client.war` to deploy the client.
 
 >Glassfish version 5.1.0 has a bug in the deployment form (GUI internal error: Archive Path is NULL). 
 >The fix is either modifying the html in the browser or 
 >opening `/usr/local/Cellar/glassfish/5.1.0/libexec/glassfish/modules/console-common.jar` and
 > replace `<sun:form id="form">` with `<sun:form id="form" enctype="multipart/form-data">` in `applications/uploadFrame.jsf`.

 ```
 vi console-common.jar
 applications/uploadFrame.jsf
 ```

6. Go to the `mdb-client` configuration and change the context root to `/mdb-client`.
7. Configure JMS Resources:
    - Create Connection Factory: Go to Resources>JMS Resources>Connection Factories>New...
        - JNDI Name: `jms/myConnectionFactory`
        - Resource Type: `javax.jms.ConnectionFactory`
        - Description: `My Connection Factory`
    - Create Destination Resource: Go to Resources>JMS Resources>Destination Resources>New...
        - JNDI Name: `jms/myQueue`
        - Physical Destination Name: `myQueue`
        - Resource Type: `javax.jms.Queue`
        
8. Access API:

```
http://localhost:8080/mdb-client/message-driven-bean?message=hello
```
   
### Deployment on WildFly

1. Install WildFly (MacOS):

    ```
    brew install wildfly-as
    ```
   
   Add to `.bash_profile` or `.zshrc`:
   
    ```
    export JBOSS_HOME=/usr/local/opt/wildfly-as/libexec
    export PATH=${PATH}:${JBOSS_HOME}/bin
    ```
   
2. Start WildFly:

    ```
    brew services start wildfly-as
    ```

3. Go to `localhost:9990`
4. Build project and deploy beans and client. 
>Remember, WildFly 20 is not compatible with business interface dependency injection, 
>you have to use JNDI lookup instead.
   