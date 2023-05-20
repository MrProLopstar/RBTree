package rbtree;
import java.awt.Color;
public class RBNode<T extends Comparable<T>>{
    private RBNode<T> left,right,parent;
    public static boolean RED=false, BLACK=true;
    private boolean color=RED;
    private T data;
    public RBNode(T data){ this.data=data; }
    //
    public T getData(){ return data; }
    public boolean getColor(){ return color; }
    public static boolean getColor(RBNode<?> node){ return node==null ? BLACK : node.getColor(); }
    public RBNode<T> getParent(){ return parent; }
    public static RBNode<?> getParent(RBNode<?> node){ return (node==null) ? null : node.getParent(); }
    public RBNode<T> getLeft(){ return left; }
    public static RBNode<?> getLeft(RBNode<?> node){ return node==null ? null : node.getLeft(); }
    public RBNode<T> getRight(){ return right; }
    public static RBNode<?> getRight(RBNode<?> node){ return node==null ? null : node.getRight(); }
    public Color getActualColor(){ return isRed() ? new Color(250,70,70) : new Color(70,70,70); }
    public RBNode<T> getGrandParent(){
        RBNode<T> parent = getParent();
        return parent==null ? null : parent.getParent();
    }
    public static RBNode<?> getGrandParent(RBNode<?> node){ return node==null ? null : node.getGrandParent(); }
    public RBNode<T> getSibling(){
        RBNode<T> parent = getParent();
        if(parent==null) return null;
        if(this==parent.getRight()) return (RBNode<T>) parent.getLeft();
        else return (RBNode<T>) parent.getRight();
    }
    public static RBNode<?> getSibling(RBNode<?> node){ return node==null ? null : node.getSibling(); }
    public RBNode<T> getUncle(){
        RBNode<T> parent = getParent();
        return parent==null ? null : parent.getSibling();
    }
    public static RBNode<?> getUncle(RBNode<?> node){ return node==null ? null : node.getUncle(); }
    //
    public void setData(T data){ this.data=data; }
    public void setColor(boolean color){ this.color=color; }
    public static void setColor(RBNode<?> node, boolean color){
        if(node==null) return;
        node.setColor(color);
    }
    public void toggleColor(){ color=!color; }
    public static void toggleColor(RBNode<?> node){ if(node!=null) node.setColor(!node.getColor()); }
    public void setParent(RBNode<T> parent){ this.parent=parent; }
    public void removeFromParent(){
        if(getParent()==null) return;
        if(parent.getLeft()==this) parent.setLeft(null);
        else if(parent.getRight()==this) parent.setRight(null);
        this.parent=null;
    }
    public void setLeft(RBNode<T> child){
        if(getLeft()!=null) getLeft().setParent(null);
        if(child!=null){
            child.removeFromParent();
            child.setParent(this);
        }
        this.left=child;
    }
    public void setRight(RBNode<T> child){
        if(getRight()!=null) getRight().setParent(null);
        if(child!=null){
            child.removeFromParent();
            child.setParent(this);
        }
        this.right=child;
    }
    //
    public boolean hasLeft(){ return left!=null; }
    public boolean hasRight(){ return right!=null; }
    public boolean isRed(){ return getColor()==RED; }
    public static boolean isRed(RBNode<?> node){ return getColor(node)==RED; }
    public boolean isBlack(){ return getColor()==BLACK; }
    public static boolean isBlack(RBNode<?> node){ return getColor(node)==BLACK; }

    //
    public String toString(){ return data.toString(); }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static int compare(RBNode<?> node, Comparable<?> b){ return ((Comparable) node.getData()).compareTo(b); }
}
