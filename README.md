**API Automation using Playwright and Java - README.md**

1. Create a maven project with archetype maven-quickstart-archetype
2. Add following dependencies in the pom.xml
3. Delete unnecessary files created inside packages.

<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.38.0</version>
</dependency>
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.8.0</version>
    <scope>test</scope>
</dependency>

4. Create a package under test->java called com.qa.api.tests and create a java class under that called getAPICall
5. When we use GET method , we get the output body in Byte array Format. But if we want to print it in JSON format , we have to convert that to JSON using Jackson-binding dependency .

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>

7. Add Lombok Plugin in IntelliJ to get the builder() methods.
8. PUT Call - Used to update user details - PUT is idempotent ie.
9. When we call PUT how many number of times , if there is an existing user, it will update the user. If there is no existing user with that parameters only , it will create a new user.
10. POST Call - create new user - not idempotent
11. What all authorisation required for POST call , same is needed for PUT as well.
12. In PUT Call , let’s say we want to update only 1 parameter like email id or name , still we should pass the entire JSON body like POST call.
13. Body of POST and PUT remains same.

**Used GIT Commands**

1. git branch      
2. git status
3. git remote add origin https://github.com/igeorge9/API-Automation-using-Playwright.git
4. git commit -m "Adding GET Call and POST call changes"
5. git push -u origin main

POJO - Plain Old Java Object

Serialization -> POJO class is converted to JSON ->
We have a method called setData() in Playwright where we can pass the object of the POJO class.
This is inbuilt in Playwright, whereas we have ti write the POJO class in Rest Assured.

Deserialization - > JSON is converted back to POJO class objects
Playwright supports thus and we can do this in PW.



