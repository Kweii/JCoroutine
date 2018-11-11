package com.jcoroutine.core;

import com.jcoroutine.common.tool.JCoroutineTools;
import org.objectweb.asm.tree.ClassNode;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: guiliehua
 * Date: 2018/11/11
 * Time: 下午3:37
 * Desc:
 */
public class JCoroutineContext {
    private static final JCoroutineContext _this = new JCoroutineContext();

    private final Set<String> declaredJCRMethods;

    private JCoroutineContext() {
        declaredJCRMethods = new TreeSet<String>();
    }

    public static JCoroutineContext getContext() {
        return _this;
    }

    public void registerDeclaredJCR(ClassNode cn, org.objectweb.asm.tree.MethodNode mn) {
        String identifier = JCoroutineTools.genMethodRef(cn.name, mn.name, mn.desc);
        _this.declaredJCRMethods.add(identifier);
    }

    public Set<String> declaredJCRs() {
        return _this.declaredJCRMethods;
    }



}
