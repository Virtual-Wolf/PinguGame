import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

public class Game extends JPanel implements Runnable {
	
	public ImageIcon background, walk1, walk2, walk3;
	
	public ArrayList<Room> rooms = new ArrayList<Room>();
	public Room currentRoom;
	
	public int gravity = 7;
	
	public int pX = 100, pY = 100;
	public int pingSpeed = 4;
	public int pingDir = 1;
	public int pingWalkStage = 1;
	public boolean pingFalling = false;
	
	private Thread game;
	
	public Game() {
		currentRoom = new Room("blank");
		walk1 = new ImageIcon(getClass().getResource("gfx/penguin_walk01.png"));
		walk2 = new ImageIcon(getClass().getResource("gfx/penguin_walk02.png"));
		walk3 = new ImageIcon(getClass().getResource("gfx/penguin_walk03.png"));
		background = new ImageIcon(getClass().getResource("gfx/Abstract_bg_blueswirl.png"));
		repaint();
		setFocusable(true);
		this.addKeyListener(new KeyEvents());
		game = new Thread(this);
		game.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {	
		super.paintComponent(g);
		g.drawImage(background.getImage(), 0, 0, null);
		g.drawImage(walk1.getImage(), pX, pY, null);
		int x = 0, y = 0;
		 for (y=0;y<16;y++) {
			 for(x=0;x<16;x++) {
				 
			 }
		 }
		 
	}

	@Override
	public void run() {
		repaint();
		fps();
	}
	
	private void fps() {
		boolean passable = true;
		for (Rectangle r : currentRoom.impassable) {
			if (r.contains(pX + 12, pY + 6 + gravity) || r.contains(pX + 56, pY + 6 + gravity)) {
				passable = false;
			}
		}
		if (passable) {
			pingFalling = true;
			pY+=gravity;
		} else {
			pingFalling = false;
		}
		try {
			game.sleep(40);
			game.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public class KeyEvents implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 49) {
				if (!pingFalling) {
					
				}
			}
			if (e.getKeyChar() == 'a') {
				pingDir = 1;
				boolean passable = true;
				for (Rectangle r : currentRoom.impassable) {
					if (r.contains(pX + 12 - pingSpeed, pY + 6) || r.contains(pX + 56 - pingSpeed, pY + 6)) {
						passable = false;
					}
				}
				if (passable) {
					pX-=pingSpeed;
				}
			}
			if (e.getKeyChar() == 'd') {
				pingDir = 1;
				boolean passable = true;
				for (Rectangle r : currentRoom.impassable) {
					if (r.contains(pX + 12 + pingSpeed, pY + 6) || r.contains(pX + 56 + pingSpeed, pY + 6)) {
						passable = false;
					}
				}
				if (passable) {
					pX+=pingSpeed;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
