class ExpBoolLiteral extends Exp
{
	boolean keyword;

	ExpBoolLiteral(boolean k)
	{
		keyword = k;
	}

	void printParseTree(String indent)
	{
		super.printParseTreeHeader(indent);
		IO.displayln((indent += " ") + indent.length() + " " + keyword);
	}
}