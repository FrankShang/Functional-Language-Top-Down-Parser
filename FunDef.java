
public class FunDef extends FunDefList 
{
	Header header;// variable on the left side 
	Exp exp;
	
	FunDef(Header h, Exp e){
		header = h;
		exp = e;
	}
	void printParseTree(String indent){
		IO.displayln(indent + indent.length() + " <fun def>");
		header.printParseTree(indent + " ");
		exp.printParseTree(indent + " ");
		IO.displayln("");
		IO.displayln("------------------------");
		IO.displayln("");
	}

}
