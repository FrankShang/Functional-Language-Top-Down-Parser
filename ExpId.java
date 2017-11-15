class ExpId extends Exp
{
	String id;

	ExpId(String id)
	{
		this.id = id;
	}

	void printParseTree(String indent)
	{
		super.printParseTreeHeader(indent);
		IO.displayln((indent += " ") + indent.length() + " " + id);
	}
}