package com.crawler.classLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Load class from local system by sepc directory.
 */
public class FileClassLoader extends CachedClassLoader {

    private final String CLASSPATH_ROOT = TClassLoaderFactory.getFactory().getPropertyValue(TClassLoaderFactory.FILEROOT_PATH);

    private final static Logger logger = LoggerFactory.getLogger(TClassLoaderFactory.class);

    public FileClassLoader(String codebase) {
        super(codebase);
    }


    /**
     * Implements CachedClassLoader.newClass method.
     *
     * @param className
     */
    protected Class<?> newClass(String className) throws ClassNotFoundException {
        try {
            String classUrl = getClassPath(className);
            logger.debug("loading remote class " + className + " from " + classUrl);
            byte data[] = loadClassData(classUrl);
            if (data == null) {
                logger.debug("Class data is null");
                return null;
            }
            logger.debug("defining class " + className);
            try {
                return defineClass(className, data, 0, data.length);
            } catch (Exception e) {
                logger.error("Init class exception", e);
            }
        } catch (IOException e) {
            return Class.forName(className);
        }
        return null;
    }

    private String getClassPath(String className) {
        if (className == null)
            return null;
        if (className.indexOf(".") > 0) {
            return className.replaceAll("\\.", File.separator);
        }
        return CLASSPATH_ROOT + File.separator + this.codebase + File.separator + className + ".class";
    }


    /**
     * Read class as a byts array.
     *
     * @return byte[]
     */
    private byte[] loadClassData(String urlString) throws IOException {
        logger.debug("loadClassData by:" + urlString);
        FileInputStream in = new FileInputStream(urlString);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileChannel channel = in.getChannel();
        WritableByteChannel outchannel = Channels.newChannel(out);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            int i = channel.read(buffer);
            if (i == 0 || i == -1) {
                break;
            }
            buffer.flip();
            outchannel.write(buffer);
            buffer.clear();
        }
        byte[] bytes = out.toByteArray();
        out.close();
        in.close();
        return bytes;
    }

    public InputStream getResourceAsStream(String name) {
        String fullPath = CLASSPATH_ROOT + File.separator + this.codebase + File.separator + name;
        logger.debug("load resource from:" + fullPath);
        try {
            return new FileInputStream(fullPath);
        } catch (FileNotFoundException fe) {
            logger.error("spec:" + fullPath, fe);
            return null;
        }
    }


}
