package com.qa.api.tests.DELETE;

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

public class DeleteUserAPITest {


    /*
         1. Create a user - POST - get a 201 (Created)
         2. Delete the user using userid - DELETE - get a 204(No Content) status code
         3. Get the user details (userid) - GET - get 404 (Not Found)
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
//        System.out.println("statusText : " + apiPostCallResponse.statusText());
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

//        2. Delete the user details using userid - DELETE

        System.out.println("================== DELETE Call ======================");

        APIResponse apiDeleteCallResponse = requestContext.delete("https://gorest.co.in/public/v2/users/" + userid,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 38d20a9503c4e6502c70d9154ae2e7e4e5654bdd4d1a9a38dd5275b87adaa11c")
                        );

        System.out.println("============ Printing the DELETE Call API Response in plain text format ==========");
//        System.out.println("status text :"+apiDeleteCallResponse.statusText());
//        System.out.println("status : "+apiDeleteCallResponse.status());

        Assert.assertEquals(apiDeleteCallResponse.statusText(),"No Content");
        Assert.assertEquals(apiDeleteCallResponse.status(), 204);
        System.out.println("Deleted user response : "+apiDeleteCallResponse.text());

//        3. Get the user details (userid) - GET

        System.out.println("================== GET Call ======================");

        APIResponse getCallResponse = requestContext.get("https://gorest.co.in/public/v2/users/" + userid,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 38d20a9503c4e6502c70d9154ae2e7e4e5654bdd4d1a9a38dd5275b87adaa11c"));

//        System.out.println("status text :"+getCallResponse.statusText());
//        System.out.println("status : "+getCallResponse.status());
        Assert.assertEquals(getCallResponse.statusText(),"Not Found");
        Assert.assertEquals(getCallResponse.status(), 404);

        String getCallResponseToText = getCallResponse.text();
        System.out.println("getCallResponse : "+getCallResponseToText);
        Assert.assertTrue(getCallResponseToText.contains("Resource not found"));
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }
}
