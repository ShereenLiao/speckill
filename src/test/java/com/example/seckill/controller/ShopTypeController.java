package com.example.seckill.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.springframework.data.redis.hash.Jackson2HashMapper;

@SpringBootTest
public class ShopTypeController {
    private static final String shopTypeUrl = "http://localhost:8081/shoptype/";
    @Test
    public void getAllShopTypesTest() throws IOException {
        HttpUriRequest request = new HttpGet( shopTypeUrl +"list");
        request.setHeader("Content-Type", "application/json");
        // When
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        Map<String, Object> map = null;
        Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
    }
}
