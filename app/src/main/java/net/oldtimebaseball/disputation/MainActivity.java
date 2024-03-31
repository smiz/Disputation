package net.oldtimebaseball.disputation;

import gameoflogic.Disputation;
import gameoflogic.Atomic;

import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import java.util.Random;
import java.util.TreeSet;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import gameoflogic.Strings;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Disputation disputation;
    private boolean active_game = false;
    private int difficultySelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStrings();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getApplicationContext().getSharedPreferences("appSettings", MODE_PRIVATE);
        difficultySelection = settings.getInt("difficulty",0);
        setContentView(R.layout.activity_main);
        ((Button)(findViewById(R.id.assentButton))).setOnClickListener(view -> assent());
        ((Button)(findViewById(R.id.refuteButton))).setOnClickListener(view -> refute());
        ((Spinner)findViewById(R.id.difficultySelector)).setSelection(difficultySelection);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getApplicationContext().getSharedPreferences("appSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("difficulty", difficultySelection);
        editor.apply();
    }

     private void setStrings() {
        Strings.AND = getResources().getString(R.string.and);
        Strings.OR = getResources().getString(R.string.or);
        Strings.IF = getResources().getString(R.string.if_);
        Strings.THEN = getResources().getString(R.string.then);
        Strings.IS = getResources().getString(R.string.is);
        Strings.TRUE = getResources().getString(R.string.true_);
        Strings.FALSE = getResources().getString(R.string.false_);
        Strings.UNDECIDED = getResources().getString(R.string.undecided);
        Strings.THEREFORE = getResources().getString(R.string.therefore);
        Strings.PRESENT_WHOLE = getResources().getString(R.string.present_whole);
        Strings.locale = Locale.getDefault();
    }
    private void lose() {
        StringBuilder string = new StringBuilder();
        string.append(getResources().getString(R.string.lose)).append('\n');
        String [] why = disputation.describeContradiction();
        for (String statement: why)
            string.append(statement).append('\n');
        string.append(String.format(Strings.locale,"%s %d\n",getResources().getString(R.string.score),
                disputation.getRounds()));
        string.append(getResources().getString(R.string.new_game));
        ((TextView)(findViewById(R.id.propositionView))).setText(string);
        gameEnds();
    }

    private void gameEnds() {
        ((Spinner)findViewById(R.id.difficultySelector)).setEnabled(true);
        active_game = false;
    }
    private void win() {
        String string = String.format(Strings.locale,"%s\n%s %d\n%s",
                getResources().getString(R.string.win),
                getResources().getString(R.string.score),
                disputation.getRounds(),
                getResources().getString(R.string.new_game));
        ((TextView)(findViewById(R.id.propositionView))).setText(string);
        gameEnds();
    }
    private void takeAction(boolean assent) {
        // Correct response
        if (disputation.reply(assent)) {
            if (disputation.questionsRemaining() == 0) {
                win();
            } else {
                ((TextView)(findViewById(R.id.propositionView))).setText(disputation.newProposition());
            }
        } else {
            lose();
        }
    }
    public void assent() {
        if (!active_game) {
            newGame();
            ((TextView)(findViewById(R.id.propositionView))).setText(disputation.newProposition());
        } else {
            takeAction(true);
        }
    }

    public void refute() {
        if (!active_game)
            return;
        takeAction(false);
    }
    private void newGame() {
        ((Spinner)findViewById(R.id.difficultySelector)).setEnabled(false);
        Random r = new Random();
        difficultySelection = ((Spinner)findViewById(R.id.difficultySelector)).getSelectedItemPosition();
        String [] positiveStatements = getResources().getStringArray(R.array.positive_statements);
        String [] negativeStatements = getResources().getStringArray(R.array.negative_statements);
        Set<Integer> selected = new TreeSet<>();
        LinkedList<Atomic> domain = new LinkedList<>();
        do {
            selected.add(r.nextInt(positiveStatements.length));
        }
        while (selected.size() < difficultySelection+2);
        for (Integer i: selected) {
            if (r.nextBoolean())
                domain.add(new Atomic(positiveStatements[i],negativeStatements[i]));
            else
                domain.add(new Atomic(negativeStatements[i],positiveStatements[i]));
        }
        disputation = new Disputation(domain);
        active_game = true;
    }
}