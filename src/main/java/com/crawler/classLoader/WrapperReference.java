package com.crawler.classLoader;

import com.crawler.wrapper.BaseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * wrapper实例类
 */
public class WrapperReference {

    private static Logger logger = LoggerFactory.getLogger(WrapperReference.class);

    //public static final String DEFAULT_CODEBASE = "wrapper";

    private WrapperReference() {

    }

//    public static BaseWrapper getBaseWrapperRef(String className) {
//        BaseWrapper wrapperIns = null;
//        try {
//            wrapperIns = getBaseWrapperRef(DEFAULT_CODEBASE, className);
//        } catch (Exception e) {
//            logger.warn(e.getMessage(), e);
//        }
//        return wrapperIns;
//    }

    public static BaseWrapper getBaseWrapperRef(String codeBase, String className) {
        BaseWrapper wrapperIns = null;
        try {
            wrapperIns = (BaseWrapper) getWrapper(codeBase, className);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return wrapperIns;
    }

    /**
     * @param codeBase
     * @param className
     * @return
     * @throws Exception 根据wrapperid,取到wrapper代码的reference,Object类型
     */
    public static Object getWrapper(String codeBase, String className) throws Exception {
        TClassLoaderFactory factory = TClassLoaderFactory.getFactory();
        ClassLoader classloader;
        try {
            classloader = factory.getClassLoader(codeBase);
        } catch (Exception e) {
            logger.error("codeBase: {},className :{} error message :{}", codeBase, className, e.getMessage(), e);
            throw e;
        }
        Class<?> c;
        try {
            c = classloader.loadClass(className);
        } catch (Exception e) {
            logger.error("codeBase: {},className :{} ,error message:{}", codeBase, className, e.getMessage(), e);
            throw e;
        }
        Object wrapperIns;
        try {
            wrapperIns = c.newInstance();
        } catch (Exception e) {
            logger.error("codeBase: {},className :{} error message :{}", codeBase, className, e.getMessage(), e);
            throw e;
        }
        return wrapperIns;
    }
}
