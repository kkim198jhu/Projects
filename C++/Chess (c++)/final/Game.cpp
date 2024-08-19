// Kyle Kim, kkim198
// Srisha Murthy Nippani, snippan1
// Joshua Brown, jbrow384
#include <cassert>
#include "Game.h"
#include "CreatePiece.h"

namespace Chess
{
	/////////////////////////////////////
	// DO NOT MODIFY THIS FUNCTION!!!! //
	/////////////////////////////////////
	Game::Game() : is_white_turn(true) {
		// Add the pawns
		for (int i = 0; i < 8; i++) {
			board.add_piece(Position('A' + i, '1' + 1), 'P');
			board.add_piece(Position('A' + i, '1' + 6), 'p');
		}

		// Add the rooks
		board.add_piece(Position( 'A'+0 , '1'+0 ) , 'R' );
		board.add_piece(Position( 'A'+7 , '1'+0 ) , 'R' );
		board.add_piece(Position( 'A'+0 , '1'+7 ) , 'r' );
		board.add_piece(Position( 'A'+7 , '1'+7 ) , 'r' );

		// Add the knights
		board.add_piece(Position( 'A'+1 , '1'+0 ) , 'N' );
		board.add_piece(Position( 'A'+6 , '1'+0 ) , 'N' );
		board.add_piece(Position( 'A'+1 , '1'+7 ) , 'n' );
		board.add_piece(Position( 'A'+6 , '1'+7 ) , 'n' );

		// Add the bishops
		board.add_piece(Position( 'A'+2 , '1'+0 ) , 'B' );
		board.add_piece(Position( 'A'+5 , '1'+0 ) , 'B' );
		board.add_piece(Position( 'A'+2 , '1'+7 ) , 'b' );
		board.add_piece(Position( 'A'+5 , '1'+7 ) , 'b' );

		// Add the kings and queens
		board.add_piece(Position( 'A'+3 , '1'+0 ) , 'Q' );
		board.add_piece(Position( 'A'+4 , '1'+0 ) , 'K' );
		board.add_piece(Position( 'A'+3 , '1'+7 ) , 'q' );
		board.add_piece(Position( 'A'+4 , '1'+7 ) , 'k' );
	}

	void Game::make_move(const Position& start, const Position& end) {
		//Throws exception if the start is not on the board
		if(!(start.first <= 'H' && start.first >= 'A' && start.second >= '1' && start.second <= '8')){
			throw Exception("start position is not on board");
		}

		//Throws exception if the end is not on the board
		if(!(end.first <= 'H' && end.first >= 'A' && end.second >= '1' && end.second <= '8')){
			throw Exception("end position is not on board");
		}

		//Throws exception if there is no starting piece on the board
		if(board(start) == NULL){
			throw Exception("no piece at start position");
		}

		char piece_designator = (board(start)) -> to_ascii();
		
		//Checks if the piece is white or black
		bool piece_is_black = (piece_designator == 'k' || piece_designator == 'q' || piece_designator == 'r' || piece_designator == 'b' || piece_designator == 'n' || piece_designator == 'p' || piece_designator == 'm');
		bool piece_is_white = (piece_designator == 'K' || piece_designator == 'Q' ||piece_designator == 'R' || piece_designator == 'B' || piece_designator == 'N' || piece_designator == 'P' || piece_designator == 'M');

		//Checks if it is a valid white piece
		if((is_white_turn && piece_is_black)){
			throw Exception("piece color and turn do not match");
		}

		//Checks if it is a valid black piece
		if((!is_white_turn && piece_is_white)){
			throw Exception("piece color and turn do not match");
		}

		// This is added for readability
		bool will_capture = (board(start) -> legal_capture_shape(start, end));
		bool legal_move = (board(start) -> legal_move_shape(start, end));
		//If it can move shape is illegal, then throw an exception
		
		// Checks if it will capture own piece
		const Piece * captured_piece = board(end);
		char captured_piece_char = 0;
		bool can_capture = false;
		if(captured_piece != NULL){
			can_capture = true;
			captured_piece_char = (captured_piece) -> to_ascii();
		}

		//Checks if a piece can move depending on if it can capture the piece or not
		if(can_capture == false){
			if(!(legal_move)){
				throw Exception("illegal move shape");
			}
		}

		//Cannot capture starting piece
		if(start.first == end.first && end.second == start.second){
			throw Exception("cannot capture own piece");
		}
		
		//Checks if the piece is white or black
		bool capture_piece_is_black = (captured_piece_char == 'k' || captured_piece_char == 'q' || captured_piece_char == 'r' || captured_piece_char == 'b' || captured_piece_char == 'n' || captured_piece_char == 'p' || captured_piece_char == 'm');
		bool capture_piece_is_white = (captured_piece_char == 'K' || captured_piece_char == 'Q' || captured_piece_char == 'R' || captured_piece_char == 'B' || captured_piece_char == 'N' || captured_piece_char == 'P' || captured_piece_char == 'M');
		//checks that there is a piece to capture and it will capture that piece
		if(can_capture){
			//Checks if it is white moving and white capturing
			if(capture_piece_is_white && is_white_turn){
				throw Exception("cannot capture own piece");
			//Checks if it is black moving and black capturing
			}else if(capture_piece_is_black && !is_white_turn){
				throw Exception("cannot capture own piece");
			}
		}

		//Test for illegal capture shape
		if(can_capture && !will_capture){
			throw Exception("illegal capture shape");
		}

		//If something is in path, then it can't do this (unless it's a knight)
		if(piece_designator != 'N' && piece_designator != 'n'){
			bool in_the_path = in_path(start, end);
			if(in_the_path){
				throw Exception("path is not clear");
			}
		}
		
		//This declares the king's position
		Position white_king_pos;
		Position black_king_pos;
		
		//Gets the Kings position through iteration
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			if (it->second) {
				switch (it->second->to_ascii()) {
					case 'K':
						white_king_pos = it->first;
						break;
					case 'k':
						black_king_pos = it->first;
						break;
				}
			}
		}

		//If the king will be placed in check if the piece move, throw exception
		if(captured_piece == NULL){
			if(in_path_of_check_uncaptured(start, end, piece_designator)){
				throw Exception("move exposes check");
			}
		}

		//If the king will be placed in check if the piece move, throw exception
		if(captured_piece != NULL){
			if(in_path_of_check_captured(start, end, captured_piece_char, piece_designator)){
				throw Exception("move exposes check");
			}
		}

		//This accounts for promotions for black
		if(piece_designator == 'p' && (end.second == '1')){
			piece_designator = 'q';
		}

		//This accounts for promotions for white
		if(piece_designator == 'P' && (end.second == '8')){
			piece_designator = 'Q';
		}

		
		//Actually Moves the piece to capture
		if(will_capture && captured_piece != NULL){
			
			board.delete_piece(end);
			board.add_piece(end, piece_designator);			
			board.delete_piece(start);
			
		//Moves the pice in general
		}else{
			board.add_piece(end, piece_designator);
			board.delete_piece(start);
			
		}
		is_white_turn = !is_white_turn;
			
		

	}

	// Returns true if can't move and return false if can move
	bool Game::in_path_of_check_uncaptured(const Position& start, const Position& end, const char piece_designator){
		//Moves the piece
		board.delete_piece(start);
		board.add_piece(end, piece_designator);
		//Checks if it is in check
		if(in_check(is_white_turn)){
			//Moves piece back
			board.add_piece(start, piece_designator);
			board.delete_piece(end);
			return true;
		}else{
			board.add_piece(start, piece_designator);
			board.delete_piece(end);
			//If it's not any of these conditions i.e. the pieces are not in these lines, then returns false
			return false;
		}
	}

	// Returns true if can't move and return false if can move
	bool Game::in_path_of_check_captured(const Position& start, const Position& end, const char capture_char, const char piece_designator){
		//Moves the piece
		board.delete_piece(start);
		board.delete_piece(end);
		board.add_piece(end, piece_designator);
		//Checks if it is in check
		if(in_check(is_white_turn)){
			//Moves piece back
			board.delete_piece(end);
			board.add_piece(start, piece_designator);
			board.add_piece(end, capture_char);
			return true;
		}else{
			board.delete_piece(end);
			board.add_piece(start, piece_designator);
			board.add_piece(end, capture_char);
			//If it's not any of these conditions i.e. the pieces are not in these lines, then returns false
			return false;
		}
	}

	// Returns true if there is something in the path and return false if it nothing
	bool Game::in_path(const Position& start, const Position& end) const{
		int distance_vertical = end.second - start.second ;
		int distance_horizontal = end.first - start.first ;
		Position test_pos;

		//Checks that it moves diagonally or horizontal or vertical
		if(!((distance_horizontal == 0 && distance_vertical != 0) || (distance_horizontal != 0 && distance_vertical == 0)
		|| (distance_horizontal == distance_vertical) || (distance_horizontal == (-1 * distance_vertical)))){
			return false;
		}

		//If horizontal is 0, therefore it must move in a straight line vertically
		if(distance_horizontal == 0){
			test_pos.first = start.first;
			//Test downwards
			if (distance_vertical < 0){
				for(int i = 1; i < (-1 * distance_vertical); i ++){
					test_pos.second = (char)(start.second - i);

					//Makes sure it doesn't skip squares and if it is the way
					if(!(board(start) -> legal_move_shape(start, end))){
						return false;
					}	

					if(board(test_pos) != NULL){
						return true;
					}
				}
			// Test upwards
			}else if (distance_vertical > 0){
				for(int i = 1; i < distance_vertical; i ++){
					test_pos.second = (char)(start.second + i);

					//Makes sure it doesn't skip squares and if it is the way
					if(!(board(start) -> legal_move_shape(start, end))){
						return false;
					}	

					if(board(test_pos) != NULL){
						return true;
					}
				}
			}
		//If vertical is 0, therefore it must move in a straight line horzontally
		}else if(distance_vertical == 0){
			test_pos.second = start.second;
			//Test left
			if (distance_horizontal < 0){
				for(int i = 1; i < (-1 * distance_horizontal); i ++){
					test_pos.first = (char)(start.first - i);

					//Makes sure it doesn't skip squares and if it is the way
					if(!(board(start) -> legal_move_shape(start, end))){
						return false;
					}	

					if(board(test_pos) != NULL){
						return true;
					}
				}
			// Test right
			}else if (distance_horizontal > 0){
				for(int i = 1; i < distance_horizontal; i ++){
					test_pos.first = (char)(start.first + i);

					//Makes sure it doesn't skip squares and if it is the way
					if(!(board(start) -> legal_move_shape(start, end))){
						return false;
					}	

					if(board(test_pos) != NULL){
						return true;
					}
				}
			}
			
		//If vertical is positive and horizontal is positive, it must move in a up-right line
		}else if(distance_vertical > 0 && distance_horizontal > 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < distance_vertical; i++){
				test_pos.first = (char)(start.first + i);
				test_pos.second = (char)(start.second + i);

				//Makes sure it doesn't skip squares and if it is the way
				if(!(board(start) -> legal_move_shape(start, end))){
					return false;
				}	

				if(board(test_pos) != NULL){
					return true;
				}
			}

		//If vertical is positive and horizontal is negative, it must move in a up-left line
		}else if(distance_vertical > 0 && distance_horizontal < 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < distance_vertical; i++){
				test_pos.first = (char)(start.first - i);
				test_pos.second = (char)(start.second + i);

				//Makes sure it doesn't skip squares and if it is the way
				if(!(board(start) -> legal_move_shape(start, end))){
					return false;
				}
				if(board(test_pos) != NULL){
					return true;
				}
			}

		//If vertical is negative and horizontal is negative, it must move in a down-left line
		}else if(distance_vertical < 0 && distance_horizontal < 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < (-1*distance_vertical); i++){
				test_pos.first = (char)(start.first - i);
				test_pos.second = (char)(start.second - i);

				//Makes sure it doesn't skip squares and if it is the way
				if(!(board(start) -> legal_move_shape(start, end))){
					return false;
				}

				if(board(test_pos) != NULL){
					return true;
				}
			}

		//If vertical is negative and horizontal is negative, it must move in a down-right line
		}else if(distance_vertical < 0 && distance_horizontal > 0){
			
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < (-1*distance_vertical); i++){
				test_pos.first = (char)(start.first + i);
				test_pos.second = (char)(start.second - i);

				//Makes sure it doesn't skip squares and if it is the way
				if(!(board(start) -> legal_move_shape(start, end))){
					return false;
				}

				if(board(test_pos) != NULL){
					return true;
				}
			}
		}

		//If there is nothing in the path
		return false;
	}

	Position Game::the_king_pos(const bool& white) const{
		//This declares the king's position
		Position white_king_pos;
		Position black_king_pos;
		
		//Gets the Kings position through iteration
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			if (it->second) {
				switch (it->second->to_ascii()) {
					case 'K':
						white_king_pos = it->first;
						break;
					case 'k':
						black_king_pos = it->first;
						break;
				}
			}
		}

		//King Position is set
		Position king_pos;
		if(white){
			king_pos = white_king_pos;
		}else{
			king_pos = black_king_pos;
		}
		return king_pos;
	}

	bool Game::in_check(const bool& white) const {
		//Gets the king position
		Position king_pos = the_king_pos(white);

		//This declares the piece's position
		Position piece_position;
		
		//Checks every single piece and if it is attacking the king
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			char test_piece_char = it -> second -> to_ascii();
			//Checks if the piece is white or black
			bool test_piece_is_black = (test_piece_char == 'k' || test_piece_char == 'q' || test_piece_char == 'r' || test_piece_char == 'b' || test_piece_char == 'n' || test_piece_char == 'p' || test_piece_char == 'm');
			bool test_piece_is_white = (test_piece_char == 'K' || test_piece_char == 'Q' || test_piece_char == 'R' || test_piece_char == 'B' || test_piece_char == 'N' || test_piece_char == 'P' || test_piece_char == 'M');

			//Gets the position of the piece
			piece_position.first = (it -> first).first;
			piece_position.second = (it -> first).second;
			Piece * my_piece = it->second;
			//Checks if it is a legal capture shape to the king's position
			if (my_piece->legal_capture_shape(piece_position, king_pos)) {
				//Checks that it's white's turn and if the piece attacking is black
				if(white && test_piece_is_black){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'n'){
						return true;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, king_pos)) == false){
							return true;
						}
					}
				//Checks that it's black's turn and if the piece attacking is white
				}else if(!(white) && test_piece_is_white){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'N'){
						return true;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, king_pos)) == false){
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	// Returns true if that square the king tries to move to cause the king to be in check and false if not
	bool Game::will_be_in_check(const bool& white, Position king_pos) const{
		//This declares the piece's position
		Position piece_position;
		
		//Checks every single piece and if it is attacking the king
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			char test_piece_char = it -> second -> to_ascii();
			//Checks if the piece is white or black
			bool test_piece_is_black = (test_piece_char == 'k' || test_piece_char == 'q' || test_piece_char == 'r' || test_piece_char == 'b' || test_piece_char == 'n' || test_piece_char == 'p' || test_piece_char == 'm');
			bool test_piece_is_white = (test_piece_char == 'K' || test_piece_char == 'Q' || test_piece_char == 'R' || test_piece_char == 'B' || test_piece_char == 'N' || test_piece_char == 'P' || test_piece_char == 'M');

			//Gets the position of the piece
			piece_position.first = (it -> first).first;
			piece_position.second = (it -> first).second;

			//Checks if it is a legal capture shape to the king's position
			if (it->second->legal_capture_shape(piece_position, king_pos)) {
				//Checks that it's white's turn and if the piece attacking is black
				if(white && test_piece_is_black){
					//Checks if it is a knight
					if(it->second->to_ascii() == 'n'){
						return true;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path_check(!white, piece_position, king_pos)) == false){
							return true;
						}
					}
				//Checks that it's black's turn and if the piece attacking is white
				}else if(!(white) && test_piece_is_white){
					//Checks if it is a knight
					if(it->second->to_ascii() == 'N'){
						return true;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path_check(!white, piece_position, king_pos)) == false){
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	// Returns if the top row (above piece) is in check and false if not
	bool Game::top_row_in_check(const bool& white, Position king_pos) const{
		king_pos.second = king_pos.second + 1;
		//If king square's above is not on board
		if(!(king_pos.second <= '8')){
			return true;
		}
		
		bool top_left_use = true;
		bool top_right_use = true;
		bool top_middle_use = true;

		//Sets and copy top_left
		Position top_left;
		top_left.first = king_pos.first - 1;
		top_left.second = king_pos.second;
		if(!(top_left.first >= 'A')){
			top_left_use = false;
		}
		if(same_piece_square(!white, top_left)){
			top_left_use = false;
		}

		//Sets and copy top_middle (No need for error checking)
		Position top_middle;
		top_middle.first = king_pos.first;
		top_middle.second = king_pos.second;
		if(same_piece_square(!white, top_middle)){
			top_middle_use = false;
		}
		
		//Sets and copy top_right
		Position top_right;
		top_right.first = king_pos.first + 1;
		top_right.second = king_pos.second;
		if(!(top_right.first <= 'H')){
			top_right_use = false;
		}
		if(same_piece_square(!white, top_right)){
			top_right_use = false;
		}

		//Checks the top left
		if(top_left_use){
			if(!(will_be_in_check(!white, top_left))){
				return false;
			}
		}
		//Checks the top middle
		if(top_middle_use && !(will_be_in_check(!white, top_middle))){
			return false;
		}

		//Checks the top right
		if(top_right_use){
			if(!(will_be_in_check(!white, top_right))){
				return false;
			}
		}

		return true;
	}


	// Returns if the top row (above piece) is in check and false if not
	bool Game::bottom_row_in_check(const bool& white, Position king_pos) const{
		king_pos.second = king_pos.second - 1;
		//If king square's above is not on board
		if(!(king_pos.second >= '1')){
			return true;
		}
		
		bool bottom_left_use = true;
		bool bottom_right_use = true;
		bool bottom_middle_use = true;

		//Sets and copy bottom_left
		Position bottom_left;
		bottom_left.first = king_pos.first - 1;
		bottom_left.second = king_pos.second;
		if(!(bottom_left.first >= 'A')){
			bottom_left_use = false;
		}
		if(same_piece_square(!white, bottom_left)){
			bottom_left_use = false;
		}

		//Sets and copy bottom_middle (No need for error checking)
		Position bottom_middle;
		bottom_middle.first = king_pos.first;
		bottom_middle.second = king_pos.second;
		if(same_piece_square(!white, bottom_middle)){
			bottom_middle_use = false;
		}

		//Sets and copy bottom_right
		Position bottom_right;
		bottom_right.first = king_pos.first + 1;
		bottom_right.second = king_pos.second;
		if(!(bottom_right.first <= 'H')){
			bottom_right_use = false;
		}
		if(same_piece_square(!white, bottom_right)){
			bottom_right_use = false;
		}

		//Checks the bottom left
		if(bottom_left_use){
			if(!(will_be_in_check(!white, bottom_left))){
				return false;
			}
		}

		//Checks the bottom middle
		if(bottom_middle_use && !(will_be_in_check(!white, bottom_middle))){
			return false;
		}

		//Checks the bottom right
		if(bottom_right_use){
			if(!(will_be_in_check(!white, bottom_right))){
				return false;
			}
		}

		return true;
	}

	// Returns if the right col (right piece) is in check and false if not
	bool Game::right_col_in_check(const bool& white, Position king_pos) const{
		king_pos.first = king_pos.first + 1;
		//If king square's above is not on board
		if(!(king_pos.first <= 'H')){
			return true;
		}
		
		bool right_top_use = true;
		bool right_middle_use = true;
		bool right_bottom_use = true;

		//Sets and copy right top
		Position right_up;
		right_up.first = king_pos.first;
		right_up.second = king_pos.second + 1;
		if(!(right_up.second <= '8')){
			right_top_use = false;
		}
		if(same_piece_square(!white, right_up)){
			right_top_use = false;
		}

		//Sets and copy right_middle (No need for error checking)
		Position right_middle;
		right_middle.first = king_pos.first;
		right_middle.second = king_pos.second;
		if(same_piece_square(!white, right_middle)){
			right_middle_use = false;
		}

		//Sets and copy right_bottom
		Position right_bottom;
		right_bottom.first = king_pos.first;
		right_bottom.second = king_pos.second - 1;
		if(!(right_bottom.second >= '1')){
			right_bottom_use = false;
		}
		if(same_piece_square(!white, right_bottom)){
			right_bottom_use = false;
		}

		//Checks the right top
		if(right_top_use){
			if(!(will_be_in_check(!white, right_up))){
				return false;
			}
		}

		//Checks the right middle
		if(right_middle_use && !(will_be_in_check(!white, right_middle))){
			return false;
		}
		//Checks the right bottom
		if(right_bottom_use){
			if(!(will_be_in_check(!white, right_bottom))){
				return false;
			}
		}

		return true;
	}

	// Returns if the left col (right piece) is in check and false if not
	bool Game::left_col_in_check(const bool& white, Position king_pos) const{
		king_pos.first = king_pos.first - 1;
		//If king square's above is not on board
		if(!(king_pos.first >= 'A')){
			return true;
		}
		
		bool left_top_use = true;
		bool left_middle_use = true;
		bool left_bottom_use = true;

		//Sets and copy left top
		Position left_up;
		left_up.first = king_pos.first;
		left_up.second = king_pos.second + 1;
		if(!(left_up.second <= '8')){
			left_top_use = false;
		}
		if(same_piece_square(!white, left_up)){
			left_top_use = false;
		}


		//Sets and copy left middle (No need for error checking)
		Position left_middle;
		left_middle.first = king_pos.first;
		left_middle.second = king_pos.second;
		if(same_piece_square(!white, left_middle)){
			left_middle_use = false;
		}
		
		//Sets and copy left bottom
		Position left_bottom;
		left_bottom.first = king_pos.first;
		left_bottom.second = king_pos.second - 1;
		if(!(left_bottom.second >= '1')){
			left_bottom_use = false;
		}
		if(same_piece_square(!white, left_bottom)){
			left_bottom_use = false;
		}


		//Checks the left top
		if(left_top_use){
			if(!(will_be_in_check(!white, left_up))){
				return false;
			}
		} 
		//Checks the left middle
		if(left_middle_use && !(will_be_in_check(!white, left_middle))){
			return false;
		}
		//Checks the left bottom
		if(left_bottom_use){
			if(!(will_be_in_check(!white, left_bottom))){
				return false;
			}
		}

		return true;
	}

	// Returns true if there is a piece of same color in a spot trying to move to else return false
	bool Game::same_piece_square(const bool& white, Position new_pos) const{
		char my_test;
		//If there is a piece run this
		if(board(new_pos) != NULL){
			// Checks if the piece is white or blakc piece
			my_test = board(new_pos) -> to_ascii();
			bool test_piece_is_black = (my_test == 'k' || my_test == 'q' || my_test == 'r' || my_test == 'b' || my_test == 'n' || my_test == 'p' || my_test == 'm');
			bool test_piece_is_white = (my_test == 'K' || my_test == 'Q' || my_test == 'R' || my_test == 'B' || my_test == 'N' || my_test == 'P' || my_test == 'M');
			//If it can't move to this square
			if(white && test_piece_is_white){
				return true;
			}else if (!white && test_piece_is_black){
				return true;
			}
		}
		return false;
	}

	// Returns true if the designated player is in mate
	bool Game::in_mate(const bool& white) const{
		//Gets the king position
		Position king_pos = the_king_pos(white);

		//King must be in check 
		//If king can move to surrounding squares, not in mate
		if(!in_check(white)){
			return false;
		}else if (!left_col_in_check(!white, king_pos)){
			return false;
		}else if (!right_col_in_check(!white, king_pos)){
			return false;
		}else if (!top_row_in_check(!white, king_pos)){
			return false;
		}else if (!bottom_row_in_check(!white, king_pos)){
			return false;
		}
		
		//If it is in double/triple check; return true;
		if(number_of_checks(white) > 1){
			return true;
		}

		//Checks if we can capture the checking piece
		if(can_be_captured(!white)){
			return false;
		}

		//If a piece can block the path of check, not in mate
		if(can_block(!white)){
			return false;
		}

		return true;
	}
	

	// Returns true if there is can be something can block the piece and return false if it nothing
	bool Game::can_block(const bool& white) const{
		Position start = the_check_piece_pos(!white);
		Position end = the_king_pos(!white);

		int distance_vertical = end.second - start.second ;
		int distance_horizontal = end.first - start.first ;
		Position test_pos;

		//Checks that it moves diagonally or horizontal or vertical
		if(!((distance_horizontal == 0 && distance_vertical != 0) || (distance_horizontal != 0 && distance_vertical == 0)
		|| (distance_horizontal == distance_vertical) || (distance_horizontal == (-1 * distance_vertical)))){
			return false;
		}

		//If horizontal is 0, therefore it must move in a straight line vertically
		if(distance_horizontal == 0){
			test_pos.first = start.first;
			//Test downwards
			if (distance_vertical < 0){
				for(int i = 1; i < (-1 * distance_vertical); i ++){
					test_pos.second = (char)(start.second - i);
					//Makes sure it can be blocked here
					if((board(start) -> legal_capture_shape(start, end))){
						//If something can move there, return true
						if(can_move_here(!white, test_pos)){
							return true;
						}
					}	
				}
			// Test upwards
			}else if (distance_vertical > 0){
				for(int i = 1; i < distance_vertical; i ++){
					test_pos.second = (char)(start.second + i);
					//Makes sure it can be blocked here
					if((board(start) -> legal_capture_shape(start, end))){
						//If something can move there, return true
						if(can_move_here(!white, test_pos)){
							return true;
						}
					}	

				}
			}
		//If vertical is 0, therefore it must move in a straight line horzontally
		}else if(distance_vertical == 0){
			test_pos.second = start.second;
			//Test left
			if (distance_horizontal < 0){
				for(int i = 1; i < (-1 * distance_horizontal); i ++){
					test_pos.first = (char)(start.first - i);

					//Makes sure it can be blocked here
					if((board(start) -> legal_capture_shape(start, end))){
						//If something can move there, return true
						if(can_move_here(!white, test_pos)){
							return true;
						}
					}	

				}
			// Test right
			}else if (distance_horizontal > 0){
				for(int i = 1; i < distance_horizontal; i ++){
					test_pos.first = (char)(start.first + i);

					//Makes sure it can be blocked here
					if((board(start) -> legal_capture_shape(start, end))){
						//If something can move there, return true
						if(can_move_here(!white, test_pos)){
							return true;
						}
					}	

				}
			}
			
		//If vertical is positive and horizontal is positive, it must move in a up-right line
		}else if(distance_vertical > 0 && distance_horizontal > 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < distance_vertical; i++){
				test_pos.first = (char)(start.first + i);
				test_pos.second = (char)(start.second + i);

				//Makes sure it can be blocked here
				if((board(start) -> legal_capture_shape(start, end))){
					//If something can move there, return true
					if(can_move_here(!white, test_pos)){
						return true;
					}
				}	
			}

		//If vertical is positive and horizontal is negative, it must move in a up-left line
		}else if(distance_vertical > 0 && distance_horizontal < 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < distance_vertical; i++){
				test_pos.first = (char)(start.first - i);
				test_pos.second = (char)(start.second + i);

				//Makes sure it can be blocked here
				if((board(start) -> legal_capture_shape(start, end))){
					//If something can move there, return true
					if(can_move_here(!white, test_pos)){
						return true;
					}
				}	

			}

		//If vertical is negative and horizontal is negative, it must move in a down-left line
		}else if(distance_vertical < 0 && distance_horizontal < 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < (-1*distance_vertical); i++){
				test_pos.first = (char)(start.first - i);
				test_pos.second = (char)(start.second - i);

				//Makes sure it can be blocked here
				if((board(start) -> legal_capture_shape(start, end))){
					//If something can move there, return true
					if(can_move_here(!white, test_pos)){
						return true;
					}
				}	

			}

		//If vertical is negative and horizontal is negative, it must move in a down-right line
		}else if(distance_vertical < 0 && distance_horizontal > 0){
			
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < (-1*distance_vertical); i++){
				test_pos.first = (char)(start.first + i);
				test_pos.second = (char)(start.second - i);

				//Makes sure it can be blocked here
				if((board(start) -> legal_capture_shape(start, end))){
					//If something can move there, return true
					if(can_move_here(!white, test_pos)){
						return true;
					}
				}	

			}
		}

		//If there is nothing in the path
		return false;
	}

	// Returns true if there is can be something can move here and return false if it nothing
	bool Game::can_move_here(const bool& white, const Position& end) const{
		//Opposing Piece
		Position piece_position;
		bool Pawn_capture_valid;
		//Checks every single piece and if it can move there
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			Pawn_capture_valid = true;
			char test_piece_char = it -> second -> to_ascii();
			//Checks if the piece is white or black excluding kings as they can't block check
			bool test_piece_is_black = (test_piece_char == 'q' || test_piece_char == 'r' || test_piece_char == 'b' || test_piece_char == 'n' || test_piece_char == 'p' || test_piece_char == 'm');
			bool test_piece_is_white = (test_piece_char == 'Q' || test_piece_char == 'R' || test_piece_char == 'B' || test_piece_char == 'N' || test_piece_char == 'P' || test_piece_char == 'M');

			//Gets the position of the piece
			piece_position.first = (it -> first).first;
			piece_position.second = (it -> first).second;
			Piece * my_piece = it->second;
			
			//Take into account black pawn movements
			if(my_piece->legal_capture_shape(piece_position, end) != my_piece->legal_move_shape(piece_position, end)){
				//Makes sure it is capturing a actual piece
				if(my_piece->legal_capture_shape(piece_position, end)){
					if(board(end) == NULL ){
						Pawn_capture_valid = false;
					}
				//Makes sure in front of the pawn isn't blocked
				}else if(my_piece->legal_move_shape(piece_position, end)){
					if(board(end) != NULL){
						Pawn_capture_valid = false;
					}
				}
			}


			//Checks if it is a legal capture shape to the end's position
			if ((!in_path_of_pin(!white, piece_position)) && ((my_piece->legal_move_shape(piece_position, end) || (my_piece->legal_capture_shape(piece_position, end)))) && Pawn_capture_valid) {
				//Checks that it's white's turn and if the piece moving is white
				if(white && test_piece_is_white){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'N'){
						return true;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, end)) == false){
							//Checks if it can move here
							return true;
						}
					}
				//Checks that it's black's turn and if the piece moving is black
				}else if(!(white) && test_piece_is_black){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'n'){
						return true;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, end)) == false){
							return true;
						}
					}
				}
			}
		}

		//This should never happen, but to added to not throw warnings
		return false;
	}

	//Returns true if there is a piece that can capture and false if not
	bool Game::can_be_captured(const bool& white) const{
		//Gets the king position and checking piece
		bool opposing_color = !white;
		
		Position checking_pos = the_check_piece_pos(opposing_color);

		//Opposing Piece
		Position piece_position;
		bool king_in_check;
		//Checks every single piece and if it is attacking the king
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			king_in_check = false;
			char test_piece_char = it -> second -> to_ascii();
			//Checks if the piece is white or black
			bool test_piece_is_black = (test_piece_char == 'k' || test_piece_char == 'q' || test_piece_char == 'r' || test_piece_char == 'b' || test_piece_char == 'n' || test_piece_char == 'p' || test_piece_char == 'm');
			bool test_piece_is_white = (test_piece_char == 'K' || test_piece_char == 'Q' || test_piece_char == 'R' || test_piece_char == 'B' || test_piece_char == 'N' || test_piece_char == 'P' || test_piece_char == 'M');

			//Gets the position of the piece
			piece_position.first = (it -> first).first;
			piece_position.second = (it -> first).second;
			Piece * my_piece = it->second;

			if(opposing_color && test_piece_is_white && test_piece_char == 'K'){
				king_in_check = will_be_in_check(!white, checking_pos);
			}else if(!(opposing_color) && test_piece_is_black && test_piece_char == 'k'){
				king_in_check = will_be_in_check(!white, checking_pos);
			}
			//Checks if it is a legal capture shape to the king's position
			if (my_piece->legal_capture_shape(piece_position, checking_pos) && !king_in_check) {
				//Checks that it's white's turn and if the piece attacking is black
				if(opposing_color && test_piece_is_white){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'N'){
						return true;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, checking_pos)) == false){
							return true;
						}
					}
				//Checks that it's black's turn and if the piece attacking is white
				}else if(!(opposing_color) && test_piece_is_black){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'n'){
						return true;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, checking_pos)) == false){
							return true;
						}
					}
				}
			}
		}

		//This should never happen, but to added to not throw warnings
		return false;
	}


	//Get the position of the piece that is checking the king and returns it
	Position Game::the_check_piece_pos(const bool& white) const{
		//Gets the king position
		Position king_pos = the_king_pos(white);

		//This declares the piece's position
		Position piece_position;
		
		//Checks every single piece and if it is attacking the king
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			char test_piece_char = it -> second -> to_ascii();
			//Checks if the piece is white or black
			bool test_piece_is_black = (test_piece_char == 'k' || test_piece_char == 'q' || test_piece_char == 'r' || test_piece_char == 'b' || test_piece_char == 'n' || test_piece_char == 'p' || test_piece_char == 'm');
			bool test_piece_is_white = (test_piece_char == 'K' || test_piece_char == 'Q' || test_piece_char == 'R' || test_piece_char == 'B' || test_piece_char == 'N' || test_piece_char == 'P' || test_piece_char == 'M');

			//Gets the position of the piece
			piece_position.first = (it -> first).first;
			piece_position.second = (it -> first).second;
			Piece * my_piece = it->second;
			//Checks if it is a legal capture shape to the king's position
			if (my_piece->legal_capture_shape(piece_position, king_pos)) {
				//Checks that it's white's turn and if the piece attacking is black
				if(white && test_piece_is_black){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'n'){
						return piece_position;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, king_pos)) == false){
							return piece_position;
						}
					}
				//Checks that it's black's turn and if the piece attacking is white
				}else if(!(white) && test_piece_is_white){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'N'){
						return piece_position;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, king_pos)) == false){
							return piece_position;
						}
					}
				}
			}
		}

		//This should never happen, but to added to not throw warnings
		return piece_position;
	}


	//Gets the number of pieces checking the king and returns it
	int Game::number_of_checks(const bool& white) const{
		//Sets count to 0
		int number_of_check = 0;

		//Gets the king position
		Position king_pos = the_king_pos(white);

		//This declares the piece's position
		Position piece_position;
		
		//Checks every single piece and if it is attacking the king
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			char test_piece_char = it -> second -> to_ascii();
			//Checks if the piece is white or black
			bool test_piece_is_black = (test_piece_char == 'k' || test_piece_char == 'q' || test_piece_char == 'r' || test_piece_char == 'b' || test_piece_char == 'n' || test_piece_char == 'p' || test_piece_char == 'm');
			bool test_piece_is_white = (test_piece_char == 'K' || test_piece_char == 'Q' || test_piece_char == 'R' || test_piece_char == 'B' || test_piece_char == 'N' || test_piece_char == 'P' || test_piece_char == 'M');

			//Gets the position of the piece
			piece_position.first = (it -> first).first;
			piece_position.second = (it -> first).second;
			Piece * my_piece = it->second;
			//Checks if it is a legal capture shape to the king's position
			if (my_piece->legal_capture_shape(piece_position, king_pos)) {
				//Checks that it's white's turn and if the piece attacking is black
				if(white && test_piece_is_black){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'n'){
						number_of_check = number_of_check + 1;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, king_pos)) == false){
							number_of_check = number_of_check + 1;
						}
					}
				//Checks that it's black's turn and if the piece attacking is white
				}else if(!(white) && test_piece_is_white){
					//Checks if it is a knight
					if(my_piece->to_ascii() == 'N'){
						number_of_check = number_of_check + 1;
					}else{
						//Check if there is something in the path if it's not a knight
						if((in_path(piece_position, king_pos)) == false){
							number_of_check = number_of_check + 1;
						}
					}
				}
			}
		}

		return number_of_check;
	}

	bool Game::in_stalemate(const bool& white) const {

		//Gets the king position
		Position king_pos = the_king_pos(white);

		//King must not be in check 
		//If king can move to surrounding squares, not in mate
		if(in_check(white)){
			return false;
		}else if (!left_col_in_check(!white, king_pos)){
			return false;
		}else if (!right_col_in_check(!white, king_pos)){
			return false;
		}else if (!top_row_in_check(!white, king_pos)){
			return false;
		}else if (!bottom_row_in_check(!white, king_pos)){
			return false;
		}

		// Player has no legal moves
		// Loop through all of the squares on the board
		Position test_pos;
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				test_pos.first = (char)('A'+i);
				test_pos.second = (char)('1'+j);

				// Checks if it will capture own piece
				const Piece * captured_piece = board(test_pos);
				char captured_piece_char = 0;
				if(captured_piece != NULL){
					captured_piece_char = (captured_piece) -> to_ascii();
				}

				//Checks if the piece is white or black
				bool capture_piece_is_black = (captured_piece_char == 'k' || captured_piece_char == 'q' || captured_piece_char == 'r' || captured_piece_char == 'b' || captured_piece_char == 'n' || captured_piece_char == 'p' || captured_piece_char == 'm');
				bool capture_piece_is_white = (captured_piece_char == 'K' || captured_piece_char == 'Q' || captured_piece_char == 'R' || captured_piece_char == 'B' || captured_piece_char == 'N' || captured_piece_char == 'P' || captured_piece_char == 'M');
				//checks that there is a piece to capture and it will capture that piece
				if(captured_piece == NULL){
					// Checks if any piece can move to this square
					if(can_move_here(white, test_pos)){
						return false;
					}
				}else{
					//Checks if it is it's own piece
					if((capture_piece_is_white && !white) || (capture_piece_is_black && white)){
						// Checks if any piece can move to this square
						if(can_move_here(white, test_pos)){
							return false;
						}
					}
				}

				
			}
		}

		return true;
	}


	int Game::white_point_value() const{
		int point_value = 0;
		Position piece_position;
		//Checks every single piece and if it is attacking the king
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			char test_piece_char = it -> second -> to_ascii();
			//Checks if the piece is white or black
			bool test_piece_is_white = (test_piece_char == 'K' || test_piece_char == 'Q' || test_piece_char == 'R' || test_piece_char == 'B' || test_piece_char == 'N' || test_piece_char == 'P' || test_piece_char == 'M');

			//Gets the position of the piece
			piece_position.first = (it -> first).first;
			piece_position.second = (it -> first).second;

			//Count only if it is white
			if(test_piece_is_white){
				//Add point value
				switch(test_piece_char){
					case 'R':
						point_value += 5;
						break;
					case 'B':
						point_value += 3;
						break;
					case 'N':
						point_value += 3;
						break;
					case 'P':
						point_value += 1;
						break;
					case 'Q':
						point_value += 9;
						break;
					case 'M':
						point_value += (it -> second) -> point_value();
						break;
				}
			}
		}

		//Returns total point value of color
        return point_value;
    
	}

	int Game::black_point_value() const{
		int point_value = 0;
		Position piece_position;
		//Checks every single piece and if it is attacking the king
		for (std::map<std::pair<char, char>, Piece*>::const_iterator it = board.get_map_beg(); it != board.get_map_end(); it++) {
			char test_piece_char = it -> second -> to_ascii();
			//Checks if the piece is white or black
			bool test_piece_is_black = (test_piece_char == 'k' || test_piece_char == 'q' || test_piece_char == 'r' || test_piece_char == 'b' || test_piece_char == 'n' || test_piece_char == 'p' || test_piece_char == 'm');
			//Gets the position of the piece
			piece_position.first = (it -> first).first;
			piece_position.second = (it -> first).second;

			//Count only if it is white
			if(test_piece_is_black){
				//Add point value
				
				switch(test_piece_char){
					case 'r':
						point_value += 5;
						break;
					case 'b':
						point_value += 3;
						break;
					case 'n':
						point_value += 3;
						break;
					case 'p':
						point_value += 1;
						break;
					case 'q':
						point_value += 9;
						break;
					case 'm':
						point_value += (it -> second) -> point_value();
						break;
				}
			}
		}

		//Returns total point value of color
        return point_value;
    
	}

    // Return the total material point value of the designated player
    int Game::point_value(const bool& white) const {
		int point_value = 0;

		//Gets point value depending if it is white or black
		if(white){
			point_value = white_point_value();
		}else{
			point_value = black_point_value();
		}
		//Returns point value
		return point_value;
    }


    std::istream& operator>> (std::istream& is, Game& game) {

		// Clears the board before loading the game
		for(char r = '8'; r >= '1'; r--) {
      		for(char c = 'A'; c <= 'H'; c++) {
				game.board.delete_piece(Position(c, r));
			}
		}

		std::string line;
		int row = 0;

		// Iterates over each line in the input stream (each row of the board)
		while (is >> line && row < 8) {

			// Iterates over each character (piece) on the board
			for (int i = 0; i < (int)line.length(); i++) {
				if (line[i] != '-') {
					game.board.add_piece(Position('A' + i, '8' - row), line[i]);
				}
			}
			row++;
		}

		// The last line is used to determine whose turn it is
		is >> line;
		if (line[0] == 'w') {
			game.is_white_turn = true;
		} else {
			game.is_white_turn = false;
		}

		return is;
	}

    /////////////////////////////////////
    // DO NOT MODIFY THIS FUNCTION!!!! //
    /////////////////////////////////////
	std::ostream& operator<< (std::ostream& os, const Game& game) {
		// Write the board out and then either the character 'w' or the character 'b',
		// depending on whose turn it is
		return os << game.board << (game.turn_white() ? 'w' : 'b');
	}

	// Helper Function of in_path_of_pin, which shortens the code if the king is in check
	// Only works on straight lines and returns if it is a opposing queen or rook
	// Returns 0 if it is protected, 1 if would be in check
	bool Game::in_path_pin_straight(const Position& piece, const Position& end) const{
		char my_piece;
		bool piece_is_white;
		bool piece_is_black;

		//Checks that it's not the same color piece and if so, return false
		my_piece = board(piece) -> to_ascii();
		piece_is_black = (my_piece == 'k' || my_piece == 'q' || my_piece == 'r' || my_piece == 'b' || my_piece == 'n' || my_piece == 'p' || my_piece == 'm');
		piece_is_white = (my_piece == 'K' || my_piece == 'Q' || my_piece == 'R' || my_piece == 'B' || my_piece == 'N' || my_piece == 'P' || my_piece == 'M');
		//If it is a white piece and black turn, then it blocks any attacks from this direction
		if(piece_is_white && is_white_turn){
			return false;
		//If it is a black piece and black turn, then it blocks any attacks from this direction
		}else if(piece_is_black && !is_white_turn){
			return false;
		//If it is being attacked/checked by a rook or queen
		}else if(is_white_turn && piece_is_black && !(in_path(piece, end))){
			return true;
		}else if(!is_white_turn && piece_is_white && !(in_path(piece, end))){
			return true;
		}

		// Returns this if the opposing piece is a knight,bishop, pawn, or king
		return false;
	}

	// Helper Function of in_path_of_check, which shortens the code if the king is in check
	// Only works on straight lines and returns if it is a opposing queen or rook
	// Returns 0 if it is protected, 1 if would be in check, and 2 if nothing happens
	bool Game::in_path_pin_diagonal(const Position& piece, const Position& end) const{
		char my_piece;
		bool piece_is_white;
		bool piece_is_black;

		//Checks that it's not the same color piece and if so, return false
		my_piece = board(piece) -> to_ascii();
		piece_is_black = (my_piece == 'k' || my_piece == 'q' || my_piece == 'r' || my_piece == 'b' || my_piece == 'n' || my_piece == 'p' || my_piece == 'm');
		piece_is_white = (my_piece == 'K' || my_piece == 'Q' || my_piece == 'R' || my_piece == 'B' || my_piece == 'N' || my_piece == 'P' || my_piece == 'M');

		//If it is a white piece and black turn, then it blocks any attacks from this direction
		if(piece_is_white && is_white_turn){
			return false;
		//If it is a black piece and black turn, then it blocks any attacks from this direction
		}else if(piece_is_black && !is_white_turn){
			return false;
		//If it is being attacked/checked by a rook or queen
		}else if(is_white_turn && piece_is_black && !(in_path(piece, end))){
			return true;
		}else if(!is_white_turn && piece_is_white && !(in_path(piece, end))){
			return true;
		}

		// Returns this if the opposing piece is a knight,bishop, pawn, or king
		return false;
	}

	// Returns true if can't move and return false if can move
	bool Game::in_path_of_pin(const bool& white, const Position& piece) const{
		Position king = the_king_pos(!white);

		int distance_vertical = king.second - piece.second;
		int distance_horizontal = king.first - piece.first;
		Position test_pos;
		bool test;

		if(in_path_unspecific(piece, king)){
			return false;
		}

		//Test if it is in check vertically
		if(distance_horizontal == 0){
			test_pos.first = piece.first;
			//Test upwards
			if (distance_vertical < 0){
				for(int i = 1; i <= ('8' - piece.second); i++){
					//Changes Position
					test_pos.second = (char)(piece.second + i);
					//Checks if there is something that can put it in check
					if(board(test_pos) != NULL){
						test = in_path_pin_straight(test_pos, piece);
						if(test == false) {
							return false;
						}else if(test == true){
							return true;
						}
					}
				}
			// Test downwards
			}else if (distance_vertical > 0){
				test_pos.first = piece.first;
				for(int i = 1; i <= (piece.second - '1'); i ++){
					//Changes Position
					test_pos.second = (char)(piece.second - i);
					//Checks if there is something that can put it in check
					if(board(test_pos) != NULL){
						test = in_path_pin_straight(test_pos, piece);
						if(test == false) {
							return false;
						}else if(test == true){
							return true;
						}
					}
				}
			}
		}else if(distance_vertical == 0){
			test_pos.second = piece.second;
			//Test rightwards
			if (distance_horizontal < 0){
				for(int i = 1; i <= ('H' - piece.first); i ++){
					//Changes Position
					test_pos.first = (char)(piece.first + i);
					//Checks if there is something that can put it in check
					if(board(test_pos) != NULL){
						test = in_path_pin_straight(test_pos, piece);
						if(test == false) {
							return false;
						}else if(test == true){
							return true;
						}
					}
				}
			// Test leftwards
			}else if (distance_horizontal > 0){
				test_pos.second = piece.second;
				for(int i = 1; i <= (piece.first - 'A'); i ++){
					//Changes Position
					test_pos.first = (char)(piece.first - i);
					//Checks if there is something that can put it in check
					if(board(test_pos) != NULL){
						test = in_path_pin_straight(test_pos, piece);
						if(test == false) {
							return false;
						}else if(test == true){
							return true;
						}
					}
				}
			}
		//Makes sure that it's in a diagonal
		}else if (distance_vertical == distance_horizontal){

			//If vertical is positive and horizontal is positive, it must move in a down-left line
			if(distance_vertical > 0){
				//Sets length, so does go out of bounds
				int length = 1;
				if((piece.second - '1') < (piece.first - 'A')){
					length = (piece.second - '1');
				}else{
					length = (piece.first - 'A');
				}
				for(int i = 1; i <= length; i ++){
					//Changes Position
					test_pos.first = (char)(piece.first - i);
					test_pos.second = (char)(piece.second - i);
					//Checks if there is something that can put it in check
					if(board(test_pos) != NULL){
						test = in_path_pin_diagonal(test_pos, piece);
						if(test == false) {
							return false;
						}else if(test == true){
							return true;
						}
					}
				}

			//If vertical is negative and horizontal is negtaive, it must move in a up-right line
			}else if(distance_vertical < 0){
				//Sets length, so does go out of bounds
				int length = 1;
				if(('8' - piece.second) < ('H' - piece.first)){
					length = ('8' - piece.second);
				}else{
					length = ('H' - piece.first);
				}

				for(int i = 1; i <= length; i ++){
					//Changes Position
					test_pos.first = (char)(piece.first + i);
					test_pos.second = (char)(piece.second + i);
					//Checks if there is something that can put it in check
					if(board(test_pos) != NULL){
						test = in_path_pin_diagonal(test_pos, piece);
						if(test == false) {
							return false;
						}else if(test == true){
							return true;
						}
					}
				}
			}
		}else if (distance_vertical == (-1 * distance_horizontal)){
			//If vertical is positive and horizontal is negative, it must move in a down-right line
			if(distance_vertical > 0){
				//Sets length, so does go out of bounds
				int length = 1;
				if((piece.second - '1') < ('H' - piece.first)){
					length = (piece.second - '1');
				}else{
					length = ('H' - piece.first);
				}

				for(int i = 1; i <= length; i ++){
					//Changes Position
					test_pos.first = (char)(piece.first - i);
					test_pos.second = (char)(piece.second + i);
					//Checks if there is something that can put it in check
					if(board(test_pos) != NULL){
						test = in_path_pin_diagonal(test_pos, piece);
						if(test == false) {
							return false;
						}else if(test == true){
							return true;
						}
					}
				}

			//If vertical is negative and horizontal is negtaive, it must move in a up-left line
			}else if(distance_vertical < 0){
				//Sets length, so does go out of bounds
				int length = 1;
				if(('8' - piece.second) < (piece.first - 'A')){
					length = ('8' - piece.second);
				}else{
					length = (piece.first - 'A');
				}
				for(int i = 1; i <= length; i ++){
					//Changes Position
					test_pos.first = (char)(piece.first + i);
					test_pos.second = (char)(piece.second - i);
					//Checks if there is something that can put it in check
					if(board(test_pos) != NULL){
						test = in_path_pin_diagonal(test_pos, piece);
						if(test == false) {
							return false;
						}else if(test == true){
							return true;
						}
					}
				}
			}
		}

		//If it's not any of these conditions i.e. the pieces are not in these lines, then returns false
		return false;
	}

	// Returns true if there is something in the path and return false if it nothing (different by if king doesn't matter)
	bool Game::in_path_check(const bool& white, const Position& start, const Position& end) const{
		int distance_vertical = end.second - start.second;
		int distance_horizontal = end.first - start.first;
		bool king_is_there = false;
		char piece_designator;
		Position test_pos;

		//Checks that it moves diagonally or horizontal or vertical
		if(!((distance_horizontal == 0 && distance_vertical != 0) || (distance_horizontal != 0 && distance_vertical == 0)
		|| (distance_horizontal == distance_vertical) || (distance_horizontal == (-1 * distance_vertical)))){
			return false;
		}

		//If horizontal is 0, therefore it must move in a straight line vertically
		if(distance_horizontal == 0){
			test_pos.first = start.first;
			//Test downwards
			if (distance_vertical < 0){
				for(int i = 1; i < (-1 * distance_vertical); i ++){
					test_pos.second = (char)(start.second - i);

					piece_designator = 0;
					//Checks if there if a king there
					if(board(test_pos) != NULL){
						piece_designator = (board(test_pos)) -> to_ascii();
					}

					if(white && piece_designator == 'k'){
						king_is_there = true;
					}else if(!white && piece_designator == 'K'){
						king_is_there = true;
					}


					//Makes sure it doesn't skip squares and if it is the way
					if(!(board(start) -> legal_capture_shape(start, end))){
						return false;
					}	

					if(board(test_pos) != NULL && !king_is_there){
						return true;
					}

					king_is_there = false;
				}
			// Test upwards
			}else if (distance_vertical > 0){
				for(int i = 1; i < distance_vertical; i ++){
					test_pos.second = (char)(start.second + i);

					piece_designator = 0;
					//Checks if there if a king there
					if(board(test_pos) != NULL){
						piece_designator = (board(test_pos)) -> to_ascii();
					}
				
					if(white && piece_designator == 'k'){
						king_is_there = true;
					}else if(!white && piece_designator == 'K'){
						king_is_there = true;
					}


					//Makes sure it doesn't skip squares and if it is the way
					if(!(board(start) -> legal_capture_shape(start, end))){
						return false;
					}	

					if(board(test_pos) != NULL && !king_is_there){
						return true;
					}

					king_is_there = false;
				}
			}
		//If vertical is 0, therefore it must move in a straight line horzontally
		}else if(distance_vertical == 0){
			test_pos.second = start.second;
			//Test left
			if (distance_horizontal < 0){
				for(int i = 1; i < (-1 * distance_horizontal); i ++){
					test_pos.first = (char)(start.first - i);

					piece_designator = 0;
					//Checks if there if a king there
					if(board(test_pos) != NULL){
						piece_designator = (board(test_pos)) -> to_ascii();
					}
				
					if(white && piece_designator == 'k'){
						king_is_there = true;
					}else if(!white && piece_designator == 'K'){
						king_is_there = true;
					}


					//Makes sure it doesn't skip squares and if it is the way
					if(!(board(start) -> legal_capture_shape(start, end))){
						return false;
					}	

					if(board(test_pos) != NULL && !king_is_there){
						return true;
					}

					king_is_there = false;
				}
			// Test right
			}else if (distance_horizontal > 0){
				for(int i = 1; i < distance_horizontal; i ++){
					test_pos.first = (char)(start.first + i);

					piece_designator = 0;
					//Checks if there if a king there
					if(board(test_pos) != NULL){
						piece_designator = (board(test_pos)) -> to_ascii();
					}
				
					if(white && piece_designator == 'k'){
						king_is_there = true;
					}else if(!white && piece_designator == 'K'){
						king_is_there = true;
					}


					//Makes sure it doesn't skip squares and if it is the way
					if(!(board(start) -> legal_capture_shape(start, end))){
						return false;
					}	

					if(board(test_pos) != NULL && !king_is_there){
						return true;
					}

					king_is_there = false;
				}
			}
			
		//If vertical is positive and horizontal is positive, it must move in a up-right line
		}else if(distance_vertical > 0 && distance_horizontal > 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < distance_vertical; i++){
				test_pos.first = (char)(start.first + i);
				test_pos.second = (char)(start.second + i);

				piece_designator = 0;
				//Checks if there if a king there
				if(board(test_pos) != NULL){
					piece_designator = (board(test_pos)) -> to_ascii();
				}
			
				if(white && piece_designator == 'k'){
					king_is_there = true;
				}else if(!white && piece_designator == 'K'){
					king_is_there = true;
				}


				//Makes sure it doesn't skip squares and if it is the way
				if(!(board(start) -> legal_capture_shape(start, end))){
					return false;
				}	

				if(board(test_pos) != NULL && !king_is_there){
					return true;
				}

				king_is_there = false;
			}

		//If vertical is positive and horizontal is negative, it must move in a up-left line
		}else if(distance_vertical > 0 && distance_horizontal < 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < distance_vertical; i++){
				test_pos.first = (char)(start.first - i);
				test_pos.second = (char)(start.second + i);

				piece_designator = 0;
				//Checks if there if a king there
				if(board(test_pos) != NULL){
					piece_designator = (board(test_pos)) -> to_ascii();
				}
			
				if(white && piece_designator == 'k'){
					king_is_there = true;
				}else if(!white && piece_designator == 'K'){
					king_is_there = true;
				}


				//Makes sure it doesn't skip squares and if it is the way
				if(!(board(start) -> legal_capture_shape(start, end))){
					return false;
				}	

				if(board(test_pos) != NULL && !king_is_there){
					return true;
				}

				king_is_there = false;
			}

		//If vertical is negative and horizontal is negative, it must move in a down-left line
		}else if(distance_vertical < 0 && distance_horizontal < 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < (-1*distance_vertical); i++){
				test_pos.first = (char)(start.first - i);
				test_pos.second = (char)(start.second - i);

				piece_designator = 0;
				//Checks if there if a king there
				if(board(test_pos) != NULL){
					piece_designator = (board(test_pos)) -> to_ascii();
				}
			
				if(white && piece_designator == 'k'){
					king_is_there = true;
				}else if(!white && piece_designator == 'K'){
					king_is_there = true;
				}


				//Makes sure it doesn't skip squares and if it is the way
				if(!(board(start) -> legal_capture_shape(start, end))){
					return false;
				}	

				if(board(test_pos) != NULL && !king_is_there){
					return true;
				}

				king_is_there = false;
			}

		//If vertical is negative and horizontal is negative, it must move in a down-right line
		}else if(distance_vertical < 0 && distance_horizontal > 0){
			
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < (-1*distance_vertical); i++){
				test_pos.first = (char)(start.first + i);
				test_pos.second = (char)(start.second - i);

				piece_designator = 0;
				//Checks if there if a king there
				if(board(test_pos) != NULL){
					piece_designator = (board(test_pos)) -> to_ascii();
				}
			
				if(white && piece_designator == 'k'){
					king_is_there = true;
				}else if(!white && piece_designator == 'K'){
					king_is_there = true;
				}


				//Makes sure it doesn't skip squares and if it is the way
				if(!(board(start) -> legal_capture_shape(start, end))){
					return false;
				}	

				if(board(test_pos) != NULL && !king_is_there){
					return true;
				}

				king_is_there = false;
			}
		}

		//If there is nothing in the path
		return false;
	}


	// Returns true if there is something in the path (not checking legal move) and return false if it nothing
	bool Game::in_path_unspecific(const Position& start, const Position& end) const{
		int distance_vertical = end.second - start.second ;
		int distance_horizontal = end.first - start.first ;
		Position test_pos;

		//Checks that it moves diagonally or horizontal or vertical
		if(!((distance_horizontal == 0 && distance_vertical != 0) || (distance_horizontal != 0 && distance_vertical == 0)
		|| (distance_horizontal == distance_vertical) || (distance_horizontal == (-1 * distance_vertical)))){
			return false;
		}

		//If horizontal is 0, therefore it must move in a straight line vertically
		if(distance_horizontal == 0){
			test_pos.first = start.first;
			//Test downwards
			if (distance_vertical < 0){
				for(int i = 1; i < (-1 * distance_vertical); i ++){
					test_pos.second = (char)(start.second - i);

					if(board(test_pos) != NULL){
						return true;
					}
				}
			// Test upwards
			}else if (distance_vertical > 0){
				for(int i = 1; i < distance_vertical; i ++){
					test_pos.second = (char)(start.second + i);

					if(board(test_pos) != NULL){
						return true;
					}
				}
			}
		//If vertical is 0, therefore it must move in a straight line horzontally
		}else if(distance_vertical == 0){
			test_pos.second = start.second;
			//Test left
			if (distance_horizontal < 0){
				for(int i = 1; i < (-1 * distance_horizontal); i ++){
					test_pos.first = (char)(start.first - i);

					if(board(test_pos) != NULL){
						return true;
					}
				}
			// Test right
			}else if (distance_horizontal > 0){
				for(int i = 1; i < distance_horizontal; i ++){
					test_pos.first = (char)(start.first + i);

					if(board(test_pos) != NULL){
						return true;
					}
				}
			}
			
		//If vertical is positive and horizontal is positive, it must move in a up-right line
		}else if(distance_vertical > 0 && distance_horizontal > 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < distance_vertical; i++){
				test_pos.first = (char)(start.first + i);
				test_pos.second = (char)(start.second + i);

				if(board(test_pos) != NULL){
					return true;
				}
			}

		//If vertical is positive and horizontal is negative, it must move in a up-left line
		}else if(distance_vertical > 0 && distance_horizontal < 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < distance_vertical; i++){
				test_pos.first = (char)(start.first - i);
				test_pos.second = (char)(start.second + i);

				if(board(test_pos) != NULL){
					return true;
				}
			}

		//If vertical is negative and horizontal is negative, it must move in a down-left line
		}else if(distance_vertical < 0 && distance_horizontal < 0){
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < (-1*distance_vertical); i++){
				test_pos.first = (char)(start.first - i);
				test_pos.second = (char)(start.second - i);

				if(board(test_pos) != NULL){
					return true;
				}
			}

		//If vertical is negative and horizontal is negative, it must move in a down-right line
		}else if(distance_vertical < 0 && distance_horizontal > 0){
			
			//Note we do not need to check if it is a valid move as that is already done
			for(int i = 1; i < (-1*distance_vertical); i++){
				test_pos.first = (char)(start.first + i);
				test_pos.second = (char)(start.second - i);

				if(board(test_pos) != NULL){
					return true;
				}
			}
		}

		//If there is nothing in the path
		return false;
	}

}