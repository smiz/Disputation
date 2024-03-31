package gameoflogic;

/**
  * A compound proposition consisting of two propositions.
  */
public class Compound
	extends Proposition
{

	public static final int OR = 0;
	public static final int AND = 1;
	public static final int IF_THEN = 2;

	final private Proposition model;
	final private Proposition A, B;

	public Proposition getA() { return A; }
	public Proposition getB() { return B; }

	public Compound(Proposition A, Proposition B, int operator)
	{
		super();
		if (operator == OR)
			model = new CompoundOR(A,B);
		else if (operator == AND)
			model = new CompoundAND(A,B);
		else
			model = new CompoundIfThen(A,B);
		this.A = A;
		this.B = B;
	}

	public boolean isConsistent()
	{
		return model.isConsistent();
	}

	public boolean setStatus(int status)
	{
		return model.setStatus(status);
	}

	public int status()
	{
		return model.status();
	}

	public String getStatement()
	{
		return model.getStatement();
	}

	public String getNegStatement()
	{
		return model.getNegStatement();
	}
}
