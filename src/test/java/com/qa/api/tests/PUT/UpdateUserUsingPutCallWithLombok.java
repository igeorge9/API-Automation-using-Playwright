package com.qa.api.tests.PUT;

import com.api.data.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class UpdateUserUsingPutCallWithLombok {
    /*
         1. Create a user - POST
         2. Update the user using userid - PUT
         3. Get the user details (userid) - GET
     */

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;
    String emailId;

    @BeforeTest
    public void preSetup() {
        playwright = Playwright.create();
        apiRequest = playwright.request();
        requestContext = apiRequest.newContext();
    }

    public String generateEmailId() {
        emailId = "playwrightestautomtion" + System.currentTimeMillis() + "@gmail.com";
        return emailId;
    }

    @Test
    public void createUserTest() throws IOException {

//        1. Create a user - POST
        System.out.println("================== POST Call ======================");

//        Create Users object using Builder pattern
        Users userCreatedUsingLombok = Users.builder()
                .name("Amit")
                .email(generateEmailId())
                .gender("male")
                .status("inactive").build();

//       POST call : Create a user

        APIResponse apiPostCallResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-type", "Application/json")
                        .setHeader("Authorization", "Bearer 38d20a9503c4e6502c70d9154ae2e7e4e5654bdd4d1a9a38dd5275b87adaa11c")
                        .setData(userCreatedUsingLombok));
        System.out.println("statusText : " + apiPostCallResponse.statusText());
        Assert.assertEquals(apiPostCallResponse.status(), 201);
        Assert.assertEquals(apiPostCallResponse.statusText(), "Created");

        System.out.println("============ Printing the POST Call API Response in plain text format ==========");
        String stringResponse = apiPostCallResponse.text();

//        Convert response text/JSON to POJO - Deserialization

        ObjectMapper mapper = new ObjectMapper();
        Users createdUser = mapper.readValue(stringResponse, Users.class);
        System.out.println("Created User : " + "\n" + createdUser);

        Assert.assertEquals(createdUser.getName(), userCreatedUsingLombok.getName());
        Assert.assertEquals(createdUser.getEmail(), userCreatedUsingLombok.getEmail());
        Assert.assertNotNull(createdUser.getId()); // ensure the generated id is not null.
        String userid = createdUser.getId();
        System.out.println("Created user id :" + userid);

//        2. Update the user details using userid - PUT
//           Update status, name and email id

        System.out.println("================== PUT Call ======================");

        userCreatedUsingLombok.setEmail(generateEmailId());
        userCreatedUsingLombok.setStatus("active");
        userCreatedUsingLombok.setName("Amit Noyal");

        APIResponse apiPutCallResponse = requestContext.put("https://gorest.co.in/public/v2/users/" + userid,
                RequestOptions.create()
                        .setHeader("Content-type", "Application/json")
                        .setHeader("Authorization", "Bearer 38d20a9503c4e6502c70d9154ae2e7e4e5654bdd4d1a9a38dd5275b87adaa11c")
                        .setData(userCreatedUsingLombok));

        System.out.println("============ Printing the PUT Call API Response in plain text format ==========");
        Assert.assertTrue(apiPutCallResponse.ok());
        Assert.assertEquals(apiPutCallResponse.status(), 200);

        String putResponseText = apiPutCallResponse.text();
        Users updatedUser = mapper.readValue(putResponseText, Users.class);
        System.out.println("Updated User : " + "\n" + updatedUser);
        Assert.assertEquals(updatedUser.getName(), userCreatedUsingLombok.getName());
        Assert.assertEquals(updatedUser.getId(), userid);
        Assert.assertEquals(updatedUser.getEmail(), userCreatedUsingLombok.getEmail());
        Assert.assertEquals(updatedUser.getStatus(), userCreatedUsingLombok.getStatus());

//        3. Get the user details (userid) - GET

        System.out.println("================== GET Call ======================");

        APIResponse getCallResponse = requestContext.get("https://gorest.co.in/public/v2/users/" + userid,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 38d20a9503c4e6502c70d9154ae2e7e4e5654bdd4d1a9a38dd5275b87adaa11c"));

        Assert.assertTrue(getCallResponse.ok());
        Assert.assertEquals(getCallResponse.status(), 200);

        String getCallResponseToText = getCallResponse.text();
        Users getUpdatedUser = mapper.readValue(getCallResponseToText, Users.class);
        System.out.println("Get Updated User : " + "\n" + getUpdatedUser);

        Assert.assertEquals(getUpdatedUser.getName(), updatedUser.getName());
        Assert.assertEquals(getUpdatedUser.getEmail(), updatedUser.getEmail());
        Assert.assertEquals(getUpdatedUser.getStatus(), updatedUser.getStatus());
        Assert.assertEquals(getUpdatedUser.getId(), userid);


    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }

}

