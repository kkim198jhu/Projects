//Includes all libraries
#include "Tree.h"
#include <iostream>
#include <sstream>
#include <ostream>
#include <string>
#include <fstream>
#include <vector>
#include <map>
#include <ctype.h>
#include <algorithm>
#include <utility> 

//This is for readability for code
using std::cout;
using std::cin;
using std::string;
using std::ifstream;
using std::cerr; 
using std::endl;
using std::stoi;
using std::reverse;
using std::to_string;
using std::ostream;


//This is the constructor for this class
template<typename T>
Tree<T>::Tree(T d){
    //This sets the data
    this -> data = d;

    //Sets all the Nodes/links to NULL unless specified
    this -> sibs = NULL;
    this -> kids = NULL;
    this -> prev = NULL;
}

//This is the destuctor of this class which frees up all the data
template<typename T>
Tree<T>::~Tree(){

    //This is a destructor for all children
    delete this -> kids;

    //This is a destructor for all siblings
    delete this -> sibs;

}

//This function adds a child to the tree, and takes in one parameter(Template)
//Returns true if it works and false if it doesn't
template<typename T>
bool Tree<T>::addChild(T d){
    //If there is no child node, add it as a child node
    if(kids == NULL){
        Tree<T> * new_child = new Tree(d);
        this -> kids = new_child;
        new_child -> prev = this;
        return true;
    //Adds the child, but sorts it alphabetically with other siblings
    }else{
        bool working = this -> kids -> addSibling(d);
        return working;
    }
}

//This function adds a child to the tree, and takes in one parameter(node)
//Returns true if it works and false if it doesn't
template<typename T>
bool Tree<T>::addChild(Tree<T> * ExistingTree){

    //This is error handling which checks if existing tree has and siblings or previous heads
    if(ExistingTree -> sibs != NULL){
        return false;
    }else if(ExistingTree -> prev != NULL){
        return false;
    }

    //If there is no child node, add it as a child node
    if(kids == NULL){
        this -> kids = ExistingTree;
        ExistingTree -> prev = this;
        return true;
    //This is adding new child branch to tree
    }else if (ExistingTree -> sibs != NULL){
        ExistingTree -> kids = this -> kids;
        ExistingTree -> prev = this;
        this -> kids -> prev = ExistingTree;
        this -> kids = ExistingTree;
        return true;
    //Adds it to siblings
    }else{
        bool working = this -> kids -> addSibling(ExistingTree);
        return working;
    }
}

//This is a helper function that takes in a template and turns it into a string
template<typename T>
string Tree<T>::turnToString(const T& value){
    std::stringstream ss;
    ss << value;
    return ss.str();
}

//Makes it easier to get the toString using recursion: has 2 parameters: Tree Pointer and String
template<typename T>
string Tree<T>::DFtraverse(Tree<T> * tracker, string new_string) {
    string short_string = turnToString(tracker -> data);
    //This happens to check with siblings and when there are nothing after child
    if(tracker -> kids == NULL){
        new_string = new_string + short_string + "\n";
        if(tracker -> sibs == NULL){
            //No sibling found, so return normal string AKA our base case
            return new_string;
        }else{
            //Sibling found, so in the process of adding the branch of sibling
            return DFtraverse(tracker -> sibs, new_string);
        }
    }

    //This checks if there is another child node beneath
    if(tracker -> kids != NULL){
        //Checks if there is a sibling
        if(tracker -> sibs == NULL){
            //No sibling found, so just add child data
            new_string = new_string + short_string + "\n";
            return DFtraverse(tracker -> kids, new_string);
        }else{
            //Sibling found, so add both the child data and the branch of sibling
            new_string = new_string + short_string + "\n";
            return DFtraverse(tracker -> kids, new_string) + DFtraverse(tracker -> sibs, "");
        }
        
    }

    //Error handling so there are no warnings.
    return new_string;
}

// Overloads the operator + by taking in a Tree referance and returns the tree
template<typename T>
Tree<T>& Tree<T>::operator+(Tree& rt){
    //assings the Tree referance into a pointer
    Tree<T> *memory = &rt;
    this -> addChild(memory);
    // Assigns the pointer into a referance to return
    Tree& returnValue = * this;
    return returnValue;
}

// Overloads the operator << by taking in a Tree referance and returns the ostrema referance
template<typename T>
ostream& operator<< (ostream& os, Tree<T>& rt) {
    //Assigns the Tree referance into a pointer
    Tree<T> *memory = &rt;

    //Assigns it to the new operator and returns
    os << memory -> toString();
    return os;
}

// Overloads the operator == by taking in a Tree referance and returns if it is true or false
template<typename T>
bool Tree<T>::operator==(const Tree<T> &root){
    //Intialize and declare certain variables
    const Tree<T> *memory = &root;
    Tree<T> *tracker = this;

    //This runs through all the child nodes
    while(tracker != NULL){
        //Keeps the memory/sibling fresh
        const Tree<T>* memory_sibling = memory;
        Tree<T>* tracker_sibling = tracker;

        //Returns false if data isn't matching
        if(tracker -> data != memory -> data){
            return false;
        }

        //Checks out the siblings after the child
        while(tracker_sibling -> sibs != NULL){
            //Goes to the next values
            memory_sibling = memory_sibling -> sibs;
            tracker_sibling = tracker_sibling -> sibs;

            //Returns false if data isn't matching
            if(memory_sibling -> data != tracker_sibling -> data){
                return false;
            }

            //Use recursion if there is a child
            if(tracker_sibling->kids != NULL){
                if(!(tracker_sibling->kids == memory_sibling->kids)){
                    return false;
                }
            }
        }
        tracker = tracker -> kids;
        memory = memory -> kids;
    }
    
    return true;
}


// all template possibility, separated by newlines, including at the end
template<typename T>
string Tree<T>::toString(){
    string my_string = DFtraverse(this, "");
    return my_string;
}

//This function adds a sibling to the tree, and takes in one parameter(Template)
//Returns true if it works and false if it doesn't
template<typename T>
bool Tree<T>::addSibling(T d){
    //Error handling with not adding to the root;
    if(this -> prev == NULL){
        return false;
    }

    //This declares and intialize the pointer that we will be using.
    Tree<T> * new_sibling = new Tree(d);
    
    //This checks the first case that if the orginal node doesn't have any siblings
    if(this -> sibs == NULL){
        //Error if they have the same data
        if(data == d){
            delete new_sibling;
            return false;
        //Place after if it comes after alphabetically
        }else if(this -> data < d){
            this -> sibs = new_sibling;
            new_sibling -> prev = this;
            return true;
        //Place before if it comes before alphabetically
        }else if(!(this -> data < d || this -> data == d)){
            this -> prev -> kids = new_sibling;
            new_sibling -> prev =  this -> prev;
            new_sibling -> sibs = this;
            this -> prev = new_sibling;
            return true;
        }
    }else{

        //Declares and intialize the pointers that we will be using to go through the list. 
        Tree<T> * prev_tracker = this;
        Tree<T> * tracker = this -> sibs;
        //Error if they have the same data
        if(prev_tracker -> data == d){
            delete new_sibling;
            return false;
        }

        //Error if they have the same data
        if(tracker -> data == d){
            delete new_sibling;
            return false;
        }

        //Place before if it comes before alphabetically
        if(!(prev_tracker -> data < d || prev_tracker -> data == d)){
            this -> prev -> kids = new_sibling;
            new_sibling -> prev =  this -> prev;
            new_sibling -> sibs = this;
            this -> prev = new_sibling;
            return true;
        }


        while(tracker != NULL){

            //Error if they have the same data
            if(tracker -> data == d){
                delete new_sibling;
                return false;
            }

            //Adds the node to our tree
            if(!(tracker -> data < d || tracker -> data == d) && prev_tracker -> data < d){
                prev_tracker -> sibs = new_sibling;
                new_sibling -> prev = tracker -> prev;
                new_sibling -> sibs = tracker;
                tracker -> prev = new_sibling;
                return true;
            }
            //Increases the tracker, so it can go to the next spot within our list
            prev_tracker = tracker;
            tracker = tracker -> sibs;
        }

        //Adds to the last position
        prev_tracker -> sibs = new_sibling;
        new_sibling -> prev = prev_tracker;
        return true;
        
    }
    //Error Handling/Removes warnings

    delete new_sibling;
    return false;
}

//This function adds a child to the tree, and takes in one parameter(node)
//Returns true if it works and false if it doesn't
template<typename T>
bool Tree<T>::addSibling(Tree<T> * new_sibling){
    //Error handling with not adding to the root;
    if(this -> prev == NULL){
        return false;
    }

    //Error handling: checks if root node is invalid (has siblings or prev)
    if(new_sibling -> prev != NULL){
        return false;
    }else if(new_sibling -> sibs != NULL){
        return false;
    }

    // Intialize a variable for data for readibility
    T d = new_sibling -> data;

    
    //This checks the first case that if the orginal node doesn't have any siblings
    if(this -> sibs == NULL){
        //Error if they have the same data
        if(data == d){
            return false;
        //Place after if it comes after alphabetically
        }else if(this -> data < d){
            this -> sibs = new_sibling;
            new_sibling -> prev = this;
            return true;
        //Place before if it comes before alphabetically
        }else if(!(this -> data < d || this -> data == d)){
            this -> prev -> kids = new_sibling;
            new_sibling -> prev =  this -> prev;
            new_sibling -> sibs = this;
            this -> prev = new_sibling;
            return true;
        }
    }else{

        //Declares and intialize the pointers that we will be using to go through the list. 
        Tree<T> * tracker = this -> sibs;
        Tree<T> * prev_tracker = this;

        //Error if they have the same data
        if(prev_tracker -> data == d){
            return false;
        }

        //Error if they have the same data
        if(tracker -> data == d){
            return false;
        }

        //Place before if it comes before alphabetically
        if(!(prev_tracker -> data < d || prev_tracker -> data == d)){
            this -> prev -> kids = new_sibling;
            new_sibling -> prev =  this -> prev;
            new_sibling -> sibs = this;
            this -> prev = new_sibling;
            return true;
        }

        while(tracker != NULL){

            //Error if they have the same data
            if(tracker -> data == d){
                return false;
            }

            //Adds the node to our tree
            if(!(tracker -> data < d || tracker -> data == d) && prev_tracker -> data < d){
                prev_tracker -> sibs = new_sibling;
                new_sibling -> prev = tracker -> prev;
                new_sibling -> sibs = tracker;
                tracker -> prev = new_sibling;
                return true;
            }
            //Increases the tracker, so it can go to the next spot within our list
            prev_tracker = tracker;
            tracker = tracker -> sibs;
        }

        //Adds to the last position
        prev_tracker -> sibs = new_sibling;
        new_sibling -> prev = prev_tracker;
        return true;
        
    }
    //Error Handling/Removes warnings
    return false;
}


