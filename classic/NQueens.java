package classic;
import java.util.Arrays;
import java.util.stream.IntStream;

public class NQueens {
	int N; // the size of the board
	int cols[]; //cols[i] indicate the row position of the queen in the i'th column
	boolean rows[]; // rows[i] indicate that a queen is covering the i'th row
	boolean TLBR[]; // (\) Top Left Bottom Right diagonal. TLBR[k] indicate that the k'th top to bottom diagonal (\) is covered by a queen
	boolean BLTR[]; // (/) Bottom Left Top Right Diagonal. BLTR[k] indicate that the k'th bottom to top diagonal (/) is covered by a queen
	/*
	 * note : the "k'th diagonal" must be understood correctly.
	 * each / diagonal can be described by the sum of the column index and the row index of each of its squares. 
	 * i.e the square of the diagonal at col=1,row=2 gives (1+2)=3. This "3" is the same for the square col=2,row=1 which is on the same diagonal
	 * each \ diagonal can be described as a constant based on the difference of cols and rows.
	 * i.e the square of the diagonal at col=1, row=1 gives (1-1)=0., which is the same as the diagonal at col=2, row=2 (2-2)=0
	 * because of the nature of array, index must be non-negative, so we always adds  "N-1" to the difference.
	*/
	public NQueens(int N)
	{
		this.N = N;
		this.cols = new int[N];
		cols = Arrays.stream(cols).parallel().map(i -> cols[i] = -1).toArray();
		this.rows = new boolean[N];
		this.TLBR = new boolean[N+N-1];
		this.BLTR = new boolean[N+N-1];
	}
	public boolean CheckFree(int column, int row)
	{
		//System.out.println(column + " ," + row);
		boolean noQueenInCurrentRow = !rows[row];
		int kTLBR = column - row + N - 1; // Top Left Bottom Right
		boolean noQueenInTLBR = !TLBR[kTLBR];
		int kBLTR = column + row;
		boolean noQueenInBLTR = !BLTR[kBLTR];
		return noQueenInCurrentRow && noQueenInTLBR && noQueenInBLTR;
	}
	public void SetQueen(int column, int row)
	{
		//System.out.println("set queen");
		cols[column] = row;
		rows[row] = true; // this queen now occupy this row
		// this queen now occupy those 2 diagonals
		int kTLBR = column - row + N - 1; // Top Left Bottom Right diagonal index
		int kBLTR = column + row; // Bottom Left Top Right diagonal index
		TLBR[kTLBR] = true; 
		BLTR[kBLTR] = true;
	}
	public void RemoveQueen(int column, int row)
	{
		//System.out.println("remove queen");
		cols[column] = -1;
		rows[row] = false; // this row is freed
		// this queen now free those 2 diagonals
		int kTLBR = column - row + N - 1; // Top Left Bottom Right diagonal index
		int kBLTR = column + row; // Bottom Left Top Right diagonal index
		TLBR[kTLBR] = false; 
		BLTR[kBLTR] = false;
	}
	public boolean StreamSearch(int currentColumn)
	{
		IntStream.range(0, N).
		filter(row -> CheckFree(currentColumn, row) && cols[currentColumn] == -1).
		peek(row -> SetQueen(currentColumn, row)).
		filter(row -> currentColumn < N-1 && !StreamSearch(currentColumn+1)).
		forEach(row -> RemoveQueen(currentColumn, row));		
		return cols[currentColumn] != -1 ;
	}
	public boolean Search(int currentColumn)
	{
		for(int currentRow = 0; currentRow < N && cols[currentColumn] == -1; currentRow++)
		{
			if ( CheckFree(currentColumn, currentRow))
			{
				SetQueen(currentColumn, currentRow);
				if ( currentColumn < N-1 && !Search(currentColumn+1))
					RemoveQueen(currentColumn, currentRow);//from here, there was no free position for the next columns. We backtrack.
			}
		}
		return cols[currentColumn] != -1 ;
	}
}
