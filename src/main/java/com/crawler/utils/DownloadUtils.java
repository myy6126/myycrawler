package com.crawler.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.ftp.FtpURLConnection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtils {

    private static final Logger logger = LoggerFactory.getLogger(DownloadUtils.class);

    private static final String PREFIX_FTP = "ftp";
    private static final String PREFIX_HTTP = "http";

    public static void downLoadFromUrl(String url, String fileName, String savePath) {
        if (StringUtils.startsWith(url, PREFIX_FTP)) {
            ftpDownLoad(url, fileName, savePath);
        } else if (StringUtils.startsWith(url, PREFIX_HTTP)) {
            httpDownLoad(url, fileName, savePath);
        }
    }

    private static void ftpDownLoad(String urlStr, String fileName, String savePath) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(urlStr);
            FtpURLConnection conn = (FtpURLConnection) url.openConnection();
            inputStream = conn.getInputStream();
            byte[] getData = readInputStream(inputStream);

            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(getData);
            logger.info("info:{} ftpDownLoad success", url);
        } catch (IOException e) {
            logger.error("httpDownLoad error{}", e);
        } finally {
            outputStreamClose(fileOutputStream);
            inputStreamClose(inputStream);
        }
    }

    private static void httpDownLoad(String urlStr, String fileName, String savePath) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            inputStream = conn.getInputStream();
            byte[] getData = readInputStream(inputStream);

            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(getData);
            logger.info("info:{} httpDownLoad success", url);
        } catch (IOException e) {
            logger.error("httpDownLoad error{}", e);
        } finally {
            outputStreamClose(fileOutputStream);
            inputStreamClose(inputStream);
        }
    }

    private static byte[] readInputStream(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        int len;
        byte[] returnByte = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            returnByte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("readInputStream error{}", e);
        } finally {
            outputStreamClose(byteArrayOutputStream);
        }
        return returnByte;
    }

    private static void inputStreamClose(InputStream inputStream){
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("httpDownLoad error{}", e);
            }
        }
    }

    private static void outputStreamClose(OutputStream outputStream){
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                logger.error("httpDownLoad error{}", e);
            }
        }
    }

    public static void main(String[] args) {

        try {
            downLoadFromUrl("ftp://ftp.jingzong.org/newmedia/64k/02/02-041/02-041-0508.mp3",
                    "百度.mp3", "/Users/nestia/test/");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
