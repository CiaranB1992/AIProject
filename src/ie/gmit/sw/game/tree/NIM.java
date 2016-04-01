package ie.gmit.sw.game.tree;

import java.io.*;
public class NIM {
	private InputStreamReader isr = new InputStreamReader(System.in); //Input stream to get input from user console
	private BufferedReader br = new BufferedReader(isr); //Buffer the input from the user 
    private NIMGameTree tree = null; //Instance variable referencing our game tree
    private boolean keepRunning = true; 
    
    
    public static void main(String[] args) throws Exception{
		new NIM();
	}

	public NIM() throws Exception{
		init(); //Initialise the Nim game tree
		Node root = tree.getGameTreeRoot(); //Get the root tree node	

		while(keepRunning){ //Keep looping while the game is running
			if (root.getMatches() == 1){ //If there is one item left in the pile, the game is over. 
				System.out.print("Game over. ");
				if (root.getType() == NodeType.MAX){
					System.out.println("You win!");
				}else{
					System.out.println("You lose!");
				}
				stats(); //Print stats
				System.exit(0);
			}
			
			//Generate the game tree to a depth of 3, i.e. with a look-ahead of 2.
			root = tree.buildTree(root.getMatches(), root.getType(), 5);
			
			
			//Is it the user's turn to play, get their move from the console
			if (root.getType() == NodeType.MIN){
				System.out.println("How many items to you want to remove<1|2|3>? ");
				int selection = Integer.parseInt(br.readLine()); //Read the number of items to remove from STDIN
				switch (selection){
					case 1: root = root.getLeft(); break; //Take the left node to remove 1 item
					case 2: root = root.getCenter(); break; //Take the center node to remove 2 items
					case 3: root = root.getRight(); break; //Take the right node to remove 3 items
					default: System.out.println("Invalid selection!."); keepRunning=false; break; //Invalid input. Quit game.
				}
			}else{
				System.out.print("Computer's turn... ");
				root = move(root); //Move to a new game state
			}
			System.out.println(root.getMatches() + " item(s) remaining.");
		}
	}
	
	//Determine the best move to make by calling the minimax method on each child and selecting the largest
	private Node move(Node root){
		int maxValue = 0;
		int maxPos = 0;
		Node[] children = root.children();
		for (int i = 0; i < children.length; i++) {
			if(minimax(children[i]) > maxValue){ //This starts a recursive method invocation of minimax on all nodes
				maxPos = i;
			}
		}
		System.out.println("taking " + (root.getMatches() - children[maxPos].getMatches()) + " items(s).");
		
		return children[maxPos]; //The best move to make is the one with the maximum lowest possible score 
	}
	
	//The minimax method computes the optimal move to make based on the values of leaf nodes
	private int minimax(Node n){
		if (n.getMatches() == 0){ //Leaf nodes have a value of zero. If the leaf node is a MAX node, out opponent must be at the losing parent MIN node!
		       return (n.getType() == NodeType.MAX) ? 1 : -1;
		}else{
			int score;
			//MAX nodes represent a decision point for the computer. Maximize the lowest possible MIN value
			if (n.getType() == NodeType.MAX){ 
				if (n.getLeft() == null) return -1;
				score = Math.max(-1, minimax(n.getLeft()));
				 if (n.getCenter() != null){
					 score = Math.max(score, minimax(n.getCenter()));
					 if (n.getRight() != null) score = Math.max(score, minimax(n.getRight()));
				 }
			}else{ //MIN nodes represent our opponent's move
				if (n.getLeft() == null) return 1;
				score = Math.min(1, minimax(n.getLeft()));
				 if (n.getCenter() != null){
					 score = Math.min(score, minimax(n.getCenter()));
					 if (n.getRight() != null) score = Math.min(score, minimax(n.getRight()));
				 }
			}
			return score;
		}
	}
	
	//Create welcome screen and generate initial game tree
	private void init() throws Exception{
		System.out.println("----------------------------------------------------------------\n");
		System.out.println("\tB.Sc. Software Development - Artificial Intelligence");
		System.out.println("\t\t\tWelcome to NIM!");
		System.out.println("----------------------------------------------------------------\n");
		System.out.println("Do you want go first? (Y|N)");
		
		NodeType rootType = NodeType.MAX;
		if (br.readLine().indexOf("Y") != -1){
			rootType = NodeType.MIN;
		}
		
		System.out.println("How many items do you want to start with?");
		tree = new NIMGameTree(Integer.parseInt(br.readLine()), rootType);
		System.out.println("\t** Game tree created with " + tree.getNodeCount() + " nodes. **");
	}
	
	//Print stats
	private void stats(){
		System.out.print(tree.getNodeCount() + " tree node states were ceated.");
	}
}
