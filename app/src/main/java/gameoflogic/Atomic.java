package gameoflogic;

/**
  * An atomic proposition.
  */
public class Atomic
	extends Proposition
{
	public Atomic(String statement, String negation_statement)
	{
		super();
		this.statement = statement;
		this.negation_statement = negation_statement;
		this.status = UNDECIDED;
	}
	public String getStatement() { return statement; }
	public String getNegStatement() { return negation_statement; }
	public boolean isConsistent() { return true; }
	public int status() { return status; }
	public boolean setStatus(int status)
	{
		if (this.status != UNDECIDED && status != this.status)
			return false;
		this.status = status;
		return true;
	}

	final private String statement, negation_statement;
	private int status;
}
