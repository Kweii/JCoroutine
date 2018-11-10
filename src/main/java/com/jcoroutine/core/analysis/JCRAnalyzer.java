package com.jcoroutine.core.analysis;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.jcoroutine.common.tool.JCRTools;
import com.jcoroutine.core.JCRContext;
import com.jcoroutine.core.JCoroutine;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;
import java.util.jar.Manifest;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-15
 */
public class JCRAnalyzer implements Opcodes {
    private static Logger logger = Logger.getLogger(JCRAnalyzer.class);

    public static void analyze() {
        try {
            ClassLoader analyzeClsLoader = AnalyzeClassLoader.singleton();
            ClassPath classPath = ClassPath.from(analyzeClsLoader);
            ImmutableSet<ClassPath.ClassInfo> classInfos = classPath.getAllClasses();
            for (ClassPath.ClassInfo classInfo : classInfos) {
                if (JCRTools.isNotSystemClass(classInfo.getName())) {
                    try {
                        classInfo.load();
                    } catch (Throwable e) {
                        logger.warn(String.format("JCoroutine Framework failed to scan  @JCoroutine methods of class %s.class, error message:%s", classInfo.getName(), e.getMessage()));
                    }
                }
            }


        } catch (Throwable e) {
            logger.error("JCoroutine Framework failed to scan @JCoroutine methods, and program runs with no @JCoroutine!!!");
            e.printStackTrace();
        }
    }

    static void doAnalyze(byte[] clsBytes) {
        ClassReader cr = new ClassReader(clsBytes);
        ClassNode cn = new ClassNode();
        cr.accept(cn, F_FULL);
        List<MethodNode> mns = cn.methods;
        for (MethodNode mn : mns) {
            if (mn.visibleAnnotations != null) {
                List<AnnotationNode> annotations = mn.visibleAnnotations;
                for (AnnotationNode annotation : annotations) {
                    if (Type.getDescriptor(JCoroutine.class).equals(annotation.desc)) {
                        String identifier = JCRTools.genMethodIdentifier(cn.name, mn.name, mn.desc);
                        JCRContext.getContext().registerJCRMethod(identifier);
                    }
                }
            }

            InsnList instructions = mn.instructions;
            Iterator<AbstractInsnNode> iterator = instructions.iterator();
            while (iterator.hasNext()) {
                AbstractInsnNode instruction = iterator.next();
                int opCode = instruction.getOpcode();
                if (INVOKEVIRTUAL <= opCode && opCode <= INVOKEINTERFACE) {
                    MethodInsnNode min = (MethodInsnNode) instruction;
                    if (JCRTools.isNotSystemClass(min.owner) && !"<init>".equals(min.name) && !"<clinit>".equals(min.name)) {
                        String caller = JCRTools.genMethodIdentifier(cn.name, mn.name, mn.desc);
                        String callee = JCRTools.genMethodIdentifier(min.owner, min.name, min.desc);
                        Invocation invocation = Invocation.build(caller, callee);
                        JCRContext.getContext().registerInvocation(invocation);
                    }
                }
            }
        }
    }

    void analyzeInvocation(){
        Set<Invocation> invocations = JCRContext.getContext().registeredInvocations();
        Map<String, Invocation> invocationMapping = new HashMap<String, Invocation>();
        for (Invocation invocation : invocations){

        }
    }
}
