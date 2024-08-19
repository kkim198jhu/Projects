// word_search.c
// name: Kyle Kim
// JHED: kkim198.
// <STUDENT: ADD YOUR INFO HERE: name, JHED, etc.>
//
//


#include <stdio.h>
#include "search_functions.h"


/*
 * This is the HW3 main function that performs a word search.
 */
int main(int argc, char* argv[]) {//Takes in command line
  
  if(argc == 1){//Makes sure that their is a text file coming in and if not there is an error
    fprintf(stdout,"Please enter a command line argument.");
    return 1;
  }
  char grid[10][10];//Establish a max grid
  int dimension = populate_grid(grid, argv[1]);//Gets the dimensions of the grid as well as populating the grid

  if(dimension < 0){//Error handling that depends on return value of populate_gride
    return dimension;
  }

  FILE* input = stdout;
  char my_word[10];
  while(scanf(" %s", my_word) == 1){//Takes in words and outputs them
    find_all (grid, dimension, my_word, input);
  }

  return 0;

}

