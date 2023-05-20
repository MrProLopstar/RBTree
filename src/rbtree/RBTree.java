package rbtree;

import java.util.ArrayList;
import java.util.List;

public class RBTree<T extends Comparable<T>>{
    protected RBNode<T> root;
    protected int size=0;
    public RBNode<T> getRoot(){ return this.root; }
    public void clear(){ this.root=null; }
    public boolean isEmpty(){ return this.root==null; }
    public void insert(T item){
        if(this.isEmpty()) this.root = new RBNode<T>(item);
        else this.insert(this.root,item);
        this.root.setColor(RBNode.BLACK);
        ++this.size;
    }
    public void insert(RBNode<T> node, T item){
        if(this.contains(item)) return;
        if(node.getData().compareTo(item)>0){
            if(node.hasLeft()) this.insert(node.getLeft(),item);
            else {
                RBNode<T> inserted = new RBNode<T>(item);
                node.setLeft(inserted);
                this.balanceAfterInsert(inserted);
            }
        } else if(node.hasRight()) this.insert(node.getRight(),item);
        else {
            RBNode<T> inserted = new RBNode<T>(item);
            node.setRight(inserted);
            this.balanceAfterInsert(inserted);
        }
    }
    public void remove(T data){
        if(!this.contains(data)) return;
        RBNode<T> node = this.find(data);
        if(node.getLeft()!=null && node.getRight()!=null){
            RBNode<T> suc = this.getSuc(node);
            node.setData(suc.getData()); node=suc;
        }
        RBNode<T> pullUp = node.getLeft()==null ? node.getRight() : node.getLeft();
        if(pullUp!=null){
            if(node==this.root){
                node.removeFromParent();
                this.root=node;
            } else if(RBNode.getLeft(node.getParent())==node) node.getParent().setLeft(pullUp);
            else node.getParent().setRight(pullUp);
            if(RBNode.isBlack(node)) this.balanceAfterDelete(pullUp);
        } else if(node==this.root) this.root=null;
        else {
            if(RBNode.isBlack(node)) this.balanceAfterDelete(node);
            node.removeFromParent();
        }
    }
    private boolean contains(RBNode<T> root, T data){
        if(root==null) return false;
        if(root.getData().compareTo(data)>0) return this.contains(root.getLeft(),data);
        if(root.getData().compareTo(data)<0) return this.contains(root.getRight(),data);
        return true;
    }
    public boolean contains(T data){ return this.contains(this.root,data); }
    private RBNode<T> find(RBNode<T> root, T data){
        if(root==null) return null;
        if(root.getData().compareTo(data)>0) return this.find(root.getLeft(),data);
        if(root.getData().compareTo(data)<0) return this.find(root.getRight(),data);
        return root;
    }
    public RBNode<T> find(T data){ return this.find(this.root,data); }
    private int getDepth(RBNode<T> node){
        if(node==null) return 0;
        int right_depth, left_depth = this.getDepth(node.getLeft());
        return left_depth>(right_depth=this.getDepth(node.getRight())) ? left_depth+1 : right_depth+1;
    }
    public int getDepth(){ return this.getDepth(this.root); }
    private RBNode<T> getSuc(RBNode<T> root){
        RBNode<T> leftTree = root.getLeft();
        if(leftTree!=null) while(leftTree.getRight()!=null) leftTree=leftTree.getRight();
        return leftTree;
    }
    private void balanceAfterInsert(RBNode<T> node){
        if(node==null || node==this.root || RBNode.isBlack(node.getParent())) return;
        if(RBNode.isRed(node.getUncle())){
            RBNode.toggleColor((node.getParent()));
            RBNode.toggleColor(node.getUncle());
            RBNode.toggleColor(node.getGrandParent());
            this.balanceAfterInsert(node.getGrandParent());
        } else if(this.hasLeftParent(node)){
            if(this.isRightChild(node)){
                node = node.getParent();
                this.rotateLeft(node);
            }
            RBNode.setColor(node.getParent(),RBNode.BLACK);
            RBNode.setColor(node.getGrandParent(),RBNode.RED);
            this.rotateRight(node.getGrandParent());
        } else if(this.hasRightParent(node)){
            if(this.isLeftChild(node)){
                node=node.getParent();
                this.rotateRight(node);
            }
            RBNode.setColor(node.getParent(),RBNode.BLACK);
            RBNode.setColor(node.getGrandParent(),RBNode.RED);
            this.rotateLeft(node.getGrandParent());
        }
    }
    private void balanceAfterDelete(RBNode<T> node){
        while (node!=this.root && node.isBlack()){
            RBNode<T> sibling = node.getSibling();
            if(node==RBNode.getLeft(node.getParent())){
                if(RBNode.isRed(sibling)){
                    RBNode.setColor(sibling,RBNode.BLACK);
                    RBNode.setColor(node.getParent(),RBNode.RED);
                    this.rotateLeft(node.getParent());
                    sibling = (RBNode<T>) RBNode.getRight(node.getParent());
                }
                if(RBNode.isBlack(RBNode.getLeft(sibling)) && RBNode.isBlack(RBNode.getRight(sibling))){
                    RBNode.setColor(sibling,RBNode.RED);
                    node = node.getParent();
                    continue;
                }
                if(RBNode.isBlack(RBNode.getRight(sibling))){
                    RBNode.setColor(RBNode.getLeft(sibling),RBNode.BLACK);
                    RBNode.setColor(sibling,RBNode.RED);
                    this.rotateRight(sibling);
                    sibling = (RBNode<T>) RBNode.getRight(node.getParent());
                }
                RBNode.setColor(sibling,RBNode.getColor(node.getParent()));
                RBNode.setColor(node.getParent(),RBNode.BLACK);
                RBNode.setColor(RBNode.getRight(sibling),RBNode.BLACK);
                this.rotateLeft(node.getParent());
                node = this.root;
                continue;
            }
            if(RBNode.isRed(sibling)){
                RBNode.setColor(sibling,RBNode.BLACK);
                RBNode.setColor(node.getParent(),RBNode.RED);
                this.rotateRight(node.getParent());
                sibling = (RBNode<T>) RBNode.getLeft(node.getParent());
            }
            if(RBNode.isBlack(RBNode.getLeft(sibling)) && RBNode.isBlack(RBNode.getRight(sibling))){
                RBNode.setColor(sibling,RBNode.RED);
                node = node.getParent();
                continue;
            }
            if(RBNode.isBlack(RBNode.getLeft(sibling))){
                RBNode.setColor(RBNode.getRight(sibling), RBNode.BLACK);
                RBNode.setColor(sibling,RBNode.RED);
                this.rotateLeft(sibling);
                sibling = (RBNode<T>) RBNode.getLeft(node.getParent());
            }
            RBNode.setColor(sibling,RBNode.getColor(node.getParent()));
            RBNode.setColor(node.getParent(),RBNode.BLACK);
            RBNode.setColor(RBNode.getLeft(sibling),RBNode.BLACK);
            this.rotateRight(node.getParent());
            node = this.root;
        }
        RBNode.setColor(node,RBNode.BLACK);
    }

    private void rotateRight(RBNode<T> node){
        if(node.getLeft()==null) return;
        RBNode<T> leftTree = node.getLeft();
        node.setLeft(leftTree.getRight());
        if(node.getParent()==null) this.root = leftTree;
        else if(node.getParent().getLeft()==node) node.getParent().setLeft(leftTree);
        else node.getParent().setRight(leftTree);
        leftTree.setRight(node);
    }

    private void rotateLeft(RBNode<T> node){
        if(node.getRight()==null) return;
        RBNode<T> rightTree = node.getRight();
        node.setRight(rightTree.getLeft());
        if(node.getParent()==null) this.root = rightTree;
        else if (node.getParent().getLeft()==node) node.getParent().setLeft(rightTree);
        else node.getParent().setRight(rightTree);
        rightTree.setLeft(node);
    }
    public List<RBNode<T>> getNodesInOrder() {
        List<RBNode<T>> nodes = new ArrayList<>();
        inOrderTraversal(root, nodes);
        return nodes;
    }

    private void inOrderTraversal(RBNode<T> node, List<RBNode<T>> nodes) {
        if (node == null) {
            return;
        }

        inOrderTraversal(node.getLeft(), nodes);
        nodes.add(node);
        inOrderTraversal(node.getRight(), nodes);
    }

    private boolean hasRightParent(RBNode<T> node){ return RBNode.getRight(node.getGrandParent())==node.getParent(); }

    private boolean hasLeftParent(RBNode<T> node){ return RBNode.getLeft(node.getGrandParent())==node.getParent(); }

    private boolean isRightChild(RBNode<T> node){ return RBNode.getRight(node.getParent())==node; }

    private boolean isLeftChild(RBNode<T> node){ return RBNode.getLeft(node.getParent())==node; }
}