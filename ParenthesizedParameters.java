class ParenthesizedParameters extends ParameterList
{
	Parameters parameters;

	ParenthesizedParameters(Parameters ps)
	{
		parameters = ps;
	}

	void printParseTree(String indent)
	{
		super.printParseTreeHeader(indent);
		parameters.printParseTree(indent + " ");
	}
}