package ie.gmit.sw.ai.runner;

import java.awt.*;
import javax.swing.*;
import ie.gmit.sw.ai.maze.*;
import ie.gmit.sw.ai.traversers.*;
public class MazeRunner{
	private static final int MAZE_WIDTH = 50;
	private Node[][] maze;
	private Node goal;
	private MazeView mv;
	
	public static void main(String[] args) {
		new MazeRunner();
	}
	
	public MazeRunner(){
		MazeGeneratorFactory factory = MazeGeneratorFactory.getInstance();
		MazeGenerator generator = factory.getMazeGenerator(MazeGenerator.GeneratorAlgorithm.BinaryTree, MAZE_WIDTH, MAZE_WIDTH);
				
		maze = generator.getMaze();
		goal = generator.getGoalNode();
    	mv = new MazeView(maze, goal);
    	
    	Dimension d = new Dimension(MazeView.DEFAULT_VIEW_SIZE, MazeView.DEFAULT_VIEW_SIZE);
    	mv.setPreferredSize(d);
    	mv.setMinimumSize(d);
    	mv.setMaximumSize(d);
    	mv.setCurrentPosition(goal.getRow(), goal.getCol());
    	
    	JFrame f = new JFrame("B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(mv);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
        
        
        //Uninformed Searches
        //-------------------------------------------------------
        Traversator t = new RandomWalk();
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
        
        t.traverse(maze, maze[0][0]);
	}
}