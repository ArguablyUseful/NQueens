package minConflict;

import java.util.Random;
import java.util.stream.IntStream;

public class minConflict {

	Random rd;
	final int N;
	int dna[];
	public minConflict(int N, long seed)
	{
		rd = new Random(seed);
		dna = new int[N];
		this.N =  N;
		reset();
	}
	public void reset()
	{
		IntStream.range(0, N).forEach(i -> dna[i] = rd.nextInt(N));
	}
	public static void main(String []args)
	{
		minConflict min = new minConflict(100000, 0L);
		long before = System.currentTimeMillis();
		min.doStuff();
		long after = System.currentTimeMillis();
		System.out.println("took : " + (after-before) + " ms");
	}
	public String toString()
	{
		String str = "";
		for(int i : dna)
		{
			str += i + " ";
		}
		return str;
	}
	public void doStuff()
	{
		int tries = 0;
		int max_tries = 10;
		while( !isOk())
		{
			boolean changed = false;
			int targetColumn = rd.nextInt(N);
			while(this.violatedConstraintsCount(targetColumn, dna[targetColumn])== 0)
				targetColumn = rd.nextInt(N);//*/
			changed = changeColumn(targetColumn);	
			if ( !changed)
				tries++;
			else
				tries = 0;
			if ( tries >= max_tries)
				reset();
			
		}
		System.out.println("found solution for the " + this.dna.length + "Queens");
		//System.out.println(this.toString());
	}
	boolean changeColumn(int col)
	{
		int savedValue = dna[col];
		int value = IntStream.range(0, N)
		.boxed()
		.min( (i1, i2) -> {
			return violatedConstraintsCount(col,i1) - violatedConstraintsCount(col,i2);
		}).get();		
		dna[col] = value;
		if ( savedValue == value)
			return false;
		return true;
	}
	
	boolean isOk()
	{
		int result = (int)IntStream.range(0, N)
				.boxed()
				.parallel()
				.filter(i -> violatedConstraintsCount(i, dna[i]) > 0)
				.count();
		return result > 0;
	}
	int violatedConstraintsCount(int col, int row)
	{
		int N = dna.length;

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
}
