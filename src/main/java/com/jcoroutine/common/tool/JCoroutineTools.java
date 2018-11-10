package com.jcoroutine.common.tool;

import org.objectweb.asm.Type;

import java.lang.reflect.Method;

import static com.jcoroutine.common.constant.JCRConstant.*;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-16
 */
public class JCoroutineTools {
    public static String genMethodIdentifier(Method method) {
        return JCoroutineTools.genMethodIdentifier(method.getDeclaringClass().getName(), method.getName(), Type.getMethodDescriptor(method));
    }

    public static String genMethodIdentifier(String className, String methodName, String methodDesc) {
        className = className.replace("/", ".");
        return className + BASE_SEPARATOR + methodName + BASE_SEPARATOR + methodDesc;
    }

    public static boolean refersJCoroutinePackage(String className) {
        for (String basePackage : jcoroutineBasePackages) {
            if (className.replace('/', '.').startsWith(basePackage)) {
                return true;
            }
        }

        return false;
    }
}
