package com.jcoroutine.core;

import com.jcoroutine.common.tool.JCRTools;
import com.jcoroutine.core.analysis.Invocation;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

import static com.jcoroutine.common.constant.JCRConstant.CLS_METHOD_SEPARATOR;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-16
 */
public class JCRContext {
    private static final JCRContext SINGLETON = new JCRContext();

    private final Set<String> DECLARED_JCR_METHODS;

    private final Set<Invocation> METHOD_INVOCATIONS;

    private JCRContext(){
        DECLARED_JCR_METHODS = new TreeSet<String>();

        METHOD_INVOCATIONS = new TreeSet<Invocation>();
    }

    public static JCRContext getContext(){
        return SINGLETON;
    }

    public void registerJCRMethod(String identifier){
        SINGLETON.DECLARED_JCR_METHODS.add(identifier);
    }

    public Set<String> registeredJCRMethods(){
        return SINGLETON.DECLARED_JCR_METHODS;
    }


    public boolean isJCRMethod(Method method) throws Exception {
        String identifier = JCRTools.genMethodIdentifier(method);
        return SINGLETON.DECLARED_JCR_METHODS.contains(identifier);
    }

    public void registerInvocation(Invocation invocation){
        String caller = invocation.caller;
        String callee = invocation.callee;
        if (caller!=null && callee!=null){
            if (caller.contains(CLS_METHOD_SEPARATOR) && callee.contains(CLS_METHOD_SEPARATOR)){
                String calleeClsName = callee.split(CLS_METHOD_SEPARATOR)[0];
                String callerClsName = caller.split(CLS_METHOD_SEPARATOR)[0];
                if (JCRTools.isNotSystemClass(calleeClsName)&&JCRTools.isNotSystemClass(callerClsName)){
                    SINGLETON.METHOD_INVOCATIONS.add(invocation);
                }
            }
        }
    }


    public Set<Invocation> registeredInvocations(){
        return SINGLETON.METHOD_INVOCATIONS;
    }

}
