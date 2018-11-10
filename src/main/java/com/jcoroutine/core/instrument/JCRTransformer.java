package com.jcoroutine.core.instrument;

import com.jcoroutine.common.tool.JCRTools;
import com.jcoroutine.core.analysis.JCRAnalyzer;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-10
 */
public final class JCRTransformer implements ClassFileTransformer {
    private static Logger logger = Logger.getLogger(JCRTransformer.class);

    static {
        JCRAnalyzer.analyze();
    }

    public static void premain(String options, Instrumentation ins) throws UnmodifiableClassException {
        ins.addTransformer(new JCRTransformer());
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (JCRTools.isNotSystemClass(className)){
            return doTransform(classfileBuffer);
        }
        return null;
    }


    private byte[] doTransform(byte[] classfileBuffer){
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        for (Object obj : cn.methods) {
            MethodNode md = (MethodNode) obj;
            if ("<init>".endsWith(md.name) || "<clinit>".equals(md.name)) {
                continue;
            }
            InsnList insns = md.instructions;
            InsnList il = new InsnList();
            il.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System",
                    "out", "Ljava/io/PrintStream;"));
            il.add(new LdcInsnNode("Enter method-> " + cn.name+"."+md.name));
            il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                    "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
            insns.insert(il);
            md.maxStack += 3;

        }
        ClassWriter cw = new ClassWriter(0);
        cn.accept(cw);
        return cw.toByteArray();
    }








}
