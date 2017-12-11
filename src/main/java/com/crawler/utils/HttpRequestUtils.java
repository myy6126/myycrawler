package com.crawler.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author caozhaorui
 */
public class HttpRequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

    public static String invoke(HttpUriRequest httpReq, HttpClient hc) {
        return invoke(httpReq, hc, null);
    }

    public static String invoke(HttpUriRequest httpReq, HttpClient hc, String charset) {
        HttpEntity entity = null;
        String ret = StringUtils.EMPTY;
        try {
            HttpResponse httpResponse = hc.execute(httpReq);
            StatusLine status = httpResponse.getStatusLine();
            entity = httpResponse.getEntity();
            ret = EntityUtils.toString(entity, StringUtils.isBlank(charset) ? "UTF-8" : charset);
            if (status.getStatusCode() >= 400 && status.getStatusCode() <  600) {
                logger.error("error status {}, uri:{}", status, httpReq.getURI().toString());
            }
        } catch (Exception e) {
            String uri = (httpReq != null && httpReq.getURI() != null) ? httpReq.getURI().toString() : "NULL_REQUEST";
            logger.error("[HttpClient] failed, url={}, error={}", uri, e.getMessage(), e);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
        return ret;
    }

    public static List<NameValuePair> buildNameValuePair(Map<String, String> params) {
        List<NameValuePair> lists = Lists.newArrayListWithCapacity(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            lists.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return lists;
    }
}
