package com.prateek.go_jek_assignment;


/*
 * Code flow starts from this class
 * */
public class Executor {

	public static void main(String[] args) {
		
		QueryProcessor queryProcessor;
		QueryDelegator queryDelegator = new QueryDelegator(new ShellBasedOutputWriter());
		
		if (args.length == 0) {
			queryProcessor = new ShellBasedQueryProcessor(queryDelegator);
		} else {
			queryProcessor = new FileBasedQueryProcessor(args[0], queryDelegator);
		}
		queryProcessor.processQueries();
	}
}
