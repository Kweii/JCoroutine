package com.jcoroutine.core.callSite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: guiliehua
 * Date: 2018/11/11
 * Time: 下午3:37
 * Desc:
 */
class TreeNode {
    private final String methodRef;

    private boolean isRoot = true;

    private final List<TreeNode> children;

    public TreeNode(String methodRef) {
        this.methodRef = methodRef;
        children = new ArrayList<TreeNode>();
    }

    public void appendChild(TreeNode child){
        children.add(child);
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public String getMethodRef() {
        return methodRef;
    }

    public boolean isRoot() {
        return isRoot;
    }
}
