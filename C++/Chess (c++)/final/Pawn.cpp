// Kyle Kim, kkim198
// Srisha Murthy Nippani, snippan1
// Joshua Brown, jbrow384

#include "Piece.h"
#include "Pawn.h"
#include "Board.h"

namespace Chess{

    //Checks the legal moves for Pawn
    bool Pawn::legal_move_shape(const Position& start, const Position& end) const {
        //Error if it's not in the board
        if(!(start.first <= 'H' && start.first >= 'A' && end.second >= '1' && end.second <= '8')){
            return false;
        }

        //This checks if the pawn has moved yet
        if((start.second == '2') || (start.second == '7')){
            //This then checks if it's in the same column as it can only be the same column
            if(start.first == end.first){
                //Checks if it's white or black
                if(this -> is_white() == true){
                    //Checks if it is within the first two moves up
                    if((start.second + 1) == end.second){
                        return true;
                    }else if(((start.second + 2) == end.second) && (start.second == '2')){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    //Checks if it is within the first two moves down
                    if((start.second - 1) == end.second){
                        return true;
                    }else if(((start.second - 2) == end.second) && (start.second == '7')){
                        return true;
                    }else{
                        return false;
                    }
                }
            }else{
                return false;
            }
        }else{
            //This checks if the pawn has moved yet
            if(start.first == end.first){
                //This then checks if it's in the same column as it can only be the same column
                if(this -> is_white() == true){
                    //Checks if it is within the one moves up
                    if((start.second + 1) == end.second){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    //Checks if it is within the one moves down
                    if((start.second - 1) == end.second){
                        return true;
                    }else{
                        return false;
                    }
                }
            }else{
                return false;
            }
        }
        //Returns false Error Handling
        return false;
    }


    //Checks the legal capture moves for Pawn
    bool Pawn::legal_capture_shape(const Position& start, const Position& end) const {
        if(this -> is_white() == true){
            //Checks if it is within the first two moves up
            if((start.second + 1) == end.second){
                //Checks if it can take horizontally
                if((start.first-1) == end.first){
                    return true;
                }else if ((start.first+1) == end.first){
                    return true;
                }else{
                    return false;
                }
                
            }else{
                return false;
            }
            
        }else{
            //Checks if it is within the first two moves up
            if((start.second - 1) == end.second){
                //Checks if it can take horizontally
                if((start.first-1) == end.first){
                    return true;
                }else if ((start.first+1) == end.first){
                    return true;
                }else{
                    return false;
                }
                
            }else{
                return false;
            }
        }
        //Returns false Error Handling
        return false;
    }
}