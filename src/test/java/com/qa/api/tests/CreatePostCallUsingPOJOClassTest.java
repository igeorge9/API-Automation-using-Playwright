package com.qa.api.tests;

import com.api.data.UserPOJOClass;
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

public class CreatePostCallUsingPOJOClassTest {

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

        UserPOJOClass userPOJOClass = new UserPOJOClass("Vinod",generateEmailId(),"active","male");

//        POST call : Create a user
        APIResponse apiResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-type", "Application/json")
                        .setHeader("Authorization", "Bearer 38d20a9503c4e6502c70d9154ae2e7e4e5654bdd4d1a9a38dd5275b87adaa11c")
                        .setData(userPOJOClass)
                        );
        System.out.println("statusText : "+apiResponse.statusText());
        Assert.assertEquals(apiResponse.status(),201);
        Assert.assertEquals(apiResponse.statusText(), "Created");

        System.out.println("============ Printing the POST Call API Response in plain text format ==========");
        System.out.println(apiResponse.text());
        String stringResponse=apiResponse.text();

//        Convert response text/JSON to POJO - Deserialization
        ObjectMapper mapper = new ObjectMapper();
        UserPOJOClass createdUser = mapper.readValue(stringResponse, UserPOJOClass.class);
        System.out.println(createdUser);
        System.out.println("Email id : "+createdUser.getEmail());
        System.out.println("Name : "+createdUser.getName());

        Assert.assertEquals(createdUser.getName(),userPOJOClass.getName());
        Assert.assertEquals(createdUser.getEmail(),userPOJOClass.getEmail());
        Assert.assertNotNull(createdUser.getId()); // ensure the generated id is not null.
        System.out.println(createdUser);
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }
}
