package com.jcoroutine.core.callSite;

import com.google.common.base.Objects;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-16
 */
public class MethodNode {
    //方法在哪个类中声明
    private String owner;
    //方法名
    private String name;
    //方法描述
    private String desc;
    //是否被@JCoroutine修饰
    private Boolean declaredJCR;
    //是否需要被transform
    private boolean toTransform;


    private MethodNode(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.declaredJCR = false;
        this.toTransform = false;
    }

    public static MethodNode build(String owner, String name, String desc) {
        return new MethodNode(owner, name, desc);
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isDeclaredJCR() {
        return declaredJCR;
    }

    public void setDeclaredJCR(Boolean declaredJCR) {
        this.declaredJCR = declaredJCR;
    }

    public void setToTransform(boolean toTransform) {
        this.toTransform = toTransform;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodNode that = (MethodNode) o;
        return
                Objects.equal(owner, that.owner) &&
                Objects.equal(name, that.name) &&
                Objects.equal(desc, that.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(owner, name, desc);
    }

    @Override
    public String toString() {
        return "MethodNode{" +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", declaredJCR=" + declaredJCR +
                '}';
    }
}
