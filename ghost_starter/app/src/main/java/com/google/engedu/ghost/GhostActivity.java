package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private static TextView ghostText;
    private static TextView label;
    private static String computerWin = "Computer wins!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        ghostText = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);


        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(v);
            }
        });

        try {
            dictionary = new FastDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        if (((String) ghostText.getText()).isEmpty()) {
            ghostText.setText(dictionary.getGoodWordStartingWith(""));
            return;
        }

        if (ghostText.length() >= 4) {
            if (dictionary.isWord((String) ghostText.getText())){
                label.setText(computerWin);
                return;
            }
        }
        String anotherWord = dictionary.getAnyWordStartingWith((String) ghostText.getText());
        if (anotherWord == null) {
            label.setText(computerWin);
        }
        else {
            anotherWord = anotherWord.substring(0, ghostText.getText().length() + 1);
            ghostText.setText(anotherWord);
            userTurn = true;
            label.setText(USER_TURN);
        }

    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        ghostText.setText("");
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode < 29 || keyCode > 54)
            return super.onKeyUp(keyCode, event);
        ghostText.setText((String)ghostText.getText() + ((char)event.getUnicodeChar()));
        if ((ghostText.getText()).length() >= 4){
            userTurn = false;
            computerTurn();
        }
        return userTurn;
    }

    public void challengeWord(View view) {
        if (dictionary.isWord((String) ghostText.getText()))
            label.setText("You win!");
        else {
            String word = dictionary.getAnyWordStartingWith((String) ghostText.getText());
            ghostText.setText(word);
            label.setText(computerWin);
        }


    }

    public void resetGame(View view) {
        onStart(view);
    }
}
