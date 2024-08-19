// Kyle Kim, kkim198
// Srisha Murthy Nippani, snippan1
// Joshua Brown, jbrow384

#include "Piece.h"
#include "Pawn.h"
#include "Board.h"

namespace Chess{
    //Checks the legal moves for Rook
    bool Rook::legal_move_shape(const Position& start, const Position& end) const  {
        //Checks if it's within the columns is the same but not the row
        if(start.first == end.first && start.second != end.second){
            return true;

        //Checks if it's within the row is the same but not the column
        }else if(start.first != end.first && start.second == end.second) {
            return true;
        
        //Returns False
        }else{
            return false;
        }
    }
}