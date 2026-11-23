#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep  7 23:06:07 2025

@author: kkim23
"""
import math

"""
Description:
    Multiples two quaternions
    
Parameters:
    firstQuat - First quat get multiplied
    secondQuat - Second quat get multiplied
    resultQuat - Stores the value

Returns:
    True if it works or false if it doesn't
"""
def quaternionsMultiplications(firstQuat, secondQuat, resultQuat):
    # Makes sure the quats are valid
    if (len(firstQuat) != 4 or len(secondQuat) != 4):
        return False
    
    # Gets generic variables
    first_a = firstQuat[0]
    first_b = firstQuat[1]
    first_c = firstQuat[2]
    first_d = firstQuat[3]
    
    second_a = secondQuat[0]
    second_b = secondQuat[1]
    second_c = secondQuat[2]
    second_d = secondQuat[3]
    
    # Sets the values
    resultQuat[0] = first_a * second_a - first_b * second_b - first_c * second_c - first_d * second_d
    resultQuat[1] = first_a * second_b + first_b * second_a + first_c * second_d - first_d * second_c
    resultQuat[2] = first_a * second_c - first_b * second_d + first_c * second_a + first_d * second_b
    resultQuat[3] = first_a * second_d + first_b * second_c - first_c * second_b + first_d * second_a
    
    # Returns true if the 
    return True

"""
Description:
    Turns the string into a vector
    
Parameters:
    axis_angle_str - String of axis_angle from user
    resultList - Stores the value

Returns:
    True if it works or false if it doesn't
"""
def vectorsAngle(vector_str, resultList):
    # Checks that it's in valid format
    if (vector_str[0] != '[' or vector_str[-1] != ']'):
        return False
    
    # Removes brackets
    vector_str = vector_str[1:]
    vector_str = vector_str[:-1]
    
    # Removes spaces
    vector_str = vector_str.replace(" ", "")
    
    # Splits it into a list
    resultList.clear()
    resultList.extend([float(x) for x in vector_str.split(',')])
    return True

"""
Description:
    Turns an angle axis to a quaternions
    
Parameters:
    aav - Axis-angle vector
    quat_angle_vector - Stores the value
    
Return
    True if work else else if it doesn't

"""
def angle_axis_to_quat(aav, quat_angle_vector):
    # Gets Theta and |e|
    theata = aav[0]
    normE = math.sqrt(aav[1]**2 + aav[2]**2 + aav[3]**2)
    if(normE == 0):
        return False
    
    # Gets the quat_angle vector
    quat_angle_vector.append(math.cos(theata/2))
    quat_angle_vector.append(aav[1]/normE * math.sin(theata/2))
    quat_angle_vector.append(aav[2]/normE * math.sin(theata/2))
    quat_angle_vector.append(aav[3]/normE * math.sin(theata/2))
    
    return True

"""
Description:
    Rotates the quaternions around a point
"""
def quaternionsRotations():
    # Gets inputs
    '''
    Example:
    axis_angle_input = "[4.71238898038, 1, 2, 4]"
    point_input = "[6, -3, 4]"
    '''
    axis_angle_input = input("Give the axis-angle vector in '[θ, x, y, z]' (only decimal): \n")
    point_input = input("Give a point '[x, y, z]' (only decimal): \n")
    
    # Gets and checks the vectors
    axis_angle = []
    point = []
    if (not (vectorsAngle(axis_angle_input, axis_angle) and vectorsAngle(point_input, point))):
        print ("Inputs weren't valid")
        return False
    
    #Changes angles axis to quaternions
    myQuaternions = []
    if(not angle_axis_to_quat(axis_angle, myQuaternions)):
        print ("Norm is 0, therefore inpossible")
        return False
    
    #Sets up for the quaternions multiplication
    pointQ = [0] + point
    myQuaternionsPrime = [myQuaternions[0], -myQuaternions[1], -myQuaternions[2], -myQuaternions[3]]
    
    #Multiplies it out
    intermidiateQuat = [0] * 4
    rotPoint = [0] * 4
    quaternionsMultiplications(myQuaternions, pointQ, intermidiateQuat)
    quaternionsMultiplications(intermidiateQuat, myQuaternionsPrime, rotPoint)
    
    #Prints Results
    eps = 1e-5  # tolerance

    def clean(val):
        return 0.0 if abs(val) < eps else val
    
    x = clean(rotPoint[1])
    y = clean(rotPoint[2])
    z = clean(rotPoint[3])
    
    print(f"x: {x:.3f}, y: {y:.3f}, z: {z:.3f}")
    
if __name__ == "__main__":
    quaternionsRotations()    