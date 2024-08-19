// test_search_functions.c
// <STUDENT: ADD YOUR INFO HERE: name, JHED, etc.>
// name: Kyle Kim
// JHED: kkim198.



#include <stdio.h>
#include <assert.h>
#include "search_functions.h"


/* 
 * Declarations for tester functions whose definitions appear below.
 * (You will need to fill in the function definition details, at the
 * end of this file, and add comments to each one.) 
 * Additionally, for each helper function you elect to add to the 
 * provided search_functions.h, you will need to supply a corresponding
 * tester function in this file.  Add a declaration for it here, its
 * definition below, and a call to it where indicated in main.
 */
void test_file_eq();      // This one is already fully defined below.
void test_populate_grid();
void test_find_right();
void test_find_left();
void test_find_down();
void test_find_up();
void test_find_all();


/*
 * Main method which calls all test functions.
 */
int main() {
  printf("Testing file_eq...\n");
  test_file_eq();
  printf("Passed file_eq test.\n\n");

  printf("Running search_functions tests...\n");
  test_populate_grid();
  test_find_right();
  test_find_left();
  test_find_down();
  test_find_up();
  test_find_all();

  /* You may add calls to additional test functions here. */

  printf("Passed search_functions tests!!!\n");
}



/*
 * Test file_eq on same file, files with same contents, files with
 * different contents and a file that doesn't exist.
 * Relies on files test1.txt, test2.txt, test3.txt being present.
 */
void test_file_eq() {
  FILE* fptr = fopen("test1.txt", "w");
  fprintf(fptr, "this\nis\na test\n");
  fclose(fptr);

  fptr = fopen("test2.txt", "w");
  fprintf(fptr, "this\nis\na different test\n");
  fclose(fptr);

  fptr = fopen("test3.txt", "w");
  fprintf(fptr, "this\nis\na test\n");
  fclose(fptr);

  assert( file_eq("test1.txt", "test1.txt"));
  assert( file_eq("test2.txt", "test2.txt"));
  assert(!file_eq("test2.txt", "test1.txt"));
  assert(!file_eq("test1.txt", "test2.txt"));
  assert( file_eq("test3.txt", "test3.txt"));
  assert( file_eq("test1.txt", "test3.txt"));
  assert( file_eq("test3.txt", "test1.txt"));
  assert(!file_eq("test2.txt", "test3.txt"));
  assert(!file_eq("test3.txt", "test2.txt"));
  assert(!file_eq("", ""));  // can't open file
}



void test_populate_grid(){
  char grid[10][10];
  //Default Grid (Should Work)
  assert(populate_grid(grid, "test.txt") > 0);

  //Empty Grid (Shouldn't Work)
  assert(populate_grid(grid, "test12.txt") == -2);

  //10x11 Grid (Shouldn't Work)
  assert(populate_grid(grid, "test11.txt") == -2);

  //10x10 Grid (Should Work)
  assert(populate_grid(grid, "test13.txt") > 0);

  //11x10 Grid (Shouldn't Work)
  assert(populate_grid(grid, "test4.txt") == -2);

  //Row off in grid (Shouldn't Work)
  assert(populate_grid(grid, "test5.txt") == -2);

  //9x10 Grid (Shouldn't Work)
  assert(populate_grid(grid, "test6.txt") == -2);

  //10x9 Grid (Shouldn't Work)
  assert(populate_grid(grid, "test7.txt") == -2);

  //3x3 Grid (Should Work)
  assert(populate_grid(grid, "test8.txt") > 0);

  //3x4 Grid with empty line (Shouldn't Work)
  assert(populate_grid(grid, "test9.txt") == -2);

  //3x3 Grid with empty line (Should Work)
  assert(populate_grid(grid, "test10.txt") > 0);
}


void test_find_right(){
  //Setting up the grid first using rightTest.txt
  char grid[10][10];
  int dimension = populate_grid(grid, "rightTest.txt");
  assert(dimension=6);

  //This is the generic test: multiple variables on different and same lines
  assert(find_right(grid, dimension, "key", stdout)==1);

  //This is the cap sensitive test
  assert(find_right(grid, dimension, "kEy", stdout)==1);
  assert(find_right(grid, dimension, "keY", stdout)==1);
  assert(find_right(grid, dimension, "Key", stdout)==1);

  //This is the generic test: one variable on one line
  assert(find_right(grid, dimension, "nope", stdout)==1);

  //This is the test to see if it will return something that isn't available
  assert(find_right(grid, dimension, "nose", stdout)==0);

  //This is the generic test: one letter long
  assert(find_right(grid, dimension, "l", stdout)==1);

  //This is a test to see if it wrap reads(it shouldn't) (lines 0-1)
  assert(find_right(grid, dimension, "keylvl", stdout)==0);

  //This is the generic test: one letter long: Row length
  assert(find_right(grid, dimension, "snomes", stdout)==1);

  //This is test to see if it will return something greater than the length of the row
  assert(find_right(grid, dimension, "lsnomes", stdout)==0);
}


void test_find_left(){
  //Setting up the grid first using leftTest.txt
  char grid[10][10];
  int dimension = populate_grid(grid, "leftTest.txt");
  assert(dimension=6);

  //This is the generic test: multiple variables on different and same lines
  assert(find_left(grid, dimension, "key", stdout)==1);

  //This is the cap sensitive test
  assert(find_left(grid, dimension, "kEy", stdout)==1);
  assert(find_left(grid, dimension, "keY", stdout)==1);
  assert(find_left(grid, dimension, "Key", stdout)==1);

  //This is the generic test: one variable on one line
  assert(find_left(grid, dimension, "nope", stdout)==1);

  //This is the test to see if it will return something that isn't available
  assert(find_left(grid, dimension, "nose", stdout)==0);

  //This is the generic test: one letter long
  assert(find_left(grid, dimension, "l", stdout)==1);

  //This is a test to see if it wrap reads(it shouldn't) (lines 0-1)
  assert(find_left(grid, dimension, "keylvl", stdout)==0);

  //This is the generic test: one letter long: Row length
  assert(find_left(grid, dimension, "snomes", stdout)==1);

  //This is test to see if it will return something greater than the length of the row
  assert(find_left(grid, dimension, "lsnomes", stdout)==0);
}


void test_find_down(){

  //Setting up the grid first using downTest.txt
  char grid[10][10];
  int dimension = populate_grid(grid, "downTest.txt");
  assert(dimension=6);

  //This is the generic test: multiple variables on different and same lines
  assert(find_down(grid, dimension, "key", stdout)==1);

  //This is the cap sensitive test
  assert(find_down(grid, dimension, "kEy", stdout)==1);
  assert(find_down(grid, dimension, "keY", stdout)==1);
  assert(find_down(grid, dimension, "Key", stdout)==1);

  //This is the generic test: one variable on one line
  assert(find_down(grid, dimension, "nope", stdout)==1);

  //This is the test to see if it will return something that isn't available
  assert(find_down(grid, dimension, "nose", stdout)==0);

  //This is the generic test: one letter long
  assert(find_down(grid, dimension, "l", stdout)==1);

  //This is a test to see if it wrap reads(it shouldn't) (lines 0-1)
  assert(find_down(grid, dimension, "keylvl", stdout)==0);

  //This is the generic test: one letter long: Row length
  assert(find_down(grid, dimension, "snomes", stdout)==1);

  //This is test to see if it will return something greater than the length of the row
  assert(find_down(grid, dimension, "lsnomes", stdout)==0);

}


void test_find_up(){

  //Setting up the grid first using upTest.txt
  char grid[10][10];
  int dimension = populate_grid(grid, "upTest.txt");
  assert(dimension=6);

  //This is the generic test: multiple variables on different and same lines
  assert(find_up(grid, dimension, "key", stdout)==1);

  //This is the cap sensitive test
  assert(find_up(grid, dimension, "kEy", stdout)==1);
  assert(find_up(grid, dimension, "keY", stdout)==1);
  assert(find_up(grid, dimension, "Key", stdout)==1);

  //This is the generic test: one variable on one line
  assert(find_up(grid, dimension, "nope", stdout)==1);

  //This is the test to see if it will return something that isn't available
  assert(find_up(grid, dimension, "nose", stdout)==0);

  //This is the generic test: one letter long
  assert(find_up(grid, dimension, "l", stdout)==1);

  //This is a test to see if it wrap reads(it shouldn't) (lines 0-1)
  assert(find_up(grid, dimension, "keylvl", stdout)==0);

  //This is the generic test: one letter long: Row length
  assert(find_up(grid, dimension, "snomes", stdout)==1);

  //This is test to see if it will return something greater than the length of the row
  assert(find_up(grid, dimension, "lsnomes", stdout)==0);

}


void test_find_all(){

  //Setting up the grid first using allTest.txt
  char grid[10][10];
  int dimension = populate_grid(grid, "allTest.txt");
  assert(dimension=6);

  //This is the generic test: all directions can be found
  assert(find_all(grid, dimension, "key", stdout)==0);

  //This is the cap sensitive test
  assert(find_all(grid, dimension, "May", stdout)==0);
  assert(find_all(grid, dimension, "mAy", stdout)==0);
  assert(find_all(grid, dimension, "maY", stdout)==0);

  //This is the generic test: one variable on one line
  assert(find_all(grid, dimension, "kymA", stdout)==0);

  //This is the test to see if it will return something that isn't available
  assert(find_all(grid, dimension, "nose", stdout)==0);

  //This is the generic test: one letter long
  assert(find_all(grid, dimension, "l", stdout)==0);

  //This is a test to see if it wrap reads(it shouldn't) (lines 0-1)
  assert(find_all(grid, dimension, "maymay", stdout)==0);

  //This is the generic test: one letter long: Row length
  assert(find_all(grid, dimension, "snomes", stdout)==0);

  //This is test to see if it will return something greater than the length of the row
  assert(find_all(grid, dimension, "ysnomes", stdout)==0);

}

