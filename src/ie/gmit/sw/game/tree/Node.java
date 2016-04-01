package ie.gmit.sw.game.tree;

import java.util.*;
public class Node {
	private Node left;
	private Node center;
	private Node right;
	private int matches;
	private NodeType type;
	private List<Node> children = new ArrayList<Node>();
	
	public Node() {
	}
	
	public Node(int matches) {
		this.matches = matches;
	}
	
	public int childCount(){
		return children.size();
	}
	
	public Node[] children(){
		return  (Node[]) children.toArray(new Node[children.size()]);
	}
	
	public boolean isLeaf(){
		if (children.size()  > 0){
			return false;
		}else{
			return true;
		}
	}
	
	public Node getLeft() {
		return left;
	}
	
	public void setLeft(Node left) {
		this.left = left;
		children.add(left);
	}
	
	public Node getCenter() {
		return center;
	}
	
	public void setCenter(Node center) {
		this.center = center;
		children.add(center);
	}
	
	public Node getRight() {
		return right;
	}
	
	public void setRight(Node right) {
		this.right = right;
		children.add(right);
	}
	
	public int getMatches() {
		return matches;
	}
	
	public void setMatches(int matches) {
		this.matches = matches;
	}
	
	public NodeType getType() {
		return type;
	}
	
	public void setType(NodeType type) {
		this.type = type;
	}
}
