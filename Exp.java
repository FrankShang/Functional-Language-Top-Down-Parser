abstract class Exp
{
	abstract void printParseTree(String indent);

	static void printParseTreeHeader(String indent)
	{
		IO.displayln(indent + indent.length() + " <exp>");
	}
}