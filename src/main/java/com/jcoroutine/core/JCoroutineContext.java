package com.jcoroutine.core;

import com.jcoroutine.common.tool.JCoroutineTools;
import com.jcoroutine.core.callSite.CallSiteNode;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

import static com.jcoroutine.common.constant.JCRConstant.BASE_SEPARATOR;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-16
 */
public class JCoroutineContext {
    private static final JCoroutineContext _this = new JCoroutineContext();

    private final Set<String> jcoroutineMethods;

    private final Set<CallSiteNode> callSites;

    private JCoroutineContext(){
        jcoroutineMethods = new TreeSet<String>();

        callSites = new TreeSet<CallSiteNode>();
    }

    public static JCoroutineContext getContext(){
        return _this;
    }

    public void registerJCoroutineMethod(String identifier){
        _this.jcoroutineMethods.add(identifier);
    }

    public Set<String> registeredJCRMethods(){
        return _this.jcoroutineMethods;
    }


    public boolean isJCoroutineMethod(Method method) throws Exception {
        String identifier = JCoroutineTools.genMethodIdentifier(method);
        return _this.jcoroutineMethods.contains(identifier);
    }

    public void registerCallSite(CallSiteNode callSiteNode){
        String caller = callSiteNode.caller;
        String callee = callSiteNode.callee;
        if (caller!=null && callee!=null){
            if (caller.contains(BASE_SEPARATOR) && callee.contains(BASE_SEPARATOR)){
                String calleeClsName = callee.split(BASE_SEPARATOR)[0];
                String callerClsName = caller.split(BASE_SEPARATOR)[0];
                if (JCoroutineTools.refersJCoroutinePackage(calleeClsName)&& JCoroutineTools.refersJCoroutinePackage(callerClsName)){
                    _this.callSites.add(callSiteNode);
                }
            }
        }
    }


    public Set<CallSiteNode> registeredCallSites(){
        return _this.callSites;
    }

}
