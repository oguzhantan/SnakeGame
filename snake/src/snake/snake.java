package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class snake extends JPanel {
	public class Board extends JPanel implements ActionListener {
    
		/*we will define the constant variables of our game*/
		
	    private final int boardWIDTH = 700;
	    private final int boardHEIGHT = 700;
	    private final int pixel_SIZE = 10;
	    private final int total_PIXELS = boardHEIGHT*boardWIDTH;
	    private final int random_POSITION = 29;
	    private final int DELAY = 140;

	    private final int x[] = new int[total_PIXELS];
	    private final int y[] = new int[total_PIXELS];

	    private int pixels;
	    private int food_x;
	    private int food_y;

	    private boolean leftDirection = false;
	    private boolean rightDirection = true;
	    private boolean upDirection = false;
	    private boolean downDirection = false;
	    private boolean inGame = true;

	    private Timer timer;
	    private Image circle;
	    private Image food;
	    private Image head;

	    public Board() {
	        
	        initBoard();
	    }
	    
	    private void initBoard() {

	        addKeyListener(new GameAdapter());
	        setBackground(Color.black);
	        setFocusable(true);
	        setDoubleBuffered(true);

	        setPreferredSize(new Dimension(boardWIDTH, boardHEIGHT));
	        loadItems();
	        initGame();
	    }

	    private void loadItems() {
            /*In this method we get the images for the game*/
	        ImageIcon snake_id = new ImageIcon("src/snake/pix.png");
	        circle = snake_id.getImage();

	        ImageIcon food_id = new ImageIcon("src/snake/food.png");
	        food = food_id.getImage();

	        ImageIcon head_id = new ImageIcon("src/snake/head.png");
	        head = head_id.getImage();
	    }

	    private void initGame() {
            /*In this method we create the snake, randomly locate an food on the board,
               and start the timer.*/
	        pixels = 3;

	        for (int z = 0; z < pixels; z++) {
	            x[z] = 50 - z * 10;
	            y[z] = 50;
	        }
	        
	        locatefood();

	        timer = new Timer(DELAY, this);
	        timer.start();
	    }

	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);

	        doDrawing(g);
	    }
	    
	    private void doDrawing(Graphics g) {
	        
	        if (inGame) {

	            g.drawImage(food, food_x, food_y, this);

	            for (int z = 0; z < pixels; z++) {
	                if (z == 0) {
	                    g.drawImage(head, x[z], y[z], this);
	                } else {
	                    g.drawImage(circle, x[z], y[z], this);
	                }
	            }

	            

	        } else {

	            gameOver(g);
	        }        
	    }

	    private void gameOver(Graphics g) {
	        
	        String msg = "Game Over";
	        Font small = new Font("Helvetica", Font.BOLD, 14);
	        FontMetrics metr = getFontMetrics(small);

	        g.setColor(Color.white);
	        g.setFont(small);
	        g.drawString(msg, (boardWIDTH - metr.stringWidth(msg)) / 2, boardHEIGHT / 2);
	    }

	    private void checkFood() {
           /*If the food collides with the head, we increase size of the snake.
            We call the locatefood() method which randomly positions a new food object.*/
	        if ((x[0] == food_x) && (y[0] == food_y)) {

	            pixels++;
	            locatefood();
	        }
	    }

	    private void move() {

	        for (int i = pixels; i > 0; i--) {
	            /*This code moves the joints up the chain.*/
	        	x[i] = x[(i - 1)];
	            y[i] = y[(i - 1)];
	        }

	        if (leftDirection) {
	        	/*This line moves the head to the left.*/
	            x[0] -= pixel_SIZE;
	        }

	        if (rightDirection) {
	        	/*This line moves the head to the right.*/
	        	x[0] += pixel_SIZE;
	        }

	        if (upDirection) {
	        	/*This line moves the head to the up.*/
	            y[0] -= pixel_SIZE;
	        }

	        if (downDirection) {
	        	/*This line moves the head to the down.*/
	            y[0] += pixel_SIZE;
	        }
	    }

	    private void checkCollision() {
            /*In this method,we determine if the snake has hit itself or one of the walls.
             The game is over*/
	        for (int z = pixels; z > 0; z--) {

	            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
	                inGame = false;
	            }
	        }

	        if (y[0] >= boardHEIGHT) {
	        	/*The game is finished if the snake hits the bottom of the board.*/
	            inGame = false;
	        }

	        if (y[0] < 0) {
	        	/*The game is finished if the snake hits the top of the board.*/
	        	inGame = false;
	        }

	        if (x[0] >= boardWIDTH) {
	        	/*The game is finished if the snake hits the left of the board.*/
	        	inGame = false;
	        }

	        if (x[0] < 0) {
	        	/*The game is finished if the snake hits the right of the board.*/
	            inGame = false;
	        }
	        
	        if (!inGame) {
	            timer.stop();
	        }
	    }

	    private void locatefood() {

	        int r = (int) (Math.random() * random_POSITION);
	        food_x = ((r * pixel_SIZE));

	        r = (int) (Math.random() * random_POSITION);
	        food_y = ((r * pixel_SIZE));
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {

	        if (inGame) {

	            checkFood();
	            checkCollision();
	            move();
	        }

	        repaint();
	    }

	    private class GameAdapter extends KeyAdapter {

	        @Override
	        public void keyPressed(KeyEvent e) {

	            int key = e.getKeyCode();

	            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
	                leftDirection = true;
	                upDirection = false;
	                downDirection = false;
	            }

	            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
	                rightDirection = true;
	                upDirection = false;
	                downDirection = false;
	            }

	            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
	                upDirection = true;
	                rightDirection = false;
	                leftDirection = false;
	            }

	            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
	                downDirection = true;
	                rightDirection = false;
	                leftDirection = false;
	            }
	        }
	    }
	}
	
	public class TheGame extends JPanel{

		public  void main(String[] args) {
			JFrame frame= new JFrame("The Snake Game");
			JPanel panel=new JPanel();
			frame.add(panel);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			panel.setSize(700,700);
			Board b=new Board();
			panel.add(b);
			
		}

}
}