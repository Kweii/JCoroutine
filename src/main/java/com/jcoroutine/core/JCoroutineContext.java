package com.jcoroutine.core;

import com.jcoroutine.common.tool.JCoroutineTools;
import com.jcoroutine.core.callSite.InvocationEdge;
import com.jcoroutine.core.callSite.MethodNode;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-16
 */
public class JCoroutineContext {
    private static final JCoroutineContext _this = new JCoroutineContext();

    private final Set<String> declaredJCRMethods;

    private final Set<InvocationEdge> methodInvocations;

    private final Map<String, MethodNode> methodNodes;

    private JCoroutineContext() {
        declaredJCRMethods = new TreeSet<String>();
        methodInvocations = new TreeSet<InvocationEdge>();
        methodNodes = new HashMap<String, MethodNode>();
    }

    public static JCoroutineContext getContext() {
        return _this;
    }

    public void registerDeclaredJCR(ClassNode cn, org.objectweb.asm.tree.MethodNode mn) {
        String identifier = JCoroutineTools.genMethodIdentifier(cn.name, mn.name, mn.desc);
        _this.declaredJCRMethods.add(identifier);
    }

    public Set<String> declaredJCRs() {
        return _this.declaredJCRMethods;
    }

    public void registerMethod(MethodNode mn){
        String identifier = JCoroutineTools.genMethodIdentifier(mn);
        _this.methodNodes.put(identifier, mn);
    }

    public MethodNode registeredMethod(String identifier){
        return _this.methodNodes.get(identifier);
    }

    public void registerInvocation(InvocationEdge edge) {
        _this.methodInvocations.add(edge);
    }


    public Set<InvocationEdge> invocations() {
        return _this.methodInvocations;
    }

}
