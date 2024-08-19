

class Polynomial:
    """
    Class:
        Polynomial
    Purpose: 
        This contains all the functions that would allow for basic operations of polynomials
    Inheritance/Arguments:
        None
    Functions: 
        1) __init__(self, alist, inorder = True)
            This function  intializes the variables of the Polynomial class and declare them to certain variable
        2) __str__(self)
            This prints out the polynomial that was given through __init__
        3)at(self, user_input)
            This function is useed to find what the output of the polynomial is if we are given an input
        4)__eq__(self, other)
            This function checks if two polynomials are the same as each other
        5)__ne__(self, other)
            This function checks if two polynomials are different from each other
        6)__add__(self, other)
            This function adds two polynomials together
        7)__sub__(self, other)
            This function subtracts two polynomials from each other
        8)__mul__(self, other)
            This function multiplies two polynomials together 
        9)__pow__(self, other)
            This function raises the polynomial to a given power
        10)__or__(self, other)
            This function gets the composite of two polynomials
        11)derivative(self, num = 1)
            This function gets a certain derivative of a polynomial
    """
    
    def __init__(self, alist, inorder = True):
        """
        Function: 
            __init__(self, alist, inorder = True)
        Purpose: 
            This function  intializes the variables of the Polynomial class and declare them to certain variable
        Arguments: 
            self(instance of class): This allows us to recall and store variables that are given
            alist(list): Gives a list of polynomial coefficents
            inorder(boolean): Gives us a boolean that tells us if the alist is in order of greatest to smallest degree of polynomial or vice versa
        Returns:
            Nothing, but intializes and declares variables
        """
        #Defines the variables that are in the Polynomial class
        self.c_list = alist.copy()
        self.in_order = inorder
        
        #Deletes 0 from the start of the list
        #This first part of the if statement just will return [0] if all values are 0
        if(self.c_list.count(0) == len(self.c_list)):
            self.c_list = [0]
        else:
            #If there is a 0 at the beginning of the list, then it removes it until it reaches the first non-zero digit
            while(self.c_list[0] == 0):
                self.c_list.remove(0)
        
        #Sets up the coefficients lists of the Polynomial
        self.coeffs = self.c_list.copy()
        
        #Allows the user to send the coefficients in reverse order
        if(self.in_order == True):
            self.coeffs.reverse()
    
        #Sets the degree of the polynomial
        self.degree = int(len(self.coeffs)-1)
    
    def __str__(self):
        """
        Function: 
            __str__(self)
        Purpose: 
            This prints out the polynomial that was given through __init__
        Arguments: 
            self(instance of class): This allows us to recall and store variables that are given
        Returns:
            Nothing, but prints out the polynomial
        """
        #Return string
        rString = ""
        #Reverse the coefficients so it's in the correct order
        tcoeff = self.coeffs.copy()
        tcoeff.reverse()
        #Sets up a range that will allow for all possible coefficients to be assigned correctly
        for i in range(self.degree + 1):
            
            #Returns a value of just a number if there is onyl a number and degrees == 0
            if(self.degree == 0):
                return f'{tcoeff[i]}'
            
            #Adds the string of value for the last term
            if(i == self.degree):
                if(tcoeff[i] > 0):
                    rString = rString + f'+ {tcoeff[i]}'
                elif(tcoeff[i] < 0):
                    rString = rString + f'- {-1*tcoeff[i]}'
                    
            #Adds the string of value for the first term 
            elif(i == 0):
                if(tcoeff[i] > 0):
                    rString = rString + f'{tcoeff[i]}x^{(self.degree)-i} '
                elif(tcoeff[i] < 0):
                    rString = rString + f'{tcoeff[i]}x^{(self.degree)-i} '
            
            #Adds the string of value for every term except the first and last term
            else:
                if(tcoeff[i] > 0):
                    rString = rString + f'+ {tcoeff[i]}x^{(self.degree)-i} '
                elif(tcoeff[i] < 0):
                    rString = rString + f'- {-1 * tcoeff[i]}x^{(self.degree)-i} '
                    
        return rString
    
    def at(self, user_input):
        """
        Function: 
            at(self, user_input)
        Purpose: 
            This function is useed to find what the output of the polynomial is if we are given an input
        Arguments: 
            self(instance of class): This allows us to recall and store variables that are given
            user_input (int): This gives us the input of what we are testing in the polynomial
        Returns:
            Int: The output of what the polynomial is at with the input of user_input
        """
        value = 0
        #Reverse the coefficients so it's in the correct order
        tcoeff = self.coeffs.copy()
        tcoeff.reverse()
        
        #Adds each term with the input to value until we get what the output is if we input a certain number
        for i in range(self.degree + 1):
            value = value + tcoeff[i] * (user_input ** (self.degree - i))
        return value
    
    def __eq__(self, other):
        """
        Function: 
            __eq__(self, other)
        Purpose: 
            This function checks if two polynomials are the same as each other
        Arguments: 
            self (instance of class): This allows us to recall and store variables that are given
            other (polynomial): This gives us a different instance of the polynomial class 
        Returns:
            Boolean: Gives us true if they are the same polynomial and false if they are not the same polynomial
        """
        #Reverse the coefficients so it's in the correct order
        tcoeff = self.coeffs.copy()
        tcoeff.reverse()
        ocoeff = other.coeffs.copy()
        ocoeff.reverse()
        #Checks if both degree and coeff are the same, if it is then return true
        if(self.degree == other.degree and tcoeff == ocoeff):
            return True
        else:
            return False
        
    def __ne__(self, other):
        """
        Function: 
            __ne__(self, other)
        Purpose: 
            This function checks if two polynomials are the same as each other
        Arguments: 
            self (instance of class): This allows us to recall and store variables that are given
            other (polynomial): This gives us a different instance of the polynomial class 
        Returns:
            Boolean: Gives us true if they are the different polynomial and false if they are not the different polynomial
        """
        print(self, other)
        #Reverse the coefficients so it's in the correct order
        tcoeff = self.coeffs.copy()
        tcoeff.reverse()
        ocoeff = other.coeffs.copy()
        ocoeff.reverse()
        #Checks if both degree and coeff aren't the same, if it isn't then return true
        if(self == other):
            return False
        else:
            return True
        
    def __add__(self, other):
        """
        Function: 
            __add__(self, other)
        Purpose: 
            This function adds two polynomials together        
        Arguments: 
            self (instance of class): This allows us to recall and store variables that are given
            other (polynomial/int): This gives us a different instance of the polynomial class or an integer to add
        Returns:
            Polynomial: The sum of the two polynomials
        """
        #Reverse the coefficients so it's in the correct order
        tcoeff = self.coeffs.copy()
        tcoeff.reverse()
        
        new_list = tcoeff.copy()
        #Chekcs if it is a polynomial and if it is then adds the coeff together
        if isinstance(other, Polynomial):
            ocoeff = other.coeffs.copy()
            ocoeff.reverse()
            #This allows a change of memory space, so it doesn't change the original 
            test_list = ocoeff.copy()
            #Makes it so that the coeff are the same length
            while(len(test_list) != len(new_list)):
                if(len(test_list) > len(new_list)):
                    new_list.insert(0, 0)
                elif(len(test_list) < len(new_list)):
                    test_list.insert(0, 0)
                    
            for i, v in enumerate(test_list):
                new_list[i] = new_list[i] + test_list[i]
        #Chekcs if it is a int and if it is then adds the 0th degree together
        elif isinstance(other, int):
            new_list[len(new_list)-1] = new_list[len(new_list)-1] + other
        
        #Returns a new Polynomail
        return Polynomial(new_list)
    
    def __sub__(self, other):
        """
        Function: 
            __sub__(self, other)
        Purpose: 
            This function subtracts two polynomials together        
        Arguments: 
            self (instance of class): This allows us to recall and store variables that are given
            other (polynomial/int): This gives us a different instance of the polynomial class or an integer to subtract
        Returns:
            Polynomial: The difference of the two polynomials
        """
        
        #Reverse the coefficients so it's in the correct order
      
        new_list = self.coeffs.copy()
        new_list.reverse()
       
        #Chekcs if it is a polynomial and if it is then adds the coeff together
        if isinstance(other, Polynomial):
            ocoeff = other.coeffs.copy()
            ocoeff.reverse()
            #This allows a change of memory space, so it doesn't change the original 
            test_list = ocoeff.copy()
            #Makes it so that the coeff are the same length
            while(len(test_list) != len(new_list)):
                if(len(test_list) > len(new_list)):
                    new_list.insert(0, 0)
                elif(len(test_list) < len(new_list)):
                    test_list.insert(0, 0)
            print(test_list, new_list)        
            for i, v in enumerate(test_list):
                new_list[i] = new_list[i] - test_list[i]
        #Chekcs if it is a int and if it is then adds the 0th degree together
        elif isinstance(other, int):
            new_list[len(new_list)-1] = new_list[len(new_list)-1] - other
        
        #Returns a new Polynomail
        return Polynomial(new_list)
    
    def __mul__(self, other):
        """
        Function: 
            __mul__(self, other)
        Purpose: 
            This function multiplies two polynomials together        
        Arguments: 
            self (instance of class): This allows us to recall and store variables that are given
            other (polynomial/int): This gives us a different instance of the polynomial class or an integer to multiply
        Returns:
            Polynomial: The product of the two polynomials
        """
        
        new_dict = {}
        new_list = []
        
        #Reverse the coefficients so it's in the correct order
        tcoeff = self.coeffs.copy()
        tcoeff.reverse()
            
        #Do this if it is in Polynomial
        if isinstance(other, Polynomial):
            
            #Reverse the coefficients so it's in the correct order
            ocoeff = other.coeffs.copy()
            ocoeff.reverse()
            #Defines the bounds of the dictionary
            for i in range(len(tcoeff)+len(ocoeff)-1):
                new_dict[i] = 0
            
            #Adds the values to certain degrees within the dictionary
            for i, v in enumerate(tcoeff):
                for j, v1 in enumerate(ocoeff):
                    new_dict[i+j] = new_dict[i+j] + v*v1
            for v in new_dict.values():
                new_list.append(v)
        
        #Do this if it is an integer
        elif isinstance(other, int):
            
            #Multiply each coeff by the integer
            new_list = tcoeff.copy()
            for i in range(len(new_list)):
                new_list[i] = new_list[i] * other
       
        return Polynomial(new_list)
    
    def __pow__(self, other):
        """
        Function: 
            __pow__(self, other)
        Purpose: 
            This function raises the polynomial to a given power
        Arguments: 
            self (instance of class): This allows us to recall and store variables that are given
            other (int): This gives us the power we need to raise to
        Returns:
            Polynomial: A certain power of the polynomial 
        """
        
        #def a new polynomial number
        new_Polynomial = self
        
        #Makes sure that we are given an int
        if isinstance(other, int):
            #Returns a Polynomial with coeff set to 1
            if(other == 0):
                return Polynomial([1])
            else:
                #This gets it set to a certain power
                for i in range(other-1):
                    new_Polynomial = new_Polynomial * self
            return new_Polynomial
    
    
    def __or__(self, other):
        """
        Function: 
            __or__(self, other)
        Purpose: 
            This function gets the composite of two polynomials
        Arguments: 
            self (instance of class): This allows us to recall and store variables that are given
            other (Polynomial): This gives us a different instance of the polynomial class 
        Returns:
            Polynomial: The composite of the two polynomials
        """
        #Get a blank Polynomial
        set_Polynomial = Polynomial([0])
        
        #Reverse the coefficients so it's in the correct order
        tcoeff = self.coeffs.copy()
        tcoeff.reverse()
        #Checks if it is a polynomial
        if isinstance(other, Polynomial):
            #Checks for index and polynomial and runs through each degree of polynomial one at a time
            for i, v in enumerate(tcoeff):
                #Use this equation to get set new values of Polynomial that will return the composite
                set_Polynomial = (other ** (len(tcoeff)-i-1)) * v + set_Polynomial

        return set_Polynomial
    
    
    def derivative(self, num = 1):
        """
        Function: 
            derivative(self, num = 1)
        Purpose: 
            This function gets a certain derivative of a polynomial
        Arguments: 
            self (instance of class): This allows us to recall and store variables that are given
            num (Int): This gives us to what derivative should we measure
        Returns:
            Polynomial: The certain degree of derivative
        """
        self.num =  num
        #Sets this so it doesn't affect original data
        new_Polynomial = self.coeffs.copy()
        new_Polynomial.reverse()
        #This counts the amount of derivatives
        for i in range(self.num):
            
            #Checks if the coeff only contains 0
            if new_Polynomial[0] == 0 and len(new_Polynomial) == 1:
                return Polynomial([0])
            else:
                #This takes the actual derivative by just multiplying the coeff by the degree it is currently at
                for i,v in enumerate(new_Polynomial):
                    new_Polynomial[i] *= (len(new_Polynomial) - i - 1)
                
                #This then removes the last coeff as it has a degree of 0
                if(len(new_Polynomial) > 1):
                    new_Polynomial.pop()
                else:
                    return Polynomial([0])
            
        return Polynomial(new_Polynomial)
    
        
    pass

if __name__ == '__main__':
    """
    p = Polynomial([6,1,2])
    print('Coefficients: ',p.coeffs)
    print(type(p.coeffs))
    print('Degree: ',p.degree)
    print(type(p.degree))
    """
    
    """
    p = Polynomial([0, 0, 6,1,2])
    q = Polynomial([0, 0])

    print(p.coeffs)
    print(q.coeffs)
    """
    """
    p = Polynomial([6,1,2])
    q = Polynomial([-7,1,-2])

    print(p.coeffs)
    print('Polynomial p: ',p)
    print('Polynomial q: ',q)
    """
    """
    p = Polynomial([6,0,2])
    print('Polynomial f(x): ', p)
    print('f(x) at x = 2: ',p.at(2))
    """
    
    """
    p = Polynomial([6,0,2])
    q = Polynomial([0, 6, 0 , 2])
    r = Polynomial([2])

    print('Polynomial p: ',p)
    print('Polynomial q: ',q)
    print('Polynomial r: ',r)
    print('p == p :',p == p)
    print('p == q :',p == q)
    print('p != r :',p != r)
    print('p != q :',p != q)
  
    """
    """
    p = Polynomial([4, 6,0,2])
    q = Polynomial([7,1,2])
    r = p + q
    t = p + 5
    print('Polynomial p: ',p)
    print('Polynomial q: ',q)
    print('p + q: ',r)
    print(type(r))
    print('p + 5: ', t)
    print(type(t))
    """
    
    """
    p = Polynomial([6,0,2])
    q = Polynomial([7,1,2])
    z = Polynomial([6,0,2])

    r = p - q
    t = q - z

    print('Polynomial p: ',p)
    print('Polynomial q: ',q)
    print('Polynomial z: ',z)

    print('p - q: ',r)
    print(type(r))

    print('q - z: ',t)
    print(type(t))
    print('p - z: ',p - z)
    print('p - 5: ',p - 5)
    """
    
    """
    p = Polynomial([6,0,2])
    q = Polynomial([7,1,2])
    r = p * q

    print('Polynomial p: ',p)
    print('Polynomial q: ',q)

    print('p * q: ',r)
    print(type(r))

    print('p * 5: ',p * 5)
    """
    
    """
    p = Polynomial([6,2])
    x = p ** 0
    y = p ** 1
    z = p ** 2

    print('Polynomial p: ',p)
    print('Results')
    print('Case 1 p^0: ',x)
    print('Case 2 p^1: ',y)
    print('Case 3 p^2: ',z)
    print(type(x))

    """
    """
    f = Polynomial([3, 2, 7])
    g = Polynomial([1, 1])

    a = Polynomial([1, 2, 7])
    b = Polynomial([1, 0])

    print('Composition: ', f|g)
    print('Composition: ', a|b)
    """
    """
    p = Polynomial([1,2,0])
    print('Polynomial p: ',p)
    print('First derivative: ', p.derivative(1))
    print('Second derivative: ', p.derivative(2))
    print('Third derivative: ', p.derivative(3))
    print('Fourth derivative: ', p.derivative(4))

    print(type(p.derivative(4)))

    """
    
    
    
    
    
    
    