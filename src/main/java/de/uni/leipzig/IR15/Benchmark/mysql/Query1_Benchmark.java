package de.uni.leipzig.IR15.Benchmark.mysql;

public class Query1_Benchmark extends MySQLBenchmark {

	@Override
	public void setUp() {
		super.setUp();
		query = String.format("select w1.w2_id from co_s w1 where w1.w1_id=%d",
				start_WordID);
	}
	
	@Override
	public void beforeRun() {
		start_WordID = getRandomStartNode();
		query = String.format("select w1.w2_id from co_s w1 where w1.w1_id=%d",
				start_WordID);
		super.beforeRun();
	}

	@Override
	public String getName() {
		return "MySQL query 1";
	}
}