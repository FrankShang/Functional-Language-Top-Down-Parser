class ParametersWithComma extends Parameters
{
	Parameter parameter;
	Parameters parameters;

	ParametersWithComma(Parameter p, Parameters ps)
	{
		this.parameter = p;
		this.parameters = ps;
	}

	void printParseTree(String indent)
	{
		parameter.printParseTree(indent);
		parameters.printParseTree(indent);
	}
}