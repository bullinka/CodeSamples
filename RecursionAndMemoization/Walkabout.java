
/**
 * Name: Karen Bullinger
  Assignment: Lab 03
  Title: Walkabout
  Course: CSCE 371
  Semester: Fall 2015
  Instructor: George Hauser
  Date: 12/9/2015
  Sources consulted: Carl Derline
  Program description: Finds number of paths from upper left corner to lower right given a 
  						2d matrix representing a graph to traverse.  Uses recursion with
  						memoization.
  Known Problems: No known bugs.
  Creativity: None.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;
/**
 * Walkabout class for solving problem of number of paths
 * from upper left corner to lower right, moving in increments
 * determined by value at current matrix position.
 * @author Karen
 *
 */
public class Walkabout {
	BigInteger[][] matrix;// matrix to hold input matrix
	BigInteger[][] cache;// matrix to hold prev. calculated values
	BigInteger zero = new BigInteger("0"); // used to initialize cache
	
	public static void main(String[] args) {
		//if correct arguments are not provided, error message
		if(args.length != 1){ 
			System.out.println("To use:  java SolveWalkabout inputfile.txt");
			System.exit(0);
		}
		
		//solve for provided data set
		Walkabout walk = new Walkabout(args[0]);
	}
	/**
	 * Takes in a file name to read and open a file. Parses
	 * data of file in to matrix of BigIntegers(used to avoid overflow problems)
	 * and determines the number of paths from the upper left to the lower right
	 * for each input matrix given.
	 * @param args
	 */
	public Walkabout(String args) {
		int matrixSize = 0;
		String current = "";
		Scanner scan;
		
		try {
			File file = new File(args);
			scan = new Scanner(file);
			matrixSize = scan.nextInt(); // get matrix size

			do {
				matrix = new BigInteger[matrixSize][matrixSize];
				cache = new BigInteger[matrixSize][matrixSize];
				for (int i = 0; i < matrixSize; i++) {
					// get row i of matrix
					current = scan.next(); 
					 // parse each entry of current row into col j of matrix
					for (int j = 0; j < matrixSize; j++) {
						matrix[i][j] = new BigInteger(current.charAt(j) + "");
						// initialize cache to all zeros
						cache[i][j] = zero; 
					}
				}
				// solves/prints numbers of paths for given input matrix
				System.out.println(numberOfPaths(zero, zero));
				// get next matrix size
				matrixSize = scan.nextInt();
			} while (matrixSize != -1);
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found.  Please check your spelling and be sure to include the file extension (.txt).");
		}
	}
	/**
	 * Finds number of paths from upper left corner to lower right, moving down
	 * and right based on value in the i, j location of the input matrix.
	 * 
	 * @param i row in matrix
	 * @param j column in matrix
	 * @return total number of paths from upper left to lower right
	 */
	public BigInteger numberOfPaths(BigInteger i, BigInteger j) {
		if (i.intValue() == matrix.length - 1 && j.intValue() == matrix.length - 1) {
			return new BigInteger("1"); // reached lower right hand of graph,
										// add 1 to cache path count
		}
		if ((matrix[i.intValue()][j.intValue()]).intValue() == 0 ||
				cache[i.intValue()][j.intValue()].intValue() != 0) {
			// already calculated number of paths to current loc -- return cached value
			return cache[i.intValue()][j.intValue()]; 
		}
		if ((i.add(matrix[i.intValue()][j.intValue()])).intValue() < matrix.length
				&& matrix[i.intValue()][j.intValue()].intValue() != 0) {
			// cache value not already calculated and not at end row of matrix, move down
			cache[i.intValue()][j.intValue()] = 
					cache[i.intValue()][j.intValue()]
							.add(numberOfPaths((i.add(matrix[i.intValue()][j.intValue()])), j));
		}
		if (j.add(matrix[i.intValue()][j.intValue()]).intValue() < matrix.length
				&& matrix[i.intValue()][j.intValue()].intValue() != 0) {
			// cache value not already calculated and not at end column of matrix, 
			// move over to right
			cache[i.intValue()][j.intValue()] = cache[i.intValue()][j.intValue()]
					.add(numberOfPaths(i, j.add(matrix[i.intValue()][j.intValue()])));
		}
		//returns total number of paths found
		return cache[i.intValue()][j.intValue()];
	}
}
