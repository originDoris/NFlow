package com.doris.nflow.engine.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author: xhz
 * @Title: HttpUtil
 * @Description:
 * @date: 2022/10/11 14:27
 */
@Slf4j
public class HttpUtil {


    public static HttpClient getClient(){
        return HttpClient.newHttpClient();
    }


    public static String send(HttpRequest httpRequest, HttpResponse.BodyHandler<String> bodyHandler) {
        HttpClient client = getClient();
        HttpResponse<String> send = null;
        try {
            send = client.send(httpRequest, bodyHandler);
        } catch (IOException | InterruptedException e) {
            log.error("send IOException | InterruptedException.||httpRequest={}||bodyHandler={}, ", httpRequest, bodyHandler, e);
            return null;
        }
        return send.body();
    }


    public static String sendAsync(HttpRequest httpRequest, HttpResponse.BodyHandler<String> bodyHandler) {
        HttpClient client = getClient();
        HttpResponse<String> send = null;
        try {
            CompletableFuture<HttpResponse<String>> async = client.sendAsync(httpRequest, bodyHandler);
            send = async.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("sendAsync InterruptedException | ExecutionException.||httpRequest={}||bodyHandler={}, ", httpRequest, bodyHandler, e);
            return null;
        }
        return send.body();
    }


    public static String appendUri(String uri, String appendQuery) throws URISyntaxException {
        URI oldUri = new URI(uri);
        String newQuery = oldUri.getQuery();
        if (newQuery == null) {
            newQuery = appendQuery;
        } else {
            newQuery += "&" + appendQuery;
        }
        URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());
        return newUri.toString();
    }

}
