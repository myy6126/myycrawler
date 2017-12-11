package com.crawler.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private final static Properties properties = new Properties();
    private static OutputStream output = null;
    private static InputStream input = null;

    public static void write(String fileName, String key, String value) {
        try {
            if (output == null) {
                output = new FileOutputStream(fileName);
            }
            properties.setProperty(key, value);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void read(String fileName, String key) {
        Properties properties = new Properties();

        try {
            input = new FileInputStream("config.properties");//加载Java项目根路径下的配置文件
            properties.load(input);// 加载属性文件
            System.out.println("url:" + properties.getProperty("url"));
        } catch (IOException io) {

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static void readAll(String fileName) {

    }
}
