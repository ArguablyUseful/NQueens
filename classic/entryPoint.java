package classic;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class entryPoint {

	
	
	public static void searchNQueenForN_StreamVersion(int N)
	{
		NQueens board = new NQueens(N);
		if ( board.StreamSearch(0) )
		{
			//solution
			System.out.println(java.util.Arrays.toString(board.cols));
		} else
		{
			//no solutions
			System.out.println("I have something to tell you but first promise me you won't get mad.");
		}
	}
	public static void searchNQueenForN_NoStreamVersion(int N)
	{
		NQueens board = new NQueens(N);
		if ( board.Search(0) )
		{
			//solution
			System.out.println(java.util.Arrays.toString(board.cols));
		} else
		{
			//no solutions
			System.out.println("I have something to tell you but first promise me you won't get mad.");
		}
	}
	public static void main(String args[])
	{
		int min = 8;
		int max = 34;
		for ( int N = min; N <= max; N++)
		{
			long before = System.currentTimeMillis();
			searchNQueenForN_StreamVersion(N);
			long after = System.currentTimeMillis();
			System.out.println("Stream version =--> " + N + " queens took " + (after-before) + " ms");
			
			before = System.currentTimeMillis();
			searchNQueenForN_NoStreamVersion(N);
			after = System.currentTimeMillis();
			System.out.println("No Stream version =--> " + N + " queens took " + (after-before) + " ms");
		}
		System.out.println("end");
		
	}
}
