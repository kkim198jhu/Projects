CC=gcc
CFLAGS=-g -std=c99 -pedantic -Wall -Wextra

# Links together files needed to create executable for the target project
project: project.o image_manip.o ppm_io.o
	$(CC) -o project project.o image_manip.o ppm_io.o -lm

# Links together files needed to create executable for the target project
img_cmp: img_cmp.o image_manip.o ppm_io.o
	$(CC) -o img_cmp img_cmp.o image_manip.o ppm_io.o -lm

# Compiles project.c to create project.o
img_cmp.o: img_cmp.c
	$(CC) $(CFLAGS) -c img_cmp.c

# Compiles project.c to create project.o
# Note that we list ppm_io.h and image_manip.h here as a file that
# project.c depends on, since project.c #includes it
project.o: project.c ppm_io.h image_manip.h
	$(CC) $(CFLAGS) -c project.c

# Compiles image_manip.c to create image_manip.o
# Note that we list image_manip.h here as a file that
# image_manip.c depends on, since image_manip.c #includes it
image_manip.o: image_manip.c image_manip.h
	$(CC) $(CFLAGS) -c image_manip.c -lm

# Compiles ppm_io.c to create ppm_io.o
# Note that we list ppm_io.h here as a file that
# ppm_io.c depends on, since ppm_io.c #includes it
ppm_io.o: ppm_io.c ppm_io.h
	$(CC) $(CFLAGS) -c ppm_io.c

# Removes all object files and the executable named image_manip,ppm_io,project,
# so we can start fresh
clean:
	rm -f *.o image_manip
	rm -f *.o ppm_io
	rm -f *.o project