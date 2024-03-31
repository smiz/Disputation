package gameoflogic;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Disputation
{

	private static class PresentedProposition
	{
		final Proposition p;
		final boolean was_negated;
		boolean reply;

		public PresentedProposition(Proposition p, boolean was_negated)
		{
			this.p = p;
			this.was_negated = was_negated;
		}
	}
	private final Random r = new Random();
	private final List<PresentedProposition> used = new ArrayList<>();
	private final List<Proposition> remaining = new ArrayList<>();
	private PresentedProposition contradiction = null;
	private Proposition presented_proposition = null;
	private boolean presented_negation = false;

	public Disputation(List<Atomic> domain)
	{
		super();
		for (int i = 0; i < domain.size(); i++)
		{
			remaining.add(domain.get(i));
			for (int j = i+1; j < domain.size(); j++)
			{
				remaining.add(new Compound(domain.get(i),domain.get(j),Compound.OR));
				remaining.add(new Compound(domain.get(i),domain.get(j),Compound.AND));
				remaining.add(new Compound(domain.get(i),domain.get(j),Compound.IF_THEN));
			}
		}
	}

	public int questionsRemaining()
	{
		return remaining.size();
	}

	// Add a new proposition to the disputation and return its statement.
	public String newProposition()
	{
		if (round == 0)
			presented_proposition = remaining.remove(0);
		else
			presented_proposition = remaining.remove(r.nextInt(remaining.size()));
		presented_negation = r.nextBoolean();
		used.add(new PresentedProposition(presented_proposition,presented_negation));
		if (presented_negation)
			return presented_proposition.getNegStatement();
		else
			return presented_proposition.getStatement();
	}

	private String disposition(int status)
	{
		if (status == Proposition.UNDECIDED) return Strings.UNDECIDED;
		else if (status == Proposition.TRUE) return Strings.TRUE;
		return Strings.FALSE;
	}
	public String [] describeContradiction()
	{
		int string_idx;
		String [] reply;
		int status = contradiction.p.status();
		if (contradiction.was_negated)
		{
			if (status == Proposition.TRUE)
				status = Proposition.FALSE;
			else
				status = Proposition.TRUE;
		}
		if (contradiction.p instanceof Compound)
		{
			string_idx = 3;
			reply = new String[3+used.size()+1];
			Compound prop = (Compound)(contradiction.p);
			reply[0] = String.format(Strings.locale,"%s %s %s",prop.getA().getStatement(),Strings.IS,disposition(prop.getA().status()));
			reply[1] = String.format(Strings.locale,"%s %s %s",prop.getB().getStatement(),Strings.IS,disposition(prop.getB().status()));
			reply[2] = String.format(Strings.locale,"%s %s %s %s",Strings.THEREFORE,
				((contradiction.was_negated) ? prop.getNegStatement() : prop.getStatement()),
				Strings.IS,disposition(status));
		}
		else
		{
			string_idx = 1;
			Atomic prop = (Atomic)(contradiction.p);
			String statement = 
				((contradiction.was_negated) ? prop.getNegStatement() : prop.getStatement());
			reply = new String[1+used.size()+1];
			reply[0] = String.format(Strings.locale,"'%s' %s %s",statement,Strings.IS,disposition(status));
		}
		reply[string_idx++] = Strings.PRESENT_WHOLE;
		for (PresentedProposition presented: used)
		{
			status = (presented.reply) ? Proposition.TRUE : Proposition.FALSE;
			if (presented.was_negated)
				reply[string_idx++] = String.format(Strings.locale,"%s : %s",presented.p.getNegStatement(),disposition(status));
			else
				reply[string_idx++] = String.format(Strings.locale,"%s : %s",presented.p.getStatement(),disposition(status));
		}
		return reply;
	}

	// Reply to the new statement by declaring it true of false. Returns true if choice is
	// ok. False and game ends.
	public boolean reply(boolean setTrue)
	{
		used.get(used.size()-1).reply = setTrue;
		boolean assignment = presented_negation != setTrue;
		// Can we make this assignment?
		if (!presented_proposition.setStatus((assignment) ? Proposition.TRUE : Proposition.FALSE))
		{
			contradiction = used.get(used.size()-1);
			return false;
		}
		// Is the new assignment consistent everywhere
		for (PresentedProposition p: used)
		{
			if (!p.p.isConsistent())
			{
				contradiction = p;
				return false;
			}
		}
		round++;
		return true;
	}

	public int getRounds() { return round; }

	private int round = 0;
}
