package com.prateek.go_jek_assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellBasedQueryProcessor extends QueryProcessor {

	public ShellBasedQueryProcessor(QueryDelegator queryDelegator) {
		super(queryDelegator);
	}
	 
	@Override
	public void processQueries() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String currentLine = br.readLine();
			while(currentLine != null) {
				processQuery(currentLine);
				currentLine = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	public static void main(String ar[]) {
		ShellBasedQueryProcessor queryProcessor = new ShellBasedQueryProcessor(new QueryDelegator(new ShellBasedOutputWriter()));
		queryProcessor.processQueries();
	}
	*/
}
