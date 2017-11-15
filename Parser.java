
/**
 * @author frankshang
This class is a top-down, recursive-descent parser for the following syntactic categories

⟨fun def list⟩ → ⟨fun def⟩ | ⟨fun def⟩ ⟨fun def list⟩ 
⟨fun def⟩ → ⟨header⟩ = ⟨exp⟩ 
⟨header⟩ → ⟨type⟩ ⟨fun name⟩ ⟨parameter list⟩ 
⟨type⟩ → "int" | "float" | "boolean" 
⟨fun name⟩ → ⟨id⟩ 
⟨parameter list⟩ → ε | "(" ⟨parameters⟩ ")" 
⟨parameters⟩ → ⟨parameter⟩ | ⟨parameter⟩ "," ⟨parameters⟩ 
⟨parameter⟩ → ⟨type⟩ ⟨id⟩ 
⟨exp⟩ → ⟨id⟩ | ⟨int⟩ | ⟨float⟩ | ⟨floatE⟩ | ⟨boolLiteral⟩ | "(" ⟨fun exp⟩ ")" | "if" ⟨exp⟩ "then" ⟨exp⟩ "else" ⟨exp⟩ 
⟨boolLiteral⟩ → "false" | "true" 
⟨fun exp⟩ → ⟨fun op⟩ ⟨exp list⟩ 
⟨exp list⟩ → ε | ⟨exp⟩ ⟨exp list⟩ 
⟨fun op⟩ → ⟨fun name⟩ | ⟨arith op⟩ | ⟨bool op⟩ | ⟨comp op⟩ 
⟨arith op⟩ → + | − | * | / 
⟨bool op⟩ → "and" | "or" | "not" 
⟨comp op⟩ → "<" | "<=" | ">" | ">=" | "=" 
 
 The definition of the tokens are given in the lexical analyzer class file "KexArithArray.java".
 
The following variables/functions of "IO"/"LexArithArray" classes are used:

static void display(String s)
static void displayln(String s)
static void setIO(String inFile, String outFile)
static void closeIO()

static void setLex()
static String t // holds an extracted token
static State state // the current state of the finite automaton
static int getToken() // extracts the next token

An explicit parse tree is constructed in the form of nested 
 */
import java.util.*;

//import LexArithArray.State;

public abstract class Parser extends LexArithArray
{
	static boolean errorFound = false;

	public static FunDefList funDefList()
	{
		// ⟨fun def list⟩ → ⟨fun def⟩ | ⟨fun def⟩ ⟨fun def list⟩
		FunDef funDef = funDef();
		if (state == State.Keyword_int || state == State.Keyword_float || state == State.Keyword_boolean)
		{
			FunDefList funDefList = funDefList();
			return new MultipleFunDef(funDef, funDefList);
		} else
		{
			return funDef;
		}
	}

	public static FunDef funDef()
	{
		// ⟨fun def⟩ → ⟨header⟩ = ⟨exp⟩
		if (state == State.Keyword_int || state == State.Keyword_float || state == State.Keyword_boolean)
		{
			Header header = header();
			getToken();
			if (state == State.Id || state == State.Int || state == State.Float || state == State.FloatE
					|| state == State.Keyword_false || state == State.Keyword_true || state == State.LParen
					|| state == State.Keyword_if)
			{
				Exp exp = exp();
				return new FunDef(header, exp);
			} else
				errorMsg(8);

		} else
			errorMsg(3);// " keyword int float boolean expected"
		return null;
	}

	public static Header header()
	{
		// ⟨header⟩ → ⟨type⟩ ⟨fun name⟩ ⟨parameter list⟩
		// ⟨type⟩ → "int" | "float" | "boolean"
		// ⟨fun name⟩ → ⟨id⟩
		if (state == State.Keyword_int || state == State.Keyword_float || state == State.Keyword_boolean)
		{
			String type = t;
			getToken();
			if (state == State.Id)
			{
				String funName = t;
				getToken();
				ParameterList parameterList = parameterList();
				if (state == State.Assign)
				{
					return new Header(type, funName, parameterList);
				} else
					errorMsg(2);// = expected
			} else
				errorMsg(4);// id expected
		} else
			errorMsg(3);
		return null;
	}

	public static ParameterList parameterList()
	{
		// ⟨parameter list⟩ → ε | "(" ⟨parameters⟩ ")"
		if (state == State.Assign)
		{
			return null;
		} else if (state == State.LParen)
		{
			getToken();
			Parameters parameters = parameters();
			if (state == State.RParen)
			{
				getToken();
				ParenthesizedParameters parenthesizedParameters = new ParenthesizedParameters(parameters);
				return parenthesizedParameters;
			} else
				errorMsg(6); // ) expected
		} else
			errorMsg(9);// = ( expected
		return null;
	}

	public static Parameters parameters()
	{
		// ⟨parameters⟩ → ⟨parameter⟩ | ⟨parameter⟩ "," ⟨parameters⟩
		Parameter parameter = parameter();
		if (state == State.Comma)
		{
			getToken();
			Parameters parameters = parameters();
			return new ParametersWithComma(parameter, parameters);
		} else

			return parameter;
	}

	public static Parameter parameter()
	{
		// ⟨parameter⟩ → ⟨type⟩ ⟨id⟩
		if (state == State.Keyword_int || state == State.Keyword_float || state == State.Keyword_boolean)
		{
			String type = t;
			getToken();
			if (state == State.Id)
			{
				String id = t;
				getToken();
				return new Parameter(type, id);
			} else
				errorMsg(4);// id expected
		} else
			errorMsg(3);// int, float, boolean expected
		return null;
	}

	public static Exp exp()
	{
		// ⟨exp⟩ → ⟨id⟩ | ⟨int⟩ | ⟨float⟩ | ⟨floatE⟩ | ⟨boolLiteral⟩ |
		// "(" ⟨fun exp⟩ ")" | "if" ⟨exp⟩ "then" ⟨exp⟩ "else" ⟨exp⟩
		switch (state)
		{
		case Id:
			ExpId id = new ExpId(t);
			getToken();
			return id;

		case Int:
			ExpInt intElem = new ExpInt(Integer.parseInt(t));
			getToken();
			return intElem;

		case Float:
		case FloatE:
			ExpFloat floatElem = new ExpFloat(Float.parseFloat(t));
			getToken();
			return floatElem;

		case Keyword_true:
		case Keyword_false:
			ExpBoolLiteral boolElem = new ExpBoolLiteral(Boolean.parseBoolean(t));
			getToken();
			return boolElem;
		case LParen:
			getToken();
			FunExp fe = funExp();
			if (state == State.RParen)
			{
				getToken();
				ParenthesizedFunExp paren = new ParenthesizedFunExp(fe);
				return paren;
			} else
			{
				errorMsg(6); // ) expected
				return null;
			}
		case Keyword_if:
			getToken();
			Exp e1 = exp();
			if (state == State.Keyword_then)
			{
				getToken();
				Exp e2 = exp();
				if (state == State.Keyword_else)
				{
					getToken();
					Exp e3 = exp();
					ExpIfThenElse expIfThenElse = new ExpIfThenElse(e1, e2, e3);
					return expIfThenElse;
				} else
				{
					errorMsg(12);// if then else expected
					return null;
				}
			} else
			{
				errorMsg(12);// if then else expected
				return null;
			}

		default:
			errorMsg(8);// id, int,float, true ,if or ( expected
			return null;
		}
	}

	public static FunExp funExp()
	{
		// ⟨fun exp⟩ → ⟨fun op⟩ ⟨exp list⟩
		// ⟨fun op⟩ → ⟨fun name⟩ | ⟨arith op⟩ | ⟨bool op⟩ | ⟨comp op⟩
		// ⟨arith op⟩ → + | − | * | /
		// ⟨bool op⟩ → "and" | "or" | "not"
		// ⟨comp op⟩ → "<" | "<=" | ">" | ">=" | "="
		if (boolSwitchFunOp(state))
		{
			String s = t;
			getToken();

			if (state == State.RParen)
			{
				return new FunExp(s, null);
			} else if (state == State.LParen || state == State.Id || state == State.Int || state == State.Float
					|| state == State.FloatE)
			{
				ExpList expList = expList();
				return new FunExp(s, expList);
			} else
				errorMsg(11);// ( or ) expected

		} else
			errorMsg(10);// + - * / and or not < <= > >= = expected

		return null;
	}

	public static ExpList expList()
	{
		// ⟨exp list⟩ → ε | ⟨exp⟩ ⟨exp list⟩
		Exp exp = exp();

		if (state == State.LParen || state == State.Id || state == State.Int || state == State.Float
				|| state == State.FloatE)
		{
			ExpList expList = expList();
			return new MultipleExp(exp, expList);
		} else
		{
			return new SingleExp(exp);
		}
	}

	public static boolean boolSwitchFunOp(State s)
	{
		switch (s)
		{
		case Id:
		case Plus:
		case Minus:
		case Times:
		case Div:
		case Keyword_and:
		case Keyword_or:
		case Keyword_not:
		case Lt:
		case Le:
		case Gt:
		case Ge:
		case Assign:
			return true;
		default:
			return false;
		}

	}

	public static void errorMsg(int i)
	{
		errorFound = true;

		display(t + " : Syntax Error, unexpected symbol where");

		switch (i)
		{
		case 1:
			displayln(" id , int, float, true, false, ) expected");
			return;
		case 2:
			displayln(" = expcted");
			return;
		case 3:
			displayln(" int, float, boolean expected");
			return;
		case 4:
			displayln(" id expected");
			return;
		case 5:
			displayln(" ( expected");
			return;
		case 6:
			displayln(" ) expected");
			return;
		case 7:
			displayln(" ,  ) expected");
			return;
		case 8:
			displayln("id, int,float, true ,if or ( expected");
			return;
		case 9:
			displayln("= ( expected");
			return;
		case 10:
			displayln(" + - * / and or not < <= > >= = expected");
			return;
		case 11:
			displayln(" (  or  ) expected");
			return;
		case 12:
			displayln(" if then else expected");
			return;

		}
	}

	public static void main(String argv[])
	{
		// argv[0]: input file containing an assignment list
		// argv[1]: output file displaying the parse tree

		setIO(argv[0], argv[1]);
		setLex();

		getToken();

		FunDefList funDefList = funDefList();// build a parse tree

		if (!t.isEmpty())
			errorMsg(4);
		else if (!errorFound)
			funDefList.printParseTree(""); // print the parse tree in linearly
											// indented form in argv[1] file

		closeIO();
	}
}
