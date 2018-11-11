package com.jcoroutine.core.callSite;

import com.jcoroutine.common.tool.JCoroutineTools;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: guiliehua
 * Date: 2018/11/11
 * Time: 下午3:47
 * Desc:
 */
public class CallSiteHolder {
    private final static Set<Invocation> invocations =  new TreeSet<Invocation>();

    private final static Map<String, MethodEntity> methodEntities = new HashMap<String, MethodEntity>();

    private final static Map<String, TreeNode> callSites = new HashMap<String, TreeNode>();

    public static Map<String, TreeNode> callSites(){
        return callSites;
    }

    public static TreeNode registeredNode(String methodRef){
        return callSites.get(methodRef);
    }

    public static void registerNode(TreeNode treeNode){
        callSites.put(treeNode.getMethodRef(), treeNode);
    }

    public static void registerMethod(MethodEntity entity){
        String identifier = JCoroutineTools.genMethodRef(entity);
        methodEntities.put(identifier, entity);
    }

    public static MethodEntity registeredMethod(String identifier){
        return methodEntities.get(identifier);
    }

    public static void registerInvocation(Invocation invocation) {
        invocations.add(invocation);
    }


    public static Set<Invocation> invocations() {
        return invocations;
    }
}
