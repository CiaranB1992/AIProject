package ie.gmit.sw.ai.game;

public class BinaryTreeMazeGenerator extends AbstractMazeGenerator {
	public BinaryTreeMazeGenerator(int rows, int cols) {
		super(rows, cols);
	}
	
	//Binary tree algorithm for creating a maze. Adds a bias into the generated structure
	//For each node in the maze (2D array), randomly create a passage either north or west, but not both
	public void generateMaze(){
		Node[][] maze = super.getMaze();
		for (int row = 0; row < maze.length; row++){
			for (int col = 0; col < maze[row].length; col++){
				int num = (int) (Math.random() * 10);
				if (col > 0 && (row == 0 || num >= 5)){
					maze[row][col].addPath(Node.Direction.West);
				}else{
					maze[row][col].addPath(Node.Direction.North);	
				}				
			}
		}
		
		//int xNum;
		//xNum = 40;
		//prepareMaze(maze, xNum);
		
		int bombs = (int)((maze.length * maze[0].length) * 0.01);
		//int enemies = (int)((maze.length * maze[0].length) * 0.01);
		int swords = (int)((maze.length * maze[0].length) * 0.01);
		addFeature('W', 'X', swords, maze);
		//addFeature('?', 'X', featureNumber, maze);
		addFeature('B', 'X', bombs, maze);
		//addFeature('H', 'X', featureNumber, maze);
	}
	
//	private void prepareMaze(Node[][] maze, int max){
//		for (int counter = 0; counter < max; counter++){
//			int row = (int) (Math.random()*maze.length);
//			int col = (int) (Math.random()*maze.length);
//			if(maze[row][col].getValue() == 'X'){
//				counter--;
//			}
//			else{
//				maze[row][col].setValue('X');
//			}
//		}
//	}
	
	private void addFeature(char feature, char replace, int number, Node[][] maze){
		int counter = 0;
		while (counter < number){
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			
			if (maze[row][col].getValue() != 'W' && maze[row][col].getValue() != 'B'){
				maze[row][col].setValue(feature);
				counter++;
			}
		}
	}
	
}