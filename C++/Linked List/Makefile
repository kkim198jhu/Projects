CC=g++
CFLAGS=-std=c++11 -Wall -Wextra -pedantic

# Links together files needed to create executable for the target cTreeTest and runs target test
cTreeTest: CTreeTest.o CTree.o
	$(CC) -o cTreeTest CTreeTest.o CTree.o

#  Links together files needed to create executable for the target tTreeTest and runs target test
tTreeTest: TTreeTest.o
	$(CC) -o tTreeTest TTreeTest.o -g

# Compiles CTreeTest.c to create CTreeTest.o
TTreeTest.o: TTreeTest.cpp Tree.h Tree.inc
	$(CC) $(CFLAGS) -c TTreeTest.cpp -g

# Compiles CTreeTest.c to create CTreeTest.o
CTreeTest.o: CTreeTest.cpp CTree.h
	$(CC) $(CFLAGS) -c CTreeTest.cpp 

# Compiles CTree.c to create CTree.o
# Note that we list CTree.h here as a file that
# CTree.c depends on, since CTree.c #includes it
CTree.o: CTree.cpp CTree.h
	$(CC) $(CFLAGS) -c CTree.cpp

# Removes all object files and the executable,
# so we can start fresh
clean:
	rm -f *.o cTreeTest tTreeTest