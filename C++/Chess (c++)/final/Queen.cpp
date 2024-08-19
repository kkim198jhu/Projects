// Kyle Kim, kkim198
// Srisha Murthy Nippani, snippan1
// Joshua Brown, jbrow384

#include "Queen.h"

namespace Chess
{
  bool Queen::legal_move_shape(const Position& start, const Position& end) const {

    // Getting the horizontal and vertical movement for the piece
    // Absolute value is used because it doesn't matter which way left/right
    // or up/down the piece moves to make this comparison.
    int horizontal = std::abs(end.first - start.first);
    int vertical = std::abs(end.second - start.second);

    //Makes sure the queen can't attack itself
    if(horizontal == 0 && vertical == 0){
      return false;
    }

    // Since the piece is a queen, it can only move diagonally,
    // horizontally, or vertically.
    if (!(horizontal == vertical || horizontal == 0 || vertical == 0 || (start.first == end.first && start.second == end.second))) {
      return false;
    } 

    return true;
  }
}
