
public class Header
{
	String type;
	String id;
	ParameterList pl;

	Header(String type, String id, ParameterList pl)
	{
		this.type = type;
		this.id = id;
		this.pl = pl;
	}

	void printParseTree(String indent)
	{
		String indent1 = indent + " ";
		IO.displayln(indent + indent.length() + " <header>");
		IO.displayln(indent1 + indent1.length() + " <type>" + type);
		IO.displayln(indent1 + indent1.length() + " <fun name>" + id);
		if(pl != null)
		pl.printParseTree(indent + " ");
	}
}
