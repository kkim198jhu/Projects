#ifndef _TREE_H
#define _TREE_H

#include <cstdlib>
#include <string>


template<typename T>

// tree of characters, can be used to implement a trie
class Tree {
  friend class TTreeTest;
  
  T data;     // the value stored in the tree node

  Tree<T> * kids;  // children - pointer to first child of list, maintain order & uniqueness

  Tree<T> * sibs;  // siblings - pointer to rest of children list, maintain order & uniqueness
                 // this should always be null if the object is the root of a tree

  Tree<T> * prev;  // pointer to parent if this is a first child, or left sibling otherwise
                 // this should always be null if the object is the root of a tree

 public:
  template<typename F>
  friend std::ostream& operator<<(std::ostream& os, Tree<F>& rt);
  Tree(T d);

  ~Tree();  // clear siblings to right and children and this node
  
  Tree& operator+(Tree& rt);  //^ operator to do the same thing as addChild
  bool operator==(const Tree &root); // return true if two Trees match node by node

  // siblings and children must be unique, return true if added, false otherwise
  bool addChild(T d);

  // add tree root for better building, root should have null prev and sibs 
  // returns false on any type of failure, including invalid root
  bool addChild(Tree *ExistingTree);

  std::string toString(); // all characters, separated by newlines, including at the end
  std::string turnToString(const T& value); //This is a helper function that takes in a template and turns it into a string

  std::string DFtraverse(Tree * tracker, std::string new_string);//Makes it easier to get the toString

 private:
  // these should only be called from addChild, and have the same restrictions
  // the root of a tree should never have any siblings
  // returns false on any type of failure, including invalid root
  bool addSibling(T d);
  bool addSibling(Tree *new_sibling);

  bool treeCompare(const Tree *a, const Tree *b) const;

};

//Necessary to include at end
#include "Tree.inc"
#endif
