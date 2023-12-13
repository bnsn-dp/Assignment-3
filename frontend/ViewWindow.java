package frontend;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public abstract class ViewWindow extends JFrame{
	private static final int DEFAULT_WIDTH = 360;
	private static final int DEFAULT_HEIGHT = 640;

	public ViewWindow(String title){
		super(title);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setVisible(true);
	}

	public abstract void addComponents();
	public abstract void defineComponents();
}
