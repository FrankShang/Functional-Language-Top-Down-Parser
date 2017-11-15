/**
 
This class is a lexical analyzer for the tokens defined by the grammar:

<plus> --> +
<minus> --> -
<times> --> *
<div> --> /
<LParen> --> "("
<RParen> --> ")"
<assign> --> "="
<semicolon> --> ";"
<int> --> { <digit> }+
<id> --> <letter> { <letter> | <digit> }
<float> --> { <digit> }+ "." { <digit> }+
<floatE> --> <float> (E|e) [+|-] { <digit> }+

This class implements a DFA that will accept the above tokens.

The DFA states are represented by the Enum type "State".
The DFA has the following 12 final states represented by enum-type literals:

state     token accepted

Id        identifiers
Int       integers
Float     floats without exponentiation part
FloatE    floats with exponentiation part
Plus      +
Minus     -
Times     *
Div       /
LParen    (
RParen    )
Assign    =
Semicolon ;

The DFA also uses the following 4 non-final states:

state      string recognized

Start      the empty string
Period     float parts ending with "."
E          float parts ending with E or e
EPlusMinus float parts ending with + or - in exponentiation part

The function "driver" operates the DFA. 
The array "nextState" returns the next state given the current state and the input character.

To recognize a different token set, modify the following:

  enum type "State" and function "isFinal"
  size of array "nextState"
  function "setNextState" 

The functions "driver", "getToken", "setLex" remain the same.

**/
import java.util.*;

public abstract class LexArithArray extends IO
{
//	public enum State 
//       	{ 
//	  // non-final states     ordinal number
//
//		Start,             // 0
//		Period,            // 1
//		E,                 // 2
//		EPlusMinus,        // 3
//
//	  // final states
//
//		Id,                // 4
//		Int,               // 5
//		Float,             // 6
//		FloatE,            // 7
//		Plus,              // 8
//		Minus,             // 9
//		Times,             // 10
//		Div,               // 11
//		LParen,            // 12
//		RParen,            // 13
//		Assign,            // 14
//		Semicolon,         // 15
//
//		UNDEF;
//
//		private boolean isFinal()
//		{
//			return ( this.compareTo(State.Id) >= 0 );  
//		}	
//	}
	public enum State 
	{ 
	// final states    ordinal number  token accepted 

		Plus,            // 0          +
		Minus,           // 1          -
		Times,           // 2          *
		Div,             // 3          /
		Lt,              // 4          <
		Le,              // 5          <=
		Gt,              // 6          >
		Ge,              // 7          >=
		Assign,          // 8          =
		Id,              // 9          identifiers
		Int,             // 10         integers
		Float,           // 11         floats without exponentiation part
		FloatE,          // 12         floats with exponentiation part
		LParen,          // 13         (
		RParen,          // 14         )
		Comma,           // 15         ,
		//Assign,          // 16         =
		Semicolon,       // 17         ;
	// non-final states                string recognized    

		Start,           // 18      the empty string
		Period,          // 19      ".", "+.", "-."
		E,               // 20      float parts ending with E or e
		EPlusMinus,      // 21      float parts ending with + or - in exponentiation part
		UnderscoreMinus,      // 22      "_"

	// keyword states

		Keyword_int,
		Keyword_float,
		Keyword_boolean,
		Keyword_if,
		Keyword_then,
		Keyword_else,
		Keyword_and,
		Keyword_or,
		Keyword_not,
		Keyword_false,
		Keyword_true,

		UNDEF;

		boolean isFinal()
		{
			//return ( this.compareTo(State.Comma) <= 0 );  //change
			return ( this.compareTo(State.Semicolon) <= 0 ); 
		}

		boolean isCompOp()
		{
			return this.compareTo(Lt) >= 0 && this.compareTo(Assign) <= 0;
		}
	}
	// By enumerating the non-final states first and then the final states,
	// test for a final state can be done by testing if the state's ordinal number
	// is greater than or equal to that of Id.

	// The following variables of "IO" class are used:

	//   static int a; the current input character
	//   static char c; used to convert the variable "a" to the char type whenever necessary

	public static String t; // holds an extracted token
	public static State state; // the current state of the FA

	private static State nextState[][] = new State[22][128];
 
          // This array implements the state transition function State x (ASCII char set) --> State.
          // The state argument is converted to its ordinal number used as
          // the first array index from 0 through 22. 
	static HashMap<String, State> keywordMap = new HashMap<String , State>();
	
	private static void setKeywordMap()
	{
		keywordMap.put("int",     State.Keyword_int);
		keywordMap.put("float",   State.Keyword_float);
		keywordMap.put("boolean", State.Keyword_boolean);
		keywordMap.put("if",      State.Keyword_if);
		keywordMap.put("then",    State.Keyword_then);
		keywordMap.put("else",    State.Keyword_else);
		keywordMap.put("and",     State.Keyword_and);
		keywordMap.put("or",      State.Keyword_or);
		keywordMap.put("not",     State.Keyword_not);
		keywordMap.put("false",   State.Keyword_false);
		keywordMap.put("true",    State.Keyword_true);
	}

	private static int driver()

	// This is the driver of the FA. 
	// If a valid token is found, assigns it to "t" and returns 1.
	// If an invalid token is found, assigns it to "t" and returns 0.
	// If end-of-stream is reached without finding any non-whitespace character, returns -1.
	

	{
		State nextSt; // the next state of the FA

		t = "";
		state = State.Start;

		if ( Character.isWhitespace((char) a) )
			a = getChar(); // get the next non-whitespace character
		if ( a == -1 ) // end-of-stream is reached
			return -1;

		while ( a != -1 ) // do the body if "a" is not end-of-stream
		{
			c = (char) a;
			nextSt = nextState[state.ordinal()][a];
			if ( nextSt == State.UNDEF ) // The FA will halt.
			{
				if ( state.isFinal() )
					return 1; // valid token extracted
				else // "c" is an unexpected character
				{
					t = t+c;
					a = getNextChar();
					return 0; // invalid token found
				}
			}
			else // The FA will go on.
			{
				state = nextSt;
				t = t+c;
				a = getNextChar();
			}
		}

		// end-of-stream is reached while a token is being extracted

		if ( state.isFinal() )
			return 1; // valid token extracted
		else
			return 0; // invalid token found
	} // end driver

	public static void getToken()

	// Extract the next token using the driver of the FA.
	// If an invalid token is found, issue an error message.

	{
		int i = driver();
		if(state == State.Id)
			keywordCheck();
		else if ( i == 0 )
			displayln(t + " : Lexical Error, invalid token");
	}

	private static void setNextState()
	{
		for (int s = 0; s <= 21; s++ ) //change 15 ->21
			for (int c = 0; c <= 127; c++ )
				nextState[s][c] = State.UNDEF;

		for (char c = 'A'; c <= 'Z'; c++)
		{
			nextState[State.Start.ordinal()][c] = State.Id;
			nextState[State.Id   .ordinal()][c] = State.Id;
			nextState[State.UnderscoreMinus.ordinal()][c] = State.Id;
		}

		for (char c = 'a'; c <= 'z'; c++)
		{
			nextState[State.Start.ordinal()][c] = State.Id;
			nextState[State.Id   .ordinal()][c] = State.Id;
			nextState[State.UnderscoreMinus.ordinal()][c] = State.Id;
		}

		for (char d = '0'; d <= '9'; d++)
		{
			nextState[State.Start     .ordinal()][d] = State.Int;
			nextState[State.Id        .ordinal()][d] = State.Id;
			nextState[State.Int       .ordinal()][d] = State.Int;
			nextState[State.Plus       .ordinal()][d] = State.Int; //change
			nextState[State.Minus       .ordinal()][d] = State.Int;//change
			nextState[State.Period    .ordinal()][d] = State.Float;
			nextState[State.Float     .ordinal()][d] = State.Float;
			nextState[State.E         .ordinal()][d] = State.FloatE;
			nextState[State.EPlusMinus.ordinal()][d] = State.FloatE;
			nextState[State.FloatE    .ordinal()][d] = State.FloatE;
			nextState[State.UnderscoreMinus.ordinal()][d] = State.Id;
		}

		nextState[State.Start.ordinal()]['+'] = State.Plus;
		nextState[State.Start.ordinal()]['-'] = State.Minus;
		nextState[State.Start.ordinal()]['*'] = State.Times;
		nextState[State.Start.ordinal()]['/'] = State.Div;
		nextState[State.Start.ordinal()]['<'] = State.Lt;//change
		nextState[State.Start.ordinal()]['>'] = State.Gt;//change
		//nextState[State.Start.ordinal()]['='] = State.Eq;//change
		nextState[State.Start.ordinal()]['('] = State.LParen;
		nextState[State.Start.ordinal()][')'] = State.RParen;
		nextState[State.Start.ordinal()]['.'] = State.Period;
		nextState[State.Start.ordinal()][','] = State.Comma;//change
		
		nextState[State.Lt.ordinal()]['='] = State.Le;//change
		nextState[State.Gt.ordinal()]['='] = State.Ge;//change
		
		nextState[State.Plus.ordinal()]['.'] = State.Period;//change
		nextState[State.Minus.ordinal()]['.'] = State.Period;//change
		nextState[State.Int.ordinal()]['.'] = State.Float;//change
		
		nextState[State.Start.ordinal()]['='] = State.Assign;
		nextState[State.Start.ordinal()][';'] = State.Semicolon;
					
		nextState[State.Int.ordinal()]['.'] = State.Float;
			
		nextState[State.Float.ordinal()]['E'] = state.E;
		nextState[State.Float.ordinal()]['e'] = state.E;
		//nextState[State.Int  .ordinal()]['e'] = State.E;
		//nextState[State.Int  .ordinal()]['E'] = State.E;
			
		nextState[State.E.ordinal()]['+'] = State.EPlusMinus;
		nextState[State.E.ordinal()]['-'] = State.EPlusMinus;
		nextState[State.Id.ordinal()]['-'] = State.UnderscoreMinus;
		
		nextState[State.Id.ordinal()]['_'] = State.UnderscoreMinus;

	} // end setNextState
	private static void keywordCheck()
	{
		State keywordState = keywordMap.get(t);
		if ( keywordState != null ) // "t" has a keyword
			state = keywordState;
	}
	public static void setLex()

	// Sets the nextState array.

	{
		setNextState();
		setKeywordMap();
	}

	public static void main(String argv[])

	{		
		// argv[0]: input file containing source code using tokens defined above
		// argv[1]: output file displaying a list of the tokens 

		setIO( argv[0], argv[1] );
		setLex();
		
		int i;

		while ( a != -1 ) // while "a" is not end-of-stream
		{
			i = driver(); // extract the next token
			if ( i == 1 ){
				if (state == State.Id)
					keywordCheck();
				displayln( t+"   : "+state.toString() );
			}
				
			else if ( i == 0 )
				displayln( t+" : Lexical Error, invalid token");
		} 

		closeIO();
	}
} 

