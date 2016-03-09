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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
/**
 * Point class used to represent x, y coordinates
 * @author Karen
 *
 */

public class Point{
	private Double x;
	private Double y;
	
	/**
	 * Constructor
	 * @param x
	 * @param y
	 */
	Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getter for x coordinate
	 * @return double x coordinate
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Setter for x coordinate
	 * @param x new x coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Getter for y coordinate
	 * @return double y coordinate
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Setter for y coordinate
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * toString for formatting points into (x.##, y.##) format
	 */
	public String toString(){
		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.UP);
		return "(" + df.format(this.x)+ ", " + df.format(this.y)+ ")";
	}
	
	/**
	 * Comparator for sorting by x coordinate
	 */
	static Comparator<Point> xCompare = new Comparator<Point>(){
		public int compare(Point p1, Point p2){
			return p1.x.compareTo(p2.x);
		}
	};

	/**
	 * Comparator for sorting by y coordinate
	 */
	static Comparator<Point> yCompare = new Comparator<Point>(){
		public int compare(Point p1, Point p2){
			if(p1.x.compareTo(p2.x) == 0){
				return p1.y.compareTo(p2.y);
			}
			return 0;
		}
	};
}



