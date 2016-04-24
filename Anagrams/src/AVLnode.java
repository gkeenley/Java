public class AVLnode<K,V> implements Position<K,V> {
    private AVLnode<K,V> parent;          // reference to the parent node
    private AVLnode<K,V> left;            // reference to the left child
    private AVLnode<K,V> right;           // reference to the right child
    private DictEntry<K,V> entry;       // reference to the entry stored at the node
    private int height;                   // height of the node for checking balance-height property

    public AVLnode(DictEntry<K,V> inputEntry, AVLnode<K,V>  inputParent, AVLnode<K,V>  inputLeft, AVLnode<K,V>  inputRight)
    {
     entry  = inputEntry;
        parent = inputParent;
        left   = inputLeft;
        right  = inputRight;
        height = 0;
        if (left != null ) height  = Math.max(height,1+left.getHeight());
        if (right != null ) height = Math.max(height,1+right.getHeight()); 
    }

    public AVLnode<K,V>  parent(){ return parent;}
    public AVLnode<K,V>  left() {return left;}
    public AVLnode<K,V>  right() {return right;}
    public int getHeight () { return height; }
    public DictEntry<K,V> getEntry() { return entry; } 
    public void setParent(AVLnode<K,V>  newParent){ parent = newParent; }
    public void setLeft(AVLnode<K,V>  newLeft) {left = newLeft;}
    public void setRight(AVLnode<K,V>  newRight) { right = newRight; }  
    public void setEntry(DictEntry<K,V> newEntry) { entry = newEntry; }
    public DictEntry<K,V> element(){return entry;}

    public void resetHeight() throws AVLTreeException{
 if ( left == null || right == null ) throw  new AVLTreeException("Attempt to update height for external node ");
 //System.out.println("Height before resetting: "+height);
 height = 1+Math.max(left.getHeight(),right.getHeight());
 //System.out.println("Height after resetting: "+height);
    }
}


