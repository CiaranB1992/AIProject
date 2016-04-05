package ie.gmit.sw.ai.game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 50;
	private static Node[][] maze;
	private static Node goal;
	private static GameView view;
	private static int currentRow;
	private static int currentCol;
	private static boolean walkWalls = false;
	private static int walkCount = 0;
	private static int[][] enemyXY = new int[16][2];
	private static boolean holdWeapon = false;
	public static boolean victory = true;
	
	private static FIS fis = FIS.load("fcl/combat.fcl", true);
	private static FunctionBlock combatFB = fis.getFunctionBlock(null);
	
	public static void setGoal(Node goal) {
		GameRunner.goal = goal;
	}
	
	public static Node getGoal() {
		return goal;
	}
	
	public GameRunner() throws Exception{
		
		MazeGeneratorFactory factory = MazeGeneratorFactory.getInstance();
		MazeGenerator generator = factory.getMazeGenerator(MazeGenerator.GeneratorAlgorithm.BinaryTree, MAZE_DIMENSION, MAZE_DIMENSION);
				
		maze = generator.getMaze();
		goal = generator.getGoalNode();
    	view = new GameView(maze);
    	
    	placePlayer();
    	placeEnemy();
    	placeExit();
    	placeGoldBar();
    	
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
	}
	
	private void placePlayer(){   	
    	currentRow = (int) (MAZE_DIMENSION * Math.random());
    	currentCol = (int) (MAZE_DIMENSION * Math.random());
    	maze[currentRow][currentCol].setValue('E');
    	updateView(); 		
	}
	
	private void placeExit(){   	
    	int row = (int) (MAZE_DIMENSION * Math.random());
    	int col = (int) (MAZE_DIMENSION * Math.random());
    	maze[row][col].setValue('L');
    	view.setFinCol(row);
    	view.setFinRow(col);
    	updateView(); 		
	}
	
	private void placeEnemy(){   
		for(int i = 0; i < 16; i++){
			int row = (int) (MAZE_DIMENSION * Math.random());
	    	int col = (int) (MAZE_DIMENSION * Math.random());
	    	enemyXY[i][0] = row;
	    	enemyXY[i][1] = col;
	    	maze[row][col].setValue('T');
		}
		updateView();
	}
	
	
	
	private void moveEnemy(){
		for(int i = 0; i < 16; i++){
			int row = enemyXY[i][0];
			int col =  enemyXY[i][1];
			if(maze[row][col].getValue() == 'E' || maze[row][col].getValue() == ' ')
			{
				
			}
			else{
				//System.out.println(row + ' ' + col);
				int randChoice = (int)(Math.random() * 4);
				
				if(randChoice == 0){
					if (isValidEnemyMove(row, col,row , col+1, Node.Direction.East)){
						maze[row][col].setValue(' ');
						maze[row][col+1].setValue('T');
						enemyXY[i][1] = col+1;  
					}
				}else if(randChoice == 1){
					if (isValidEnemyMove(row, col,row + 1 , col , Node.Direction.South)){
						maze[row][col].setValue(' ');
						maze[row+1][col].setValue('T');
						enemyXY[i][0] = row+1;
					}
					
				}else if(randChoice == 2){
					if (isValidEnemyMove(row, col,row , col -1, Node.Direction.West)){
						maze[row][col].setValue(' ');
						maze[row][col-1].setValue('T');
						enemyXY[i][1] = col-1;
					}
					
				}else{
					if (isValidEnemyMove(row, col,row-1,col, Node.Direction.North)){
						maze[row][col].setValue(' ');
						maze[row-1][col].setValue('T');
						enemyXY[i][0] = row-1;
					}	
				}
			}
		}
		updateView();
	}
	
//	private static void EnemySearchMove(){
//		for(int i = 0; i < 16; i++){
//			int row = enemyXY[i][0];
//			int col =  enemyXY[i][1];
//		
//			int direction = 0;
//			if(row - currentRow > 0)
//			{
//				if(isValidEnemyMove(row, col,row, col+1 , Node.Direction.East))
//				{
//					direction = 2;
//				}
//				else
//				{
//					if(col - currentCol > 0)
//					{
//						if(isValidEnemyMove(row, col,row-1 , col, Node.Direction.North))
//						{
//							direction = 1;
//						}
//					}
//					else
//					{
//						if(isValidEnemyMove(row, col, row + 1, col, Node.Direction.South))
//						{
//							direction = 3;
//						}
//					}
//				}
//			}
//			else
//			{
//				if(isValidEnemyMove(row, col,row,col -1, Node.Direction.West))
//				{
//					direction = 2;
//				}
//				else
//				{
//					if(col - currentCol > 0)
//					{
//						if(isValidEnemyMove(row, col,row - 1,col, Node.Direction.North))
//						{
//							direction = 1;
//						}
//					}
//					else
//					{
//						if(isValidEnemyMove(row, col,row +1 , col, Node.Direction.South))
//						{
//							direction = 3;
//						}
//					}
//				}
//				
//			}
//			
//			
//			if(direction == 0){
//				if (isValidEnemyMove(row, col,row,col+1, Node.Direction.East)){
//					maze[row][col].setValue(' ');
//					maze[row][col+1].setValue('T');
//					enemyXY[i][1] = col+1;  
//				}
//			}else if(direction == 1){
//				if (isValidEnemyMove(row, col,row+1,col, Node.Direction.South)){
//					maze[row][col].setValue(' ');
//					maze[row+1][col].setValue('T');
//					enemyXY[i][0] = row+1;
//				}
//				
//			}else if(direction == 2){
//				if (isValidEnemyMove(row, col,row,col-1, Node.Direction.West)){
//					maze[row][col].setValue(' ');
//					maze[row][col-1].setValue('T');
//					enemyXY[i][1] = col-1;
//				}
//				
//			}else{
//				if (isValidEnemyMove(row, col,row-1,col, Node.Direction.North)){
//					maze[row][col].setValue(' ');
//					maze[row-1][col].setValue('T');
//					enemyXY[i][0] = row-1;
//				}	
//			}
//			updateView();
//		}
//		
//	}
	
	private static void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}
	
	public void placeGoldBar (){
		int max = MAZE_DIMENSION - 1;
		int min = 10;
		
		int randX = min + (int)(Math.random() * ((max - min) + 1));
		int randY = min + (int)(Math.random() * ((max - min) + 1));
		
		int x = currentRow + randX;
		int y = currentCol + randY;
		if(x > MAZE_DIMENSION)
			x -= MAZE_DIMENSION;
		
		if(y > MAZE_DIMENSION)
			y -= MAZE_DIMENSION;
			
		view.setBarCol(currentCol+1);
		view.setBarRow(currentRow);
		maze[x][y].setValue('G');
		//maze[currentRow][currentCol + 1].setValue('G');
	}

    public void keyPressed(KeyEvent e) {
    	moveEnemy();
    	//EnemySearchMove();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) {
        	if(walkWalls){
        		becomeGhost();
        		currentCol++;
        		maze[currentRow][currentCol].setValue('E');
        		maze[currentRow][currentCol - 1].setValue(' ');
        	}else if (isValidMove(currentRow, currentCol + 1, Node.Direction.East))
        		currentCol++;   		
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
        	if(walkWalls){ 
        		becomeGhost();
        		currentCol--;
        		maze[currentRow][currentCol].setValue('E');
        		maze[currentRow][currentCol + 1].setValue(' ');
    	}else if (isValidMove(currentRow, currentCol - 1, Node.Direction.West)) 
        		currentCol--;	
        }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
        	if(walkWalls){ 
        		becomeGhost();
        		currentRow--;
        		maze[currentRow][currentCol].setValue('E');
        		maze[currentRow + 1][currentCol].setValue(' ');
        	}else if (isValidMove(currentRow - 1, currentCol, Node.Direction.North))
        		currentRow--;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1) {
        	if(walkWalls){
        		becomeGhost();
        		currentRow++;
        		maze[currentRow][currentCol].setValue('E');
        		maze[currentRow - 1][currentCol].setValue(' ');
        	}else if (isValidMove(currentRow + 1, currentCol, Node.Direction.South)) currentRow++;
        }else if (e.getKeyCode() == KeyEvent.VK_Z){
        	view.toggleZoom();
        }else{
        	return;
        }
        updateView();       
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore

    
	private void becomeGhost(){
		if(walkCount < 10){
			walkCount++;
			walkWalls = true;
		}else{
			walkWalls = false;
			walkCount = 0;
		}
	}
	private static boolean isValidEnemyMove(int enemyRow, int enemyCol, int destRow, int destCol, Node.Direction direction){ // r - c = destRow, destCol. currentRow - enemyRow
		if(destRow > 49 || destRow < 0 || destCol > 49 ||destCol < 0 ){
			return false;
		}
		if(direction == Node.Direction.North){
			if (maze[enemyRow][enemyCol].hasDirection(Node.Direction.North) /*|| maze[enemyRow-1][enemyCol].hasDirection(Node.Direction.South)*/){
				if(maze[destRow][destCol].getValue() == 'W'){
					maze[destRow][destCol].setValue(' ');
					view.setPlayer_state(9);
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
				}else if(maze[destRow][destCol].getValue() == 'B'){
					walkWalls = true;
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
				}else
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
			}
		}else if(direction == Node.Direction.South){
			if (maze[enemyRow+1][enemyCol].hasDirection(Node.Direction.North) /*|| maze[enemyRow+1][enemyCol].hasDirection(Node.Direction.North)*/){
				if(maze[destRow][destCol].getValue() == 'W'){
					maze[destRow][destCol].setValue(' ');
					view.setPlayer_state(9);
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
				}else if(maze[destRow][destCol].getValue() == 'B'){
					walkWalls = true;
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
				}else
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
			}
		}else if(direction == Node.Direction.East){
			if (maze[enemyRow][enemyCol+1].hasDirection(Node.Direction.West)/* || maze[enemyRow][enemyCol+1].hasDirection(Node.Direction.West)*/){
				if(maze[destRow][destRow].getValue() == 'W'){
					maze[destRow][destCol].setValue(' ');
					view.setPlayer_state(9);
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
				}else if(maze[destRow][destCol].getValue() == 'B'){
					walkWalls = true;
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
				}else
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
			}
		}else if(direction == Node.Direction.West){
			if (maze[enemyRow][enemyCol].hasDirection(Node.Direction.West)/* || maze[enemyRow][enemyCol-1].hasDirection(Node.Direction.East)*/){
				if(maze[destRow][destCol].getValue() == 'W'){
					maze[destRow][destCol].setValue(' ');
					view.setPlayer_state(9);
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
				}else if(maze[destRow][destCol].getValue() == 'B'){
					walkWalls = true;
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
				}else
					maze[enemyRow][enemyCol].setValue(' ');
					maze[destRow][destCol].setValue('E');
					return true;
			//if (r <= maze.length - 1 && c <= maze[r].length - 1 && maze[r][c].getValue() == ' '){
			}
		
		}
			
		return false;
	}
	
	private boolean isValidMove(int r, int c, Node.Direction direction){
		if(r == view.getFinRow() && c == view.getFinCol() ){
			System.exit(1);
			System.out.println("You have found the exit!");
		}
		
		if(direction == Node.Direction.North){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.North) || maze[currentRow-1][currentCol].hasDirection(Node.Direction.South)){
				if(maze[r][c].getValue() == 'W'){
					holdWeapon = true;
					maze[r][c].setValue(' ');
					view.setPlayer_state(9);
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}else if(maze[r][c].getValue() == 'B'){
					walkWalls = true;
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}else if(maze[r][c].getValue() == 'T'){
					victory = combat();
				}else{
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}
			}
		}else if(direction == Node.Direction.South){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.South) || maze[currentRow+1][currentCol].hasDirection(Node.Direction.North)){
				if(maze[r][c].getValue() == 'W'){
					maze[r][c].setValue(' ');
					view.setPlayer_state(9);
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}else if(maze[r][c].getValue() == 'B'){
					walkWalls = true;
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}else if(maze[r][c].getValue() == 'T'){
					victory = combat();
				}else{
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}
			}
		}else if(direction == Node.Direction.East){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.East) || maze[currentRow][currentCol+1].hasDirection(Node.Direction.West)){
				if(maze[r][c].getValue() == 'W'){
					holdWeapon = true;
					maze[r][c].setValue(' ');
					view.setPlayer_state(9);
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}else if(maze[r][c].getValue() == 'B'){
					walkWalls = true;
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}else if(maze[r][c].getValue() == 'T'){
					victory = combat();
				}else{
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}
			}
		}else if(direction == Node.Direction.West){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.West) || maze[currentRow][currentCol-1].hasDirection(Node.Direction.East)){
				if(maze[r][c].getValue() == 'W'){
					holdWeapon = true;
					maze[r][c].setValue(' ');
					view.setPlayer_state(9);
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}else if(maze[r][c].getValue() == 'B'){
					walkWalls = true;
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}else if(maze[r][c].getValue() == 'T'){
					if(combat()){
						maze[r][c].setValue(' ');
					}
					else{
						
					}
				}else{
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
				}
			//if (r <= maze.length - 1 && c <= maze[r].length - 1 && maze[r][c].getValue() == ' '){
			}
		
		}
		
		return false;
		
	}
	
	public boolean combat()
	{
		double value = Math.random();
		System.out.println(value);
		if (value > 0.3){
			combatFB.setVariable("enemyStr", 0);
		}
		else{
			combatFB.setVariable("enemyStr", 1);
		}
		if(!holdWeapon){
			combatFB.setVariable("player", 0);
		}
		else{
			combatFB.setVariable("player", 1);
		}
		combatFB.evaluate();
		System.out.println("Current risk of combat = " + combatFB.getVariable("risk").getValue());
		value = Math.random();
		System.out.println("Number to beat : " + value);
		if(value < combatFB.getVariable("risk").getValue()){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public static void main(String[] args) throws Exception{
		new GameRunner();
		while (true){
			//moveEnemy();
	    	//EnemySearchMove();
		}
	}
}