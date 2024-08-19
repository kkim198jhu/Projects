#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <assert.h>
#include "image_manip.h"
#include "ppm_io.h"


////////////////////////////////////////
// Definitions of the functions       //
// declared in image_manip.h go here! //
////////////////////////////////////////

/** Takes in an Image and returns its grayscale equivalent.
* @param: in - Image to be taken in and used to return a grayscale image.
* @return: new_in - grayscale Image, made from in
*/
Image grayscale(const Image in) {

    // Intialize variables
    float gray;
    Image new_in = make_image(in.rows, in.cols);

    // Runs through each pixel to change it to greyscale
    for (int i = 0; i < in.rows * in.cols; i++) {
        gray = (int)floor(0.3 * in.data[i].r + 0.59 * in.data[i].g + 0.11 * in.data[i].b);
        
        // Makes sure that grey stays within the range of 0-255 and assigns it to correct pixel color

        // This assigns it to red
        if (gray >= 255) {
            new_in.data[i].r = 255;
        } else if (gray <= 0){
            new_in.data[i].r = 0;
        } else {
            new_in.data[i].r = gray;
        }

        // This assigns it to blue
        if (gray >= 255) {
            new_in.data[i].b = 255;
        } else if (gray <= 0){
            new_in.data[i].b = 0;
        } else{
            new_in.data[i].b = gray;
        }

        // This assigns it to green
        if (gray >= 255) {
            new_in.data[i].g = 255;
         }else if (gray <= 0){
            new_in.data[i].g = 0;
        } else {
            new_in.data[i].g = gray;
        }
    }
    return new_in;
}

/** Takes in two images and returns an image of them blended together, with the prevalence
* of each image dependent upon an alpha value.
* @param: in1 - First image used to blend
* @param: in2 - Second image used to blend
* @param: alpha - Decimal (from 0 to 1), tells how much of im1 to blend compared to im2
* @return: new_in - Image resulting from the blend between in1 and in2, dependent upon alpha
*/
Image blend(const Image in1, const Image in2, double alpha) {
    // Intialize all variables
    int Min_Cols;
    int Max_Cols;
    int Min_Rows;
    int Max_Rows;

    // Tells us which image has greater rows/cols
    int Which_Cols;
    int Which_Rows; 

    // Pictures won't go out of bounds and pixels are only counted if used
    int in1_counter = 0;
    int in2_counter = 0;


    // Figures out which image has greater number of cols
    if (in1.cols > in2.cols) {
        Min_Cols = in2.cols;
        Max_Cols = in1.cols;
        Which_Cols = 0;
    } else if (in1.cols < in2.cols) {
        Min_Cols = in1.cols;
        Max_Cols = in2.cols;
        Which_Cols = 1;
    } else {
        Min_Cols = in2.cols;
        Max_Cols = Min_Cols;
        Which_Cols = 2;
    }

    // Figures out which image has greater number of rows
    if (in1.rows > in2.rows) {
        Min_Rows = in2.rows;
        Max_Rows = in1.rows;
        Which_Rows = 0;
    } else if (in1.rows < in2.rows) {
        Min_Rows = in1.rows;
        Max_Rows = in2.rows;
        Which_Rows = 1;
    } else {
        Min_Rows = in2.rows;
        Max_Rows = Min_Rows;
        Which_Rows = 2;
    }


    // Makes Image
    Image new_in = make_image(Max_Rows, Max_Cols);
    
    // Saves the data into a the new image
    for (int i = 0; i < Max_Rows * Max_Cols; i++) {
        // This happens when the number of rows on one image is greater than the other
        if ((i / Max_Cols) - Min_Rows >= 0) {

            // This means that in1 has more rows
            if (Which_Rows == 0) {
                new_in.data[i].r = in1.data[in1_counter].r;
                new_in.data[i].g = in1.data[in1_counter].g;
                new_in.data[i].b = in1.data[in1_counter].b;
                in1_counter++;

            // This means that in2 has more rows
            } else if(Which_Rows == 1){
                new_in.data[i].r = in2.data[in2_counter].r;
                new_in.data[i].g = in2.data[in2_counter].g;
                new_in.data[i].b = in2.data[in2_counter].b;
                in2_counter++;
            }
        // This happens when the number of cols on one image is greater than the other
        } else if ((i % Max_Cols) - Min_Cols >= 0) {
            if (Which_Cols == 0) {

                // This means that in1 has more cols
                new_in.data[i].r = in1.data[in1_counter].r;
                new_in.data[i].g = in1.data[in1_counter].g;
                new_in.data[i].b = in1.data[in1_counter].b;
                in1_counter++;
            } else if(Which_Cols == 1) {

                // This means that in1 has more cols
                new_in.data[i].r = in2.data[in2_counter].r;
                new_in.data[i].g = in2.data[in2_counter].g;
                new_in.data[i].b = in2.data[in2_counter].b;
                in2_counter++;
            }

        // This happens when the both images have pixels at the same position
        } else {
            new_in.data[i].r = in1.data[in1_counter].r * alpha + in2.data[in2_counter].r * (1 - alpha);
            new_in.data[i].g = in1.data[in1_counter].g * alpha + in2.data[in2_counter].g * (1 - alpha);
            new_in.data[i].b = in1.data[in1_counter].b * alpha + in2.data[in2_counter].b * (1 - alpha);
            in1_counter++;
            in2_counter++;
        }
    }
    // Returns the new image to be used.
    return new_in; 
}

/** Takes in an Image and returns a counterclockwise-rotated version of it. 
* @param: in - Image to be taken in and used to return a rotated image.
* @return: new_in - Image, Counterclockwise-rotated version of in.
*/
Image rotate_ccw(const Image in) {

    // Intialize and Declare Variables
    int cols = in.cols;
    int rows = in.rows;
    Image new_in = make_image(cols, rows);

    // Keeps track of which row it is in and the corresponding column
    int difference = 1;

    // Keeps track of which col it is with it's corresponding column
    int multiplier = 1;
 
    // Loop that writes in each pixel
    for (int i = 0; i < cols*rows; i++) {

        // Stores the data with corresponding pixels
        new_in.data[i].r = in.data[(cols*multiplier)-difference].r;
        new_in.data[i].g = in.data[(cols*multiplier)-difference].g;
        new_in.data[i].b = in.data[(cols*multiplier)-difference].b;

        // Changes differences if it gets to the end of the row
        if (multiplier == rows) {
            difference++;
            multiplier = 0;
        }

        // Increases multiplier so it corresponds to the correct column in the original image
        multiplier++;
    }
    return new_in;
}

/** Takes in an Image and returns a version of it with the pointilism effect.
* In pointilism, 3% of the pixels are selected, and each pixel creates a circle of a random radius
* (from 1 to 5) of that color around themselves, making a painting effect. Unselected pixels, in
* addition to pixels not within the radius of any selected pixel's circle, are left black.
* @param: in - Image to be taken in and used to create the pointilism effect.
* @param: seed - Seed used for random number generation to ensure the same set of "random" numbers
*   are picked each time. These random numbers are used to sample a random 3% of the pixels.
* @return: new_in - Image, a version of Image in that has the pointilism effect applied.
*/
Image pointilism(const Image in, unsigned int seed) {

    // Picks seed (assigned in main) to use for random pixel selection
    srand(seed);

    // Makes the new Image
    Image new_in = make_image(in.rows, in.cols);

    // Iterates over 3% of the pixels (picked by random)
    for (int k = 0; k < in.rows * in.cols * 0.03; k++) {

        // Picks random radius size (for the circle) and pixel
        int rand_col = rand() % in.cols;
        int rand_row = rand() % in.rows;
        int rand_pixel = rand_row * in.cols + rand_col;
        int r = rand() % 5 + 1;

        // Cases for each possible radius.
        /* Note that instead of using a radius function, I manually set each
        pixel based on what the radius is, to ensure the shapes are consistent
        with those in the directions. */
        if (r == 1) {

            // Copies the central pixel
            copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel]);

            for (int i = -1; i < 2; i += 2) {

                // Copies the pixels directly above and below the center
                if (rand_pixel + i * in.cols < in.cols * in.rows && rand_pixel + i * in.cols >= 0) {
                    copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + i * in.cols]);
                }

                // Copies the pixels directly left and right
                if (rand_pixel + i < in.cols * in.rows && rand_pixel + i >= 0) {
                    if ((rand_pixel + i) / in.cols == rand_pixel / in.cols) {
                        copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + i]);
                    }
                }
            }
    
        } else if (r == 2) {

            // Pixels 2 above and 2 below the center
            for (int i = -2; i < 3; i+=4) {
                if (rand_pixel + i * in.cols < in.cols * in.rows && rand_pixel + i * in.cols >= 0) {
                    copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + i * in.cols]);
                }
            }

            // Pixels 1 above (3 of them) and 1 below (another 3)
            for (int i = -1; i < 2; i++) {
                if (rand_pixel - in.cols + i < in.cols * in.rows && rand_pixel - in.cols + i >= 0) {
                    if ((rand_pixel + i) / in.cols == rand_pixel / in.cols) {
                        copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel - in.cols + i]);
                    }
                }

                if (rand_pixel + in.cols + i < in.cols * in.rows && rand_pixel + in.cols + i >= 0) {
                    if ((rand_pixel + i) / in.cols == rand_pixel / in.cols) {
                        copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + in.cols + i]);
                    }
                }
            }

            // Pixels in the same row as the pixel (from 2 left to 2 right, so 5 total)
            for (int i = -2; i < 3; i++) {
                if (rand_pixel + i < in.cols * in.rows && rand_pixel + i >= 0) {
                    if ((rand_pixel + i) / in.cols == rand_pixel / in.cols) {
                        copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + i]);
                    }
                }
            }

        } else if (r == 3) {

            // 5x5 square of pixels around the center pixel
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    if(rand_pixel + i * in.cols + j < in.cols * in.rows && rand_pixel + i * in.cols + j >= 0) {
                        if ((rand_pixel + j) / in.cols == rand_pixel / in.cols) {
                            copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + i * in.cols + j]);
                        }
                    }
                }
            }

            // Pixels 3 right, 3 left, 3 above, and 3 below the center pixel respectively
            // Note that checks are not as rigorous because when adding to rand pixel, we can't hit 0, and vice versa for subtracting
            if (rand_pixel + 3 < in.cols * in.rows) {
                if ((rand_pixel + 3) / in.cols == rand_pixel / in.cols) {
                    copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + 3]);
                }
            }

            if (rand_pixel - 3 > 0) {
                if ((rand_pixel - 3) / in.cols == rand_pixel / in.cols) {
                    copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel - 3]);
                }
            }

            if (rand_pixel + 3 * in.cols < in.cols * in.rows) {
                copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + 3 * in.cols]);
            }

            if (rand_pixel - 3 * in.cols > 0) {
                copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel - 3 * in.cols]);
            }

        } else if (r == 4) {

            // 5x7 box of pixels around the center pixel
            for (int i = -2; i < 3; i++) {
                for (int j = -3; j < 4; j++) {
                    if (rand_pixel + i * in.cols + j < in.cols * in.rows && rand_pixel + i * in.cols + j >= 0) {
                        if ((rand_pixel + j) / in.cols == rand_pixel / in.cols) {
                            copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + i * in.cols + j]);
                        }
                    }
                }
            }
            
            // rows 3 above and 3 below around the center pixel, iterating over column
            for (int i = -2; i < 3; i++) {
                if (rand_pixel + 3 * in.cols + i < in.cols * in.rows && rand_pixel + 3 * in.cols + i >= 0) {
                    if ((rand_pixel + i) / in.cols == rand_pixel / in.cols) {
                        copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + 3 * in.cols + i]);
                    }
                }

                if (rand_pixel - 3 * in.cols + i < in.cols * in.rows && rand_pixel - 3 * in.cols + i >= 0) {
                    if ((rand_pixel + i) / in.cols == rand_pixel / in.cols) {
                        copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel - 3 * in.cols + i]);
                    }
                }
            }

            // 4 right, 4 left, 4 below, and 4 above the center pixel (respectively)
            if (rand_pixel + 4 < in.cols * in.rows) {
                if ((rand_pixel + 4) / in.cols == rand_pixel / in.cols) {
                    copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + 4]);
                }
            }

            if (rand_pixel - 4 > 0) {
                if ((rand_pixel - 4) / in.cols == rand_pixel / in.cols) {
                    copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel - 4]);
                }
            }

            if (rand_pixel + 4 * in.cols < in.cols * in.rows) {
                copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + 4 * in.cols]);
            }

            if (rand_pixel - 4 * in.cols > 0) {
                copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel - 4 * in.cols]);
            }
        } else { // case when r = 5

            // 7 x 9 rectangle around the center pixel
            for (int i = -3; i < 4; i++) {
                for (int j = -4; j < 5; j++) {
                    if (rand_pixel + i * in.cols + j < in.cols * in.rows && rand_pixel + i * in.cols + j >= 0) {
                        if ((rand_pixel + j) / in.cols == rand_pixel / in.cols) {
                            copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + i * in.cols + j]);
                        }
                    }
                }
            }

            // rows 4 above and 4 below around the center pixel, iterating over column
            for (int i = -3; i < 4; i++) {
                if (rand_pixel + 4 * in.cols + i < in.cols * in.rows && rand_pixel + 4 * in.cols + i >= 0) {
                    if ((rand_pixel + i) / in.cols == rand_pixel / in.cols) {
                        copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + 4 * in.cols + i]);
                    }
                }

                if (rand_pixel - 4 * in.cols + i < in.cols * in.rows && rand_pixel - 4 * in.cols + i >= 0) {
                    if ((rand_pixel + i) / in.cols == rand_pixel / in.cols) {
                        copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel - 4 * in.cols + i]);
                    }
                }
            }

            // pixels 5 right, 5 left, 5 below, and 5 above the center pixel
            if (rand_pixel + 5 < in.cols * in.rows) {
                if ((rand_pixel + 5) / in.cols == rand_pixel / in.cols) {
                    copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + 5]);
                }
            }

            if (rand_pixel - 5 > 0) {
                if ((rand_pixel - 5) / in.cols == rand_pixel / in.cols) {
                    copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel - 5]);
                }
            }

            if (rand_pixel + 5 * in.cols < in.cols * in.rows) {
                copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel + 5 * in.cols]);
            }

            if (rand_pixel - 5 * in.cols > 0) {
                copy_pixel(&in.data[rand_pixel], &new_in.data[rand_pixel - 5 * in.cols]);
            }
        }
    }
    return new_in;
}

/* Copies the RGB values from one Pixel into another Pixel using pointers.
* Used to make the pointilism code a bit more concise.
* @param: *orig_pixel - Pointer to the pixel whose RGB values are going to be copied into *new_pixel.
* @param: *new_pixel - Pointer to the pixel that is taking on the RGB value.
*/
void copy_pixel(Pixel *orig_pixel, Pixel *new_pixel) {
    new_pixel->r = orig_pixel->r;
    new_pixel->g = orig_pixel->g;
    new_pixel->b = orig_pixel->b;
}


/* Performs the Gaussian blur effect on an Image in, based on a sigma value, and returns
* a new blurred image new_in.
* @param: in - Image used to create a blurred version of itself.
* @param: sigma - Value used in calculations that determines the extent of the blur.
* @return: new_in - Image, a blurred version of the Image in.
*/
Image blur(const Image in , double sigma) {

    Image new_in = make_image(in.rows, in.cols);

    /* Making Gaussian Matrix */

    // Determining dimensions (must always be square, with an odd length, that is at least 10 * sigma)
    int g_size = (int)(10 * sigma);
    if (g_size % 2 == 0) {
        g_size++;
    }
    double Gaussian[g_size][g_size];

    // Represents vertical space from the center pixel (same as center of the Gaussian matrix)
    // Starts positive since top left is above the center
    int dy = g_size * 0.5;

    for (int r = 0; r < g_size; r++) {
        int dx = 0 - g_size * 0.5; // represents horizontal space from center, starts negative since top left is left of the center
        for (int c = 0; c < g_size; c++) {
            Gaussian[r][c] = (1.0 / (2.0 * PI * sq(sigma))) * exp(-(sq(dx) + sq(dy)) / (2.0 * sq(sigma))); // formula from directions
            dx++; // moves closer to the center horizontally
        }
        dy--; // moves closer to the center vertically
    }

    /* Actual Blurring */

    // dx is declared here so that it doesn't have to be redeclared for every single pixel
    int dx;

    /* These variables store the sum of the total red, green, and blue stored in the pixel after every iteration of the filter is applied,
    and also stores the sum of the filter elements that were applied (to account for when the matrix doesn't sum to 1, or when the pixel
    being worked with is on an edge. */
    double curr_filter_pixel_sum_r, curr_filter_pixel_sum_g, curr_filter_pixel_sum_b, curr_filter_total_sum;

    // iterates over every pixel
    for (int i = 0; i < in.rows * in.cols; i++) {
        curr_filter_pixel_sum_r = 0.0;
        curr_filter_pixel_sum_g = 0.0;
        curr_filter_pixel_sum_b = 0.0;
        curr_filter_total_sum = 0.0;

        // sums the products of the Gaussian distribution's weight and the pixel's color values
        for (int r = 0; r < g_size; r++) {
            dy = i / in.cols - g_size / 2 + r; // starting g_size / 2 above, as r increases, moves down
            for (int c = 0; c < g_size; c++) {
                dx = i % in.cols - g_size / 2 + c; // starting g_size / 2 left, as c increases, moves right
                if (dx >= 0 && dx < in.cols && dy >= 0 && dy < in.rows) {
                    curr_filter_pixel_sum_r += in.data[dy * in.cols + dx].r * Gaussian[r][c];
                    curr_filter_pixel_sum_g += in.data[dy * in.cols + dx].g * Gaussian[r][c];
                    curr_filter_pixel_sum_b += in.data[dy * in.cols + dx].b * Gaussian[r][c];
                    curr_filter_total_sum += Gaussian[r][c];
                }
            }
        }

        /* Sets the pixel's new color. Dividing by the total Gaussian sum is to account for edge pixels and low sigma values,
        since the Gaussian distribution matrix (or at least the extent of it that gets applied) doesn't sum to 1 in these cases.
        Without this, RGB values > 255 are set, causing issues. */
        new_in.data[i].r = curr_filter_pixel_sum_r / curr_filter_total_sum;
        new_in.data[i].g = curr_filter_pixel_sum_g / curr_filter_total_sum;
        new_in.data[i].b = curr_filter_pixel_sum_b / curr_filter_total_sum;
        
    }

    return new_in;
}
/* Performs a saturation effect on input Image in, and does it to the extent of scale.
* @param: in - Image used to create a saturated version of itself.
* @param: scale - Value specifying to what extent the saturation effect should be done.
* @return: new_in - Image, saturated version of the Image in.
*/
Image saturate(const Image in, double scale) {

    // Intialize and Declare Variables
    float gray;
    float difference_red;
    float difference_green;
    float difference_blue;
    Image new_in = make_image(in.rows, in.cols);

    // Runs through each pixel to change it to saturated
    for (int i = 0; i < in.rows * in.cols; i++) {

        // Gets greyscale variable
        gray = (int)floor(0.3 * in.data[i].r + 0.59 * in.data[i].g + 0.11 * in.data[i].b);

        // Gets the difference between each color 
        difference_red = in.data[i].r - gray;
        difference_green = in.data[i].g - gray;
        difference_blue = in.data[i].b - gray;

        // Ensures that grey stays within the range of 0-255 and assigns it to correct pixel color

        // This assigns it to red
        if (scale * difference_red + gray >= 255) {
            new_in.data[i].r = 255;
        } else if (scale * difference_red + gray <= 0) {
            new_in.data[i].r = 0;
        } else {
            new_in.data[i].r = scale * difference_red + gray;
        }

        // Assigns it to blue
        if (scale * difference_blue + gray >= 255) {
            new_in.data[i].b = 255;
        } else if (scale * difference_blue + gray <= 0) {
            new_in.data[i].b = 0;
        } else {
            new_in.data[i].b = scale * difference_blue + gray;
        }

        // Assigns it to green
        if (scale * difference_green + gray >= 255) {
            new_in.data[i].g = 255;
        } else if (scale * difference_green + gray <= 0) {
            new_in.data[i].g = 0;
        } else {
            new_in.data[i].g = scale * difference_green + gray;
        }

    }
    return new_in;
}