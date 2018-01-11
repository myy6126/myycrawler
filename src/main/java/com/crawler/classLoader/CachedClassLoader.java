package com.crawler.classLoader;

import java.util.HashMap;

/**
 * @author caozhaorui
 */
public abstract class CachedClassLoader extends ClassLoader {
    protected HashMap<String, Class<?>> cache = null;
    protected String codebase;

    public CachedClassLoader(String codebase) {
        super();
        this.codebase = codebase;
        this.cache = new HashMap<>();
    }

    /**
     * 具有缓存功能的classLoader, 不遵从双亲委派机制, 保证完全隔离.
     *
     * @param classname
     * @param resolve
     * @return
     * @throws ClassNotFoundException
     */
    protected synchronized Class<?> loadClass(String classname, boolean resolve) throws ClassNotFoundException {
        Class<?> c = this.cache.get(classname);
        if (c == null) {
            c = this.newClass(classname);
            cache.put(classname, c);
            if (resolve) {
                resolveClass(c);
            }
        }
        return c;
    }

    /**
     * Abstract method for create new class object.
     *
     * @return
     */
    abstract Class<?> newClass(String className) throws ClassNotFoundException;

    public String getCodebase() {
        return codebase;
    }


}
