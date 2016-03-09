Assignment:

There is an area in the two-dimensional Euclidean plane defined by a set of points P1(x1, y1), ... Pn(xn, yn). 
This set of points defines a convex region that must not be entered. You are also given a pair of points A(xa, ya) 
and B(xb, yb). Find the shortest path from A to B that does not enter the region defined by the convex hull of 
the points P1, ..., Pn. 

Your solution must use the text's recursive divide and conquer algorithm to find the convex hull of P1, ..., Pn.

To compile:  javac ShortestPath.java
	     javac Point.java

To run:	     java ShortestPath pairs.txt