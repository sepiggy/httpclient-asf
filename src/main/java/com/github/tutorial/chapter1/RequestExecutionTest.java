package com.github.tutorial.chapter1;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class RequestExecutionTest {

    // 1.1.1. HTTP request
    @Test
    public void testURL() throws URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.google.com")
                .setPath("/search")
                .setParameter("q", "httpclient")
                .setParameter("btnG", "Google Search")
                .setParameter("aq", "f")
                .setParameter("oq", "")
                .build();
        HttpGet httpget = new HttpGet(uri);
        System.out.println(httpget.getURI());
    }

    // 1.1.2. HTTP response
    @Test
    public void testHttpResponse() {
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");

        System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().toString());
    }

    // 1.1.3. Working with message headers
    @Test
    public void testHeader() {
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        response.addHeader("Set-Cookie",
                "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie",
                "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
        Header h1 = response.getFirstHeader("Set-Cookie");
        System.out.println(h1);
        Header h2 = response.getLastHeader("Set-Cookie");
        System.out.println(h2);
        Header[] hs = response.getHeaders("Set-Cookie");
        System.out.println(hs.length);

        // 使用迭代器遍历 Header
        HeaderIterator headerIterator = response.headerIterator("Set-Cookie");
        while (headerIterator.hasNext()) {
            System.out.println(headerIterator.next());
        }

        // 遍历单个的 Header 元素
        headerIterator = response.headerIterator("Set-Cookie");
        BasicHeaderElementIterator basicHeaderElementIterator = new BasicHeaderElementIterator(headerIterator);
        while (basicHeaderElementIterator.hasNext()) {
            HeaderElement element = basicHeaderElementIterator.nextElement();
            System.out.println(element.getName() + " = " + element.getValue());
            NameValuePair[] parameters = element.getParameters();
            for (int i=0; i<parameters.length; i++) {
                System.out.println(" " + parameters[i]);
            }
        }
    }

    // 1.1.4. HTTP entity
    @Test
    public void testHttpEntity() throws IOException {
        StringEntity myEntity = new StringEntity("important message",
                ContentType.create("text/plain", "UTF-8"));

        System.out.println(myEntity.getContentType());
        System.out.println(myEntity.getContentLength());
        System.out.println(EntityUtils.toString(myEntity));
        System.out.println(EntityUtils.toByteArray(myEntity).length);
    }

    // 1.1.6. Consuming entity content
    @Test
    public void testConsumingEntityContent() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://people.cn");
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            HttpEntity entity = response.getEntity();
            BufferedReader bufferedReader;
            String data;

            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                } else {
                    // Stream content out
                    bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "GB2312"));
                    while ((data = bufferedReader.readLine()) != null) {
                        System.out.println(data);
                    }
                }
            }
        } finally {
            response.close();
        }
    }

    // 1.1.7. Producing entity content
    @Test
    public void testProducingEntityContent() {

    }


}
