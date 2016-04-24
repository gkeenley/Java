import java.util.*;
//import java.util.Iterator;
//import java.util.LinkedList;

public class AVLTree< K, V >{
    
  // START Private Fields
  
  private Comparator comparator;
  private int numEntries;
  private int numNodes;
  private AVLnode<K,V> root;
  private boolean traversalFlag;
  private AVLnode<K,V> inOrderSuccessor;
  
  // END Private Fields
  
  
  // START Constructors
  
  public AVLTree(Comparator<K> inputComparator){
    // Determine whether input comparator is null.
    if (inputComparator==null){ 
      System.out.println("Null input comparator.");
      System.exit(0);
    }
    
    // Initialize fields of tree.
    comparator=inputComparator;
    root=new AVLnode<K,V>(null,null,null,null); // Insert root node only.
    numEntries=0; // numEntries still 0 because we have not inserted any entries.
    numNodes=1; // numNodes is 1 because we have inserted the root node.
    traversalFlag=false;
    inOrderSuccessor=null;
  }
  
  // END Constructors
  
  
  // START Public Methods
  
  public boolean external(Position< K, V > p){
    if (p.element()==null){ // If p does not contain an entry (leaves do not contain values)...
      return true;
    } else{ // If p DOES contain an entry...
      return false;
    }
  }
  
  public Position < K, V > left(Position< K, V > p){
    AVLnode<K,V> P=(AVLnode<K,V>)p; // Cast as AVLnode because Position interface does not include left() method.
    return P.left();
  }
  
  public Position < K, V > right(Position< K, V > p){
    AVLnode<K,V> P=(AVLnode<K,V>)p; // Cast as AVLnode because Position interface does not include right() method.
    return P.right();
  }
  
  public DictEntry< K, V > find(K key){
    AVLnode<K,V> node=(AVLnode<K,V>)giveRoot();  // Cast as AVLnode because Position interface does not include the methods we need.
    while (node.element()!=null){ // While we have not yet hit a leaf...
      int comparison=comparator.compare(node.getEntry().key(),key); // ...compare key of entry at node with input key.
      if (comparison==0){ // If the keys are equal...
        return node.getEntry(); // Return entry at node.
      } else if (comparison>0){ // If key of entry at node is greater than input key...
        node=node.left(); // Move to left child of node.
        continue;
      } else { // If key of entry at node is less than input key...
        node=node.right(); // Move to right child of node.
        continue;
      }
    }
    // At this point, we have exited loop without finding key. Therefore, key is not present in tree.
    return null;
  }
  
  public Iterator < DictEntry < K, V >> findAll(K key){
    
    LinkedList<DictEntry<K,V>> list=new LinkedList<DictEntry<K,V>>();
    
    // Find highest node containing entry with key, and if one doesn't exist, return null.
    AVLnode<K,V> foundNode=null;
    AVLnode<K,V> node=(AVLnode<K,V>)giveRoot();  // Cast as AVLnode because Position interface does not include the methods we need.
    while (node.element()!=null){ // While we have not yet hit a leaf...
      int comparison=comparator.compare(node.getEntry().key(),key); // ...compare key of entry at node with input key.
      if (comparison==0){ // If the keys are equal...
        foundNode=node; // Return entry at node.
        break;
      } else if (comparison>0){ // If key of entry at node is greater than input key...
        node=node.left(); // Move to left child of node.
        continue;
      } else { // If key of entry at node is less than input key...
        node=node.right(); // Move to right child of node.
        continue;
      }
    }
    if (foundNode==null){
      return null;
    }
    //System.out.println("node is "+foundNode.right().getEntry().key()+" "+foundNode.right().getEntry().value());
    
    // At this point, we have found a node containing entry with key, so we search left and right subtrees using recursion.
    addIfEqualKey(foundNode, key, list);
    
    Iterator<DictEntry<K,V>> itr=list.iterator();
    return itr;
  }
  
  public void insert(K key,V value) throws AVLTreeException{
    
    //System.out.println("inserting "+key+" "+value);
    AVLnode<K,V> w=TreeInsert(key, value, root); // Insert node containing entry with key and value into tree (expanding external node).
    AVLnode<K,V> z=w; // Declare z, which will take on the identity of consecutive nodes as we move up the tree until we find imbalanced children (if ever).
    
    while (z!=null){ // While we still have nodes in the tree to consider...
      //System.out.println("z: "+z.element().key()+" "+z.element().value());
      z.resetHeight(); // Reset height of z, as height may have changed with the insertion of w below.
      int heightDifference=z.left().getHeight()-z.right().getHeight(); // Calculate difference between heights of children.
      //System.out.println("Height difference: "+heightDifference);
      // Make heightDifference absolute value.
      if (heightDifference<0){ // If heightDifference is negative...
        heightDifference=-heightDifference; // ...make it positive.
      }
      //System.out.println("Absolute height difference: "+heightDifference);
      // Perform TriNodeRestructure if necessary.
      if (heightDifference>1){ // If children imbalanced...
        //System.out.println("restructuring with x="+tallerChild(tallerChild(z)).element().key()+" y="+tallerChild(z).element().key()+" z="+z.element().key());
        z=TriNodeRestructure(tallerChild(tallerChild(z)),tallerChild(z),z); // Perform trinode restructuring on z and its taller child, returning 'b' node, the one of the three that ends up as the parent of the other two.
        // Reset heights of all three nodes involved in restructuring.
        //System.out.println("Resetting height of left.");
        z.left().resetHeight();
        //System.out.println("Resetting height of right.");
        z.right().resetHeight();
        //System.out.println("Resetting height of z itself.");
        z.resetHeight();
        break;
      }
      // At this point, z was NOT imbalanced, so we search its parent.
      if (z.parent()!=null){
        //System.out.println("z was NOT imbalanced, so we search its parent: "+z.parent().element().key()+" "+z.parent().element().value());
      } else{
        //System.out.println("z was not imbalanced, and is root, so we finish.");
      }
      z=z.parent();
    }

  }
  
  
  
  public DictEntry< K, V > remove(K key) throws AVLTreeException, NullPointerException{
    DictEntry<K,V> entryToReturn=find(key); // Find entry in order to return it at the end.
    AVLnode<K,V> w=TreeDelete(key, root); // Delete node containing entry with key k. w holds parent of deleted node.
    AVLnode<K,V> z=w; // Define z as node whose children's heights we will compare as we scan up tree. Initially set to w, and scan upward from there.
    
    // Check and restore heigh-balance property, if needed.
    while (z!=null){ // While we have not yet reached 'parent' (null) of root...
      z.resetHeight(); // Reset height of z because deletion of node in child's path may have changed this height.
      int heightDifference=z.left().getHeight()-z.right().getHeight(); // Calculate difference between heights of children.
      
      // Make heightDifference absolute value.
      if (heightDifference<0){ // If heightDifference is negative...
        heightDifference=-heightDifference; // ...make it positive.
      }
      
      // Perform TriNodeRestructure if necessary.
      if (heightDifference>1){ // If children imbalanced...
        z=TriNodeRestructure(tallerChild(tallerChild(z)),tallerChild(z),z); // Perform trinode restructuring on z and its taller child, returning 'b' node, the one of the three that ends up as the parent of the other two.
        // Reset heights of all three nodes involved in restructuring.
        z.left().resetHeight();
        z.right().resetHeight();
        z.resetHeight();
      }

      // At this point, z was NOT imbalanced, so we search its parent.
      z=z.parent();
    }
    
    // Return entry from removed node.
    return entryToReturn;
  }
  
  public Iterator< DictEntry < K, V >> inOrder(){
    LinkedList<DictEntry<K,V>> list=new LinkedList<DictEntry<K,V>>(); // Create and initialize linked list which will contain entries in iterator.
    //System.out.println("about to fill in order");
    FillInOrder(list, root); // Fill list in-order with entries in tree, given root of tree.
    Iterator<DictEntry<K,V>> itr=list.iterator(); // Create iterator over list.
    return itr; // Return iterator.
  }
  
  public void modifyValue(K key,V newValue) throws AVLTreeException{
    // If node whose entry contains key does not exist in tree, throw excpetion.
    DictEntry<K,V> e=find(key); // Attempt to find key.
    if (e==null){ // If not found...
      throw new AVLTreeException("Cannot modify value because key is not in tree.");
    }
    
    
    AVLnode<K,V> node=(AVLnode<K,V>)giveRoot();  // Cast as AVLnode because Position interface does not include the methods we need.
    while (node.element()!=null){ // While we have not yet hit a leaf...
      int comparison=comparator.compare(node.getEntry().key(),key); // ...compare key of entry at node with input key.
      if (comparison==0){ // If the keys are equal...
        DictEntry<K,V> newEntry=new DictEntry<K,V>(key,newValue); // Create new entry with new key.
        node.setEntry(newEntry); // Reset entry of node to newEntry.
        return;
      } else if (comparison>0){ // If key of entry at node is greater than input key...
        node=node.left(); // Move to left child of node.
        continue;
      } else { // If key of entry at node is less than input key...
        node=node.right(); // Move to right child of node.
        continue;
      }
    }
  }
  
  public Position< K, V > giveRoot(){
    return root;
  }
  
  boolean isEmpty() {
    return (root.getHeight()==0); // If height of root is >0, tree is not empty. 
  }
  
  public int size(){
    return numEntries;
  }
  
  public int treeHeight(){
    return root.getHeight();
  }
  
  // END Public Methods
  
  
  // START Private Methods
  
  private AVLnode<K,V> tallerChild(AVLnode<K,V> parentNode){ // Called only when z is imbalanced, returns taller child of z.
    if (parentNode.left().getHeight()>parentNode.right().getHeight()){ // If left child of z is taller...
      return parentNode.left(); // ...return left child.
    } else if (parentNode.left().getHeight()<parentNode.right().getHeight()){ // If right child of z is taller...
      return parentNode.right(); // ...return right child.
    } else{ // If heights of children are equal (could occur only when selecting x)...
      if (parentNode.parent().left()==parentNode){ // If parentNode is left child of its parent...
        return parentNode.left();
      } else{ // If parentNode is right child of its parent...
        return parentNode.right();
      }
    }
  }
  
  private AVLnode<K,V> TreeInsert(K key,V value, AVLnode<K,V> treeRoot){ // Inserts node with entry with key and value into tree, and returns this node.
    //System.out.println("Performing TreeInsert on "+key+" "+value);
    if (treeRoot.element()!=null){
      //System.out.println("First insertNode is root: "+treeRoot.element().key()+" "+treeRoot.element().value());
    } else{
      //System.out.println("First insertNode is empty root");
    }
    AVLnode<K,V> insertNode=treeRoot; // Define node insertNode, which will take on the identity of consecutive nodes as we search down the tree for the position at which to insert our node. Initialize to root.
    while (!external(insertNode)){ // While we have not yet reached an external node...
      //System.out.println("Insertnode is not external");
      int comparison=comparator.compare(insertNode.getEntry().key(),key); // ...compare key of entry at node with input key.
      //System.out.println("Comparison: "+comparison);
      if (comparison==0){ // If key of entry at insertNode is equal to input...
        insertNode=insertNode.left(); // ...continue search at left child.
      } else if (comparison>0){ // If key of entry at insertNode is greater than input key...
        insertNode=insertNode.left(); // ...continue search at left child.
      } else{ // If key of entry at insertNode is less than input key...
        insertNode=insertNode.right(); // ...continue search at right child.
      }
    }
    //System.out.println("InsertNode is external");
    // At this point, insertNode points at external node where entry with key and value should be inserted.
    // Set properties of new entry at this node.
    insertNode.setEntry(new DictEntry<K,V>(key, value)); // Where this node previously contained no entry because it was external, now set entry to include key and value.
    //System.out.println("Setting entry to "+key+" "+value);
    insertNode.setLeft(new AVLnode<K,V>(null, insertNode, null, null)); // Create external left child for node.
    //System.out.println("Setting left to leaf with parent "+insertNode.element().key()+" "+insertNode.element().value());
    insertNode.setRight(new AVLnode<K,V>(null, insertNode, null, null)); // Create external right child for node.
    //System.out.println("Setting right to leaf with parent "+insertNode.element().key()+" "+insertNode.element().value());
    if ((insertNode.parent()!=null)&&(insertNode.element().key().equals("OPT"))){
      //System.out.println("node "+key+" "+value+" has parent: "+insertNode.parent().element().key());
    }
    if (insertNode.left().element()!=null){
      //System.out.println("node "+key+" "+value+" has left: "+insertNode.left().element().key());
    }
    if (insertNode.right().element()!=null){
      //System.out.println("node "+key+" "+value+" has right: "+insertNode.right().element().key());
    }
    // Update properties of tree.
    numEntries++; // Added one entry at (previously) external node.
    
    numNodes+=2; // Added two external nodes (children of insertNode).
    //System.out.println("numEntries="+numEntries+" numNodes="+numNodes);
    //System.out.println("Returning the node with "+insertNode.element().key()+" "+insertNode.element().value());
    return insertNode;
  }
  
  private AVLnode<K,V> TriNodeRestructure(AVLnode<K,V> x, AVLnode<K,V> y, AVLnode<K,V> z){
    // Declare nodes a, b, and c, to which we will set x, y, and z in order of increasing key (corresponding to the 4 types of imbalance that can exist).
    int i=0;
    AVLnode<K,V> a=null;
    AVLnode<K,V> b=null;
    AVLnode<K,V> c=null;
    
    // Case 3 from lecture
    if ((z.right()==y)&&(y.left()==x)){
      //System.out.println("Case 1");
      a=z;
      b=x;
      c=y;
    }
    
    if ((z.right()==y)&&(y.right()==x)){
      //System.out.println("Case 2");
      a=z;
      b=y;
      c=x;
    }
    
    if ((z.left()==y)&&(y.left()==x)){
      //System.out.println("Case 3");
      a=x;
      b=y;
      c=z;
    }
    
    if ((z.left()==y)&&(y.right()==x)){
      //System.out.println("Case 4");
      a=y;
      b=x;
      c=z;
    }
    
    if (z==root){ // If z is the root, then b (whether it is equal to x, y, or z) will now be root.
      //System.out.println(z.element().key()+" "+z.element().value()+" was root, so now "+b.element().key()+" "+b.element().value()+" will be root.");
      root=b; // Set root to b.
      b.setParent(null); // b has no parent since it is now the root.
    } else{ // Otherwise, z is NOT root.
      //System.out.println(z.element().key()+" "+z.element().value()+" was NOT the root...");
      if (z.parent().left()==z){ // If z is a left child...
        //System.out.println(z.element().key()+" "+z.element().value()+" was a left child...");
        MakeLeftChild(z.parent(),b); // ...set b to left child of z's parent.
        //System.out.println("Making "+b.element().key()+" "+b.element().value()+" the left child of "+z.parent().element().key()+" "+z.parent().element().value());
      } else{ // If z is a right child...
        //System.out.println(z.element().key()+" "+z.element().value()+" was a right child...");
        MakeRightChild(z.parent(),b); // ...set b to right child of z's parent.
        //System.out.println("Making "+b.element().key()+" "+b.element().value()+" the right child of "+z.parent().element().key()+" "+z.parent().element().value());
      }
    }
    //System.out.println("Redistributing the children of "+b.element().key()+" "+b.element().value());
    // Re-distribute b's children.
    if ((b.left()!=x)&&(b.left()!=y)){ // If b's left child is not one of our trinodes...
      //System.out.println(b.element().key()+"'s left child is not one of our trinodes...");
      MakeRightChild(a, b.left()); // ...make it the right child of a.
      //System.out.println("Making "+b.element().key()+"'s left child the right child of "+a.element().key()+" "+a.element().value());
    }
    if ((b.right()!=x)&&(b.right()!=y)){ // If b's right child is not one of our trinodes...
      //System.out.println(b.element().key()+"'s right child is not one of our trinodes...");
      MakeLeftChild(c, b.right()); // ...make it the left child of c.
      //System.out.println("Making "+b.element().key()+"'s right child the left child of "+c.element().key()+" "+c.element().value());
    }
    // Shift b up by making a and c its left and right children, respectively.
    //System.out.println("Shift "+b.element().key()+" up by making "+a.element().key()+" and "+c.element().key()+" its left and right children, respectively.");
    //System.out.println("Making "+a.element().key()+" "+a.element().value()+" the left child of "+b.element().key()+" "+b.element().value());
    MakeLeftChild(b, a); // Make a b's new left child.
    //System.out.println("Making "+c.element().key()+" "+c.element().value()+" the right child of "+b.element().key()+" "+b.element().value());
    MakeRightChild(b, c); // Make c b's new right child.
    //System.out.println("Done trinode restructuring, returning "+b.element().key()+" "+b.element().value());
    return b;
  }
  
  private AVLnode<K,V> TreeDelete(K key, AVLnode<K,V> searchNode) throws AVLTreeException{ // Starting at searchNode, scan downward through tree and delete node whose entry contains key, if it exists.
    // If INITIAL search node on which TreeDelete() is called is leaf, throw exception.
    if (searchNode.getEntry()==null){
      throw new AVLTreeException("You called TreeDelete() on leaf node, which cannot contain entries.");
    }
    
    // Search tree for node to delete.
    while (comparator.compare(searchNode.getEntry().key(), key)!=0){ // While searchNode's entry's key is not equal to input key...
      int comparison=comparator.compare(searchNode.getEntry().key(), key); // ...compare key of entry at node with input key.
      
      if (comparison>0){
        searchNode=searchNode.left();
      } else{
        searchNode=searchNode.right();
      }
      
      // If we have now arrived at leaf, conclude that input key does not exist in the entry of any node in the tree.
      if (searchNode.getEntry()==null){
        throw new AVLTreeException("Key does not exist in the entry of any node in tree.");
      }
    }
    
    // At this point, searchNode is the node containing entry with key equal to input key. Return this so that remove() can use it to start its scan up tree, to determine if TriNodeRestructuring is necessary.
    AVLnode<K,V> parentOfDeletedNode=deleteNode(searchNode);
    return parentOfDeletedNode;
  }
  
  private AVLnode<K,V> deleteNode(AVLnode<K,V> nodeToDelete){
    // Consider 3 types of nodes to delete: 1) root, 2) node with leaf children only, and 3) node with at least one internal child.
    
    // Case 1) root
    if (nodeToDelete.parent()==null){ // If nodeToDelete is root...
      if (external(nodeToDelete.right())){ // ...if nodeToDelete has no right child...
        root=nodeToDelete.left(); // ...then nodeToDelete's left child becomes root.
        nodeToDelete.left().setParent(null); // Since left child is now root, its parent is now null.
        numEntries--; // nodeToDelete contained one entry.
        numNodes-=2; // Deleted nodeToDelete and its right (leaf) child.
        return null; // Parent of nodeToDelete is null.
      } else{ // ...if nodeToDelete has a right child...
        // Replace nodeToDelete with its in-order successor.
        nodeToDelete.setEntry(InOrderSuccessor(nodeToDelete).getEntry()); // Replace entry of nodeToDelete with entry of its in-order successor.
        return deleteNode(InOrderSuccessor(nodeToDelete)); // Delete nodeToDelete's in-order successor and return its parent.
      }
    }
    
    // Case 2) Node with only leaf children.
    if (nodeToDelete.getHeight()==1){ // If height of nodeToDelete is 1...
      
      // Depending on which child of its parent nodeToDelete is, replace that child with leaf.
      if (nodeToDelete.parent().left()==nodeToDelete){ // ...if nodeToDelete is a left child...
        nodeToDelete.parent().setLeft(new AVLnode<K,V>(null, nodeToDelete.parent(), null, null)); // Replace nodeToDelete with leaf node.
      } else{ // ...if nodeToDelete is a right child...
        nodeToDelete.parent().setRight(new AVLnode<K,V>(null, nodeToDelete.parent(), null, null)); // Replace nodeToDelete with leaf node.
      }
      
      // Update tree properties to reflect deletion.
      numEntries--; // nodeToDelete contained one entry.
      numNodes-=2; // Replacing (internal node + 2 leaves) with (1 leaf).
      return nodeToDelete.parent(); // Return parent, in order to begin scan to determine if TriNodeRestructuring is necessary.
    }
    
    // Case 3) Non-root node with at least 1 internal child (at this point, this is the only remaining case)
    
    // 
    if (!external(nodeToDelete.right())){ // If nodeToDelete has an internal right child...
      // Replace nodeToDelete with its in-order successor.
      nodeToDelete.setEntry(InOrderSuccessor(nodeToDelete).getEntry()); // Replace entry of nodeToDelete with entry of its in-order successor.
      return deleteNode(InOrderSuccessor(nodeToDelete)); // Delete nodeToDelete's in-order successor and return its parent.
    } else{ // If nodeToDelete has only a left internal child...
      // Replace nodeToDelete with left child.
      nodeToDelete.setEntry(nodeToDelete.left().getEntry()); // Replace entry with entry of left child.
      return deleteNode(nodeToDelete.left()); // Delete nodeToDelete's left child and return its parent.
    }
  }
  
  private AVLnode<K,V> InOrderSuccessor(AVLnode<K,V> n){
    boolean flag=false;
    InOrderTraversal(root, n);
    AVLnode<K,V> temp=inOrderSuccessor;
    inOrderSuccessor=null;
    traversalFlag=false;
    return temp;
  }
  
  private void InOrderTraversal(AVLnode<K,V> currentNode, AVLnode<K,V> targetNode){
    // Left
    if (!external(currentNode.left())){ // If currentNode has internal left child...
      InOrderTraversal(currentNode.left(), targetNode); // ...visit left child.
    }
    
    // Current
    if (traversalFlag){
      traversalFlag=false;
      inOrderSuccessor=currentNode;
    }
    if (currentNode==targetNode){
      traversalFlag=true;
    }
    
    // Right
    if (!external(currentNode.right())){ // If currentNode has internal right child...
      InOrderTraversal(currentNode.right(), targetNode); // ...visit right child.
    }
    return;
  }
  
  private void FillInOrder(LinkedList<DictEntry<K,V>> list, Position<K,V> position){
    if (external(position)){ // If we have reached a leaf...
      return; // Do not add anything to list, because leaves do not contain entries.
    } else{
      FillInOrder(list, left(position)); // Fill in-order left subtree.
      list.add(position.element()); // Add entry from current node.
      FillInOrder(list, right(position)); // Fill in-order right subtree.
    }
  }
  
  private void MakeLeftChild(AVLnode<K,V> node1, AVLnode<K,V> node2){ // Make node2 the left child of node1.
    node1.setLeft(node2); // Make node2 the left child of node1.
    node2.setParent(node1); // Make node1 the parent of node2.
  }
  
  private void MakeRightChild(AVLnode<K,V> node1, AVLnode<K,V> node2){ // Make node2 the right child of node1.
    node1.setRight(node2); // 
    node2.setParent(node1);
  }
  
  private void addIfEqualKey(AVLnode<K,V> node, K key, LinkedList<DictEntry<K,V>> list){
    if (node.element()!=null){
      int comparison=comparator.compare(node.getEntry().key(),key);
      if (comparison==0){
        list.add(node.getEntry());
        addIfEqualKey(node.left(), key, list);
        addIfEqualKey(node.right(), key, list);
      }
    }
  }
  
  // END Private Methods
  
}