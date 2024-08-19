// Kyle Kim, kkim198
// Srisha Murthy Nippani, snippan1
// Joshua Brown, jbrow384
#include "Bishop.h"

namespace Chess
{
  bool Bishop::legal_move_shape(const Position& start, const Position& end) const {

    // Getting the horizontal and vertical movement for the piece
    // Absolute value is used because it doesn't matter which way left/right
    // or up/down the piece moves to make this comparison.
    int horizontal = std::abs(end.first - start.first);
    int vertical = std::abs(end.second - start.second);
    
    //Makes sure the queen can't attack itself
    if(horizontal == 0 && vertical == 0){
      return false;
    }

    // Since the piece is a bishop, it can only move diagonally, so it must move
    // the same amount vertically as it does horizontally.
    if (!(horizontal == vertical || (start.first == end.first && start.second == end.second))) {
      return false;
    } 

    return true;
  }
}
