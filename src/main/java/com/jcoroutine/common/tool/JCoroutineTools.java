package com.jcoroutine.common.tool;

import com.jcoroutine.core.callSite.MethodEntity;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

import static com.jcoroutine.common.constant.JCRConstant.*;

/**
 * Created with IntelliJ IDEA.
 * User: guiliehua
 * Date: 2018/11/11
 * Time: 下午3:37
 * Desc:
 */
public class JCoroutineTools {
    public static String genMethodRef(Method method) {
        return JCoroutineTools.genMethodRef(method.getDeclaringClass().getName(), method.getName(), Type.getMethodDescriptor(method));
    }

    public static String genMethodRef(MethodEntity mn){
        return genMethodRef(mn.getOwner(), mn.getName(), mn.getDesc());
    }

    public static String genMethodRef(String className, String methodName, String methodDesc) {
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
