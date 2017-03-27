package com.prateek.go_jek_assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileBasedQueryProcessor extends QueryProcessor {
	
	private String fileName;
	
	public FileBasedQueryProcessor(String fileName, QueryDelegator queryDelegator) {
		super(queryDelegator);
		this.fileName = fileName;
	}

	@Override
	public void processQueries() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String currentLine = br.readLine();
			while (currentLine != null) {
				processQuery(currentLine);
				currentLine = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/*
	public static void main(String ar[]) {
		FileBasedQueryProcessor queryProcessor = new FileBasedQueryProcessor("file_inputs.txt", new QueryDelegator(new ShellBasedOutputWriter()));
		queryProcessor.processQueries();
	}
	*/
}
