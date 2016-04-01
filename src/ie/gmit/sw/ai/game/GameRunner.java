package ie.gmit.sw.ai.game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ie.gmit.sw.game.tree.NodeType;

//import ie.gmit.sw.ai.traversers.AStarTraversator;
//import ie.gmit.sw.ai.traversers.Traversator;
public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 50;
	private Node[][] maze;
	private Node goal;
	private GameView view;
	private int currentRow;
	private int currentCol;
	private boolean walkWalls = false;
	private int walkCount = 0;
	
	public GameRunner() throws Exception{
		
		
		MazeGeneratorFactory factory = MazeGeneratorFactory.getInstance();
		MazeGenerator generator = factory.getMazeGenerator(MazeGenerator.GeneratorAlgorithm.BinaryTree, MAZE_DIMENSION, MAZE_DIMENSION);
				
		maze = generator.getMaze();
		goal = generator.getGoalNode();
    	view = new GameView(maze);
        
        
        //Uninformed Searches
        //-------------------------------------------------------
        //Traversator t = new RandomWalk();
        //Traversator t = new BruteForceTraversator(true);
        //Traversator t = new RecursiveDFSTraversator();
        //Traversator t = new DepthLimitedDFSTraversator(maze.length / 2);
        //Traversator t = new IDDFSTraversator();
        
        //Heuristic Searches
        //-------------------------------------------------------       
        //Traversator t = new BasicHillClimbingTraversator(goal);     
        //Traversator t = new SteepestAscentHillClimbingTraversator(goal);
        //Traversator t = new SimulatedAnnealingTraversator(goal);
        //Traversator t = new BestFirstTraversator(goal);
        //Traversator t = new BeamTraversator(goal, 2);
        //Traversator t = new AStarTraversator(goal);
        //Traversator t = new IDAStarTraversator(goal);
        //t.traverse(maze, maze[0][0]);
    	
    	placePlayer();
    	placeEnemy();
    	placeExit();
    	
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
	    	maze[row][col].setValue('T');
		}
//    	updateView(); 		
	}
	
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) {
        	becomeGhost();
        	if(walkWalls){ 
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
		if(walkCount < 15){
			walkCount++;
			walkWalls = true;
		}else{
			walkWalls = false;
			walkCount = 0;
		}
	}
	
	private boolean isValidMove(int r, int c, Node.Direction direction){
		if(r == view.getFinRow() && c == view.getFinCol() ){
			System.exit(1);
		}
		
		if(direction == Node.Direction.North){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.North) || maze[currentRow-1][currentCol].hasDirection(Node.Direction.South)){
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
				}else
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
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
				}else
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
			}
		}else if(direction == Node.Direction.East){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.East) || maze[currentRow][currentCol+1].hasDirection(Node.Direction.West)){
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
				}else
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
			}
		}else if(direction == Node.Direction.West){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.West) || maze[currentRow][currentCol-1].hasDirection(Node.Direction.East)){
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
				}else
					maze[currentRow][currentCol].setValue(' ');
					maze[r][c].setValue('E');
					return true;
			//if (r <= maze.length - 1 && c <= maze[r].length - 1 && maze[r][c].getValue() == ' '){
			}
		
		}
			
		return false;
	}
	
	private boolean isValidEnemyMove(int r, int c, Node.Direction direction){
		if(direction == Node.Direction.North){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.North)){
				maze[currentRow][currentCol].setValue(' ');
				maze[r][c].setValue('F');
				return true;
			}
		}else if(direction == Node.Direction.South){
			if (maze[currentRow+1][currentCol].hasDirection(Node.Direction.North)){
				maze[currentRow][currentCol].setValue(' ');
				maze[r][c].setValue('F');
				return true;
			}
		}else if(direction == Node.Direction.East){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.East) || maze[currentRow][currentCol+1].hasDirection(Node.Direction.West)){
				maze[currentRow][currentCol].setValue(' ');
				maze[r][c].setValue('F');
				return true;
			}
		}else if(direction == Node.Direction.West){
			if (maze[currentRow][currentCol].hasDirection(Node.Direction.West) || maze[currentRow][currentCol-1].hasDirection(Node.Direction.East)){
				maze[currentRow][currentCol].setValue(' ');
				maze[r][c].setValue('F');
				return true;
			//if (r <= maze.length - 1 && c <= maze[r].length - 1 && maze[r][c].getValue() == ' '){
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		new GameRunner();
	}
}