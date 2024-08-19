//project.c

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "ppm_io.h"
#include "image_manip.h"
#include <ctype.h>

// Return (exit) codes
#define RC_SUCCESS            0
#define RC_MISSING_FILENAME   1
#define RC_OPEN_FAILED        2
#define RC_INVALID_PPM        3
#define RC_INVALID_OPERATION  4
#define RC_INVALID_OP_ARGS    5
#define RC_OP_ARGS_RANGE_ERR  6
#define RC_WRITE_FAILED       7
#define RC_UNSPECIFIED_ERR    8

void print_usage();

int main (int argc, char* argv[]) {
  if (argc >= 4) {
    
    // Intialize and Declare Variables
    FILE *input_file = fopen(argv[1], "rb");
    printf("asdsa");
    // Used for output file errors makes sure that file that is passed is ppm file
    char ppm_check[5];
    ppm_check[4] = '\0';
    char my_ppm_string[5] = ".ppm";
    int count = 0;

    // Error Handling of Input file I/O error
    if (input_file == NULL) { // Happens if you cannot open the file
      fprintf(stderr, "Input file I/O error.\n");
      return 2; // indicate error
    }
    
    Image input_image = read_ppm(input_file);

    // Error Handling for The Input file cannot be read as a PPM file
    if (input_image.data == NULL) {
      fprintf(stderr, "The Input file cannot be read as a PPM file \n");
      free_image(&input_image);
      fclose(input_file);
      return 3;
    }
    
    // Intialize many variables, so no warnings appear
    Image output_image;
    FILE *output_file;
    FILE *input_file_2;
    Image input_image_2;
    int len;

    // Changes which arguement declares to output depending on what mode the user wants
    if (strcmp(argv[3], "blend") == 0) {
      input_file_2 = fopen(argv[2], "rb");

      // Error Handling of Input file I/O error
      if (input_file_2 == NULL) {// This happens if you cannot open the file
        fprintf(stderr, "Input file I/O error.\n");
        free_image(&input_image);
        fclose(input_file);
        return 2; // indicate error
      }

      input_image_2 = read_ppm(input_file_2);

      // Error Handling for The Input file cannot be read as a PPM file
      if (input_image_2.data == NULL) {
        fprintf(stderr, "The Input file cannot be read as a PPM file \n");
        free_image(&input_image);
        free_image(&input_image_2);
        free_image(&output_image);
        fclose(input_file);
        fclose(input_file_2);
        return 3;
      }
      
      // Error Handling of Output file I/O error
      for(int i = strlen(argv[4])-4; i < (int)strlen(argv[4]); i++){
        ppm_check[count] = argv[4][i];
        count++;
      }

      // Error Handling of Output file I/O error
      if(strcmp(ppm_check, my_ppm_string) != 0){
        fprintf(stderr, "Output file I/O error.\n");
        free_image(&input_image);
        free_image(&input_image_2);
        fclose(input_file);
        fclose(input_file_2);
        return 7; // indicate error
      }

      output_file = fopen(argv[4], "wb");
    }else {
      // Error Handling of Output file I/O error
      for (int i = strlen(argv[2])-4; i < (int)strlen(argv[2]); i++) {
        ppm_check[count] = argv[2][i];
        count++;
      }

      // Error Handling of Output file I/O error
      if (strcmp(ppm_check, my_ppm_string) != 0) {
        fprintf(stderr, "Output file I/O error.\n");
        free_image(&input_image);
        fclose(input_file);
        return 7; // indicate error
      }

      output_file = fopen(argv[2], "wb");
    }

    // Error Handling of Output file I/O error
    if (output_file == NULL) { // Happens if you cannot open the file
      fprintf(stderr, "Output file I/O error.\n");
      free_image(&input_image);
      free_image(&output_image);
      fclose(input_file);
      fclose(output_file);
      return 7; // indicate error
    }

    // These if statements check if it is in which value

    // Do this if it is grayscale
    if (strcmp(argv[3], "grayscale") == 0) {

      // Error Handling for Incorrect number of arguments for the specified operation
      if (argc != 4) {
        fprintf(stderr, "Incorrect number of arguments for the specified operation\n");
        free_image(&input_image);
        free_image(&output_image);
        fclose(input_file);
        fclose(output_file);
        return 5;
      }

      output_image = grayscale(input_image);
      write_ppm(output_file, output_image);
    // Do this if it is blend
    } else if (strcmp(argv[3], "blend") == 0) {

      // Error Handling for Incorrect number of arguments for the specified operation
      if (argc != 6) {
        fprintf(stderr, "Incorrect number of arguments for the specified operation\n");
        free_image(&input_image);
        free_image(&input_image_2);
        free_image(&output_image);
        fclose(input_file);
        fclose(output_file);
        fclose(input_file_2);
        return 5;
      }

      len = strlen(argv[5]);
      // Error Handling for Invalid arguments for the specified operation
      for (int i = 0; i < len; i++) {
          if (!(isdigit(argv[5][i]) || argv[5][i] == '.')) {
              fprintf(stderr, "Invalid arguments for the specified operation\n");
              free_image(&input_image);
              free_image(&output_image);
              fclose(input_file);
              fclose(output_file);
              return 6;
          }
      }

      output_image = blend(input_image, input_image_2, atof(argv[5]));
      write_ppm(output_file, output_image);

      // Error Handling for The Input file cannot be read as a PPM file
      if (ferror(input_file_2)) {// Error within the input file
        fprintf(stderr, "Input file I/O error.\n");
        free_image(&input_image);
        free_image(&output_image);
        fclose(input_file);
        fclose(output_file);
        return 2; // indicate error
      }
      // This is to prevent memory leaks that come with extra images and trees
      free_image(&input_image_2);
      fclose(input_file_2);

    // Do this if it is rotate-ccw
    } else if (strcmp(argv[3],"rotate-ccw") == 0) {

      // Error Handling for Incorrect number of arguments for the specified operation
      if (argc != 4) {
        fprintf(stderr, "Incorrect number of arguments for the specified operation\n");
        free_image(&input_image);
        free_image(&output_image);
        fclose(input_file);
        fclose(output_file);
        return 5;
      }

      output_image = rotate_ccw(input_image);
      write_ppm(output_file, output_image);
    // Do this if it is pointilism
    } else if (strcmp(argv[3], "pointilism") == 0){

      // Error Handling for Incorrect number of arguments for the specified operation
      if (argc != 4) {
        fprintf(stderr, "Incorrect number of arguments for the specified operation\n");
        free_image(&input_image);
        free_image(&output_image);
        fclose(input_file);
        fclose(output_file);
        return 5;
      }

      output_image = pointilism(input_image, 1);
      write_ppm(output_file, output_image);

    // Do this if it is blur
    } else if (strcmp(argv[3],"blur") == 0) {

      // Error Handling for Incorrect number of arguments for the specified operation
      if (argc != 5) {
        fprintf(stderr, "Incorrect number of arguments for the specified operation\n");
        free_image(&input_image);
        free_image(&output_image);
        fclose(input_file);
        fclose(output_file);
        return 5;
      }

      len = strlen(argv[4]);

      // Error Handling for Invalid arguments for the specified operation
      for (int i = 0; i < len; i++) {
          if (!(isdigit(argv[4][i]) || argv[4][i] == '.')) {
              fprintf(stderr, "Invalid arguments for the specified operation\n");
              free_image(&input_image);
              free_image(&output_image);
              fclose(input_file);
              fclose(output_file);
              return 6;
          }
      }

      output_image = blur(input_image, atof(argv[4]));
      write_ppm(output_file, output_image);

    // Do this if it is saturate
    } else if (strcmp(argv[3], "saturate") == 0) {

      // Error Handling for Incorrect number of arguments for the specified operation
      if (argc != 5) {
        fprintf(stderr, "Incorrect number of arguments for the specified operation\n");
        free_image(&input_image);
        free_image(&output_image);
        fclose(input_file);
        fclose(output_file);
        return 5;
      }

      len = strlen(argv[4]);

      // Error Handling for Invalid arguments for the specified operation
      for (int i = 0; i < len; i++) {
          if (!(isdigit(argv[4][i]) || argv[4][i] == '.')) {
              fprintf(stderr, "Invalid arguments for the specified operation\n");
              free_image(&input_image);
              free_image(&output_image);
              fclose(input_file);
              fclose(output_file);
              return 6;
          }
      }

      output_image = saturate(input_image, atof(argv[4]));
      write_ppm(output_file, output_image);

    } else {

      // Error Handling of Unsupported image processing operations
      fprintf(stderr, "Unsupported image processing operations\n");
      free_image(&input_image);
      free_image(&output_image);
      fclose(input_file);
      fclose(output_file);
      return 4;
    }

    // Error Handling of Output file I/O error
    if (ferror(output_file)) { // Error within the output file
      fprintf(stderr, "Output file I/O error.\n");
      free_image(&input_image);
      free_image(&output_image);
      fclose(input_file);
      fclose(output_file);
      return 7; // indicate error
    }

    // Error Handling for The Input file cannot be read as a PPM file
    if (ferror(input_file)) { // Error within the input file
      fprintf(stderr, "Input file I/O error.\n");
      free_image(&input_image);
      free_image(&output_image);
      fclose(input_file);
      fclose(output_file);
      return 2; // indicate error
    }


    // Free all data used
    free_image(&input_image);
    free_image(&output_image);
    fclose(input_file);
    fclose(output_file);
    
  } else {
    
    // Error Handling of Wrong usage (i.e. mandatory arguments are not provided)
    fprintf(stderr, "Wrong usage (i.e. mandatory arguments are not provided)\n");
    return 1;
  }
  return 0;
}

void print_usage() {
  printf("USAGE: ./project <input-image> <output-image> <command-name> <command-args>\n");
  printf("SUPPORTED COMMANDS:\n");
  printf("   grayscale\n" );
  printf("   blend <target image> <alpha value>\n" );
  printf("   rotate-ccw\n" );
  printf("   pointilism\n" );
  printf("   blur <sigma>\n" );
  printf("   saturate <scale>\n" );
}


