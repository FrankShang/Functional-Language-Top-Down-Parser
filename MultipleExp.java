class MultipleExp extends ExpList
{
	Exp exp;
	ExpList expList;

	MultipleExp(Exp e, ExpList el)
	{
		this.exp = e;
		this.expList = el;
	}

	void printParseTree(String indent)
	{	
		exp.printParseTree(indent);
		expList.printParseTree(indent);
	}
}