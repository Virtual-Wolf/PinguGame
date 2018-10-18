
import javax.swing.*;

public class Frame extends JFrame{
	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	new Frame();
	        }
		}
	        );
	}
	
	public Frame() {
		ImageIcon logo = new ImageIcon(getClass().getResource("gfx/icon_penguinjump.png"));
		setIconImage(logo.getImage());
		setSize(1024,1024);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Game");	
		setVisible(true);
		Game g = new Game();
		add(g);
	}

}
