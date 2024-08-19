CC = g++
CONSERVATIVE_FLAGS = -std=c++11 -Wall -Wextra -pedantic
DEBUGGING_FLAGS = -g -O0
CFLAGS = $(CONSERVATIVE_FLAGS) $(DEBUGGING_FLAGS)


chess: main.o Board.o Game.o CreatePiece.o Bishop.o King.o Knight.o Pawn.o Queen.o Rook.o
	$(CC) -o chess main.o Board.o Game.o CreatePiece.o Bishop.o King.o Knight.o Pawn.o Queen.o Rook.o -g

Board.o: Board.cpp Piece.h Pawn.h Rook.h Knight.h Bishop.h Queen.h King.h Mystery.h CreatePiece.h Terminal.h
	$(CC) -c Board.cpp $(CFLAGS) -g

Game.o: Game.cpp Board.h Game.h Piece.h
	$(CC) -c Game.cpp $(CFLAGS) -g

CreatePiece.o: CreatePiece.cpp Board.h Game.h Piece.h Pawn.h Rook.h Knight.h Bishop.h Queen.h King.h Mystery.h 
	$(CC) -c CreatePiece.cpp $(CFLAGS) -g

Bishop.o: Bishop.cpp Bishop.h Piece.h
	$(CC) -c Bishop.cpp $(CFLAGS) -g

King.o: King.cpp King.h Piece.h
	$(CC) -c King.cpp $(CFLAGS) -g

Knight.o: Knight.cpp Knight.h Piece.h
	$(CC) -c Knight.cpp $(CFLAGS) -g

Pawn.o: Pawn.cpp Pawn.h Piece.h
	$(CC) -c Pawn.cpp $(CFLAGS) -g

Queen.o: Queen.cpp Queen.h Piece.h
	$(CC) -c Queen.cpp $(CFLAGS) -g

Rook.o: Rook.cpp Rook.h Piece.h
	$(CC) -c Rook.cpp $(CFLAGS) -g

main.o: main.cpp Board.h Game.h Piece.h Pawn.h Rook.h Knight.h Bishop.h Queen.h King.h Mystery.h 
	$(CC) -c main.cpp $(CFLAGS) -g

.PHONY: clean all
clean:
	rm -f *.o chess
