package gameoflogic;

/**
  * Base class for composite and atomic propositions.
  */
public abstract class Proposition
{
	public static final int TRUE = 0;
	public static final int UNDECIDED = 1;
	public static final int FALSE = 2;

	public Proposition()
	{
		super();
	}
	/// Get a printable form of the proposition
	public abstract String getStatement();
	/// Get the negation of the proposition
	public abstract String getNegStatement();
	/// Get status of proposition
	public abstract int status();
	/// Assign a status to a proposition. Returns true
	/// if assignment is consistent.
	public abstract boolean setStatus(int status);
	public abstract boolean isConsistent();
}


