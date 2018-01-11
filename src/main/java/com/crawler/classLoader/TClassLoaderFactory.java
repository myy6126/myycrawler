package com.crawler.classLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassLoader factory, Select FileClasslaoder or NetworkClassLoader by rdfconfiguation.properties.
 *
 * @author caozhaorui
 */
public class TClassLoaderFactory {

    private final static Logger logger = LoggerFactory.getLogger(TClassLoaderFactory.class);

    public static final String LOADER_NAME = "com.crawler.classloader.name";
    public static final String NETROOT_URL = "com.crawler.classloader.NetworkClassLoader";
    public static final String FILEROOT_PATH = "com.crawler.classloader.FileClassLoader";

    // private static transient ClassLoader classloader = null;
    private Map<String, ClassLoader> loaderMap = null;

    private static TClassLoaderFactory factory = new TClassLoaderFactory();

    public Properties conf = new Properties();

    private TClassLoaderFactory() {
        this.loaderMap = new ConcurrentHashMap<>();
        InputStream in = FileClassLoader.class.getResourceAsStream("/wrapperConfig.properties");
        try {
            conf.load(in);
            logger.debug("TClassLoaderFactory init:" + LOADER_NAME + this.conf.getProperty(LOADER_NAME));
            logger.debug("TClassLoaderFactory init:" + NETROOT_URL + this.conf.getProperty(NETROOT_URL));
            logger.debug("TClassLoaderFactory init:" + FILEROOT_PATH + this.conf.getProperty(FILEROOT_PATH));
        } catch (IOException ie) {
            logger.error("Init classpath exception", ie);
        }
    }

    /**
     * Implements factory pattern.
     *
     * @return
     */
    public static TClassLoaderFactory getFactory() {
        return factory;
    }

    protected String getPropertyValue(String propertyName) {
        return this.conf.getProperty(propertyName);
    }

    /**
     * Create new classloader object for this wrapper. default classloader is FileClassLoader.
     *
     * @param codebase
     * @return ClassLoader
     */
    public ClassLoader getClassLoader(String codebase) {
        long startTime = System.currentTimeMillis();
        if (!this.loaderMap.containsKey(codebase)) {
            synchronized (this.loaderMap) {
                if (this.loaderMap.containsKey(codebase))
                    return this.loaderMap.get(codebase);
                try {
                    Class<?> cl = Class.forName(this.conf.getProperty(LOADER_NAME));
                    Constructor<?> constructor = cl.getConstructor(String.class);
                    logger.info("create new ClassLoader for:" + codebase + " consume:"
                            + (System.currentTimeMillis() - startTime) + " (ms)");
                    this.loaderMap.put(codebase, (ClassLoader) constructor.newInstance(codebase));
                } catch (ClassNotFoundException cne) {
                    logger.error("init classloader failed. system occure fetal error.!!!" + codebase, cne);
                } catch (Exception e) {
                    logger.error("codebase:" + codebase + "get classloader failed.", e);
                }
            }
        } else {
            logger.debug("retrieve classloader from cache map, codebase:" + codebase);
        }
        return this.loaderMap.get(codebase);
    }

    public void reloadWrapper(String codebase) {
        if (loaderMap.containsKey(codebase)) {
            synchronized (this.loaderMap) {
                loaderMap.remove(codebase);
                logger.info("Wrapper classes for codebase:" + codebase + " were removed!");
            }
        }
    }

    public void reloadAll() {
        synchronized (this.loaderMap) {
            loaderMap.clear();
            logger.info("Wrapper classes for all codebase were removed!");
        }
    }

}
