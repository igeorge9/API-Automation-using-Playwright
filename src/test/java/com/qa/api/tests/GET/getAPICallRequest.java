package com.qa.api.tests.GET;

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

import java.io.IOException;
import java.util.Map;

public class getAPICallRequest {
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
    public void getSpecificUserAPITest() throws IOException {
        APIResponse apiGetCallResponse = requestContext.get("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
//                        .setQueryParam("id",5711499)
                        .setQueryParam("status","active")
                        .setQueryParam("gender","male"));
        int statusCode = apiGetCallResponse.status();
        System.out.println("Response status code is " + statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(apiGetCallResponse.ok());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiGetCallResponse.body());
        String prettyJsonResponse = jsonNode.toPrettyString();
        System.out.println("===================== Printing the API Response =====================");
        System.out.println(prettyJsonResponse);
    }
    @Test
    public void getUsersAPITest() throws IOException {
        APIResponse apiGetCallResponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiGetCallResponse.status();
        System.out.println("Response status code is " + statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(apiGetCallResponse.ok());
//                Assert.assertEquals(apiGetCallResponse.ok(),true); =====>>  same as above statement
        String statusText = apiGetCallResponse.statusText();
        System.out.println("Response status text is " + statusText);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiGetCallResponse.body());
        String prettyJsonResponse = jsonNode.toPrettyString();
        System.out.println("===================== Printing the API Response =====================");
        System.out.println(prettyJsonResponse);

        System.out.println("===================== Printing the API Response in plain text format =====================");
        System.out.println(apiGetCallResponse.text());

        System.out.println("===================== Printing the API url =====================");
        System.out.println(apiGetCallResponse.url());

        Map<String, String> headerMap = apiGetCallResponse.headers();
        System.out.println("===================== Printing the API headers =====================");
        System.out.println(headerMap);
        Assert.assertEquals(headerMap.get("content-type"), "application/json; charset=utf-8");
        Assert.assertEquals(headerMap.get("connection"), "close");
    }

    @AfterTest
    public void tearDown()
    {
        playwright.close();
    }
}
