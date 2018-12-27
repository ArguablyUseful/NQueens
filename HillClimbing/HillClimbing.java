package HillClimbing;

import java.util.HashSet;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class HillClimbing {

	
	public static HashSet<Individual> test2(final int N, long seed, int maxPop, int max_tries) 
	{
		final boolean stopFlag[] = {false};
		final HashSet<Individual> result = new HashSet<Individual>();
		IntStream.range(0,  maxPop).parallel().forEach(pop -> {
			if ( stopFlag[0])
				return;
			Random rd = new Random(seed+pop);
			Individual i = new Individual(N, seed+pop);
			for(int tries = 0; i.Cost() > 0 && tries < max_tries; tries++)
			{
				i = i.getBestNeigbor();
			}
			if ( !(i.Cost() > 0))
			{
				result.add(i);
				stopFlag[0] = true;
			}	
		});			
		return result; 
	}
	public static void main(String []args)
	{
		long seed = 0L;
		int maxPop = 12;
		int max_tries = 50;
		TreeMap<Integer,HashSet<Individual>> results = new TreeMap<Integer, HashSet<Individual>>();
		
		long before, after;
		before = System.currentTimeMillis();
		for(int N = 8; N < 100; N++)
		{
			seed+=N;
			HashSet<Individual> result =test2(N, seed, maxPop, max_tries);
			
			if ( result.isEmpty() )
			{
				N--;
			} else
			{
				after = System.currentTimeMillis();
				System.out.println("Found " + result.size() + " results for N = " + N + " took " + (after-before) + " ms");
				results.put(N,  result);
				before = System.currentTimeMillis();
			}
		}
		
	}
}
