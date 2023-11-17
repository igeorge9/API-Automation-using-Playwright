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

public class BookingTest {

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;
    private static String TOKEN_ID = null;

    @BeforeTest
    public void preSetup() throws IOException {
        playwright = Playwright.create();
        apiRequest = playwright.request();
        requestContext = apiRequest.newContext();

        String inputRequestBody = "{\n" + "    \"username\" : \"admin\",\n" + "    \"password\" : \"password123\"\n" + "}";
        APIResponse apiPostCallResponse = requestContext.post("https://restful-booker.herokuapp.com/auth", RequestOptions.create().setHeader("Content-type", "Application/json").setData(inputRequestBody));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(apiPostCallResponse.body());
        TOKEN_ID = jsonResponse.get("token").asText();
    }

    @AfterTest
    public void tearDown() {
        playwright.close();
    }

    @Test
    public void updateBookingTest() {
        String bookingJSON = "{\n" +
                "    \"firstname\" : \"James\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-12-20\",\n" +
                "        \"checkout\" : \"2023-12-23\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        APIResponse updateBookingResponse = requestContext.put("https://restful-booker.herokuapp.com/booking/1",
                RequestOptions.create()
                        .setHeader("Content-type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setHeader("Cookie", "token=" + TOKEN_ID)
                        .setData(bookingJSON));
        System.out.println("Updated booking is : " + "\n" + updateBookingResponse.text());
        System.out.println(updateBookingResponse.url());

        Assert.assertEquals(updateBookingResponse.status(), 200);
        Assert.assertTrue(updateBookingResponse.ok());
    }

    @Test
    public void deleteBookingTest() {
        APIResponse deleteBookingResponse = requestContext.delete("https://restful-booker.herokuapp.com/booking/1",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Cookie", "token=" + TOKEN_ID));
        System.out.println(deleteBookingResponse.statusText() + " " + deleteBookingResponse.status());
        System.out.println("Deleted Booking response : " + deleteBookingResponse.text());
        Assert.assertEquals(deleteBookingResponse.statusText(), "Created");
        Assert.assertEquals(deleteBookingResponse.status(), 201);
    }
}
