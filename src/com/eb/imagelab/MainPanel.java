package com.eb.imagelab;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import javax.swing.JPanel;

import com.eb.imagelab.lab.ImageLab;
import com.eb.imagelab.model.EnumGreyScale;
import com.eb.imagelab.model.MyImage;

public class MainPanel extends JPanel{
	
	private MyImage image;
	private static final long serialVersionUID = 1429192900238766835L;
	
	public MainPanel() {
		try {
			image = ImageLab.readImage("pictures/img.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(image == null){
			setPreferredSize(new Dimension(100, 100));
		}
		else{
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		}
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
		
	}

}
