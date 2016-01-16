package com.github.exp0nge.pigdicegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Handler;

public class PigActivity extends AppCompatActivity {
    private static int userTurnScore = 0;
    private static int userOverallScore = 0;
    private static int computerTurnScore = 0;
    private static int computerOverallScore = 0;
    private static Random random = new Random();
    private static final HashMap<Integer, Integer> diceRoll = new HashMap<>();

    TextView userScoreTextView;
    TextView computerScoreTextView;
    Button holdButton;
    Button resetButton;
    ImageView diceFaceImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pig);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        diceRoll.put(1, R.drawable.dice1);
        diceRoll.put(2, R.drawable.dice2);
        diceRoll.put(3, R.drawable.dice3);
        diceRoll.put(4, R.drawable.dice4);
        diceRoll.put(5, R.drawable.dice5);
        diceRoll.put(6, R.drawable.dice6);

        userScoreTextView = (TextView) findViewById(R.id.userScore);
        holdButton = (Button) findViewById(R.id.holdScoreButton);
        resetButton = (Button) findViewById(R.id.resetScoreButton);
        diceFaceImageView = (ImageView) findViewById(R.id.diceFace);
        computerScoreTextView = (TextView) findViewById(R.id.computerScoreTextView);


        userScoreTextView.setText(Integer.toString(userTurnScore));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pig, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void rollDice(View view) {
        int roll = random.nextInt(6) + 1;
        if (roll != 1){
            userTurnScore += roll;
            diceFaceImageView.setImageResource(diceRoll.get(roll));
            Toast.makeText(getApplicationContext(), Integer.toString(userTurnScore), Toast.LENGTH_SHORT).show();
        }
        else {
            diceFaceImageView.setImageResource(diceRoll.get(1));
            userTurnScore = 0;
            holdScore(findViewById(R.id.holdScoreButton));
        }

    }

    public void resetScore(View view) {
        userOverallScore = 0;
        userTurnScore = 0;
        computerOverallScore = 0;
        computerTurnScore = 0;
        Toast.makeText(getApplicationContext(), "Score reset", Toast.LENGTH_SHORT).show();
        computerScoreTextView.setText("0");
        userScoreTextView.setText("0");
        diceFaceImageView.setImageResource(diceRoll.get(1));
        setHandlers();
    }

    private void setHandlers(){
        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holdScore(view);
            }
        });
        diceFaceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice(view);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetScore(view);
            }
        });
    }

    public void holdScore(View view) {
        Toast.makeText(getApplicationContext(), "Computer's turn", Toast.LENGTH_SHORT).show();
        userOverallScore += userTurnScore;
        userScoreTextView.setText(Integer.toString(userOverallScore));
        new Thread(new ComputerAI()).start();
    }

    class ComputerAI implements Runnable {

        @Override
        public void run() {
            Log.d("AIX", "AI running");
            diceFaceImageView.setOnClickListener(null);
            holdButton.setOnClickListener(null);
            resetButton.setOnClickListener(null);

            int roll = random.nextInt(6) + 1;
            computerTurnScore = roll;
            while(computerTurnScore <= 20) {
                final int localRoll = roll;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        diceFaceImageView.setImageResource(diceRoll.get(localRoll));
                        Toast.makeText(getApplicationContext(), Integer.toString(computerTurnScore), Toast.LENGTH_SHORT).show();
                    }
                });
                if (roll == 1){
                    computerTurnScore = 0;
                    break;
                }
                computerTurnScore += roll;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                roll = random.nextInt(6) + 1;
            }

            computerOverallScore += computerTurnScore;
            final TextView computerTV = (TextView) findViewById(R.id.computerScoreTextView);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    computerTV.setText(Integer.toString(computerOverallScore));
                }
            });
            computerTurnScore = 0;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Your turn", Toast.LENGTH_SHORT).show();
                }
            });
            setHandlers();
        }
    }
}