package ie.gmit.sw.game.tree;

public class NIMGameTree {
	private Node root; //The root of the NIM game tree
	private static int nodeCount; //Keeps track of the total number of game states created
	private static final int LIMIT=4; //Depth must be less than this limit. Includes the current node(MAX)+MIN+MAX.
	
	public NIMGameTree(int matches, NodeType type){
		root = buildTree(matches, type, 0);
	}
	
	//Recursive method to construct game tree 
	public Node buildTree(int matches, NodeType type, int depth){
		Node n = new Node ();
		nodeCount++;
		n.setMatches(matches);
		n.setType(type);
		if (depth < LIMIT){ //Keep generating successors while we are within the depth limit
			if (matches >= 1) n.setLeft(buildTree(matches-1, type == NodeType.MAX?NodeType.MIN:NodeType.MAX, depth+1));
			if (matches >= 2) n.setCenter(buildTree(matches-2, type == NodeType.MAX?NodeType.MIN:NodeType.MAX, depth+1));
			if (matches >= 3) n.setRight(buildTree(matches-3, type == NodeType.MAX?NodeType.MIN:NodeType.MAX, depth+1));
		}
		return n;
	}
	
	public Node getGameTreeRoot(){
		return root;
	}
	
	public int getNodeCount(){
		return nodeCount;
	}
}
