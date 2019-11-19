package com.tcd.ds.kde.ontps.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	
	private String queryDirectory;
	private ArrayList<String> fileNames = new ArrayList<String>();
	public FileManager (String queryDirectory){
		this.queryDirectory=queryDirectory;
	}
	
	public ArrayList<String> getFileNames(){
		File[] files = new File(queryDirectory).listFiles();
		for(File file : files) {
			if(file.isFile())
				fileNames.add(file.getName());
		}
		return fileNames;
	}
	
	public String getFileContents(String fileName) throws IOException {
		String contents="";
		List<String> allLines = Files.readAllLines(Paths.get(queryDirectory+"\\"+fileName));
		for(String line : allLines) {
			contents+=line+"\n";
		}
		return contents;
	}
	
	public static String getExtension(String fileName) {
		if(fileName.indexOf('.')!=-1) {
			return fileName.substring(fileName.indexOf('.')+1);
		}
		return "";
	}
	
	public static void main(String args[]) throws IOException {
		FileManager fm = new FileManager("resource\\queries");
		System.out.println(fm.getFileContents("query1"));
	}

}
