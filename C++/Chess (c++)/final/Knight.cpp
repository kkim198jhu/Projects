// Kyle Kim, kkim198
// Srisha Murthy Nippani, snippan1
// Joshua Brown, jbrow384
#include "Knight.h"

namespace Chess
{
  bool Knight::legal_move_shape(const Position& start, const Position& end) const {
    /* Let horizontal change be dx and vertical change be dy.
    Then, a legal move implies dx^2 + dy^2 = 1^2 + 2^2 = 5.
    Since dx and dy must be integers, a move is legal if and only if dx^2 + dy^2 = 5.
    */
    int dx = std::abs(end.first - start.first);
    int dy = std::abs(end.second - start.second);
    if ((dx*dx + dy*dy) != 5) return false;
    if ((dx*dx + dy*dy) == 0) return false; // piece did not move
    return true;
  }
}