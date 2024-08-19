#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Sep 11 11:24:11 2023

@author: Paola Garcia (lgarci27@jhu.edu)

Gateway Python Project - Sentiment Analysis

"""

import extras


def main():
    """
    Function:
        main()
    Purpose: 
        This runs the code, which allows us to test out other part functions as well
    Arguments:
        None
    Returns: 
        printed text and just runs the main code
    """
    """
    sentiment_ analysis main
    """
    
    # Define a list of positive keywords
    positive_keywords = ["good", "excellent", "fantastic", "wonderful", "amazing", "great", "love", "comfortable", "friendly", "excellent", "nice", "clean", "helpful", "pleasant", "thanks", "amazing", "upgrade"]
    
    # Define a list of negative keywords
    negative_keywords = ["bad", "terrible", "horrible", "awful", "sad", "late", "poor", "return", "warning", "hard,", "cramped", "difficult", "problem", "sin", "never", "regret", "worst"]
    
    # Define unwanted characters
    unwanted_characters = '!"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~'
    
    # Define a list of stop words (customized for airlines)
    stop_words = ["fly", "united","ua","i", "im", "me", "my", "myself", "we", "our", "ours", "why", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "so", "just", "no", "not", "would", "will", "get", "ever", "now", "can", "how"]

    reviews = extras.reading_csv_tweet('final_United_Tweets.csv')
    
    # Analyze sentiments
    sentiment_scores=[]
    for review in reviews:
        sentiment_scores.append(analyze_sentiment(review, positive_keywords, negative_keywords, unwanted_characters, stop_words))
    # Extract aspects
    positive_aspects, negative_aspects, neutral_aspects = extract_aspects(reviews, positive_keywords, negative_keywords, unwanted_characters, stop_words)
    # Visualize the sentiment distribution (text-based output)
    sentiment_distribution(sentiment_scores)
    # Display the top positive and negative aspects
    extract_rank_aspect("positive", positive_aspects, 5)
    extract_rank_aspect("negative", negative_aspects, 5)
    extract_rank_aspect("neutral", neutral_aspects, 5)         


####################
## Preprocess
####################


def lower_split(text):
    """
    Function: 
        lower_split(text)
    Purpose: 
        Splits the text into a lowercase list 
    Arguments: 
        text (str): This gives us the text that we will split and make sure that everything is lowercase
    Returns:
        List: A list that contains only lowercase words
    """
    #get the message in lower case
    message = text.lower()
    
    #return it split up
    return message.split(" ")


def unwanted_characters_filter(text, unwanted_characters):
    """
    Function: 
        unwanted_characters_filter(text, unwanted_characters)
    Purpose: 
        This filter out unwanted characters from the text
    Arguments:
        Text (str): The string that we are checking for unwanted characters
        Unwanted_characters (str): A string of the unwanted characters
    Returns:
        String: The text without any unwanted characters
    """
    #keeps the count if there is any unwanted characters
    count = 0
    message = ""
    
    #checks and replace any unwanted characters
    for i in text:
        for j in unwanted_characters:
            #only run this if statement if there is an unwanted character
            if(i == j):
                count += 1
        
        #add this to message depending if it an unwanted character or not
        if(count == 1):
            message = message
            count = 0
        else:
            message = message + i
    #removes unnecessary spaces at the beginning
    while(message.startswith(" ") == True):
        message = message [1:]
    
    return message

                

def unwanted_numbers(text):
    """
    Function: 
        unwanted_numbers(text)
    Purpose: 
        This filter out unwanted numbers from the text
    Arguments:
        Text (str): The string that we are checking for numbers
    Returns:
        The text without any numbers characters
    """
    #keeps the count if there is any unwanted numbers
    count = 0
    message = ""
    unwanted_number = "0123456789"
    
    #checks and replace any unwanted numbers
    for i in text:
        for j in unwanted_number:
            #only run this if statement if there is an unwanted numbers
            if(i == j):
                count += 1
        
        #add this to message depending if it an unwanted numbers or not
        if(count == 1):
            count = 0
        else:
            message = message + i

    return message
               

def unwanted_words(text,stop_words):
    """
    Function: 
        unwanted_words(text,stop_words)
    Purpose: 
        This filter out unwanted words from the text
    Arguments:
        Text (str): The string that we are checking for unwanted words
        Stop_words (list): A list of the unwanted words
    Returns:
        String: The text without any unwanted words
    """
    #keeps the count if there is any stop words
    count = 0
    message = ""
    new_text = lower_split(text)
    #checks and replace any stop words
    for i in new_text:
        for j in stop_words:
            #only run this if statement if there is a stop words
            if(i == j):
                count += 1
        
        #add this to message depending if it an stop words or not
        if(count == 1):
            count = 0
        else:
            message = message + " " + i
    
    #remove the mistake of the first place
    message = message[1:]
    return message



#returns concatentes for clean words
def concatenating_clean_words(tokens, unwanted_characters):
    """
    Function: 
        concatenating_clean_words(tokens, unwanted_characters)
    Purpose: 
        This filter out unwanted characters from a list
    Arguments:
        Token (list): The list that we are checking for unwanted words
        Unwanted_characters (str): A string of the unwanted characters
    Returns:
        String: The token without any unwanted words and formed into a string instead of a list
    """
    
    message = ""
    #changes list into string
    for i in tokens:
        message = message + " " + i 
    message = message[1:]
    #calls back unwanted characters to gain new 
    new_string = unwanted_characters_filter(message, unwanted_characters) 
    return new_string


#Starts to clean up all the data received by user
def preprocess_text(text, unwanted_characters, stop_words):   
    """
    Function: 
        preprocess_text(text, unwanted_characters, stop_words)
    Purpose: 
        This filter out unwanted characters, numbers, and stop_words from a string
    Arguments:
        Text (str): The string that we are checking for unwanted characters, numbers, and stop_words
        Unwanted_characters (str): A string of the unwanted characters
        Stop_words (list): A list of the unwanted words
    Returns:
        String: The string without any unwanted characters, numbers, and stop_words
    """
    
    #turns the text to lowercase
    message = text.lower()
    #removes unwanted_characters
    message = unwanted_characters_filter(message, unwanted_characters)
    #removes unwanted_woords
    message = unwanted_words(message, stop_words)
    #Removes all numbers
    message = unwanted_numbers(message)
    
    #removes unnecessary spaces at the ending and beginning
    while(message.endswith(" ") == True):
        message = message [:-1]
    return message
    


####################
##  Analyze sentiment
####################

#find if keywords are in the list of words
def word_search(keywords, words):
    """
    Function: 
        word_search(keywords, words)
    Purpose: 
        Counts the amount of keywords are in the list of words
    Arguments:
        Keywords (list): A predefined list of words that will be used to check if there is a word that matches an object in the keyword list
        Words (list): A list of random words
    Returns:
        Int: Number of keywords are in the list of words
    """
    
    #set the variable for count
    count = 0
    
    #uses for loops to increase count and find the amount of times
    for i in words:
        if i in keywords:
            count += 1
        
    return count

#analyze the sentiment of each line
def analyze_sentiment(text, positive_keywords, negative_keywords, unwanted_characters, stop_words):
    """
    Function: 
        analyze_sentiment(text, positive_keywords, negative_keywords, unwanted_characters, stop_words)
    Purpose: 
        Tells us if there is a positive reaction or a negative reaction
    Arguments:
        Text (str): The string that we are checking to see if it is postive or negative
        positive_keywords (list): A list of positive words
        negative_keywords (list): A list of negative words
        Unwanted_characters (str): A string of the unwanted characters
        Stop_words (list): A list of the unwanted words
    Returns:
        Int: Judges how positive (greater than 0), negative (less than 0), or neutral (0) the string is
    """
    
    #changes the list so it only have the values we need
    message = preprocess_text(text, unwanted_characters, stop_words)
    
    #changes message into a list
    new_list = message.split(" ")
    
    #Gets the positive and negative counts
    neg_count = word_search(negative_keywords, new_list)
    pos_count = word_search(positive_keywords, new_list)
    
    #Partial Score
    pos_partial_score = 2 * (pos_count)
    neg_partial_score = 2 * (neg_count)
    
    #Sentimental score equation
    sentimental_score = pos_partial_score - neg_partial_score
    return sentimental_score


####################
##  Extract aspects
####################

def extract_aspects(texts, positive_keywords, negative_keywords, unwanted_characters,stop_words):
    """
    Function: 
        extract_aspects(texts, positive_keywords, negative_keywords, unwanted_characters, stop_words)
    Purpose: 
         Creates new lists that contain words that are seen in either a positive, negative, or neutral context depending on the keywords within the same text
    Arguments:
        Text (str): The string that we are checking to see if it is postive or negative
        positive_keywords (list): A list of positive words
        negative_keywords (list): A list of negative words
        Unwanted_characters (str): A string of the unwanted characters
        Stop_words (list): A list of the unwanted words
    Returns:
        sorted_positive_aspects(list): A list of words that were found in a positive light that wasn't aread in positive keywords
        sorted_negative_aspects(list): A list of words that were found in a negative light that wasn't aread in negative keywords
        sorted_neutral_aspects(list): A list of words that were found in a neutral light that wasn't aread in positive or negative keywords
    """
    
    # Set the variables here
    positive_aspects = []
    negative_aspects = []
    neutral_aspects = []
    #This will test if we add a new word or add to existing words
    pos_test = False
    neg_test = False
    neu_test = False
    
    #Gets certain texts in all of the text
    for text in texts:
        
        #Gain the sentimental score, so we know if it is good, bad, or neutral
        sentiment_score = analyze_sentiment(text, positive_keywords, negative_keywords, unwanted_characters, stop_words)
        
        #Gets the list of words
        words = preprocess_text(text, unwanted_characters, stop_words).split()
        #gets the individual words
        for word in words:
            
            #this if statement runs if it positive
            if sentiment_score > 0:
                #checks if it is actually in a predefined list
                if (word not in positive_aspects) and (word not in positive_keywords):
                    #This either adds the word to a new part of the list or add it to the count
                    for i in positive_aspects:
                        if i[0] == word and pos_test == False:
                            i[1] += 1
                            pos_test = True
                    #Adds word
                    if(pos_test == False):
                        positive_aspects.append([word, 1])
                    #Add it to the count
                    else:
                        pos_test = False
            #this if statement runs if it negative
            elif sentiment_score < 0:
                #checks if it is actually in a predefined list
                if (word not in negative_aspects) and (word not in negative_keywords):
                    #This either adds the word to a new part of the list or add it to the count
                    for i in negative_aspects:
                        if i[0] == word and neg_test == False:
                            i[1] += 1
                            neg_test=True
                    #Adds word
                    if(neg_test == False):
                        negative_aspects.append([word, 1])
                    #Add it to the count
                    else:
                        neg_test = False    
                        
            #this if statement runs if it neutral
            elif sentiment_score == 0:
                #checks if it is actually in a predefined list
                if (word not in neutral_aspects) and (word not in negative_keywords) and (word not in positive_keywords):
                    #This either adds the word to a new part of the list or add it to the count
                    for i in neutral_aspects:
                        if i[0] == word and neu_test == False:
                            i[1] += 1
                            neu_test=True
                    #Adds word
                    if(neu_test == False):
                        neutral_aspects.append([word, 1])
                    #Just adds it to the count
                    else:
                        neu_test = False
    
    #This sorts out the postive words
    sorted_positive_aspects = []
    count = 0
    index = 0
    test = 0
    for i in positive_aspects:
        for j in sorted_positive_aspects:
            count += 1
            #This compares frequency
            if(i[1] > j[1]):
                if(test == 0):
                    index = count
                    test += 1
            
        #This adds the word to the correct position
        if(test == 0):
            sorted_positive_aspects.append(i)
        else:
            sorted_positive_aspects.insert(index-1, i)
        count = 0
        index = 0
        test = 0

    
    #This sorts out the negative words
    sorted_negative_aspects = []
    count = 0
    index = 0
    test = 0
    for i in negative_aspects:
        for j in sorted_negative_aspects:
            count += 1
            #This compares frequency
            if(i[1] > j[1]):
                if(test == 0):
                    index = count
                    test += 1
            
        #This adds the word to the correct position
        if(test == 0):
            sorted_negative_aspects.append(i)
        else:
            sorted_negative_aspects.insert(index-1, i)
        count = 0
        index = 0
        test = 0
   
    #This sorts out the neutral words
    sorted_neutral_aspects = []
    count = 0
    index = 0
    for i in neutral_aspects:
        for j in sorted_neutral_aspects:
            count += 1
            #This compares frequency
            if(i[1] > j[1]):
                if(test == 0):
                    index = count
                    test += 1
            
        #This adds the word to the correct position
        if(test == 0):
            sorted_neutral_aspects.append(i)
        else:
            sorted_neutral_aspects.insert(index-1, i)
        count = 0
        index = 0
        test = 0
    
    #Return statement
    return sorted_positive_aspects, sorted_negative_aspects, sorted_neutral_aspects
    
####################
##  Visualization
####################

#this gives us the stats of the sentimental distrubution
def sentiment_distribution (sentiment_scores):
    """
    Function: 
        sentiment_distribution (sentiment_scores)
    Purpose: 
        Gives us a graph and the distribution of the positive, negative, and neutral data
    Arguments:
        sentiment_scores (list): Gives a list of positve, negative, or neutral numbers to be sorted
    Returns:
        List: Just have 3 indexs that have count [positive, negative, neutral] data 
    """
    #setting the list up
    distribution_list = [0,0,0]
    
    #This makes it go through every value
    for i in sentiment_scores:
        #If it is positive it will go through here
        if(i > 0):
            distribution_list[0] += 1
        #If it is negative it will go through here
        elif(i < 0):
            distribution_list[1] += 1
        #If it is neutral it will go through here
        elif(i == 0):
            distribution_list[2] += 1
    extras.pie_plot(distribution_list)
    return distribution_list

#This display the rank of a certain aspect
def extract_rank_aspect(aspect_category, aspects, num):
    """
    Function: 
        extract_rank_aspect(aspect_category, aspects, num)
    Purpose: 
        Prints the top # of a certain catagory of data
    Arguments:
        aspect_category (list): A sorted list that contain all the data
        aspects (str): Gives us a str that stats what we are measuring
        num (int): Tells the top # of what we are looking at
    Returns:
        Nothing, but this is a print function
    """
    #This gets a count for how many we want to compare with number
    count = 0
    #General Print Statement
    print("Top", aspect_category, "aspects:")
    #Gets every list of aspects
    for i in aspects:
        count += 1
        print(f'{i[0]}: {i[1]}')
        #Breaks out of the loop is it reaches the number
        if(count == num):
            break;
    print()

if __name__ == "__main__":
    main()