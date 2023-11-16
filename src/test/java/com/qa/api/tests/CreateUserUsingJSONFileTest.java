package com.qa.api.tests;

import com.fasterxml.jackson.databind.JsonNode;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CreateUserUsingJSONFileTest {

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
    @Test
    public void createUserTest() throws IOException {

        byte[] fileInBytes = null;
        File file = new File("src/test/data/user.json");
        fileInBytes = Files.readAllBytes(file.toPath());
//        POST call : Create a user
        APIResponse apiResponse = requestContext.post("https://gorest.co.in/public/v2/users", RequestOptions.create().setHeader("Content-type", "Application/json").setHeader("Authorization", "Bearer 38d20a9503c4e6502c70d9154ae2e7e4e5654bdd4d1a9a38dd5275b87adaa11c").setData(fileInBytes));
        Assert.assertEquals(apiResponse.statusText(), "Created");
        System.out.println("============ Printing the POST Call API Response in plain text format ==========");
        System.out.println(apiResponse.text());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode postJSONResponse = mapper.readTree(apiResponse.body());
        System.out.println("============ Printing the POST Call API Response in JSON format ==========");
        System.out.println(postJSONResponse.toPrettyString());

//      Capture id from the POST call JSON Response
        String userId = postJSONResponse.get("id").asText();
        System.out.println("Generated userid is : " + userId);

//        GET call : fetch the same user by userid
        APIResponse getCallResponseFetchByUserId = requestContext.get("https://gorest.co.in/public/v2/users/" + userId, RequestOptions.create().setHeader("Authorization", "Bearer 38d20a9503c4e6502c70d9154ae2e7e4e5654bdd4d1a9a38dd5275b87adaa11c"));

//                     .setQueryParam("id",userId)
// we can send user id through query parameter as well rather than appending in url directly

        Assert.assertEquals(getCallResponseFetchByUserId.status(), 200);
        Assert.assertEquals(getCallResponseFetchByUserId.statusText(), "OK");
        System.out.println("======== Printing the GET Call API Response in plain text format =========");
        System.out.println(getCallResponseFetchByUserId.text());

//       How to validate the data from get Call response corresponding to the ID

        Assert.assertTrue(getCallResponseFetchByUserId.text().contains(userId));
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }
}
