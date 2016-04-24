public class TicTacToe { 
  
  
  // START Attributes
  
 private int boardSize; // Dimensions of board.
 private int inline; // Number of characters you need in a row in order to win.
 private int maxLevels; // Maximum depth that algorithm will recursively search.
 private char[][] gameBoard; // Allocate space for 2D gameboard.
 private final int size_dict=3499; // Arbitrary prime number.
 
 // END Attributes
 
 
 // START Constructors
 
 public TicTacToe (int board_size, int inline, int max_levels) {
   this.boardSize=board_size; // Set input board size as attribute of TicTacToe method.
   this.inline=inline; // Set input inline # as attribute of TicTacToe method.
   this.maxLevels=max_levels; // Set input max # of levels as attribute of TicTacToe method.
   this.gameBoard=new char[boardSize][boardSize]; // Initialize gameboard.
   for(int i=0; i<boardSize; i++){ // Fill board with empty spaces.
    for (int j=0; j<boardSize;j++) {
     gameBoard[i][j]=' ';
    }
   }
 }
 
 // END Constructors
 
 
 // START Public Methods
 
 public Dictionary createDictionary() { // Returns an empty Dictionary of the size that you have selected.
  Dictionary dict=new Dictionary(size_dict); // Create new dictionary.
  return dict;
 }
 
 public int repeatedConfig(Dictionary configurations) { // Represents content of gameBoard string, then checks whether string representing gameBoard is in configuration's dictionary.
  return configurations.find(configToString()); // Convert to string and return.
 }
 
 public void insertConfig(Dictionary configurations, int score) { // Represents content of gameBoard as string, then inserts string and score in configuration's dictionary.
  DictEntry entry = new DictEntry(configToString(),score); // Create new DictEntry object.
  try{
   configurations.insert(entry);
  }catch(DictionaryException e) {
   System.out.println("DictionaryException: Configuration already exists");
  }
 }
 
 public void storePlay(int row, int col, char symbol) { // Stores symbol in gameBoard[row][col].
  gameBoard[row][col]=symbol; // Return element at these coordinates.
 }

 public boolean squareIsEmpty (int row, int col) { // Returns true if gameBoard[row][col] is Õ Õ; otherwise returns false.
   if(gameBoard[row][col]==' '){ // If empty...
   return true;
   } else{
  return false; // (otherwise)
   }
 }
 
 public boolean wins (char symbol) { // Returns true if there are k adjacent occurrences of symbol in the same row, column, or diagonal of gameBoard
  String wins=""; // Initialize string 'wins'.
  String config=configToString(); // String containing all symbols on board.
  String subst; // String of characters corresponding to a row, column etc., whichever we're looking for.
  for(int i=0; i<inline; i++) {
   wins+=symbol; // String containing the number of occurences of X/O needed in order to win.
  }
  //Test for horizontal wins
  for (int i=0;i<boardSize;i++) { // For each row...
    subst = config.substring(i*boardSize, (i+1)*boardSize); // Create substring containing elements of that row.
    if(subst.contains(wins)){ // If there are k consecutive occurences of X/O in this substring/row...
      return true; // The corresponding player wins.
    }
  }
  //Test for vertical wins
  for (int i=0;i<boardSize;i++) { // For each column...
   subst=""; // Start new substring for new column.
   for(int j=0; j<boardSize; j++) { // For each row (ie each element of given column)...
    subst+=config.charAt(j*boardSize+i); // Add next consecutive (descending) character to substring for given column.
   }
   if(subst.contains(wins)){ // If this column contains k consecutive occurences of X/O...
     return true; // The corresponding player wins.
   }
  }
  //Test for diagonal Wins;
  for(int i=0; i<boardSize; i++) { // Scan horizontally.
   if((boardSize-i) >= inline) { // If diagonal is at least as long as inline, ie. if it is possible to get a win along this diagonal...
     // Test top-right half of board.
    subst=""; // Reset substring to be empty for next test.
    for(int k=0; k<(boardSize-i); k++) { // For each element of diagonal...
     subst+=gameBoard[k+i][k]; // Add consecutive down-and-right elements of diagonal to substring.
    }
    if(subst.contains(wins)){ // If this diagonal contains k consecutive occurences of X/O...
      return true; // The corresponding player wins.
    }
    // Test bottom-left half of board.
    subst=""; // Reset substring to be empty for next test.
    for(int k=0; k<(boardSize-i); k++) { // For each element of diagonal...
     subst+=gameBoard[k][k+i]; // Reverse indices to test other half of board in same direction
    }
    if(subst.contains(wins)) // If this diagonal contains k consecutive occurences of X/O...
     return true; // The corresponding player wins.
   }
  }
  //Test for diagonal wins in opposite direction
  for(int i=0; i<boardSize; i++) { // Scan horizontally.
   if((boardSize-i)>=inline) { // If diagonal is at least as long as inline...
    // Test top-left half of board.
    subst=""; // Reset substring to be empty for next test.
    for(int k=0; k<(boardSize-i); k++) { // For each element of diagonal...
     subst+=gameBoard[boardSize-1-i-k][k]; // Add consecutive down-and-left elements of diagonal to substring.
    }
    if(subst.contains(wins)) // If this diagonal contains k consecutive occurences of X/O...
     return true; // The corresponding player wins.
    // Test bottom-right half of board.
    subst=""; // Reset substring to be empty for next test.
    for(int k=0; k<(boardSize-1); k++) { // For each element of diagonal...
     subst+=gameBoard[k][boardSize-1-i-k]; // Reverse indices to test other half of board in same direction.
    }
    if(subst.contains(wins)) // If this diagonal contains k consecutive occurences of X/O...
     return true; // The corresponding player wins.
   }
  }
  return false; // Otherwise, nobody wins, and we return false.
 }
 
 public boolean isDraw() { // Returns true if gameBoard has no empty positions left and no player has won the game.
  if(!configToString().contains(" ") && !wins('X') && !wins('O')) { // If no entries in board are " ", and neither X nor O has a win...
   return true;
  } else{ // IF gameboard has at least one empty position, or if one player has won...
  return false;
  }
 }
 
 public int evalBoard() { // Return corresponding value depending on outcome of game.
  if (wins('X')) // Human wins
   return 0; 
  if (wins('O')) // Computer wins
   return 3;
  if (isDraw()) //Draw occurs
   return 2;
  return 1; //Undecided
 }
 
 // END Public Methods
 
 
 // START Private Methods
 
 private String configToString() { // Set given configuration as string variable.
  String config = ""; // Initialize to empty.
  // Fill config with elements of gameboard.
  for(int i=0; i<boardSize; i++) {
   for (int j=0; j<boardSize; j++) {
    config=config + gameBoard[i][j]; // Add element to gameboard.
   }
  }
  return config; // Return final gameboard configuration.
 }
 
 // END Private Methods
 
}