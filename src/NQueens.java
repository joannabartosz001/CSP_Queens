import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NQueens {

	private int n;
	private int solutions_number;
	private long visited_nodes_number;
	
	NQueens(int n)
	{
		this.n=n;
		solutions_number=0;
		visited_nodes_number=0;
	}
	
	public int getN()
	{
		return n;
	}
	
	public int getSolutionsNumber()
	{
		return solutions_number;
	}
	
	public long getVisitedNodesNumber()
	{
		return visited_nodes_number;
	}
	
	public void startBacktracking(boolean var_heuristic, boolean val_heuristic)
	{
		backtracking(new Chessboard(n), var_heuristic, val_heuristic);
	}
	
	private void backtracking(Chessboard board, boolean var_heuristic, boolean val_heuristic)
	{
		ArrayList<Row>to_assign=board.notAssignedRows();
		if(to_assign.size()==0)
			solutions_number++;
		else
		{
			Row var = getNextToAssign(false, board, to_assign, var_heuristic);
			ArrayList<Integer>domain = orderValues(var.getDomain(), val_heuristic);
			for(int i=0; i<domain.size(); i++)
			{
				visited_nodes_number++;
				if(check(domain.get(i), var, board, var_heuristic))
				{
					var.setColumn(domain.get(i));
					backtracking(board, var_heuristic, val_heuristic);
					var.eraseColumn();
				}
			}
		}
	}
	
	public void startForward(boolean var_heuristic, boolean val_heuristic)
	{
		forwardChecking(new Chessboard(n), var_heuristic, val_heuristic);
	}
	
	private void forwardChecking(Chessboard board, boolean var_heuristic, boolean val_heuristic)
	{
		ArrayList<Row>to_assign=board.notAssignedRows();
		if(to_assign.size()==0)
			solutions_number++;
		else
		{
			Row var = getNextToAssign(true, board, to_assign, var_heuristic);
			ArrayList<Integer>domain = orderValues(var.getDomain(), val_heuristic);
			for(int i=0; i<domain.size(); i++)
			{
				visited_nodes_number++;
				Map<Integer, ArrayList<Row>> changes = new HashMap<Integer, ArrayList<Row>>();
				if(deleteFromDomains(board, var, domain.get(i), changes))
				{
					var.setColumn(domain.get(i));
					forwardChecking(board, var_heuristic, val_heuristic);
					var.eraseColumn();
				}
				
				undoChanges(changes);
			}
		}
	}
	
	private Row getNextToAssign(boolean if_forward, Chessboard board, List<Row> to_assign, boolean heuristic)
	{
		Row result;
		
		if(heuristic&&if_forward)
		{
			result = to_assign.get(0);
			for(Row r : to_assign)
			{
				if(r.getDomain().size()<result.getDomain().size())
					result=r;		
			}
			return result;
		}
		
		else if(heuristic)
		{
			result = to_assign.get(0);
			for(Row r : to_assign)
			{
				if(r.getRowNr()<result.getRowNr())
					result=r;		
			}
			return result;
		}
		
		else
		{
			Random rand = new Random();
			return to_assign.get(rand.nextInt(to_assign.size()));
		}
	}
	
	private boolean check(int col, Row row, Chessboard board, boolean heuristic)
	{
		Row[] rows = board.getRows();
		for(int i=0; i<board.getSize(); i++)
		{
			if(i!= row.getRowNr() && rows[i].getColumn()==col)
				return false;
		}
		
		//up and left
		for(int i=row.getRowNr(), j=col; i>=0 && j>=0; i--, j--)
		{
			if(rows[i].getColumn()==j)
				return false;
		}
		
		//up and right
		for(int i=row.getRowNr(), j=col; i>=0 && j<board.getSize(); i--, j++)
		{
			if(rows[i].getColumn()==j)
				return false;
		}
		
		if(!heuristic)
		{
			//down and left
			for(int i=row.getRowNr(), j=col; i<board.getSize() && j>=0; i++, j--)
			{
				if(rows[i].getColumn()==j)
					return false;
			}
			
			//down and right
			for(int i=row.getRowNr(), j=col; i<board.getSize() && j<board.getSize(); i++, j++)
			{
				if(rows[i].getColumn()==j)
					return false;
			}
		}
		
		return true;
	}
	
	private ArrayList<Integer> orderValues(ArrayList<Integer> domain, boolean val_heuristic)
	{
		if(!val_heuristic)
			return domain;
		else
		{
			Comparator<Integer> comp= new Comparator<Integer>()
					{
						public int compare(Integer first, Integer second)
						{
							int x1 = n-1-first<first ? n-1-first : first;
							int x2 = n-1-second<second ? n-1-second : second;
							return ((Integer)x1).compareTo((Integer)x2);
							
						}
					};
			ArrayList<Integer> sorted = new ArrayList<Integer>(domain);
			Collections.sort(sorted, comp);
			return sorted;
		}
		
	}
	
	
	private boolean deleteFromDomains(Chessboard board, Row row, int col, Map<Integer, ArrayList<Row>> changes)
	{
		Row[] rows = board.getRows();
		
		ArrayList<Row> temp = new ArrayList<Row>();
		changes.put(col, temp);
		
		for(int i=0; i<board.getSize(); i++)
		{
			if(rows[i]!=row && rows[i].getColumn()==-1 && rows[i].getDomain().contains((Integer)col))
			{
				rows[i].getDomain().remove((Integer)col);
				changes.get(col).add(rows[i]);
				if(rows[i].getDomain().size()==0)
					return false;
			}
		}
		
		//up and left
		for(int i=row.getRowNr()-1, j=col-1; i>=0 && j>=0; i--, j--)
		{
			if(rows[i].getColumn()==-1 && rows[i].getDomain().contains((Integer)j))
			{
				rows[i].getDomain().remove((Integer)j);
				
				if(changes.containsKey(j))
					changes.get(j).add(rows[i]);
				else
				{
					ArrayList<Row> temp2 = new ArrayList<Row>();
					temp2.add(rows[i]);
					changes.put(j, temp2);
				}
				
				if(rows[i].getDomain().size()==0)
					return false;
			}		
		}
		
		//up and right
		for(int i=row.getRowNr()-1, j=col+1; i>=0 && j<board.getSize(); i--, j++)
		{
			if(rows[i].getColumn()==-1 && rows[i].getDomain().contains((Integer)j))
			{
				rows[i].getDomain().remove((Integer)j);
				
				if(changes.containsKey(j))
					changes.get(j).add(rows[i]);
				else
				{
					ArrayList<Row> temp2 = new ArrayList<Row>();
					temp2.add(rows[i]);
					changes.put(j, temp2);
				}
			
				if(rows[i].getDomain().size()==0)
					return false;
			}		
		}
		
		//down and left
		for(int i=row.getRowNr()+1, j=col-1; i<board.getSize() && j>=0; i++, j--)
		{
			if(rows[i].getColumn()==-1 && rows[i].getDomain().contains((Integer)j))
			{
				rows[i].getDomain().remove((Integer)j);
				
				if(changes.containsKey(j))
					changes.get(j).add(rows[i]);
				else
				{
					ArrayList<Row> temp2 = new ArrayList<Row>();
					temp2.add(rows[i]);
					changes.put(j, temp2);
				}
			
				if(rows[i].getDomain().size()==0)
					return false;
			}		
		}
		
		//down and right
		for(int i=row.getRowNr()+1, j=col+1; i<board.getSize() && j<board.getSize(); i++, j++)
		{
			if(rows[i].getColumn()==-1 && rows[i].getDomain().contains((Integer)j))
			{
				rows[i].getDomain().remove((Integer)j);
				
				if(changes.containsKey(j))
					changes.get(j).add(rows[i]);
				else
				{
					ArrayList<Row> temp2 = new ArrayList<Row>();
					temp2.add(rows[i]);
					changes.put(j, temp2);
				}
			
				if(rows[i].getDomain().size()==0)
					return false;
			}		
		}
		return true;	
	}
	
	private void undoChanges(Map<Integer, ArrayList<Row>> changes)
	{
		for(Map.Entry<Integer, ArrayList<Row>> entry : changes.entrySet())
		{
			for(Row r: entry.getValue())
			{
				if(!r.getDomain().contains(entry.getKey()))
					r.getDomain().add(entry.getKey());
			}
		}
	}
}
