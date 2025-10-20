StringTokenizer st = new StringTokenizer( "75+9+36=10" , "\\+" );
		PrintStream ps = new PrintStream(System.out);
		
		ps.println( st.countTokens() ); // 3
		while( st.hasMoreTokens() )
		{
			ps.println( st.nextToken() );   // el texto en si como STRING
		}
public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_MAGENTA = "\u0033[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_
RESET = "\u001B[0m";
