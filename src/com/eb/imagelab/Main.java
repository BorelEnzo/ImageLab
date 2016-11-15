package com.eb.imagelab;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("ImageLab");
		frame.setPreferredSize(new Dimension(1024, 1024));
		frame.setContentPane(new MainPanel());
		frame.pack();
		frame.setVisible(true);
	}

}
