import java.util.ArrayList;

public class Chessboard {

	//rows as variables, columns as values
	private Row[] rows;
	private int size;
	
	Chessboard(int size)
	{
		this.size=size;
		rows = new Row[size];
		initChessboard();
	}
	
	public Row[] getRows()
	{
		return rows;
	}
	
	public int getSize()
	{
		return size;
	}
	
	private void initChessboard()
	{
		ArrayList<Integer>domain=new ArrayList<Integer>();
		for(int i=0; i<size; i++)
			domain.add(i);
		
		for(int i=0; i<size; i++)
			rows[i]=new Row(i, new ArrayList<Integer>(domain));
	}
	
	public ArrayList<Row> notAssignedRows()
	{
		ArrayList<Row>notAssigned = new ArrayList<Row>();
		for(int i=0; i<size; i++)
		{
			if(rows[i].getColumn()==-1)
				notAssigned.add(rows[i]);
		}
		
		return notAssigned;
	}
	
	@Override
	public String toString()
	{
		String result="{";
		for(int i=0; i<size-1; i++)
			result+=rows[i].getColumn()+", ";
		
		result+=rows[size-1].getColumn()+"}";
		
		return result;
	}
	
}
