# name: Kyle Kim
# JHED: kkim198.

CC=gcc
CFLAGS=-g -std=c99 -pedantic -Wall -Wextra

# Links together files needed to create executable for the target word_search
word_search: search_functions.o word_search.o
	$(CC) -o word_search search_functions.o word_search.o

# Links together files needed to create executable for the target test and runs target test
test: search_functions.o test_search_functions.o
	$(CC) -o test search_functions.o test_search_functions.o
	./test 
	
# Compiles word_search.c to create word_search.o
# Note that we list search_functions.h here as a file that
# word_search.c depends on, since word_search.c #includes it
word_search.o: word_search.c search_functions.h
	$(CC) $(CFLAGS) -c word_search.c

# Compiles test_search_functions.c to create test_search_functions.o
# Note that we list search_functions.h here as a file that
# test_search_functions.c depends on, since test_search_functions.c #includes it
test_search_functions.o: test_search_functions.c search_functions.h
	$(CC) $(CFLAGS) -c test_search_functions.c

# Compiles search_functions.c to create search_functions.o
# Note that we list search_functions.h here as a file that
# search_functions.c depends on, since search_functions.c #includes it
search_functions.o: search_functions.c search_functions.h
	$(CC) $(CFLAGS) -c search_functions.c

# Removes all object files and the executable named main,
# so we can start fresh
clean:
	rm -f *.o main