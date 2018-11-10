package com.jcoroutine.common.tool;

import org.objectweb.asm.Type;

import java.lang.reflect.Method;

import static com.jcoroutine.common.constant.JCRConstant.*;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-16
 */
public class JCRTools {
    public static String genMethodIdentifier(Method method){
        return JCRTools.genMethodIdentifier(method.getDeclaringClass().getName(), method.getName(), Type.getMethodDescriptor(method));
    }

    public static String genMethodIdentifier(String className, String methodName, String methodDesc){
        className = className.replace("/", ".");
        return className + CLS_METHOD_SEPARATOR + methodName + methodDesc;
    }

    public static boolean isNotSystemClass(String className){
        for (String sysClsPkg : KNOWN_SYS_CLASS_PKG){
            if (className.replace('/', '.').startsWith(sysClsPkg)){
                return false;
            }
        }

        return true;
    }
}
