class ParenthesizedFunExp extends Exp
{
	//FunExp fe;
	FunExp fe;
	ParenthesizedFunExp(FunExp fe)
	{
		this.fe = fe;
	}

	void printParseTree(String indent)
	{
		super.printParseTreeHeader(indent);
		if(fe != null)
		fe.printParseTree(indent + " ");
	}
}