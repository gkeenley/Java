public class OrderedDictionary implements OrderedDictionaryADT{
  
  
  
  // START Attributes
  
  DictEntry root; // Create new, initially empty, node that will be the root since it is the first node.
  boolean flag; // Signal for inOrder and reverseInOrder functions, that will signal when the word in question has been reached in a traversal.
  String returnValue; // String that inOrder and reverseInOrder functions will modify, and that successor and predecessor functions will then return. 
  
  // END Attributes
  
  
  
  // START Constructors
  
  public OrderedDictionary(){ // Constrcutor to create new OrderedDictinoary object.
  // Flag are returnValue are the only attributes that need to be initialized.
  flag=false;
  returnValue=null;
  }
  
  // END Constructors
  
  
  
  // START Public Methods
  
  public String findWord (String word){ // Returns the definition of the given word, or it returns an empty string if the word is not in the dictionary.
    if (root==null){ // If dictionary is empty...
      return null; // Don't bother looking for entry.
    }
    // Otherwise...
    DictEntry testDictEntry=root; // First consider root;
    while (word.compareTo(testDictEntry.entryWord)!=0){ // While we have not yet found the given entry...
      if (word.compareTo(testDictEntry.entryWord)<0){ // If word lexicographically PREcedes test entry...
        testDictEntry=testDictEntry.leftChild;
      } else { // If word lexicographically PROcedes test entry...
        testDictEntry=testDictEntry.rightChild;
      }
      if (testDictEntry==null){ // If we have reached a leaf without finding word...
        System.out.println("The word "+word+" is not in the dictionary.");
        return "";
      }
    }
    return testDictEntry.entryDefinition; // We reach this point if we have successfully left the loop, ie. found the word. Return definition. 
  }
  
  
  public int findType (String word){ // Returns the type of the given word, or it returns -1 if the word is not in the dictionary.
    if (root==null){ // If dictionary is empty...
      return -1; // Don't bother looking for entry.
    }
    // Otherwise...
    DictEntry testDictEntry=root; // First consider root;
    while (word.compareTo(testDictEntry.entryWord)!=0){ // While we have not yet found the given entry...
      if (word.compareTo(testDictEntry.entryWord)<0){ // If word lexicographically PREcedes test entry...
        testDictEntry=testDictEntry.leftChild;
      } else { // If word lexicographically PROcedes test entry...
        testDictEntry=testDictEntry.rightChild;
      }
      if (testDictEntry==null){ // If we have reached a leaf without finding word...
        return -1;
      }
    }
    return testDictEntry.entryType; // We reach this point if we have successfully left the loop, ie. found the word. Return definition. 
  }
  
  
  public void insert (String word, String definition, int type) throws DictionaryException{ //  Adds the word, its definition, and type into the dictionary. It throws a DictionaryException if the word is already in the dictionary.
    DictEntry newDictEntry=new DictEntry(word, definition, type); // Create new entry corresponding to input.
    if (root==null){ // If there are no entries in tree yet...
      root=newDictEntry; // This new entry will be the root.
    } else { // Otherwise, if the root is NOT null (ie. if there is at least one entry in the tree already)...
      DictEntry testDictEntry=root; // Entry currently under consideration. We will consider its children to determine where to place newDictEntry. Initialize to root. 
      DictEntry parent; // Entry which will be the parent of our new entry. We will first test root, and scan through tree until we find where to place our new entry.
      while(true){ // Infinite loop which we will signal to break. Iterations of this loop correspond to iterations of testing a given node to see if it should be the parent of our new entry.
        parent=testDictEntry; // Test root first.
        if (word.compareTo(testDictEntry.entryWord)==0){ // If word is already in dictionary...
          System.out.println("The word "+word+" is already in the dictionary");
          throw new DictionaryException(""); // Throw exception.
        }
        else if (word.compareTo(testDictEntry.entryWord)<0){ // If word to insert lexicographically PREcedes test entry...
          testDictEntry=testDictEntry.leftChild; // Test left child of test entry.
          if (testDictEntry==null){ // If test entry is empty...
            parent.leftChild=newDictEntry; // Insert new entry at this location.
            newDictEntry.entryParent=parent; // Set parent of newDictEntry to parent.
            return; // Exit loop.
          }
        } else { // If word to insert lexicographically PROcedes test entry...
          testDictEntry=testDictEntry.rightChild; // Test left child of test entry.
          if (testDictEntry==null){ // If test entry is empty...
            parent.rightChild=newDictEntry; // Insert new entry at this location.
            newDictEntry.entryParent=parent; // Set parent of newDictEntry to parent.
            return; // Exit loop.
          }
        }
      }
    }
  }
  
  
  public void remove (String word) throws DictionaryException{ //  Removes the entry with the given word from the dictionary. It throws a DictionaryException if the word is not in the dictionary.
    DictEntry testDictEntry=root; // Entry currently under consideration;
    DictEntry parent=root; // Parent of entry to be removed.
    boolean wordIsInLeftBranch=true; // Condition on which to determine whether to search left or right branch.
    while(word.compareTo(testDictEntry.entryWord)!=0){ // Until we have located word in tree in order to remove...
      parent=testDictEntry; // So we can consider left and right branches.
      if (word.compareTo(testDictEntry.entryWord)<0){ // If word is in left branch...
        wordIsInLeftBranch=true;
        testDictEntry=testDictEntry.leftChild; // Move down left branch.
      } else { // If word is in right branch...
        wordIsInLeftBranch=false;
        testDictEntry=testDictEntry.rightChild; // Move down right branch.
      }
      if (testDictEntry==null){ // If, after moving to left/right branch, the entry we arrive at is null, we know the word is not in the dictionary.
        throw new DictionaryException(""); // Throw exception.
      }
    } // END while loop
    // By this point, either we have located the entry and testDictEntry is equal to that entry, OR we have exited because the entry was not in the dictionary.
    // Consider 4 situations: testDictEntry has no children, has only left/right children, or has both children.
    
    // Situation 1: no children
    if (testDictEntry.leftChild==null && testDictEntry.rightChild==null){ // If entry has no children...
      if (testDictEntry==root){ // ...if testDictEntry is root...
        root=null; // Simply delete root.
      }
      else if (wordIsInLeftBranch==true){ // ...if testDictEntry is a left child...
        parent.leftChild=null; // Delete entry.
      }
      else { // ...if testDictEntry is a right child...
        parent.rightChild=null; // Delete entry.
      }
    }
    // Situation 2: Right child only.
    else if (testDictEntry.leftChild==null){ // If entry is not childless but has no left child (ie. has only right)...
      if (testDictEntry==root){
        root=testDictEntry.rightChild;
      }
      else if (wordIsInLeftBranch){
        parent.leftChild=testDictEntry.rightChild;
      }
      else{
        parent.rightChild=testDictEntry.rightChild;
      }
    }
    // Situation 3: Left child only.
    else if (testDictEntry.rightChild==null){ // If entry is not childless but has no right child (ie. has only left)...
      if (testDictEntry==root){
        root=testDictEntry.leftChild;
      }
      else if (wordIsInLeftBranch){
        parent.leftChild=testDictEntry.leftChild;
      }
      else{
        parent.rightChild=testDictEntry.leftChild;
      }
    }
    // Situation 4: Both children.
    else{ // If entry has left and right children...
      // Replacement entry is lowest (leftmost) entry of right subtree.
      DictEntry tempParent=testDictEntry; // Create new 'parent' entry so we can keep track of parent of node to be removed/replaced. Set equal to testDictEntry since we are scanning down one level to right child. 
      DictEntry replacement=testDictEntry.rightChild; // Shift focus to right child.
      while (replacement.leftChild!=null){ // Until we reach bottom (leftmost extent) of left branch...
        tempParent=replacement; // Shift tempParent down.
        replacement=replacement.leftChild; // Shift testDictEntry down.
      }
      // At this point, we have located replacement entry.
      // Shift replacement up to replace removed entry, so...
      replacement.leftChild=testDictEntry.leftChild; // Left child of removed entry becomes left child of replacement.
      replacement.leftChild.entryParent=replacement; // ...and replacement becomes parent of this child.
      if (replacement==testDictEntry.rightChild){ // If the replacement is simply the right child of the removed entry, ie. we didn't have to travel down the right child's left branch...
        replacement.rightChild=null; // The replacement at its new location will have no right child.
      } else{
        replacement.rightChild=testDictEntry.rightChild; // Right child of removed entry becomes right child of replacement.
        replacement.rightChild.entryParent=replacement; // ...and replacement becomes parent of this child.
      }
      if (wordIsInLeftBranch==true && testDictEntry!=root){ // If the replacement was the left child of parent...
        parent.leftChild=replacement; // ...set the left child of parent to replacement...
        replacement.entryParent=parent; // ...and old parent of removed entry becomes parent of replacement.
      } else if (wordIsInLeftBranch==false && testDictEntry!=root){ // If the replacement was the right child of parent...
        parent.rightChild=replacement; // ...set the right child of parent to replacement...
        replacement.entryParent=parent; // ...and old parent of removed entry becomes parent of replacement.
      } else{ // If removed entry was root...
        replacement.entryParent=null; // Replacement will be root, so it will have no parent.
        root=replacement;
      }
      tempParent.leftChild=null; // Entry that is now leftmost entry in right subbranch now has no left child because it moved up to replace the removed entry.
    }
  }
  
  
  public String successor(String word){
    DictEntry testDictEntry=root; // start at root
    inOrder(testDictEntry, word); // Do inOrder traversal of entire tree. inOrder method will modify returnValue if it finds a successor to 'word'.
    if (returnValue!=null){ // If 'word' has a successor...
      String returnValue2=returnValue; // Temporarily save returnValue in dummy variable.
      returnValue=null; // Re-set returnValue to null so that it will be null again the next time we run successor.
      return returnValue2; // Return value of successor.
    } else{
      System.out.println("The word "+word+" does not have a successor.");
      return null;
    }
  }
  
  
  public String predecessor(String word){
    DictEntry testDictEntry=root; // start at root
    reverseInOrder(testDictEntry, word); // Do reverseInOrder traversal of entire tree. Once we reach 'word', signal that we should return next word.
    if (returnValue!=null){ // If 'word' has a predecessor...
      String returnValue2=returnValue; // Temporarily save returnValue in dummy variable.
      returnValue=null; // Re-set returnValue to null so that it will be null again the next time we run successor.
      return returnValue2; // Return value of successor.
    } else{
      System.out.println("The word "+word+" does not have a predecessor.");
      return null;
    }
  }
  
  // END Public Methods
  
  
  
  // START Private Methods
  
  private DictEntry findWordFullEntry (String word){ // Returns the definition of the given word, or it returns an empty string if the word is not in the dictionary.
    if (root==null){ // If dictionary is empty...
      return null; // Don't bother looking for entry.
    }
    // Otherwise...
    DictEntry testDictEntry=root; // First consider root;
    while (word.compareTo(testDictEntry.entryWord)!=0){ // While we have not yet found the given entry...
      if (word.compareTo(testDictEntry.entryWord)<0){ // If word lexicographically PREcedes test entry...
        testDictEntry=testDictEntry.leftChild;
      } else { // If word lexicographically PROcedes test entry...
        testDictEntry=testDictEntry.rightChild;
      }
      if (testDictEntry==null){ // If we have reached a leaf without finding word...
        return null;
      }
    }
    return testDictEntry; // We reach this point if we have successfully left the loop, ie. found the word. Return definition. 
  }
 
  private void inOrder(DictEntry testDictEntry, String word){
    if (testDictEntry!=null){     
      // InOrder on left child
      inOrder(testDictEntry.leftChild, word);
      // Visit current entry
      if (flag==true){
        returnValue=testDictEntry.entryWord;
        flag=false;
      }
      if (testDictEntry.entryWord.equals(word)){
        flag=true;
      }
      // InOrder on right child
      inOrder(testDictEntry.rightChild, word);
    }
  }
  
  private void reverseInOrder(DictEntry testDictEntry, String word){
    if (testDictEntry!=null){     
      // InOrder on right child
      reverseInOrder(testDictEntry.rightChild, word);
      // Visit current entry
      if (flag==true){
        returnValue=testDictEntry.entryWord;
        flag=false;
      }
      if (testDictEntry.entryWord.equals(word)){
        flag=true;
      }
      // InOrder on left child
      reverseInOrder(testDictEntry.leftChild, word);
    }
  }
  
  // END Private Methods
  
  
}