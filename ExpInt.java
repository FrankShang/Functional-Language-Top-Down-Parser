class ExpInt extends Exp
{
	int i;

	ExpInt(int i)
	{
		this.i = i;
	}

	@Override
	void printParseTree(String indent)
	{
		super.printParseTreeHeader(indent);
		IO.displayln((indent += " ") + indent.length() + " " + i);
	}
}