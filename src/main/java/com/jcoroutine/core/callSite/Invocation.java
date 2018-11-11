package com.jcoroutine.core.callSite;

import com.google.common.base.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: guiliehua
 * Date: 2018/11/10
 * Time: 下午10:45
 * Desc: 表示一条调用信息
 */
class Invocation implements Comparable<Invocation>{
    //调用者
    private String callerRef;
    //被调用者
    private String calleeRef;
    //调用行数
    private Integer lineNum;
    //调用层次，从上往下依次增加
    private Integer callLevel = -1;
    //是否递归调用
    private Boolean isRecursive;

    Invocation(String callerRef, String calleeRef, Integer lineNum) {
        this.callerRef = callerRef;
        this.calleeRef = calleeRef;
        this.lineNum = lineNum;
        this.isRecursive = calleeRef.equals(callerRef);
    }

    public void setCallLevel(Integer callLevel) {
        this.callLevel = callLevel;
    }

    public String getCallerRef() {
        return callerRef;
    }

    public String getCalleeRef() {
        return calleeRef;
    }

    public Integer getCallLevel() {
        return callLevel;
    }

    public Boolean getRecursive() {
        return isRecursive;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invocation edge = (Invocation) o;
        return Objects.equal(callerRef, edge.callerRef) &&
                Objects.equal(calleeRef, edge.calleeRef) &&
                Objects.equal(lineNum, edge.lineNum);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(callerRef, calleeRef, lineNum);
    }

    @Override
    public int compareTo(Invocation o) {
        return this.callerRef.toString().compareTo(o.callerRef.toString())
                +this.calleeRef.toString().compareTo(o.calleeRef.toString());
    }
}
