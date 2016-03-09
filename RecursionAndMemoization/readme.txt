Assignment:

An n x n game board is populated with integers, one nonnegative integer per square. 
The goal is to travel along any legitimate path from the upper left corner to the lower 
right corner of the board. The integer in any one square dictates how large a step away 
from that location must be. If the step size would advance travel off the game board, 
then a step in that particular direction is forbidden.  All steps must be either to the 
right or toward the bottom. 

The solution implemented in Walkabout.java utilizes recursion and memoization.  The memoization
is particularly important, as it makes the algorithm far more efficient by avoiding having 
to calculate the same paths more than once.  Once a path is known, as soon as the algorithm
falls on it again, it stops and increments the path count.  

To compile:  javac Walkabout.java
To run:  java Walkabout input.txt
