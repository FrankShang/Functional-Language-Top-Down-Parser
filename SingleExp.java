class SingleExp extends ExpList
{
	Exp e;

	SingleExp(Exp e)
	{
		this.e = e;
	}

	void printParseTree(String indent){
   // IO.displayln(indent + indent.length() + " <exp>");
    e.printParseTree(indent);
    }
}