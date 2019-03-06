package com.taotao.search.controller.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

public class HTTPTest {
    public static void main(String[] args) throws Exception {
        f1();
    }
    //    @Test
    public static void f1() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        final URIBuilder uriBuilder = new URIBuilder("http://localhost:8081/content/category/list?id=30");
        final URI build = uriBuilder.build();

        final HttpGet httpGet = new HttpGet(build);
        final CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.println(response.getStatusLine().getStatusCode());

        final String string = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(string);

    }
}
