package HillClimbing;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Individual {
	int dna[];
	public Individual(int N)
	{
		this(N, System.currentTimeMillis());
	}
	public Individual(int N, long seed)
	{
		dna = new int[N];
		Random rd = new Random(seed);
		IntStream.range(0, dna.length).parallel().forEach(index -> {
			dna[index] = rd.nextInt(dna.length);
		});
	}
	public Individual(int dna[])
	{
		this.dna = dna.clone();
	}
	public Individual(Individual parent, int column, boolean positive)
	{
		this(parent.dna);
		if ( positive )
		{
			dna[column]++;
			if ( dna[column] >= dna.length)
				dna[column] = 0;
		} else
		{
			dna[column]--;
			if ( dna[column] < 0)
				dna[column] = dna.length - 1;
		}
	}
	public Individual(Individual parent, int column, int positive)
	{
		this(parent.dna);
		dna[column]+=positive;
		if ( dna[column] >= dna.length)
			dna[column] -= dna.length;
	}
	public int GetDNASize()
	{
		return dna.length;
	}
	
	public Individual getBestNeigbor(int col)
	{
		final HashSet<Individual> res = new HashSet<Individual>();
		final int N = dna.length;
		IntStream.range(0, N).forEach(i -> {
			int row = i;
			Individual next = new Individual(this,col,row+1);
			res.add(next);
		});
		
		//it's faster because the parallel computing is used for "cost"
		//note that "this" is part of the collection so that if there is no neighbors that is better, "this" is kept
		double thisCost = this.Cost();
		Individual best = res.stream().min( (i1, i2) -> (int)(i1.Cost()-thisCost) - (int)(i2.Cost() - thisCost)).get();
		return best;
	}
	public Individual getBestNeigbor()
	{
		final HashSet<Individual> res = new HashSet<Individual>();
		final int N = dna.length;
		IntStream.range(0, N*N).forEach(i -> {
			int col = i%N;
			int row = i/N;
			Individual next = new Individual(this,col,row+1);
			res.add(next);
		});
		
		//it's faster because the parallel computing is used for "cost"
		//note that "this" is part of the collection so that if there is no neighbors that is better, "this" is kept
		double thisCost = this.Cost();
		Individual best = res.stream().min( (i1, i2) -> (int)(i1.Cost()-thisCost) - (int)(i2.Cost() - thisCost)).get();
		return best;
		
	}
	public Individual getBestNeigbor2() //slower
	{
		Individual next, saved, best;
		best = saved = this;
		double bestCost = best.Cost();
		for(int col = 0; col < dna.length; col++)
		{
			for(int row = 0; row < dna.length; row++)
			{
				next = new Individual(saved,col,true);
				double nextCost = next.Cost();
				if ( nextCost < bestCost)
				{
					best = next;
					bestCost = nextCost;
				}
				saved = next;
			}	
		}
		return best;
	}
	
	public double Cost()
	{
		int N = dna.length;
		double cost = 0;
		cost = IntStream.range(0, N).map(i -> queenTarget(i)).sum();
		return cost;
	}
	
	//how many other queen the queen designed by "col" contact. the more, the worse
	int queenTarget(int col)
	{
		int N = dna.length;
		int row = dna[col];
		int kTLBR = col - row + N - 1; // Top Left Bottom Right diagonal index
		int kBLTR = col + row; // Bottom Left Top Right diagonal index
		int result = (int)IntStream.range(0, N).parallel().filter(i -> {
			if ( i != col)
			{
				int col_tg = i;
				int row_tg = dna[col_tg];
				int kTLBR_tg = col_tg - row_tg + N - 1; // Top Left Bottom Right diagonal index
				int kBLTR_tg = col_tg + row_tg; // Bottom Left Top Right diagonal index
				if ( col_tg == col || row_tg == row || kTLBR_tg == kTLBR || kBLTR_tg == kBLTR)
					return true;	
			}
			return false;
		}).count();
		
		return result;
	}
	@Override
	public String toString() {
		String str = "";
		for(int i = 0; i < dna.length; i++)
			str += dna[i] + " ";
		return str;
	}
}
