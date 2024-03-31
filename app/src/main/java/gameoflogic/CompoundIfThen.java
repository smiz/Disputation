package gameoflogic;

/**
  * A compound proposition consisting of two propositions.
  */
public class CompoundIfThen
	extends Proposition
{

	public CompoundIfThen(Proposition A, Proposition B)
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
		// To be consistent A->B must not be contradicted
		if (assigned_status == TRUE)
		{
			// A is true requires B to be true
			if (A.status() == TRUE)
				return B.setStatus(TRUE);
		}
		// If the sentence is false, then A is true and B is false
		else
		{
			return A.setStatus(TRUE) && B.setStatus(FALSE);
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
		if (B_status == TRUE || A_status == FALSE)
			return TRUE;
		else if (A_status == TRUE && B_status == FALSE)
			return FALSE;
		return UNDECIDED;
	}

	public String getStatement()
	{
		return String.format(Strings.locale,"%s %s %s %s",Strings.IF,A.getStatement(),Strings.THEN,B.getStatement());
	}

	public String getNegStatement()
	{
		return String.format(Strings.locale,"%s %s %s",B.getNegStatement(),Strings.AND,A.getStatement());
	}

	final private Proposition A, B;
	private int assigned_status;
}
