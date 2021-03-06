import java.awt.Rectangle;
import java.io.BufferedInputStream; 
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Room {
	public String name;
	public int ySize;
	public String[][] map;
	public ArrayList<Rectangle> impassable;
	public ArrayList<Enemy> enemies;
	public HashMap<String, Rectangle> snowflakes;
	public HashMap<String, Rectangle> sfHitboxes;
	public Rectangle finishPoint;
	@SuppressWarnings("deprecation")
	public Room(String name) {
		impassable = new ArrayList<Rectangle>();
		enemies = new ArrayList<Enemy>();
		snowflakes = new HashMap<String, Rectangle>();
		sfHitboxes = new HashMap<String, Rectangle> ();
		this.name = name;
		try(DataInputStream in= new DataInputStream(new BufferedInputStream(getClass().getResourceAsStream("/rooms/" + name + ".txt")))){
			ySize = Integer.parseInt(in.readLine());
			map = new String[16][ySize];
			int sfCounter = 1;
			int x = 0, y = 0;
			 for (y=0;y<ySize;y++) {
				 String[] s = in.readLine().split(" ");
				 for(x=0;x<16;x++) {
					 if(s[x].equals("b")) {
						 impassable.add(new Rectangle(x*64, y*64, 64, 64));
					 } else
					 if(s[x].equals("x")) {
						 snowflakes.put("sf" + sfCounter, new Rectangle(x*64, y*64, 64, 64));
						 sfHitboxes.put("sf" + sfCounter, new Rectangle(x*64 + 16, y*64 + 16, 32, 32));
						 sfCounter++;
					 } else
					 if(s[x].equals("z")) {
						 finishPoint = new Rectangle(x*64 - 64, y*64 -32, 128, 96);
					 } else
					 if(s[x].equals("y")) {
						 enemies.add(new Enemy(x*64, y*64));
					 }
					 map[x][y] = s[x];
			     }
			 }
			 impassable.add(new Rectangle(-64, 0, 64, ySize*64));
			 impassable.add(new Rectangle(1024, 0, 64, ySize*64));
			 impassable.add(new Rectangle(0, ySize*64, 1024, 64));
		} catch(Exception e) {
			System.out.println("Error loading map: " + name);
			e.printStackTrace();
		}
	}
	
	public class Enemy {
		public int x, y;
		public boolean alive;
		public int dir, walkState;
		
		public Enemy(int x, int y) {
			this.x = x;
			this.y = y;
			alive =  true;
			dir = 1;
			walkState = 0;
		}
		
		public Rectangle getHitbox() {
			return new Rectangle(x + 16, y + 16, 32, 32);
		}
		
		public Rectangle getImgHitbox() {
			return new Rectangle(x, y, 64, 64);
		}
	}
}