
public class TestQueens {

	public static void main(String[]args)
	{
		
		NQueens n_queens_problem = new NQueens(10);
		
			
		//backtracking
		
		/*long time_start_back = System.currentTimeMillis();
		long time_start_back_nano = System.nanoTime();
		
		n_queens_problem.startBacktracking(true,true);
		
		long time_end_back = System.currentTimeMillis();
		long time_end_back_nano = System.nanoTime();
		
		System.out.println("Czas w ms: "+ (time_end_back-time_start_back));
		System.out.println("Czas w ns: "+ (time_end_back_nano-time_start_back_nano));*/
				
		
		//forward checking
		long time_start_forw = System.currentTimeMillis();
		long time_start_forw_nano = System.nanoTime();
		
		n_queens_problem.startForward(false, true);
		
		long time_end_forw = System.currentTimeMillis();
		long time_end_forw_nano = System.nanoTime();
		
		System.out.println("Czas w ms: "+ (time_end_forw-time_start_forw));
		System.out.println("Czas w ns: "+ (time_end_forw_nano-time_start_forw_nano));
		
		
		
		System.out.println("Odwiedzone wêz³y: "+n_queens_problem.getVisitedNodesNumber());
		System.out.println("Liczba rozwi¹zañ: "+n_queens_problem.getSolutionsNumber());
		
	}
	
}
