package gameoflogic;

/**
  * A compound proposition consisting of two propositions.
  */
public class CompoundAND
	extends Proposition
{

	public CompoundAND(Proposition A, Proposition B)
	{
		super();
		this.A = A;
		this.B = B;
		this.assigned_status = UNDECIDED;
	}

	public boolean isConsistent()
	{
		if (assigned_status == UNDECIDED)
			return true;
		// Both must be true if assigned status is true
		if (assigned_status == TRUE)
		{
			if (!A.setStatus(assigned_status)) return false;
			return B.setStatus(assigned_status);
		}
		// At least one must be false or undecided if assigned status is false
		else
		{
			if (A.status() == TRUE) return B.setStatus(FALSE);
			if (B.status() == TRUE) return A.setStatus(FALSE);
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
		if (A_status == TRUE && B_status == TRUE)
			return TRUE;
		else if (A_status == FALSE || B_status == FALSE)
			return FALSE;
		return UNDECIDED;
	}

	public String getStatement()
	{
		return String.format(Strings.locale,"%s %s %s",A.getStatement(),Strings.AND,B.getStatement());
	}

	public String getNegStatement()
	{
		return String.format(Strings.locale,"%s %s %s",A.getNegStatement(),Strings.OR,B.getNegStatement());
	}

	final private Proposition A, B;
	private int assigned_status;
}
