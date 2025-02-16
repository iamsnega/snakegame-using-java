import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
public class GamePanel extends JPanel implements ActionListener{
	static final int SCREEN_WIDTH = 950;
	static final int SCREEN_HEIGHT = 550;
	static final int UNIT_SIZE = 60;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 175;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 3;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
        @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
    if (running) {
        g.setColor(Color.red); 
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); 
        for (int i = 0; i < bodyParts; i++) {
            g.setColor(Color.green); 
            g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE); 
        }
        g.setColor(Color.white);
        int boxWidth = 200; 
        int boxHeight = 50; 
        int boxX = SCREEN_WIDTH - boxWidth - 10; 
        int boxY = 10;
        g.fillRect(boxX, boxY, boxWidth, boxHeight);
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String scoreText = "SCORE: " + applesEaten;
        int textX = boxX + (boxWidth - metrics.stringWidth(scoreText)) / 2;
        int textY = boxY + ((boxHeight - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(scoreText, textX, textY);
    } else {
        gameOver(g);
    }
}
	public void newApple(){
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move(){
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U' -> y[0] = y[0] - UNIT_SIZE;
		case 'D' -> y[0] = y[0] + UNIT_SIZE;
		case 'L' -> x[0] = x[0] - UNIT_SIZE;
		case 'R' -> x[0] = x[0] + UNIT_SIZE;
		}
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		if(x[0] < 0) {
			running = false;
		}
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		if(y[0] < 0) {
			running = false;
		}
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
    g.setColor(Color.green); 
    g.setFont(new Font("Arial", Font.BOLD, 40)); 
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    g.setColor(Color.red); 
    g.setFont(new Font("Arial", Font.BOLD, 75)); 
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT -> {
                            if(direction != 'R') {
                                direction = 'L';
                            }
                        }
			case KeyEvent.VK_RIGHT -> {
                            if(direction != 'L') {
                                direction = 'R';
                            }
                        }
			case KeyEvent.VK_UP -> {
                            if(direction != 'D') {
                                direction = 'U';
                            }
                        }
			case KeyEvent.VK_DOWN -> {
                            if(direction != 'U') {
                                direction = 'D';
                            }
                        }
			}
		}
        }
}
