package com.crawler.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpExecuteUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpExecuteUtils.class);

    private static final HttpClient httpClient = HttpClients.createDefault();


    /**
     * https请求页面
     */
    public static String executeHttps(String url) {
        return executeHttps(url, Collections.emptyMap(), Collections.emptyMap());
    }

    /**
     * https请求页面
     */
    public static String executeHttps(String url, String charset) {
        return executeHttps(url, charset, Collections.emptyMap(), Collections.emptyMap());
    }

    /**
     * https请求页面
     */
    public static String executeHttps(String url, Map<String, String> formParams) {
        return executeHttps(url, Collections.emptyMap(), formParams);
    }

    /**
     * https请求页面
     */
    public static String executeHttps(String url, String charset, Map<String, String> headers, Map<String, String> formParams) {
        List<NameValuePair> nameValuePairs = HttpRequestUtils.buildNameValuePair(formParams);
        if (CollectionUtils.isEmpty(nameValuePairs)) {
            HttpGet get = new HttpGet(url);
            addHeaders(get, headers);
            return HttpRequestUtils.invoke(get, httpClient, charset);
        } else {
            HttpPost httpPost = new HttpPost(url);
            addHeaders(httpPost, headers);
            HttpEntity entity;
            try {
                entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("[HttpClient] utf-8 unsupported encoding.");
                return null;
            }
            httpPost.setEntity(entity);
            return HttpRequestUtils.invoke(httpPost, httpClient, charset);
        }
    }

    /**
     * https请求页面
     */
    public static String executeHttps(String url, Map<String, String> headers, Map<String, String> params) {
        return executeHttps(url, null, headers, params);
    }

    private static void addHeaders(HttpRequestBase request, Map<String, String> headers) {
        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(request::addHeader);
        }
    }
}
