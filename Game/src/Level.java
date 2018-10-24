
public enum Level {
			L1(1, "Level 1", new Room("level1")), 
			L2(2, "Level 2", new Room("level2"));
	public int id;
	public String name;
	public Room room;
	
	Level(int i, String n, Room r) {
		id = i;
		name = n;
		room = r;
	}
}
