package gameoflogic;

/**
  * A compound proposition consisting of two propositions.
  */
public class CompoundOR
	extends Proposition
{

	public CompoundOR(Proposition A, Proposition B)
	{
		super();
		this.A = A;
		this.B = B;
		assigned_status = UNDECIDED;
	}

	public boolean isConsistent()
	{
		if (assigned_status == UNDECIDED)
			return true;
		// If one term is false, then the other must be true
		// for the statement to be true
		if (assigned_status == TRUE)
		{
			// B must be true
			if (A.status() == FALSE)
				return B.setStatus(TRUE);
			// A must be true
			if (B.status() == FALSE)
				return A.setStatus(TRUE);
		}
		// If the sentence is false, both terms must be false
		else
		{
			if (!A.setStatus(FALSE)) return false;
			return B.setStatus(FALSE);
		}
		// Both undecided doesn't present a problem
		return true;

	}
	public boolean setStatus(int status)
	{
		if (assigned_status == UNDECIDED || assigned_status == status)
		{
			assigned_status = status;
			return isConsistent();
		}
		return false;
	}

	public int status()
	{
		int A_status = A.status();
		int B_status = B.status();
		if (A_status == TRUE || B_status == TRUE)
			return TRUE;
		else if (A_status == FALSE && B_status == FALSE)
			return FALSE;
		return UNDECIDED;
	}

	public String getStatement()
	{
		return String.format(Strings.locale,"%s %s %s",A.getStatement(),Strings.OR,B.getStatement());
	}

	public String getNegStatement()
	{
		return String.format(Strings.locale,"%s %s %s",A.getNegStatement(),Strings.AND,B.getNegStatement());
	}

	final private Proposition A, B;
	private int assigned_status;
}
