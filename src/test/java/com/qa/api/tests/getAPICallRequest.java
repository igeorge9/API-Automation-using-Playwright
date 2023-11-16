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
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
//                        .setQueryParam("id",5711499)
                        .setQueryParam("status","active")
                        .setQueryParam("gender","male"));
        int statusCode = apiResponse.status();
        System.out.println("Response status code is " + statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(apiResponse.ok());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponse.body());
        String prettyJsonResponse = jsonNode.toPrettyString();
        System.out.println("===================== Printing the API Response =====================");
        System.out.println(prettyJsonResponse);
    }
    @Test
    public void getUsersAPITest() throws IOException {
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode = apiResponse.status();
        System.out.println("Response status code is " + statusCode);
        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(apiResponse.ok());
//                Assert.assertEquals(apiResponse.ok(),true); =====>>  same as above statement
        String statusText = apiResponse.statusText();
        System.out.println("Response status text is " + statusText);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponse.body());
        String prettyJsonResponse = jsonNode.toPrettyString();
        System.out.println("===================== Printing the API Response =====================");
        System.out.println(prettyJsonResponse);

        System.out.println("===================== Printing the API Response in plain text format =====================");
        System.out.println(apiResponse.text());

        System.out.println("===================== Printing the API url =====================");
        System.out.println(apiResponse.url());

        Map<String, String> headerMap = apiResponse.headers();
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
