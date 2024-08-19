// search_functions.c
// name: Kyle Kim
// JHED: kkim198.
// <STUDENT: ADD YOUR INFO HERE: name, JHED, etc.>
//
//


#include <stdio.h>
#include "search_functions.h"
#include <ctype.h>
#include <string.h>


/* 
 * Given a filename and a MAX_SIZExMAX_SIZE grid to fill, this function 
 * populates the grid and returns n, the actual grid dimension. 
 * If filename_to_read_from can't be opened, this function returns -1.
 * If the file contains an invalid grid, this function returns -2.
 */
int populate_grid(char grid[][MAX_SIZE], char filename_to_read_from[]){

  if(filename_to_read_from[0] == '\0'){
    fprintf(stdout, "Please enter a command line argument.");
    return 1;
  }

  FILE* input = fopen(filename_to_read_from, "r");
  if (input == NULL) {//This happens if you cannot open the file
    fprintf(stdout, "Grid file failed to open.");
    return -1; // indicate error
  }

  //Intialize variables that are needed
  char my_strings[12][12];
  char my_read[256];//This is what reads each line within the fscanf
  int col_length = 0;
  int row_length;
  
  while(fgets (my_read, 256, input)){//Runs until done reading file

    if(my_read[0] == '\n' || my_read[0] == ' '){//Breaks if it is the first value
      break;
    }

    int len = strlen (my_read);          // Gets length
    if (len && my_read[len-1] == '\n'){      // checks the last character and overwrite with nul-character 
        my_read[--len] = 0;                  
    }

    
    for(int i = 0; i <= (int)strlen(my_read); i ++){//Stores the value of these lines
        my_strings[col_length][i] = my_read[i];
    }

    if(strlen(my_read) > 10){//If the line exceeds 10 characters, then it's an invalid grid type error
        fprintf(stdout,"Invalid grid.");
        return -2;
    }
    if(my_read[0] == '\n' || my_read[0] == ' '){
      fprintf(stdout,"run");
      break;
    }
    col_length++;
    if(col_length > 10){ //If the number of lines exceeds 10 lines, then it's an invalid grid type error
        fprintf(stdout,"Invalid grid.");
        return -2;
    }
  }

  if(col_length == 0){//If northing is in the file, then there is an invalid grid type error
    fprintf(stdout,"Invalid grid.");
    return -2;
  }

  if (ferror(input)) {//Error within the input file
    fprintf(stdout,"Grid file failed to open.");
    return -1; 
  }
  
  fclose(input);//Closes the file so it can't be accessed anymore 
  
  
  for(int col = 0; col < col_length; col++){//This for loop is for error handling of invalid grid types
    if(col == 0){
      row_length = strlen(my_strings[col]);
      if(row_length > 11){//If northing is in the file, then there is an invalid grid type error
        fprintf(stdout,"Invalid grid.");
        return -2;
      }
    }else{
      if(row_length != (int)(strlen(my_strings[col]))){//This checks to make sure that all rows have the same amount of characters
        fprintf(stdout,"Invalid grid.");
        return -2;
      }
      if((int)(strlen(my_strings[col])) > 11){//If the line exceeds 10 characters, then it's an invalid grid type error (double check from earlier)
        fprintf(stdout,"Invalid grid.");
        return -2;
      }
    }
  }

  if(row_length != col_length){//This checks if the row and col lengths are the same
    fprintf(stdout,"Invalid grid.");
    return -2;
  }
  
  for(int row = 0; row < row_length; row ++){//This nested for loop saves the value into grid.
    for(int col = 0; col < col_length; col++){
      if(isalpha(my_strings[row][col])){//This makes sure that the saved value is a character
        grid[row][col] = my_strings[row][col];
      }else{
        fprintf(stdout,"Invalid grid.");
        return -2;
      }
    }
  }
  
  return row_length; //Returns the dimensions of the matrix

}


/* 
 * Given a square martrix, the dimensions of the matrix, a word to find, and 
 * a file to write in, this function finds the word reading from left to right 
 * (for letters and printing purposes)
 * and will only find it if if the word is read left to right within the grid and
 * write if it find sthe word within the file
 * If the word is found, then this function returns 1
 * If the word isn't found, then this function returns 0
 */
int find_right(char grid[][MAX_SIZE], int n, char word[], FILE *write_to){

  //Intialize variablse
  int length_of_word = strlen(word);
  int count;
  int saved_col;//This saves the original value
  int run = 0;

  char my_word[length_of_word];//Turns word to lower case
  for(int i = 0; i < length_of_word; i++){
    my_word[i] = tolower(word[i]);
  }
  my_word[length_of_word] = '\0';

  if((int)strlen(my_word)>n){//Happens when the word is bigger than the row length, therefore automatic returns
    return 0;
  }

  //Nested loop to look at all possible starting letters within the boundaries
  for (int row = 0; row < n; row++){
    for (int col = 0; col < n-length_of_word+1; col++){
      count = 0;
      for(int seer = 0; seer < length_of_word; seer ++){//This looks for each word rightwards
        if(tolower(grid[row][col+seer]) == my_word[seer]){
          if(count == 0){
            saved_col = col;
          }
          count++;
        }
        if(count == length_of_word){//Prints the word if found completely
          run++;
          fprintf(write_to, "%s %d %d R\n", my_word, row, saved_col);
        }
      }
    }
  }
  
  if(run == 0){//Tells us if it ran to tell us if we should write "Not Found"
    return 0;
  }else{
    return 1;
  }
  
}


/* 
 * Given a square martrix, the dimensions of the matrix, a word to find, and 
 * a file to write in, this function finds the word reading from left to right 
 * (for letters and printing purposes)
 * and will only find it if the word is read right to left within the grid and
 * write if it find sthe word within the file
 * If the word is found, then this function returns 1
 * If the word isn't found, then this function returns 0
 */
int find_left (char grid[][MAX_SIZE], int n, char word[], FILE *write_to){
  //Intialize variable
  int length_of_word = strlen(word);
  int count;
  int saved_col;//This saves the original value
  int run = 0;

  char my_word[length_of_word];
  for(int i = 0; i < length_of_word; i++){//Turns word to lower case
    my_word[i] = tolower(word[i]);
  }
  my_word[length_of_word] = '\0';

  if((int)strlen(my_word)>n){//Happens when the word is bigger than the row length, therefore automatic returns
    return 0;
  }

  //Nested loop to look at all possible starting letters within the boundaries
  for (int row = 0; row < n; row++){
    for (int col = length_of_word-1; col < n; col++){
      count = 0;
      for(int seer = 0; seer < length_of_word; seer ++){//This looks for each word leftwards
        if(tolower(grid[row][col-seer]) == my_word[seer]){
          if(count == 0){
            saved_col = col;
          }
          count++;
        }
        if(count == length_of_word){//Prints the word if found completely
          run++;
          fprintf(write_to, "%s %d %d L\n", my_word, row, saved_col);
        }
      }
    }
  }

  if(run == 0){//Tells us if it ran to tell us if we should write "Not Found"
    return 0;
  }else{
    return 1;
  }

  if(run == 0){//Tells us if it ran to tell us if we should write "Not Found"
    return 0;
  }else{
    return 1;
  }
}


/* 
 * Given a square martrix, the dimensions of the matrix, a word to find, and 
 * a file to write in, this function finds the word reading from left to right 
 * (for letters and printing purposes)
 * and will only find it if the word is read downwards within the grid and
 * write if it find sthe word within the file
 * If the word is found, then this function returns 1
 * If the word isn't found, then this function returns 0
 */
int find_down (char grid[][MAX_SIZE], int n, char word[], FILE *write_to){

  //Intialize variable
  int length_of_word = strlen(word);
  int count;
  int saved_col;//This saves the original value
  int run = 0;
  
  
  char my_word[length_of_word];
  for(int i = 0; i < length_of_word; i++){//Turns word to lower case
    my_word[i] = tolower(word[i]);
  }
  my_word[length_of_word] = '\0';


  if((int)strlen(my_word)>n){//Happens when the word is bigger than the row length, therefore automatic returns
    return 0;
  }

  //Nested loop to look at all possible starting letters within the boundaries
  for (int row = 0; row < n-length_of_word+1; row++){
    for (int col = 0; col < n; col++){
      count = 0;
      for(int seer = 0; seer < length_of_word; seer ++){//This looks for each word downwards
        if(tolower(grid[row+seer][col]) == my_word[seer]){
          if(count == 0){
            saved_col = col;
          }
          count++;
        }
        if(count == length_of_word){//Prints the word if found completely
          run++;
          fprintf(write_to, "%s %d %d D\n", my_word, row, saved_col);
        }
      }
    }
  }

  if(run == 0){//Tells us if it ran to tell us if we should write "Not Found"
    return 0;
  }else{
    return 1;
  }
}


/* 
 * Given a square martrix, the dimensions of the matrix, a word to find, and 
 * a file to write in, this function finds the word reading from left to right 
 * (for letters and printing purposes)
 * and will only find it if the word is read upwards within the grid and
 * write if it find sthe word within the file
 * If the word is found, then this function returns 1
 * If the word isn't found, then this function returns 0
 */
int find_up (char grid[][MAX_SIZE], int n, char word[], FILE *write_to){

  //Intialize variable
  int length_of_word = strlen(word);
  int count;
  int saved_col;//This saves the original value
  int run = 0;

  char my_word[length_of_word];
  for(int i = 0; i < length_of_word; i++){
    my_word[i] = tolower(word[i]);
  }
  my_word[length_of_word] = '\0';

  if((int)strlen(my_word)>n){//Happens when the word is bigger than the row length, therefore automatic returns
    return 0;
  }
  
  //Nested loop to look at all possible starting letters within the boundaries
  for (int row = length_of_word-1; row < n; row++){
    for (int col = 0; col < n; col++){
      count = 0;
      for(int seer = 0; seer < length_of_word; seer ++){//This looks for each word upwards
        if(tolower(grid[row-seer][col]) == my_word[seer]){
          if(count == 0){
            saved_col = col;
          }
          count++;
        }
        if(count == length_of_word){//Prints the word if found completely
          run++;
          fprintf(write_to, "%s %d %d U\n", my_word, row, saved_col);
        }
      }
    }
  }

  if(run == 0){//Tells us if it ran to tell us if we should write "Not Found"
    return 0;
  }else{
    return 1;
  }

}


/* 
 * Given a square martrix, the dimensions of the matrix, a word to find, and 
 * a file to write in, this function finds the word reading from left to right 
 * and write in the file the position and the way the words faces. It will also
 * write Not Found in the file if there is no words found
 * If everything runs correctly, it will return 1
 */
int find_all (char grid[][MAX_SIZE], int n, char word[], FILE *write_to) {
    
    //Calls all the finds in the right order
    int right = find_right(grid, n, word, write_to);
    int left = find_left(grid, n, word, write_to);
    int down = find_down(grid, n, word, write_to);
    int up = find_up(grid, n, word, write_to);

    if(right == 0 && left == 0 && down == 0 && up == 0){//Prints Not Found if word was not found
      fprintf(write_to, "%s - Not Found\n", word);
    }
    
    return 0;
} 


/*
 * Reads lhs and rhs character by character until either reaches eof.
 * Returns true if the files can be read and the two files match
 * character by character. Returns false if two files either can't be
 * opened or don't match. The definition of this function is provided 
 * for you.
 */
int file_eq(char lhs_name[], char rhs_name[]) {
  FILE* lhs = fopen(lhs_name, "r");
  FILE* rhs = fopen(rhs_name, "r");

  // don't compare if we can't open the files
  if (lhs == NULL || rhs == NULL) return 0;

  int match = 1;
  // read until both of the files are done or there is a mismatch
  while (!feof(lhs) || !feof(rhs)) {
	if (feof(lhs) ||                      // lhs done first
		feof(rhs) ||                  // rhs done first
		(fgetc(lhs) != fgetc(rhs))) { // chars don't match
	  match = 0;
	  break;
	}
  }
  fclose(lhs);
  fclose(rhs);
  return match;
}

