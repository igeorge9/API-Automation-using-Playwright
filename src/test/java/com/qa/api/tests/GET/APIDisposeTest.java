package com.qa.api.tests.GET;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class APIDisposeTest {

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;

    @BeforeTest
    public void preSetup() {
        playwright = Playwright.create();
        apiRequest = playwright.request();
        requestContext = apiRequest.newContext();
    }

    @Test
    public void getUsersAPITest() throws IOException {
        APIResponse apiGetCallResponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiGetCallResponse.status();
        System.out.println("Response status code is " + statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(apiGetCallResponse.ok());
//                Assert.assertEquals(apiResponse.ok(),true); =====>>  same as above statement
        String statusText = apiGetCallResponse.statusText();
        System.out.println("Response status text is " + statusText);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiGetCallResponse.body());
        String prettyJsonResponse = jsonNode.toPrettyString();
        System.out.println("===================== Printing the API Response =====================");
        System.out.println(prettyJsonResponse);
        apiGetCallResponse.dispose();
/*
            dispose() method will dispose only the body, but the status code and status text will remain the same.
            Also, it will give Playwright Exception as follows. Hence, catch it inside a try-catch block and give appropriate error message.

          "  com.microsoft.playwright.PlaywrightException: Response has been disposed "

            dispose() method can be used to any particular api response or in whole request context.
            For example , lets say there is a request context and there are 3 request-response under that .
            We can dispose the request context as a whole Or we can individually dispose single request-response coming under that context.
            dispose() method is not available in Postman and RestAssured.
 */

        try {
            System.out.println("========= Printing the API Response in plain text format after disposing the body ===========");
            System.out.println(apiGetCallResponse.text());
        } catch (PlaywrightException e) {
            System.out.println("Response has been disposed");
        }

        int statusCode1 = apiGetCallResponse.status();
        System.out.println("Response status code after dispose is " + statusCode1);

        String statusText1 = apiGetCallResponse.statusText();
        System.out.println("Response status text after dispose is " + statusText1);

    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }
}
