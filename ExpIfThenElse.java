class ExpIfThenElse extends Exp
{
	Exp e1;
	Exp e2;
	Exp e3;

	ExpIfThenElse(Exp e1, Exp e2, Exp e3)
	{
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
	}

	void printParseTree(String indent)
	{   String indent1 = indent + " ";
		super.printParseTreeHeader(indent);
		IO.displayln(indent1+ indent1.length() + " if");
		e1.printParseTree(indent1 + " ");
		IO.displayln(indent1+ indent1.length() + " then");
		e2.printParseTree(indent1 + " ");
		IO.displayln(indent1+ indent1.length()+ " else");
		e3.printParseTree(indent1 + " ");
	}

}