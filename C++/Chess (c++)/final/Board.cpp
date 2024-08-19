// Kyle Kim, kkim198
// Srisha Murthy Nippani, snippan1
// Joshua Brown, jbrow384

#include <iostream>
#include <utility>
#include <map>
#ifndef _WIN32
#include "Terminal.h"
#endif // !_WIN32
#include "Board.h"
#include "CreatePiece.h"
#include "Exceptions.h"

namespace Chess
{
  /////////////////////////////////////
  // DO NOT MODIFY THIS FUNCTION!!!! //
  /////////////////////////////////////
  Board::Board(){}

  //Default destructor
  Board::~Board(){
    //Deletes all the pieces from the board
    for (std::map<std::pair<char, char>, Piece*>::const_iterator it = occ.begin(); it != occ.end();it++) {
      delete (*it).second;
	  }
  }

  const Piece* Board::operator()(const Position& position) const {
    //Checks if position is correct 
    if (occ.find(position) == occ.end()) {
      //Can't find the position
      return NULL;
    } else {
      //Finds piece and returns value
      Piece* my_piece = (occ.find(position))->second;
      return my_piece;
    }
  }

  //Deletes the piece at the position
  void Board::delete_piece(const Position& position){
    if (occ.find(position) != occ.end()) {
      //Finds piece and returns value
      const Piece* my_piece = (occ.find(position))->second;
      delete my_piece;
      occ.erase(position);
    }
  }
  
  void Board::add_piece(const Position& position, const char& piece_designator) {
    //Error if the piece is not a piece
    if(!(piece_designator == 'K' || piece_designator == 'Q' ||  piece_designator == 'R' || piece_designator == 'B' || 
    piece_designator == 'N' || piece_designator == 'P' || piece_designator == 'M' || piece_designator == 'k' || 
    piece_designator == 'q' || piece_designator == 'r' || piece_designator == 'b' ||
    piece_designator == 'n' || piece_designator == 'p' || piece_designator == 'm')){
      throw Exception("invalid designator");
    } 

    //Error if it's not in the board
    if(!(position.first <= 'H' && position.first >= 'A' && position.second >= '1' && position.second <= '8')){
      throw Exception("invalid position");
    }

    //Error position is already filled
    if(occ[position] != NULL){
      throw Exception("position is occupied");
    }

    occ[position] = create_piece(piece_designator);
  }

  void Board::display() const {
    for(char r = '8'; r >= '1'; r--) {
      std::cout << r << " ";
      for(char c = 'A'; c <= 'H'; c++) {
        const Piece* piece = (*this)(Position(c, r));
        if (piece) {
          std::cout << piece->to_ascii();
        } else {
          std::cout << '-';
        }
        std::cout << ' ';
      }
      std::cout << std::endl;
    }
     std::cout << "  A B C D E F G H" << std::endl;

  }


  
  bool Board::has_valid_kings() const {
    int white_king_count = 0;
    int black_king_count = 0;
    for (std::map<std::pair<char, char>, Piece*>::const_iterator it = occ.begin();
	 it != occ.end();
	 it++) {
      if (it->second) {
	switch (it->second->to_ascii()) {
	case 'K':
	  white_king_count++;
	  break;
	case 'k':
	  black_king_count++;
	  break;
	}
      }
    }
    return (white_king_count == 1) && (black_king_count == 1);
  }

  /////////////////////////////////////
  // DO NOT MODIFY THIS FUNCTION!!!! //
  /////////////////////////////////////
  std::ostream& operator<<(std::ostream& os, const Board& board) {
    for(char r = '8'; r >= '1'; r--) {
      for(char c = 'A'; c <= 'H'; c++) {
	const Piece* piece = board(Position(c, r));
	if (piece) {
	  os << piece->to_ascii();
	} else {
	  os << '-';
	}
      }
      os << std::endl;
    }
    return os;
  }
}
