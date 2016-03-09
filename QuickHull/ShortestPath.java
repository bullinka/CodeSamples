/**
 * Name: Karen Bullinger
  Assignment: Lab 02
  Title: QuickHull
  Course: CSCE 371
  Semester: Fall 2015
  Instructor: George Hauser
  Date: 11/12/2015
  Sources consulted: Carl Derline
  Program description: Finds convex hull of a set of points using Quickhull.
  Known Problems: If three points given on a straight line, returns all three points as part of the
    			  hull.  Also, important to note that depending on threshold value, program can enter 
    			  infinite recursion.
  Creativity: None.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
/**
 * ShortestPath class implements Quickhull to find the
 * convex hull of a given set of points.
 *
 */
public class ShortestPath {
	final double THRESHOLD = 0.00000000001;

	public static void main(String[] args){
		//if correct arguments are not provided, exit
		if(args.length < 1){
			System.out.println("Usage: java ShortestPath [input.txt]");
			System.exit(1);
		}
		
		ShortestPath shortest = new ShortestPath();
		ArrayList<Point> points = new ArrayList<Point>();
		Scanner fileScan;
		Point a, b;
		int numProbs;
		int numPoints;
		int counter = 1; 
		double pointAx, pointAy, pointBx, pointBy;
		double x, y;

		try {
			File file = new File(args[0]);
			fileScan = new Scanner(file);

			numProbs = fileScan.nextInt();
			/**
			 * Reads in data file and solves for 
			 * convex hull and shortest path.
			 */
			while(numProbs > 0){
				pointAx = fileScan.nextDouble();
				pointAy = fileScan.nextDouble();
				pointBx = fileScan.nextDouble();
				pointBy = fileScan.nextDouble();
				a = new Point(pointAx, pointAy);
				b = new Point(pointBx, pointBy);

				numPoints = fileScan.nextInt();
				while(numPoints > 0){
					x = fileScan.nextDouble();
					y = fileScan.nextDouble();

					points.add(new Point(x, y));
					numPoints--;
				}
				
				Collections.sort(points, Point.xCompare);
				Collections.sort(points, Point.yCompare);	
				
				ArrayList<Point> convexHull = new ArrayList<Point>();
				
				//find the convex hull
				convexHull = shortest.preQuickHull(points);
				
				System.out.println("Problem #" + counter);
				System.out.println("Convex Hull:");

				for(int i = 0; i < convexHull.size(); i++){
					System.out.println(convexHull.get(i).toString());
				}

				System.out.println("Points on hull: "+ convexHull.size());
				
				//call shortestPath
				System.out.print(shortest.shortestPath(convexHull, a, b));

				points.clear();
				convexHull.clear();
				numProbs--;	
				counter++;
			}
			fileScan.close();
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found.  Please check your spelling and be sure to include the file extension (.txt).");
		}

	}

	/**
	 * Finds shortest path from point A to point B using only the points given in n.
	 * @param n ArrayList of Points to use for finding a path
	 * @param a Starting point
	 * @param b Ending point
	 * @return String detailing start point, shortest path to end point,
	 *          end point, and total distance.
	 */
	public String shortestPath(ArrayList<Point> n, Point a, Point b){
		DecimalFormat df = new DecimalFormat("0.00");
		ArrayList<Point> upper = new ArrayList<Point>(); //holds points of upper hull
		ArrayList<Point> lower = new ArrayList<Point>(); //holds points of lower hull
		ArrayList<Point> resultA = new ArrayList<Point>(); //holds upper path result
		ArrayList<Point> resultB = new ArrayList<Point>(); //holds lower path result
		double distA, distB;

		n.add(0, a); //add points A, B to list for finding hull
		n.add(b);

		Point p1 = minDistance(n, a); //finds point closest to A
		Point pn = minDistance(n, b); //finds point closest to B

		for(int i = 0; i< n.size(); i++){//builds list of points on upper hull
			if(checkPoint(p1, pn, n.get(i))==1){
				upper.add(n.get(i));
			}
			else if( checkPoint(p1, pn, n.get(i))== -1){//builds list of points on lower hull
				lower.add(n.get(i));
			}
		}
		resultA.addAll(quickHull(upper, p1, pn)); //use quickhull to determine path from A
		if(!pn.equals(p1))										  //to B along upper points
			resultB.addAll(quickHull(lower, pn, p1)); //use quickhull to determine path from A
		//to B along lower points
		Collections.sort(resultB, Point.xCompare); //sort points by x coordinate
		Collections.sort(resultB, Point.yCompare); //sort points by y coordinate while
		//maintaining x ordering
		resultA.add(b); //add end point to upper path
		resultB.add(0, a); //add starting point to beginning of lower path
		distA = totalDistance(resultA); //find total distance of upper path
		distB = totalDistance(resultB); //find total distance of lower path

		if(distA < distB){ //choose the shortest path to print based on length
			return toString(resultA) + " distance: " + df.format(distA) + "\n\n";
		}
		else if(distB < distA){
			return toString(resultB) + " distance: " + df.format(distB) + "\n\n";
		}
		else{
			if( resultA.size() < resultB.size()){//if lengths are equal pick path with
				//fewest points
				return toString(resultA) + " distance: " + df.format(distA) + "\n\n";
			}else if(resultA.size() == resultB.size() && distA == distB){
				return toString(resultA) + " distance: " + df.format(distA) + "\n\n";				
			}
			else{//if num points and distance are equal, print "lower" path
				return toString(resultB) + " distance: " + df.format(distB) + "\n\n"; 
			}
		}
	}

	/**
	 * toString method for printing out shortest path.
	 * Called by shortestPath method.
	 * @param a ArrayList of type Point
	 * @return String of points beginning with starting point, 
	 * followed by path, followed by end point.
	 */
	public String toString(ArrayList<Point> a){
		String ret = "";
		ret += "Shortest Path: \nA: ";
		ret += a.get(0).toString() + "\n";
		if(a.size()> 2){
			for(int i = 1; i <a.size()-1; i++){
				ret += a.get(i) + "\n";
			}
		}
		ret += "B: " + a.get(a.size()-1);
		return ret;
	}

	/**
	 * Calculates the distance between two points
	 * @param p1 Point with x, y coordinates
	 * @param p2 Point with x, y coordinates
	 * @return distance between two points
	 */
	public double distance(Point p1, Point p2){
		double x, y;
		x = p2.getX() - p1.getX();
		x *= x;
		y = p2.getY() - p1.getY();
		y *= y;
		x += y;
		x = Math.sqrt(x);
		return x;
	}

	/**
	 * Prepares data set of points for quickHull.  Sorts them in to
	 * upper and lower sets depending on their location relative to 
	 * Points are sorted before calling this method.
	 * the line formed by p1 and pn.
	 * @param a ArrayList of points
	 * @return ArrayList of points consisting of the convex hull
	 */
	public ArrayList<Point> preQuickHull(ArrayList<Point> a){
		ArrayList<Point> upper = new ArrayList<Point>();
		ArrayList<Point> lower = new ArrayList<Point>();
		ArrayList<Point> result = new ArrayList<Point>();
		if(a.size() < 3) //if less than three data points, all are on hull
		{
			return a;
		}

		Point p1 = a.get(0);  //get smallest point 
		Point pn = a.get(a.size()-1); //get largest point

		for(int i = 1; i < a.size()-1; i++){ //sort points into upper and lower sets
			if(checkPoint(p1, pn, a.get(i)) == 1){
				upper.add(a.get(i));
			}
			else if(checkPoint(p1, pn, a.get(i)) == -1){
				lower.add(a.get(i));
			}
		}
		result.addAll(quickHull(upper, p1, pn));//quickhull on upper
		result.addAll(quickHull(lower, pn, p1));//quickhull on lower
		return result;
	}

	/**
	 * Recursively finds the convex hull of a given set of points.
	 * @param a ArrayList of points
	 * @param p1 minimum point
	 * @param pn maximum point
	 * @return
	 */
	public ArrayList<Point> quickHull(ArrayList<Point> a, Point p1, Point pn){
		ArrayList<Point> s1 = new ArrayList<Point>(); 
		ArrayList<Point> s2 = new ArrayList<Point>();
		ArrayList<Point> result = new ArrayList<Point>();
		if(a.isEmpty()){ //base case
			a.add(p1);
			return a;
		}		
		Point pmax = maxDistance(p1, pn, a); //find point in list a farthest away from line
		//formed by p1 --> pn 
		for(int i = 0; i <a.size(); i++){
			if(checkPoint(p1, pmax, a.get(i))== 1 || checkPoint(p1, pmax, a.get(i)) == 0){ //finds current points position
				//relative to p1 --> pmax and adds
				//to s1 if "above" p1 --> pmax
				s1.add(a.get(i));
			}
		}
		for(int i = 0; i < a.size(); i++){
			if(checkPoint(pmax, pn, a.get(i)) == 1 || checkPoint(pmax, pn, a.get(i)) == 0 ){//finds current points position
				//relative to pmax --> pn and adds
				// to s2 if "above" pmax --> pn
				s2.add(a.get(i));
			}
		}
		if(!pn.equals(pmax)){
			result.addAll(quickHull(s1, p1, pmax)); //recursively call quickhull on newly
		}
		//created set 1
		if(!p1.equals(pmax)){
			result.addAll(quickHull(s2, pmax, pn));	//recursively call quickhull on newly
			//created set 2
		}
		return result;
	}

	/**
	 * Finds p3's position relative to p1 --> p2
	 * @param p1 starting point of base line
	 * @param p2 ending point of base line
	 * @param p3 tip of triangle formed by p1-->p2
	 * @return where p3 lies in relation to p1-->p2
	 */
	public int checkPoint(Point p1, Point p2, Point p3){
		double eq =  p1.getX()*p2.getY() + p3.getX()*p1.getY() + p2.getX()*p3.getY()
				- p3.getX()*p2.getY() - p2.getX()*p1.getY() - p1.getX()*p3.getY();
		if(eq > THRESHOLD){// above line p1-->p2
			return 1;
		}
		else if(eq < THRESHOLD ){ //below line p1-->p2
			return -1;
		}
		else return 0;// on the line p1-->p2
	}

	/**
	 * Finds point that maximizes distance from p1-->p2 in given list of points
	 * @param p1 beginning of baseline
	 * @param p2 end of base line p1-->p2
	 * @param a list of points to check for max distance
	 * @return point with maximum distance from p1-->p2
	 * distance is found by using area formula
	 */
	public Point maxDistance(Point p1, Point p2, ArrayList<Point> a){
		double max = 0, currpoint = 0;
		Point p3;
		int index = -1;
		for(int i = 0; i <a.size(); i++){
			p3 = a.get(i);
			//find distance
			currpoint =  p1.getX()*p2.getY() + p3.getX()*p1.getY() + p2.getX()*p3.getY()
					- p3.getX()*p2.getY() - p2.getX()*p1.getY() - p1.getX()*p3.getY();

			currpoint = Math.abs(currpoint);		
			if(max <= currpoint){//keep track of point creating maximum distance
				max = currpoint;
				index = i;
			}
		}
		return a.get(index);
	}

	/**
	 * Finds point in a closest to given point blank
	 * @param a ArrayList of points
	 * @param b Point
	 * @return Point which has smallest distance to a from b
	 */
	public Point minDistance( ArrayList<Point> a, Point b ){
		double min = Double.MAX_VALUE;
		double x, y;
		int index = -1;
		Point p1;
		for(int i = 0 ; i <a.size(); i++){
			p1 = a.get(i);
			x = b.getX() - p1.getX();
			x *= x;
			y = b.getY() - p1.getY();
			y *= y;
			x += y;
			x = Math.sqrt(x);
			if( min >= x){
				min = x;
				index = i;
			}
		}
		return a.get(index);
	}

	/**
	 *Finds total distance of a given path
	 * @param a ArrayList of points
	 * @return total distance of given path
	 */
	public double totalDistance(ArrayList<Point> a){
		double total = 0.0;
		double x, y;
		Point p1, p2;
		for( int i = 0; i <a.size()-1; i++){
			p1 = a.get(i);
			p2 = a.get(i+1);
			x = p2.getX() - p1.getX();
			y = p2.getY() - p1.getY();	
			x *= x;
			y *= y;
			x+= y;
			x = Math.sqrt(x);
			total += x;		
		}
		return total;
	}

}
