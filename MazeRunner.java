/*============================================================================================================================================
MazeRunner
Scott Hopton
November 3, 2019
Java 4.8.0
==============================================================================================================================================
Problem Definition: Given a starting position, locate the cheese and exit point (if possible), and print the maze showing the shortest path.
Input: No user input. Reading a text file.
Output: Maze with shortest path from mouse to cheese, and from cheese to exit.
Process: 
- Read a text file which contains the maze.
- Search for the coordinates of the mouse and cheese.
- Determine the distance of each spot in the maze from the mouse.
- Use recursion to backtrack from the cheese to the mouse, determining the shortest path.
- Print the path.
- Search for the coordinates of the exit.
- Determine the distance of each spot in the maze from the cheese.
- Use recursion to backtrack from the exit to the cheese, determining the shortest path.
- Print the path.
==============================================================================================================================================
List of identifiers: 
- let obj.maze represent the object that contains the maze character array (from readFile)
- let distance represent the integer array that contains the distance from a starting point to each point in the maze
- let path represent the character array that contains the maze with the shortest path
- let mouseY represent the y-coordinate of the mouse
- let mouseX represent the x-coordinate of the mouse
- let cheeseY represent the y-coordinate of the cheese
- let cheeseX represent the x-coordinate of the cheese
- let cheeseSteps represent the distance from the mouse to the cheese
- let exitX represent the x-coordinate of the exit
- let exitY represent the y-coordinate of the exit
- let exitSteps represent the distance from the cheese to the exit
==============================================================================================================================================
 */ 

import java.io.*;
public class MazeRunner {
	
/**
 * main Method:
 * This procedural method is called automatically and is used to organize the calling of other methods defined in the class
 * 
 * @param args
 * @throws IOException
 * @reuturn void
 */
	public static void main(String[] args) throws IOException {
		Objects obj = new Objects();
		int distance[][] = new int[8][13];
		char path[][] = new char[8][13];
		System.out.println("Welcome to the PATH FINDER!");
		System.out.println();
		System.out.println("This is the maze:");
		System.out.println();
		printMaze(obj.maze);
		System.out.println();
		System.out.println();
		copyMaze(path, obj.maze);
		assignValue(distance, obj.maze);
		int mouseY = findMouseY(obj.maze);
		int mouseX = findMouseX(obj.maze);
		assignDistanceMC(mouseX, mouseY, 1, distance);
		int cheeseY = findCheeseY(obj.maze);
		int cheeseX = findCheeseX(obj.maze);
		int cheeseSteps = intCheeseSteps(obj.maze, distance);
		if (shortestPathMC(mouseX, mouseY, distance, cheeseX, cheeseY, cheeseSteps, path) == true) {
			showPlaces(path, obj.maze);
			System.out.println("Here is the shortest path from the MOUSE to the CHEESE:");
			printPath(path);
			System.out.println();
			System.out.println();
		}
		else {
			System.out.println("There is no path to the cheese.");
			System.out.println();
		}
		int exitX = findExitX(obj.maze);
		int exitY = findExitY(obj.maze);
		resetDistance(distance, obj.maze);
		resetPath(path, obj.maze);
		assignDistanceCX(cheeseX, cheeseY, 1, distance);
		int exitSteps = intExitSteps(obj.maze, distance);
		if (shortestPathCX(cheeseX, cheeseY, distance, exitX, exitY, exitSteps, path) == true) {
			showPlaces(path, obj.maze);
			System.out.println("Here is the shortest path from the CHEESE to the EXIT:");
			printPath(path);
			System.out.println();
			System.out.println();
			System.out.println("See you next time!");
			System.out.println(":)");
		}
		else {
			System.out.println("There is no path to the exit from the cheese.");
			System.out.println();
		}


	} //end of main method

	/**
	 * printMaze Method:
	 * This procedural method prints the original character maze
	 * 
	 * @param maze
	 * @return void
	 */
	public static void printMaze(char maze[][]) {
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < 12; b++) {
				System.out.print((maze[a][b]) + " ");
			}
			System.out.println();
		}
	} //end of printMaze method
	
	/**
	 * assignValue Method:
	 * This procedural method assigns the value of -1 to walls, and 0 to the rest, and stores it in the distance array
	 * 
	 * @param distance
	 * @param maze
	 * @return void
	 */
	public static void assignValue(int distance[][], char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'B') {
					distance[i][j] = -1;
				} 
				else {
					distance[i][j] = 0;
				}
			}
		}
	} //end of assignValue method
	
	/**
	 * copyMaze Method:
	 * This procedural method copies the original maze to the path array 
	 * 
	 * @param path
	 * @param maze
	 * @return void
	 */
	public static void copyMaze(char path[][], char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				path[i][j] = maze[i][j];
			}
		}
	} //end of copyMaze method

	/**
	 * findMouseY Method:
	 * This functional method returns the y-coordinate of the mouse
	 * 
	 * @param maze
	 * @return y-coordinate of the mouse, or 0 if there is no mouse
	 */
	public static int findMouseY(char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'M') {
					return j;
				}
			}
		}
		System.out.println("There is no mouse in the maze.");
		System.out.println();
		return 0;
	} //end of findMouseY method

	/**
	 * findMouseX Method:
	 * This functional method returns the x-coordinate of the mouse
	 * 
	 * @param maze
	 * @return x-coordinate of the mouse, or 0 if there is no mouse
	 */
	public static int findMouseX(char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'M') {
					return i;
				}
			}
		}
		return 0;
	} //end of findMouseX method

	/**
	 * assignDistanceMC Method:
	 * This procedural method marks the distance of each spot in the maze from the mouse
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @param steps - integer that increases each time a valid step is taken away from the mouse
	 * @param distance
	 * @return void
	 */
	public static void assignDistanceMC(int mouseX, int mouseY, int steps, int distance[][]) {
		if (mouseX < 8 && mouseY < 12 && mouseX >= 0 && mouseY >= 0) {
			if (distance [mouseX][mouseY] == 0 || steps < distance [mouseX][mouseY]) {
				distance[mouseX][mouseY] = steps;
				steps++;
				assignDistanceMC(mouseX+1,mouseY,steps, distance);
				assignDistanceMC(mouseX-1,mouseY,steps, distance);
				assignDistanceMC(mouseX,mouseY+1,steps, distance);
				assignDistanceMC(mouseX,mouseY-1,steps, distance);
			}
		}
	} //end of assignDistanceMC method

	/**
	 * findCheeseY Method:
	 * This functional method returns the y-coordinate of the cheese
	 * 
	 * @param maze
	 * @return y-coordinate of the cheese, or 0 if there is no cheese
	 */
	public static int findCheeseY(char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'C') {
					return j;
				}
			}
		}
		System.out.println("There is no cheese in the maze.");
		System.out.println();
		return 0;
	} //end of findCheeseY method

	/**
	 * findCheeseX Method:
	 * This functional method returns the x-coordinate of the cheese
	 * 
	 * @param maze
	 * @return x-coordinate of the cheese, or 0 if there is no cheese
	 */
	public static int findCheeseX(char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'C') {
					return i;
				}
			}
		}
		return 0;
	} //end of findCheeseX method

	/**
	 * intCheeseSteps Method:
	 * This functional method returns the distance of the cheese from the mouse
	 * 
	 * @param maze
	 * @param distance
	 * @return distance from the cheese to the mouse
	 */
	public static int intCheeseSteps(char maze[][], int distance[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'C') {
					int num = distance[i][j];
					return num;
				}
			}
		}
		return 0;
	} //end of intCheeseSteps method

	/**
	 * shortestPathMC Method:
	 * This functional method uses recursion to backtrack from the cheese to the mouse, determining the shortest path
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @param distance
	 * @param cheeseX
	 * @param cheeseY
	 * @param cheeseSteps
	 * @param path
	 * @return true or false
	 */
	public static boolean shortestPathMC(int mouseX, int mouseY, int distance[][], int cheeseX, int cheeseY, int cheeseSteps, char path[][]) {
		if (cheeseX < 8 && cheeseY < 12 && cheeseX >= 0 && cheeseY >= 0)
			if (distance[cheeseX][cheeseY] == cheeseSteps) {
				cheeseSteps--;
				if (cheeseX == mouseX && cheeseY == mouseY) {
					path[mouseX][mouseY] = '*';
					return true;
				}
				else if (shortestPathMC(mouseX, mouseY, distance, cheeseX + 1, cheeseY, cheeseSteps, path)
						|| shortestPathMC(mouseX, mouseY, distance, cheeseX - 1, cheeseY, cheeseSteps, path)
						|| shortestPathMC(mouseX, mouseY, distance, cheeseX, cheeseY + 1, cheeseSteps, path)
						|| shortestPathMC(mouseX, mouseY, distance, cheeseX, cheeseY - 1, cheeseSteps, path)) {
					path[cheeseX][cheeseY] = '*';
					return true;
				}
				return false;
			}
		return false;

	} //end of shortestPathMC method

	/**
	 * showPlaces Method:
	 * This procedural method shows the mouse, cheese, and exit on the maze if they have been replaced by '*'
	 * 
	 * @param path
	 * @param maze
	 * @return void
	 */
	public static void showPlaces(char path[][], char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'M') {
					path[i][j] = 'M';
				}
				if (maze[i][j] == 'C') {
					path[i][j] = 'C';
				}
				if (maze[i][j] == 'X') {
					path[i][j] = 'X';
				}
			}
		}
	} //end of showPlaces method

	/**
	 * printPath Method:
	 * This procedural method prints the maze with the shortest path
	 * 
	 * @param path
	 * @return void
	 */
	public static void printPath(char path[][]) {
		System.out.println();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				System.out.print(path[i][j] + " ");

			}
			System.out.println();
		}
	} //end of printPath method

	/**
	 * findExitY Method:
	 * This functional method returns the y-coordinate of the exit
	 * 
	 * @param maze
	 * @return y-coordinate of the exit, or 0 if there is no exit
	 */
	public static int findExitY(char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'X') {
					return j;
				}
			}
		}
		System.out.println("There is no exit.");
		System.out.println();
		return 0;
	} //end of findExitY method

	/**
	 * findExitX Method:
	 * This functional method returns the x-coordinate of the exit
	 * 
	 * @param maze
	 * @return x-coordinate of the exit, or 0 if there is no exit
	 */
	public static int findExitX(char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'X') {
					return i;
				}
			}
		}
		return 0;
	} //end of findExitX method

	/**
	 * resetDistance Method:
	 * This procedural method resets the distance array, -1 in the spot of walls, and 0 in the rest of the spots
	 * 
	 * @param distance
	 * @param maze
	 * @return void
	 */
	public static void resetDistance(int distance[][], char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'B') {
					distance[i][j] = -1;
				} 
				else {
					distance[i][j] = 0;
				}
			}
		}
	} //end of resetDistance method

	/**
	 * resetPath Method:
	 * This procedural method resets the path array to the original maze
	 * 
	 * @param path
	 * @param maze
	 * @return void
	 */
	public static void resetPath(char path[][], char maze[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				path[i][j] = maze[i][j];
			}
		}
	} //end of resetPath method

	/**
	 * assignDistanceCX Method:
	 * This procedural method marks the distance of each spot in the maze from the cheese
	 * 
	 * 
	 * @param cheeseX
	 * @param cheeseY
	 * @param steps - integer that increases each time a valid step is taken away from the cheese
	 * @param distance
	 * @return void
	 */
	public static void assignDistanceCX(int cheeseX, int cheeseY, int steps, int distance[][]) {
		if (cheeseX < 8 && cheeseY < 12 && cheeseX >= 0 && cheeseY >= 0) {
			if (distance [cheeseX][cheeseY] == 0 || steps < distance [cheeseX][cheeseY]) {
				distance[cheeseX][cheeseY] = steps;
				steps++;
				assignDistanceMC(cheeseX+1,cheeseY,steps, distance);
				assignDistanceMC(cheeseX-1,cheeseY,steps, distance);
				assignDistanceMC(cheeseX,cheeseY+1,steps, distance);
				assignDistanceMC(cheeseX,cheeseY-1,steps, distance);
			}
		}
	} //end of assignDistanceCX method

	/**
	 * intExitSteps Method:
	 * This functional method returns the distance of the exit from the cheese
	 * 
	 * @param maze
	 * @param distance
	 * @return distance from the exit to the cheese
	 */
	public static int intExitSteps(char maze[][], int distance[][]) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				if (maze[i][j] == 'X') {
					int num = distance[i][j];
					return num;
				}
			}
		}
		return 0;
	} //end of intExitSteps method

	/**
	 * shortestPathMC Method:
	 * This functional method uses recursion to backtrack from the exit to the cheese, determining the shortest path
	 * 
	 * @param cheeseX
	 * @param cheeseY
	 * @param distance
	 * @param exitX
	 * @param exitY
	 * @param exitSteps
	 * @param path
	 * @return true or false
	 */
	public static boolean shortestPathCX(int cheeseX, int cheeseY, int distance[][], int exitX, int exitY, int exitSteps, char path[][]) {
		if (exitX < 8 && exitY < 12 && exitX >= 0 && exitY >= 0)
			if (distance[exitX][exitY] == exitSteps) {
				exitSteps--;
				if (exitX == cheeseX && exitY == cheeseY) {
					path[cheeseX][cheeseY] = '*';
					return true;
				}
				else if (shortestPathMC(cheeseX, cheeseY, distance, exitX + 1, exitY, exitSteps, path)
						|| shortestPathMC(cheeseX, cheeseY, distance, exitX - 1, exitY, exitSteps, path)
						|| shortestPathMC(cheeseX, cheeseY, distance, exitX, exitY + 1, exitSteps, path)
						|| shortestPathMC(cheeseX, cheeseY, distance, exitX, exitY - 1, exitSteps, path)) {
					path[cheeseX][cheeseY] = '*';
					return true;
				}
				return false;
			}
		return false;

	} //end of shortestPathCX method


} //end of MazeRunner class

/**
 * Objects Class:
 * This class is called to access the maze character array using obj.maze
 * 
 * List of identifiers: 
 * - let maze represent the character array that contains the maze (class level)
 */
class Objects {
	public char maze[][];
	
	public Objects()throws IOException { //constructor
		maze = readFile();
	}

	/**
	 * readFile Method:
	 * This functional method reads a text file and returns the maze array in which it is stored in
	 * 
	 * @return maze
	 * @throws IOException
	 */
	public static char[][] readFile() throws IOException {
		String line;
		String desktop = System.getProperty("user.home") + "/Desktop/";
		File myFile = new File(desktop + "maze.txt");
		FileReader file = new FileReader(myFile);
		char maze[][] = new char[8][13];
		int i, x = 0, y = 0;
		while ((i = file.read()) != -1) {
			if (x < 12) {
				maze[y][x] = (char) i;
				x++;
			}

			else {
				maze[y][x] = (char) i;
				x = 0;
				y++;
			}
		}
		return maze;

	} //end of readFile method


} //end of Objects class
