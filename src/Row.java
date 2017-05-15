import java.util.ArrayList;

public class Row {

	private int row_nr;
	private int column;
	private ArrayList<Integer> domain;
	
	Row(int nr, ArrayList<Integer> dom)
	{
		row_nr=nr;
		domain=dom;
		column=-1;
	}
	
	public int getRowNr()
	{
		return row_nr;
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public void setColumn(int col)
	{
		column=col;
	}
	
	public void eraseColumn()
	{
		setColumn(-1);
	}
	
	public ArrayList<Integer> getDomain()
	{
		return domain;
	}
}
