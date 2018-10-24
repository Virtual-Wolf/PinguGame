import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class Game extends JPanel implements Runnable {
	private ScheduledExecutorService executor;
	
	public ImageIcon background, walk1, walk2, walk3, walk4, jump1, jump2, slide1, slide2, fall, death1, death2, death3, death4;
	public ImageIcon block1, igloo;
	public ImageIcon snowflake;
	public ImageIcon gameW, gameO;
	
	public ArrayList<Room> rooms = new ArrayList<Room>();
	public ArrayList<String> usedSnowflakes = new ArrayList<String>();
	public Room currentRoom;
	
	public Random rand;
	
	public int scrollPos = 0;
	public int scrollSpeed = 1;
	public int scrollBackground = 0;
	
	public int gravity = 3;
	public int snowGravity = 4;
	
	public int pX = 100, pY = 100;
	public int pFY = 0;
	public int score = 0;
	public int pingSpeed = 2;
	public int pingJumpSpeed = 3;
	public int pingSlideSpeed = 3;
	public int pingDir = 1;
	public int pingWalkStage = 1;
	public int pingJumpStage = 0;
	public int pingSlideStage = 0;
	public int pingDeathStage = 0;
	public boolean noJumpSlide = false;
	public boolean pingFalling = false;
	public boolean pingJumping = false;
	public boolean pingSliding = false;
	public boolean pingAlive = true;
	public boolean pingWon = false;
	
	public boolean keyA = false;
	public boolean keyD = false;
	
	public long dStart, dStop;
	
	
	public Game(Room r, Frame f) {
		rand = new Random();
		currentRoom = r;
		walk1 = new ImageIcon(getClass().getResource("gfx/penguin_walk01.png"));
		walk2 = new ImageIcon(getClass().getResource("gfx/penguin_walk02.png"));
		walk3 = new ImageIcon(getClass().getResource("gfx/penguin_walk03.png"));
		walk4 = new ImageIcon(getClass().getResource("gfx/penguin_walk04.png"));
		jump1 = new ImageIcon(getClass().getResource("gfx/penguin_jump01.png"));
		jump2 = new ImageIcon(getClass().getResource("gfx/penguin_jump02.png"));
		slide1 = new ImageIcon(getClass().getResource("gfx/penguin_slide01.png"));
		slide2 = new ImageIcon(getClass().getResource("gfx/penguin_slide02.png"));
		fall = new ImageIcon(getClass().getResource("gfx/penguin_jump03.png"));
		death1 = new ImageIcon(getClass().getResource("gfx/penguin_die01.png"));
		death2 = new ImageIcon(getClass().getResource("gfx/penguin_die02.png"));
		death3 = new ImageIcon(getClass().getResource("gfx/penguin_die03.png"));
		death4 = new ImageIcon(getClass().getResource("gfx/penguin_die04.png"));
		background = new ImageIcon(getClass().getResource("gfx/Abstract_bg_blueswirl.png"));
		block1 = new ImageIcon(getClass().getResource("gfx/block_ground_00_single.png"));
		gameW = new ImageIcon(getClass().getResource("gfx/GameWon.png"));
		gameO = new ImageIcon(getClass().getResource("gfx/GameOver.png"));
		snowflake = new ImageIcon(getClass().getResource("gfx/object_snowflake.png"));
		igloo = new ImageIcon(getClass().getResource("gfx/object_igloo.png"));
		repaint();
		addKeyListener(new KeyEvents());
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
            	if (pingWon || !pingAlive) {
            		executor.shutdown();
            		f.setContentPane(new Menu(f));
            		f.revalidate();
            	}
            }
		});
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(this, 0, 15, TimeUnit.MILLISECONDS);
		dStart = System.currentTimeMillis();
	}
	
	@Override
	public void paintComponent(Graphics g) {	
		super.paintComponent(g);
		Image pingI;
		g.drawImage(background.getImage(), 0, 1023 - scrollBackground, 1024, 1024, null);
		g.drawImage(background.getImage(), 0, 0 - scrollBackground, 1024, 1024, null);
		for(String s : currentRoom.snowflakes.keySet()) {
			if(!usedSnowflakes.contains(s)) {
				Rectangle r = currentRoom.snowflakes.get(s);
				g.drawImage(snowflake.getImage(), r.x, r.y - scrollPos, null);
			}
		}
		if(!pingWon) {
			if(!pingAlive) {
				if(pingDeathStage < 10) {
					pingI = death1.getImage();
				} else if (pingDeathStage < 20) {
					pingI = death2.getImage();
				} else if (pingDeathStage < 30) {
					pingI = death3.getImage();
				} else {
					pingI = death4.getImage();
				}
			} else if (pingFalling) {
				pingI = fall.getImage();
			} else if (pingJumping) {
				if (pingJumpStage < 20) {
					pingI = jump1.getImage();
				} else {
					pingI = jump2.getImage();
				}
			} else if (pingSliding) {
				if (pingSlideStage < 6) { 
					pingI = slide1.getImage();
				} else {
					pingI = slide2.getImage();
				}
			} else {
				if(pingWalkStage < 8) {
					pingI = walk1.getImage();
				} else if (pingWalkStage < 16) {
					pingI = walk2.getImage();
				} else if (pingWalkStage < 24) {
					pingI = walk3.getImage();
				} else {
					pingI = walk4.getImage();
				}
			}
			if (pingDir == -1) {
				g.drawImage(pingI, pX  + pingI.getWidth(null), pY, -pingI.getWidth(null), pingI.getHeight(null), null);
			} else {
				g.drawImage(pingI, pX, pY, null);
			}
		}
		int x = 0, y = 0;
		 for(x=0;x<16;x++) {
			 for (y=0;y<currentRoom.ySize;y++) {
				 try {
				 if (currentRoom.map[x][y].equals("b")) {
					 g.drawImage(block1.getImage(), x*64, y*64 - scrollPos, 64, 64, null);
				 } else if (currentRoom.map[x][y].equals("z")) {
					 g.drawImage(igloo.getImage(), x*64 - 64, y*64 - 64 - scrollPos, null);
				 }
				 } catch (Exception e){}
			 }
			 for (y=currentRoom.ySize; y<=currentRoom.ySize*64; y++) {
				 g.drawImage(block1.getImage(), x*64, y*64 - scrollPos, 64, 64, null);
			 }
		 }
		 if(!pingAlive) {
			 if(pingDeathStage > 201) {
				 if(pingDeathStage < 401) {
					 g.setColor(new Color(0,0,0,pingDeathStage-201));
					 g.fillRect(0, 0, 1024, 1024);
				 } else {
					 g.setColor(new Color(0,0,0,200));
					 g.fillRect(0, 0, 1024, 1024);
					 if(pingDeathStage > 600) {
						 g.drawImage(gameO.getImage(), 112, 112, null);
						 if (pingDeathStage > 700) {
							 g.setFont(new Font("TimesRoman", Font.PLAIN, 25)); 
							 g.setColor(Color.WHITE);
							 g.drawString("Distance : " + pFY / 64 + " metres", 224, 400);
							 g.drawString("Time : " + (dStop - dStart) / 1000 + " seconds", 224, 425);
							 g.drawImage(snowflake.getImage(), 335, 425, 32, 32, null);
							 g.drawString("Score : " + score, 224, 450);
						 }
					 }
				 }
			 }
		 }
		 if(pingWon) {
			 if(pingDeathStage > 201) {
				 if(pingDeathStage < 401) {
					 g.setColor(new Color(0,0,0,pingDeathStage-201));
					 g.fillRect(0, 0, 1024, 1024);
				 } else {
					 g.setColor(new Color(0,0,0,200));
					 g.fillRect(0, 0, 1024, 1024);
					 if(pingDeathStage > 600) {
						 g.drawImage(gameW.getImage(), 112, 112, null);
						 if (pingDeathStage > 700) {
							 g.setFont(new Font("TimesRoman", Font.PLAIN, 25)); 
							 g.setColor(Color.WHITE);
							 g.drawString("Distance : " + pFY / 64 + " metres", 224, 400);
							 g.drawString("Time : " + (dStop - dStart) / 1000 + " seconds", 224, 425);
							 g.drawImage(snowflake.getImage(), 335, 425, 32, 32, null);
							 g.drawString("Score : " + score, 224, 450);
						 }
					 }
				 }
			 }
		 }
		 if(pingAlive & !pingWon) {
			 g.setColor(Color.WHITE);
			 g.drawImage(snowflake.getImage(), 960, 0, null);
			 g.setFont(new Font("ComicSans", Font.PLAIN, 27));
			 g.drawString("" + score, 940, 44);
		 }
	}
	@Override
	public void run() {
		this.requestFocus();
		if (pingAlive && !pingWon) {
			if((scrollPos/64) < currentRoom.ySize) {
				scrollPos += scrollSpeed;
				scrollBackground += scrollSpeed;
				pY -= scrollSpeed;
				if (scrollBackground > 1023) {
					scrollBackground = 0;
				}
			}
			noJumpSlide = false;
				if(!pingSliding) {
					if (keyD && !keyA) {
						pingDir = 1;
						boolean passable = true;
						Rectangle pHb = getPingHitbox();
						pHb.setBounds((int)pHb.getX() + pingSpeed, (int) pHb.getY(), 48, 62);
						for (Rectangle r : currentRoom.impassable) {				
							if (r.intersects(pHb)) {
								passable = false;
							}
						}
						if (passable) {
							pX+=pingSpeed;
						}
						if(pingWalkStage > 32) {
							pingWalkStage = 1;
						}
						pingWalkStage += 1;
					}
					if (keyA && !keyD) {
						pingDir = -1;
						boolean passable = true;
						Rectangle pHb = getPingHitbox();
						pHb.setBounds((int)pHb.getX() - pingSpeed, (int) pHb.getY(), 48, 62);
						for (Rectangle r : currentRoom.impassable) {				
							if (r.intersects(pHb)) {
								passable = false;
							}
						}
						if (passable) {
							pX-=pingSpeed;
						}
						if(pingWalkStage > 32) {
							pingWalkStage = 1;
						}
						pingWalkStage += 1;
					}
				}
				if(pingJumping) {
					pingJumpStage += 1;
					if (pingJumpStage < 40) {
						boolean passable = true;
						Rectangle pHb = getPingHitbox();
						pHb.setBounds((int)pHb.getX(), (int) pHb.getY() - pingJumpSpeed, 48, 62);
						for (Rectangle r : currentRoom.impassable) {				
							if (r.intersects(pHb)) {
								passable = false;
							}
						}
						if (passable) {
							pY-= pingJumpSpeed;
						} else {
							pingJumping = false;
							pingFalling = true;
							pingJumpStage = 0;
							noJumpSlide = true;
						}
					} else {
						pingJumping = false;
						pingFalling = true;
						pingJumpStage = 0;
						noJumpSlide = true;
					}
				}  else if (pingSliding) {
					pingSlideStage += 1;
					if (pingSlideStage < 40) {
						boolean passable = true;
						Rectangle pHb = getPingHitbox();
						pHb.setBounds((int)pHb.getX() + pingDir * pingSlideSpeed, (int) pHb.getY(), 48, 62);
						for (Rectangle r : currentRoom.impassable) {				
							if (r.intersects(pHb)) {
								passable = false;
							}
						}
						if (passable) {
							pX += pingDir * pingSlideSpeed;
						} else {
							pingSliding = false;
							pingSlideStage = 0;
							noJumpSlide = true;
						}
					} else {
						pingSliding = false;
						pingSlideStage = 0;
						noJumpSlide = true;
					}
				} 
				if (!pingJumping) {
					boolean passable = true;
					Rectangle pHb = getPingHitbox();
					pHb.setBounds((int)pHb.getX(), (int) pHb.getY() + gravity, 48, 62);
					for (Rectangle r : currentRoom.impassable) {				
						if (r.intersects(pHb)) {
							passable = false;
						}
					}
					if (passable) {
						pingFalling = true;
						pY+=gravity;
					} else {
						pingFalling = false; 
					}
				}
			if(pY < 0 || pY > 1000) {
				pingAlive = false;
				pFY = pY + scrollPos;
				dStop = System.currentTimeMillis();
			}
			Rectangle pHb = getPingHitbox();
			pHb.setBounds((int)pHb.getX(), (int) pHb.getY(), 48, 62);
			for(String s : currentRoom.sfHitboxes.keySet()) {
				if(!usedSnowflakes.contains(s)) {
				Rectangle r = currentRoom.sfHitboxes.get(s);
					if(r.intersects(pHb)) {
						score++;
						usedSnowflakes.add(s);
					}
				}
			}
			if(currentRoom.finishPoint.intersects(pHb)) {
				pingWon = true;
				pFY = pY + scrollPos;
				dStop = System.currentTimeMillis();
			}
		} else if(pingDeathStage < 1000) {
			boolean passable = true;
			for (Rectangle r : currentRoom.impassable) {
				Rectangle pHb = getPingHitbox();
				pHb.setBounds((int)pHb.getX(), (int) pHb.getY() + gravity, 48, 62);
				if (r.intersects(pHb)) {
					passable = false;
				}
			}
			if (passable) {
				pingFalling = true;
				pY+=gravity;
			} else {
				pingFalling = false; 
			}
			pingDeathStage += 1;
		}
		repaint();
	}
	
	public class KeyEvents implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 32) {
				if (!pingFalling && !pingJumping && !pingSliding && !noJumpSlide) {
					pingJumping = true;
					pingWalkStage = 1;
				}
			}
			if (e.getKeyChar() == 'a') {
				keyA = true;
			}
			if (e.getKeyChar() == 'd') {
				keyD = true;
			}
			if (e.getKeyCode() == 17) {
				if (!pingFalling && !pingJumping && !pingSliding && !noJumpSlide) {
					pingSliding = true;
					pingWalkStage = 1;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyChar() == 'a') {
				keyA = false;
			}
			if (e.getKeyChar() == 'd') {
				keyD = false;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public Rectangle getPingHitbox() {
		return new Rectangle(pX + 10, pY + scrollPos, 48, 62);
	}
}