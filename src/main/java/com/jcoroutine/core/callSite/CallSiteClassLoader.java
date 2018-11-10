package com.jcoroutine.core.callSite;

import com.jcoroutine.common.tool.JCoroutineTools;
import sun.misc.Launcher;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.jar.Manifest;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-17
 */
class CallSiteClassLoader extends URLClassLoader {
    private static CallSiteClassLoader singleton = null;

    private CallSiteClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    static ClassLoader singleton() throws Exception {
        if (singleton == null) {
            synchronized (CallSiteClassLoader.class) {
                if (singleton == null) {
                    String classPath;
                    URL[] urls;
                    if ((classPath = System.getProperty("java.class.path")) == null) {
                        urls = new URL[0];
                    } else {
                        Method getClassPath = Launcher.class.getDeclaredMethod("getClassPath", String.class);
                        Method pathToUrls = Launcher.class.getDeclaredMethod("pathToURLs", File[].class);

                        getClassPath.setAccessible(true);
                        pathToUrls.setAccessible(true);

                        File[] files = (File[]) getClassPath.invoke(null, classPath);
                        urls = (URL[]) pathToUrls.invoke(null, new Object[]{files});
                    }
                    //设置扩展类加载器为父类加载器，不能使用当前上下文类加载器，否则无法分析
                    singleton = new CallSiteClassLoader(urls, ClassLoader.getSystemClassLoader().getParent());
                }
            }
        }

        return singleton;
    }


    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        Class<?> result = null;
        try {
            Class superCls = getClass().getSuperclass();
            Field ucpField = superCls.getDeclaredField("ucp");
            ucpField.setAccessible(true);
            final URLClassPath ucp = (URLClassPath) ucpField.get(this);

            String path = name.replace('.', '/').concat(".class");
            Resource res = ucp.getResource(path, false);
            result = defineClass(name, res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new ClassNotFoundException(name);
        }
        return result;
    }

    //搬自URLClassloader的defineClass(String name, Resource res)
    private Class<?> defineClass(String name, Resource res) throws Exception {
        long t0 = System.nanoTime();
        int i = name.lastIndexOf('.');
        URL url = res.getCodeSourceURL();
        if (i != -1) {
            String pkgname = name.substring(0, i);
            Manifest man = res.getManifest();
            Method method = URLClassLoader.class.getDeclaredMethod("definePackageInternal", new Class[]{String.class, Manifest.class, URL.class});
            method.setAccessible(true);
            method.invoke(this, pkgname, man, url);
        }
        byte[] bytes = res.getBytes();
        if (JCoroutineTools.refersJCoroutinePackage(name)) {
            CallSiteAnalyzer.analyzeClassMethods(bytes);
        }

        CodeSigner[] signers = res.getCodeSigners();
        CodeSource cs = new CodeSource(url, signers);
        sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
        return defineClass(name, bytes, 0, bytes.length, cs);
    }
}
