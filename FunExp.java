class FunExp
{
	String f;
	ExpList e;

	FunExp(String f, ExpList e)
	{
		this.f = f;
		this.e = e;
	}

	void printParseTree(String indent){
      IO.displayln(indent + indent.length() + " <Fun exp>");
      IO.displayln((indent += " ") + indent.length() + " " + f); 
      if(e!=null)
      e.printParseTree(indent);
   }
}