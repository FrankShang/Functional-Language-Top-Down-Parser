class Parameter extends Parameters
{
	String type;
	String id;

	Parameter(String t, String id)
	{
		this.type = t;
		this.id = id;
	}

	void printParseTree(String indent)
	{
		IO.displayln(indent + indent.length() + " <parameter> " + type + " " + id);
	}
}