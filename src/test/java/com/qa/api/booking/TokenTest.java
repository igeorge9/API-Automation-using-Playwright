package com.qa.api.booking;

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

public class TokenTest {

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

//        Create post call request body

        String inputRequestBody = "{\n" + "    \"username\" : \"admin\",\n" + "    \"password\" : \"password123\"\n" + "}";

//       POST call : Create a Token

        APIResponse apiPostCallResponse = requestContext.post("https://restful-booker.herokuapp.com/auth", RequestOptions.create().setHeader("Content-type", "Application/json").setData(inputRequestBody));
        Assert.assertEquals(apiPostCallResponse.status(), 200);
        Assert.assertEquals(apiPostCallResponse.statusText(), "OK");

        System.out.println("============ Printing the POST Call API Response ==========");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(apiPostCallResponse.body());
        String token = jsonResponse.get("token").asText();
        String responseText = jsonResponse.toPrettyString();
        System.out.println("Response JSON is : " + "\n" + responseText + "\n" + "Token is : " + token);
        Assert.assertNotNull(token);

    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }
}
