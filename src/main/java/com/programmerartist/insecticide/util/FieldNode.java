package com.programmerartist.insecticide.util;


/**
 * Created by 程序员Artist on 16/3/22.
 */
public class FieldNode {

    private String name;
    private FieldNode parent;
    private FieldNode child;

    private boolean self=false;

    public FieldNode() {
    }

    /**
     *
     * @param name
     * @param parent
     * @param child
     * @param self
     */
    public FieldNode(String name, FieldNode parent, FieldNode child, boolean self) {
        this.name = name;
        this.parent = parent;
        this.child = child;
        this.self = self;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldNode getParent() {
        return parent;
    }

    public void setParent(FieldNode parent) {
        this.parent = parent;
    }

    public FieldNode getChild() {
        return child;
    }

    public void setChild(FieldNode child) {
        this.child = child;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    /**
     * 不可以同时打印以下两个属性,否则会死循环
     *
     * ", parent=" + parent +
     ", child=" + child +
     *
     * @return
     */
    @Override
    public String toString() {
        return "FieldNode{" +
                "name='" + name + '\'' +
                ", parent=" + parent +
                ", self=" + self +
                '}';
    }


    /**
     *
     * 得到顶层Node,不改变this对象本身
     *
     * @return
     */
    public FieldNode toRoot() {

        FieldNode root = this;
        int self = 0;
        for(;;) {
            if(null != root.getParent()) {
                root = root.getParent();
            }else {
                break ;
            }

            self++;
            if(self >= 20) {
                ExceptionUtill.throwError(ExceptionEnum.ILL_CODE_DIE_LOOP, "safe>=" + 20);
            }
        }

        return root;
    }

}
