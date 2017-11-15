class ExpFloat extends Exp
{
	float f;

	ExpFloat(float g)
	{
		this.f = g;
	}

	void printParseTree(String indent)
	{
		super.printParseTreeHeader(indent);
		IO.displayln((indent += " ") + indent.length() + " " + f);
	}
}