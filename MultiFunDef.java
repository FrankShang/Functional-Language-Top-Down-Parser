class MultipleFunDef extends FunDefList
{
	FunDef funDef;
	FunDefList funDefList;

	MultipleFunDef(FunDef fd, FunDefList fdl){
        this.funDef = fd;
        this.funDefList = fdl;
    }

	void printParseTree(String indent)
	{
		funDef.printParseTree(indent);
		funDefList.printParseTree(indent);
	}
}