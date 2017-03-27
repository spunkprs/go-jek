package com.prateek.go_jek_assignment;

public abstract class QueryProcessor {
	
	private QueryDelegator queryDelegator;
	
	public QueryProcessor(QueryDelegator queryDelegator) {
		this.queryDelegator = queryDelegator;
	}
	
	abstract public void processQueries();
	
	protected void processQuery(String query) {
		queryDelegator.processQuery(query);
	}

}
