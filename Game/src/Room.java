import java.awt.Rectangle;
import java.io.BufferedInputStream; 
import java.io.DataInputStream;
import java.util.ArrayList;

public class Room {
	public String name;
	public String[][] map;
	public ArrayList<Rectangle> impassable;
	@SuppressWarnings("deprecation")
	public Room(String name) {
		impassable = new ArrayList<Rectangle>();
		this.name = name;
		map = new String[16][16];
		try(DataInputStream in= new DataInputStream(new BufferedInputStream(getClass().getResourceAsStream("/rooms/" + name + ".txt")))){
			int x = 0, y = 0;
			 for (y=0;y<16;y++) {
				 String[] s = in.readLine().split(" ");
				 for(x=0;x<16;x++) {
					 if(s[x].equals("b")) {
						 impassable.add(new Rectangle(x*64, y*64, 64, 64));
					 }
					 map[x][y] = s[x];
			     }
			 }
		} catch(Exception e) {
			System.out.println("Error loading map: " + name);
			e.printStackTrace();
		}
	}
}
