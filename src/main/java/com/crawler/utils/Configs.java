package com.crawler.utils;

import com.google.common.io.Closeables;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author caozhaorui
 */
public class Configs {

    private static final Properties prop = new Properties();
    static {
        InputStream inputStream = Configs.class.getResourceAsStream("/crawlerConfig.properties");
        try {
            prop.load(inputStream);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        } finally {
            Closeables.closeQuietly(inputStream);
        }
    }

    public static String getValue(String key) {
        return getValue(key, StringUtils.EMPTY);
    }

    public static String getValue(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

}
