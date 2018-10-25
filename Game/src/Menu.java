import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Menu extends JPanel implements Runnable {

	public ImageIcon background, play;
	
	public Menu(Frame f) {
		background = new ImageIcon(getClass().getResource("gfx/Abstract_bg_blueswirl.png"));
		play = new ImageIcon(getClass().getResource("gfx/block_UI_play.png"));
		setFocusable(true);
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
            	int offset = 0;
                for(Level l : Level.values()) {
                	if((new Rectangle(400, 257 + offset, 64, 64)).contains(e.getPoint())) {
                		f.setContentPane(new Game(l.room, f));
                		f.revalidate();
                	}
                	offset+= 100;
                }
            }
        });
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background.getImage(), 0, 0, 1024, 1024, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 50)); 
		g.drawString("Pingu Fall", 350, 200);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 35)); 
		int offset = 0;
		for (Level l : Level.values()) {
			g.drawString(l.name, 240, 300 + offset);
			g.drawImage(play.getImage(), 400, 257 + offset, null);
			offset += 100;
		}
	}

	@Override
	public void run() {
		repaint();
	}
		
}
