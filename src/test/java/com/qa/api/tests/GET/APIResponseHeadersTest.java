package com.qa.api.tests.GET;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class APIResponseHeadersTest {

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
public void getHeadersTest() {
    APIResponse apiGetCallResponse = requestContext.get("https://gorest.co.in/public/v2/users");
    int statusCode = apiGetCallResponse.status();
    System.out.println("Response status code is " + statusCode);
    Assert.assertEquals(statusCode, 200);

//   Using Map - headers() method.

    Map<String,String> headersMap = apiGetCallResponse.headers();
    headersMap.forEach((key,value) -> System.out.println(key+" : "+value));
    System.out.println("Total number of response headers : " +headersMap.size());
    Assert.assertEquals(headersMap.get("server"),"cloudflare");
    Assert.assertTrue(headersMap.get("x-frame-options").contains("SAME"));

//    Using List - HeadersArray - headersArray() method
    System.out.println("================Using List - HeadersArray - headersArray() method ================");
    List<HttpHeader> headersList=apiGetCallResponse.headersArray();
    for(HttpHeader header: headersList)
    {
        System.out.println(header.name+" : "+header.value);
    }
}
    @AfterTest
    public void tearDown() {
        playwright.close();
    }
}
