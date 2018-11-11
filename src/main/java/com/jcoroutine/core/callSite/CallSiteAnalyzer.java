package com.jcoroutine.core.callSite;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.jcoroutine.common.tool.JCoroutineTools;
import com.jcoroutine.core.JCoroutineContext;
import com.jcoroutine.core.JCoroutine;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-15
 */
public class CallSiteAnalyzer implements Opcodes {
    private static Logger logger = Logger.getLogger(CallSiteAnalyzer.class);

    //为callSite分析做准备，通过自定义类加载器加载类路径下的所有类
    public static void preAnalyze() {
        try {
            ClassLoader analyzeClsLoader = CallSiteClassLoader.singleton();
            ClassPath classPath = ClassPath.from(analyzeClsLoader);
            ImmutableSet<ClassPath.ClassInfo> classInfos = classPath.getAllClasses();
            for (ClassPath.ClassInfo classInfo : classInfos) {
                //只分析指定jar包下的类
                if (JCoroutineTools.refersJCoroutinePackage(classInfo.getName())) {
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


    public static void analyze(){
        JCoroutineContext context = JCoroutineContext.getContext();
        for (String declaredJCR : context.declaredJCRs()){
            for (InvocationEdge invocation : context.invocations()){
                String caller = invocation.getCaller();
                if (declaredJCR.equals(caller)){
                    context.registeredMethod(caller).setDeclaredJCR(true);
                }

                String callee = invocation.getCallee();
                if (declaredJCR.equals(callee)){
                    context.registeredMethod(callee).setDeclaredJCR(true);
                }
            }
        }

        Set<InvocationEdge> invocations = context.invocations();
    }


    static void analyzeClassMethods(byte[] bytes) {
        JCoroutineContext context = JCoroutineContext.getContext();
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, F_FULL);
        List<org.objectweb.asm.tree.MethodNode> methodNodes = classNode.methods;
        for (org.objectweb.asm.tree.MethodNode methodNode : methodNodes) {
            //分析类中的每一个方法，如果是被@JCoroutine修饰的方法，需要注册到上下文中
            if (methodNode.visibleAnnotations != null) {
                List<AnnotationNode> annotations = methodNode.visibleAnnotations;
                for (AnnotationNode annotation : annotations) {
                    if (Type.getDescriptor(JCoroutine.class).equals(annotation.desc)) {
                        context.registerDeclaredJCR(classNode, methodNode);
                    }
                }
            }
            //分析方法中字节码，分析每一个方法调用
            InsnList instructions = methodNode.instructions;
            Iterator<AbstractInsnNode> iterator = instructions.iterator();
            while (iterator.hasNext()) {
                AbstractInsnNode instruction = iterator.next();
                int opCode = instruction.getOpcode();
                if (INVOKEVIRTUAL <= opCode && opCode <= INVOKEINTERFACE) {
                    MethodInsnNode insNode = (MethodInsnNode) instruction;
                    //将涉及到指定jar包的每一个方法调用注册到上下文中
                    if (!"<init>".equals(insNode.name) && !"<clinit>".equals(insNode.name)
                            && JCoroutineTools.refersJCoroutinePackage(insNode.owner)) {

                        MethodNode caller = MethodNode.build(classNode.name, methodNode.name, methodNode.desc);
                        MethodNode callee = MethodNode.build(insNode.owner, insNode.name, insNode.desc);

                        context.registerMethod(caller);
                        context.registerMethod(callee);

                        InvocationEdge edge = new InvocationEdge(JCoroutineTools.genMethodIdentifier(caller),
                                JCoroutineTools.genMethodIdentifier(callee), instructions.indexOf(instruction));

                        context.registerInvocation(edge);
                    }
                }
            }
        }
    }
}
