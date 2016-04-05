package ie.gmit.sw.ai.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

import ie.gmit.sw.ai.game.Node.Direction;

public class GameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;	
	private static final int IMAGE_COUNT = 12;
	private int cellspan = 5;	
	private int cellpadding = 2;
	private Node[][] maze;
	private Node[][] zoomView;
	private BufferedImage[] images;
	private int player_state = 5;
	private int enemy_state = 7;
	private Timer timer;
	private int currentRow;
	private int currentCol;
	private int finRow;
	private int finCol;
	private int barRow;
	private int barCol;
	private boolean zoomOut = false;
	private int imageIndex = -1;
		
	public int getPlayer_state() {
		return player_state;
	}

	public void setPlayer_state(int player_state) {
		this.player_state = player_state;
	}
	

	public int getBarRow() {
		return barRow;
	}

	public void setBarRow(int barRow) {
		this.barRow = barRow;
	}

	public int getBarCol() {
		return barCol;
	}

	public void setBarCol(int barCol) {
		this.barCol = barCol;
	}

	public int getFinRow() {
		return finRow;
	}

	public int getFinCol() {
		return finCol;
	}

	public GameView(Node[][] maze) throws Exception{
		init();
		this.maze = maze;
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		timer = new Timer(300, this);
		timer.start();
	}
	
	public void setFinRow(int row) {
		this.finRow = row;
	}

	public void setFinCol(int col) {
		this.finCol = col;
	}
	
	public void setCurrentRow(int row) {
		if (row < cellpadding){
			currentRow = cellpadding;
		}else if (row > (maze.length - 1) - cellpadding){
			currentRow = (maze.length - 1) - cellpadding;
		}else{
			currentRow = row;
		}
	}

	public void setCurrentCol(int col) {
		if (col < cellpadding){
			currentCol = cellpadding;
		}else if (col > (maze[currentRow].length - 1) - cellpadding){
			currentCol = (maze[currentRow].length - 1) - cellpadding;
		}else{
			currentCol = col;
		}
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
              
        cellspan = zoomOut ? maze.length : 5;         
        final int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
        
        if (zoomOut){
        	for(int row = 0; row < cellspan; row++) {
	        	for (int col = 0; col < cellspan; col++){  
	        		int x1 = col * size;
	        		int y1 = row * size;
	        		int x2 = (col + 1) * size;
	        		int y2 = (row + 1) * size;
	        		
	        		char ch;
	        		ch = maze[row][col].getValue();
	        		
	        		if (ch == 'T'){
	    				g2.setColor(Color.BLUE);
	    				g2.fillRect(x1, y1, size, size);
	    				continue;
	    			}
	        		
	    			if (row == currentRow && col == currentCol){
	    				g2.setColor(Color.CYAN);
	    				g2.fillRect(x1, y1, size, size);
	    				continue;
	    			}
	    			
	    			if (row == finRow && col == finCol){
	    				g2.setColor(Color.WHITE);
	    				g2.fillRect(x1, y1, size, size);
	    				continue;
	    			}
	    			
	    			if (ch == 'G'){
	    				g2.setColor(Color.YELLOW);
	    				g2.fillRect(x1, y1, size, size);
	    				continue;
	    			}
	    			
  	
	        		if (ch == 'X'){        			
	        			imageIndex = 0;
	        		}else if (ch == 'W'){
	        			imageIndex = 1;
	        		}else if (ch == '?'){
	        			imageIndex = 2;
	        		}else if (ch == 'B'){
	        			imageIndex = 3;
	        		}else if (ch == 'G'){
	        			imageIndex = 11;
	        		}else if (ch == 'E'){
	        			imageIndex = player_state;
	        		}else if(ch == 'T'){
	        			imageIndex = enemy_state;
	        		}else{
	        			imageIndex = -1;
	        		}
	        		
	        		if (imageIndex >= 0){
	        			g2.drawImage(images[imageIndex], x1, y1, null);
	        		}else{
	        			g2.setColor(Color.BLACK); // set colour of zoomed out background
	        			g2.fillRect(x1, y1, size, size);
	        		}
	        		
	        		g2.setColor(Color.RED);
	        		g2.drawLine(x1, y1, x2, y1); //N
	        		g2.drawLine(x1, y2, x2, y2); //S
	        		g2.drawLine(x2, y1, x2, y2); //E
	        		g2.drawLine(x1, y1, x1, y2); //W
	        		
	        		g2.setColor(maze[row][col].getColor());
	        		
	        		Direction[] paths = maze[row][col].getPaths();
	        		for (int i = 0; i < paths.length; i++){
	        			if (paths[i] == Direction.North){
	        				g2.drawLine(x1 + 1, y1, x2 - 1, y1); //N
	            		}else if (paths[i] == Direction.South){     			
	            			g2.drawLine(x1, y2, x2, y2); //S
	            		}else if (paths[i] == Direction.East){           			
	            			g2.drawLine(x2, y1, x2, y2); //E
	            		}else if (paths[i] == Direction.West){ 
	            			g2.drawLine(x1, y1 + 1, x1, y2 -1); //W
	        			}
	        		}
	        	}
	        }
		}
        else{
        	zoomView = new Node[cellspan][cellspan];
        	int tempRow = (currentRow -(int)(cellspan / 2));
        	int tempCol = (currentCol -(int)(cellspan / 2));
        	for(int newRow = 0; newRow < cellspan; newRow++){
        		tempCol = (currentCol -(int)(cellspan / 2));
        		for(int newCol = 0; newCol < cellspan; newCol++){
        			try{
        				zoomView[newRow][newCol] = maze[tempRow][tempCol];
        			}
        			catch(Exception e){
        				
        			}
        			tempCol++;
        		}
        		tempRow++;
        	}
	    	for(int row = 0; row < cellspan; row++) {
	        	for (int col = 0; col < cellspan; col++){  
	        		int x1 = col * size;
	        		int y1 = row * size;
	        		int x2 = (col + 1) * size;
	        		int y2 = (row + 1) * size;
	        		
	        		char ch = zoomView[row][col].getValue();
	        		//char ch = 'X';
	        		//try{
	        			
	        			
	        		//}
	        		//catch(Exception e){
	        		//	System.out.println("Failed to set ch for row: " + row + " and col: " + col);
	        		//}
	        		
	        		
	        		
	        		if (ch == 'X'){        			
	        			imageIndex = 0;
	        		}else if (ch == 'W'){
	        			imageIndex = 1;
	        		}else if (ch == '?'){
	        			imageIndex = 2;
	        		}else if (ch == 'B'){
	        			imageIndex = 3;
	        		}else if (ch == 'H'){
	        			imageIndex = 4;
	        		}else if (ch == 'G'){
	        			imageIndex = 11;
	        		}else if (ch == 'E'){
	        			imageIndex = player_state;       			
	        		}else if(ch == 'T'){
	        			imageIndex = enemy_state;
	        		}else{
	        			imageIndex = -1;
	        		}
	        		
	        		if (imageIndex >= 0){
	        			g2.drawImage(images[imageIndex], x1, y1, null);
	        		}else{
	        			g2.setColor(Color.BLACK);
	        			g2.fillRect(x1, y1, size, size);
	        		}
	        		
	        		g2.setColor(Color.RED);
	        		g2.drawLine(x1, y1, x2, y1); //N
	        		g2.drawLine(x1, y2, x2, y2); //S
	        		g2.drawLine(x2, y1, x2, y2); //E
	        		g2.drawLine(x1, y1, x1, y2); //W
	        		
	        		try{
	        			g2.setColor(zoomView[row][col].getColor());
	        		}
	        		catch(Exception e){
	        			
	        		}
	        		
	        		Direction[] paths = zoomView[row][col].getPaths();
	        		for (int i = 0; i < paths.length; i++){
	        			if (paths[i] == Direction.North){
	        				g2.drawLine(x1 + 1, y1, x2 - 1, y1); //N
	            		}else if (paths[i] == Direction.South){     			
	            			g2.drawLine(x1, y2, x2, y2); //S
	            		}else if (paths[i] == Direction.East){           			
	            			g2.drawLine(x2, y1, x2, y2); //E
	            		}else if (paths[i] == Direction.West){ 
	            			g2.drawLine(x1, y1 + 1, x1, y2 -1); //W
	        			}
	        		}
	        	}
	        }
        }
	}
	
	public void toggleZoom(){
		zoomOut = !zoomOut;		
	}

	public void actionPerformed(ActionEvent e) {	
	
		switch (player_state) {
		case 5: player_state = 6; break;
		case 6: player_state = 5; break;
		case 9: player_state = 10; break;
		case 10: player_state = 9; break;
		default: break;
		}
		if (enemy_state < 0 || enemy_state == 7){
			enemy_state = 8;
		}else{
			enemy_state = 7;
		}
		this.repaint();
	}
	
	private void init() throws Exception{
		images = new BufferedImage[IMAGE_COUNT];
		images[0] = ImageIO.read(new java.io.File("resources/hedge.png"));
		images[1] = ImageIO.read(new java.io.File("resources/sword.png"));		
		images[2] = ImageIO.read(new java.io.File("resources/help.png"));
		images[3] = ImageIO.read(new java.io.File("resources/bomb.png"));
		images[4] = ImageIO.read(new java.io.File("resources/h_bomb.png"));
		images[5] = ImageIO.read(new java.io.File("resources/spider_down.png"));
		images[6] = ImageIO.read(new java.io.File("resources/spider_up.png"));
		images[7] = ImageIO.read(new java.io.File("resources/fly_swat.png"));
		images[8] = ImageIO.read(new java.io.File("resources/fly_swat_down.png"));
		images[9] = ImageIO.read(new java.io.File("resources/spider_down_weapon.png"));
		images[10] = ImageIO.read(new java.io.File("resources/spider_up_weapon.png"));
		images[11] = ImageIO.read(new java.io.File("resources/Golden_Bar.png"));
	}
}