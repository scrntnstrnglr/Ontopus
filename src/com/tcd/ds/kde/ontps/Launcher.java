package com.tcd.ds.kde.ontps;

import java.awt.EventQueue;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;
import com.tcd.ds.kde.ontps.gui.ApplicationWindow;
import com.tcd.ds.kde.ontps.utils.FileManager;

public class Launcher {
	private static String resDir = "resource";
	private static String queryDir = "resource\\queries";
	private static String imgDir = "resource\\img";
	public static void main(String args[]) {

		try {
			FileManager fm1 = new FileManager(queryDir);
			FileManager fm2 = new FileManager(imgDir);
			fm1.getFileNames();
			fm2.getFileNames();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ApplicationWindow window = new ApplicationWindow(resDir,queryDir,imgDir);
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Resource directory not set. Please read README");
		}
	}

}
