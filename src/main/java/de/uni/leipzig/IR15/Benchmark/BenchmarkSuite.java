package de.uni.leipzig.IR15.Benchmark;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Benchmark.neo4j.GetNeighboursBenchmark;
import de.uni.leipzig.IR15.Importer.DEXImporter;
import de.uni.leipzig.IR15.Importer.Neo4JImporter;
import de.uni.leipzig.IR15.Importer.Neo4JImporter.RelTypes;

public class BenchmarkSuite {
	
	public static Logger log = Logger.getLogger(BenchmarkSuite.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();
				
		Benchmark neo4jImportBench = new ImportBenchmark(new Neo4JImporter());
		neo4jImportBench.setWarmups(0);
		neo4jImportBench.setRuns(1);
		benchmarks.add(neo4jImportBench);

		Benchmark neo4jNeighbours = new GetNeighboursBenchmark(
				new Neo4JImporter(), 5, RelTypes.CO_N, 137);
		neo4jNeighbours.setRuns(100);
		neo4jNeighbours.setWarmups(10);
		benchmarks.add(neo4jNeighbours);
		
		Benchmark dexImportBench = new ImportBenchmark(new DEXImporter());
		dexImportBench.setWarmups(0);
		dexImportBench.setRuns(1);
		benchmarks.add(dexImportBench);

		Benchmark dexNeighbours = new de.uni.leipzig.IR15.Benchmark.dex.GetNeighboursBenchmark(
				new DEXImporter(), 5, DEXImporter.RelTypes.CO_N, 137);
		dexNeighbours.setRuns(100);
		dexNeighbours.setWarmups(10);
		benchmarks.add(dexNeighbours);
		
		runBenchmarks(benchmarks);
	}
	
	public static void runBenchmarks(List<Benchmark> benchmarks) {
		for(Benchmark bm : benchmarks) {
			runBenchmark(bm);
		}		
	}
	
	public static void runBenchmark(Benchmark benchmark) {
		int warmups = benchmark.getWarmups();
		int runs = benchmark.getRuns();
		
		long start;
		long diff;

		long[] results = new long[runs];
		
		log.info(String.format("Starting Benchmark: %s with %d warmups and %d runs", benchmark.getName(), warmups, runs));
				
		benchmark.setUp();
		
		// do warmup		
		for (int i = 0; i < warmups; i++) {
			benchmark.run();
			benchmark.reset();
		}
		
		// do measurement		
		for (int i = 0; i < runs; i++) {
			
			start = System.currentTimeMillis();
			benchmark.run();
			diff = System.currentTimeMillis() - start;
			results[i] = diff;
			benchmark.reset();
		}
		benchmark.tearDown();
		
		long sum = 0L;
		for (int i = 0; i < results.length; i++) {
			sum += results[i];
		}
					
		for(Entry<String, Object> e : benchmark.getResults(results).entrySet()) {
			log.info(String.format("%s = %s", e.getKey(), e.getValue().toString()));
		}		
		log.info(String.format("Finished Benchmark: %s", benchmark.getName()));
	}

}
