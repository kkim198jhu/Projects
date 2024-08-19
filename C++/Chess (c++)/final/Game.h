// Kyle Kim, kkim198
// Srisha Murthy Nippani, snippan1
// Joshua Brown, jbrow384
#ifndef GAME_H
#define GAME_H

#include <iostream>
#include "Piece.h"
#include "Board.h"
#include "Exceptions.h"

namespace Chess
{

	class Game {

	public:
		// This default constructor initializes a board with the standard
		// piece positions, and sets the state to white's turn
		Game();

		// Returns true if it is white's turn
		/////////////////////////////////////
		// DO NOT MODIFY THIS FUNCTION!!!! //
		/////////////////////////////////////
		bool turn_white() const { return is_white_turn; }
    
    /////////////////////////////////////
		// DO NOT MODIFY THIS FUNCTION!!!! //
		/////////////////////////////////////
    // Displays the game by printing it to stdout
		void display() const { board.display(); }
    
    /////////////////////////////////////
		// DO NOT MODIFY THIS FUNCTION!!!! //
		/////////////////////////////////////
    // Checks if the game is valid
		bool is_valid_game() const { return board.has_valid_kings(); }

		// Attempts to make a move. If successful, the move is made and
		// the turn is switched white <-> black. Otherwise, an exception is thrown
		void make_move(const Position& start, const Position& end);

		// Returns true if the designated player is in check
		bool in_check(const bool& white) const;

		// Returns true if the designated player is in mate
		bool in_mate(const bool& white) const;

		// Returns true if the designated player is in mate
		bool in_stalemate(const bool& white) const;

        // Return the total material point value of the designated player
        int point_value(const bool& white) const;

		// Returns the total material point value of the black player
		int black_point_value() const;

		// Returns the total material point value of the white player
		int white_point_value() const;

		// Returns true if there is something in the path and return false if it nothing
		bool in_path(const Position& start, const Position& end) const;

		// Returns true if that square the king tries to move to cause the king to be in check and 
		// false if not
		bool will_be_in_check(const bool& white, Position king_pos) const;

		//Gets the position of the king and returns it
		Position the_king_pos(const bool& white) const;

		//Get the position of the piece that is checking the king and returns it
		Position the_check_piece_pos(const bool& white) const;

		//Returns true if there is a piece that can capture and false if not
		bool can_be_captured(const bool& white) const; 

		//Gets the number of pieces checking the king and returns it
		int number_of_checks(const bool& white) const;

		// Returns if the top row (above piece) is in check and false if not
		bool top_row_in_check(const bool& white, Position king_pos) const;

		// Returns if the bottom row (bellow piece) is in check and false if not
		bool bottom_row_in_check(const bool& white, Position king_pos) const;

		// Returns if the right col (right piece) is in check and false if not
		bool right_col_in_check(const bool& white, Position king_pos) const;

		// Returns if the right col (right piece) is in check and false if not
		bool left_col_in_check(const bool& white, Position king_pos) const;

		// Returns true if there is a piece of same color in a spot trying to move to else return false
		bool same_piece_square(const bool& white, Position new_pos) const;

		// Returns true if can't move and return false if can move if it is not capturing something
		bool in_path_of_check_uncaptured(const Position& start, const Position& end, const char piece_designator);

		// Returns true if can't move and return false if can move if it is capturing something
		bool in_path_of_check_captured(const Position& start, const Position& end, const char capture_char, const char piece_designator);

		// Returns true if there is can be something can move here and return false if it nothing
		bool can_move_here(const bool& white, const Position& end) const;

		// Returns true if there is can be something can block the piece and return false if it nothing
		bool can_block(const bool& white) const;

		// Helper Function of in_path_of_pin, which shortens the code if the king is in pin
		// Only works on straight lines and returns if it is a opposing queen or rook
		// Returns 0 if it is protected, 1 if would be in check, 
		bool in_path_pin_diagonal(const Position& piece, const Position& end) const;

		// Helper Function of in_path_of_pin, which shortens the code if the king is in pin
		// Only works on straight lines and returns if it is a opposing queen or rook
		// Returns 0 if it is protected, 1 if would be in check
		bool in_path_pin_straight(const Position& piece, const Position& end) const;

		// Returns true if can't move and return false if can move
		bool in_path_of_pin(const bool& white, const Position& piece) const;

		// Returns true if there is something in the path and return false if it nothing (different by if king doesn't matter)
		bool in_path_check(const bool& white, const Position& start, const Position& end) const;

		// Returns true if there is something in the path (not checking legal move) and return false if it nothing
		bool in_path_unspecific(const Position& start, const Position& end) const;

	private:
		// The board
		Board board;

		// Is it white's turn?
		bool is_white_turn;

        // Writes the board out to a stream
        friend std::ostream& operator<< (std::ostream& os, const Game& game);

        // Reads the board in from a stream
        friend std::istream& operator>> (std::istream& is, Game& game);
	};
}
#endif // GAME_H
