package com.williambauer.cs193a.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private HashSet<String> wordSet;
    private String[] wordArray;
    private String currentWord = "";
    private HashSet<String> guesses = new HashSet<String>();
    private int gallowsImgNum = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Do stuff onload
        wordSet = loadTextFile();
        wordArray = wordSet.toArray(new String[wordSet.size()]);

        Toast.makeText(this, String.valueOf(wordArray.length) + " words loaded", Toast.LENGTH_SHORT).show();

        Random rand = new Random();

        currentWord = wordArray[rand.nextInt(wordArray.length)];

        displayWord(currentWord);
    }

    private void displayWord(String fullWord) {
        String maskedWord = "";

        for (int i = 0; i < fullWord.length(); i++) {
            if (guesses.contains(String.valueOf(fullWord.charAt(i)))) {
                maskedWord += " " + fullWord.charAt(i);
            } else {
                maskedWord += " _";
            }
        }

        TextView guessWordText = (TextView) findViewById(R.id.guessWordText);
        guessWordText.setText(maskedWord);

        if (!maskedWord.contains("_")) {
            endGame(true);
        }
    }

    // Assumes file is "@raw/dictionary.txt"
    // http://stackoverflow.com/questions/24291721/reading-a-text-file-line-by-line-in-android
    public HashSet<String> loadTextFile() {
        BufferedReader reader;
        HashSet<String> words = new HashSet<String>();

        try {

            final InputStream file = getResources().openRawResource(R.raw.dictionary);
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while(line != null){
                Log.d("StackOverflow", line);
                line = reader.readLine();

                // do something with the line
                words.add(line);

            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        return words;
    }

    public void guessLetter(View view) {
        EditText guessEditText = (EditText) findViewById(R.id.guessEditText);
        TextView guessedLetters = (TextView) findViewById(R.id.guessedLetters);

        // keep only the first char if they enter more
        String guessedLetter = String.valueOf(guessEditText.getText().charAt(0));

        if (!guesses.contains(guessedLetter) && !currentWord.contains(guessedLetter)) {
            guessedLetters.setText(guessedLetters.getText() + " " + guessedLetter);
            updateGallows();
//            gallowsImgNum++;

            if (gallowsImgNum == 7) {
                endGame(false);
            }
        }

        guesses.add(guessedLetter);

        guessEditText.setText("");

        displayWord(currentWord);

    }

    private void endGame(boolean win) {
        Button submitGuessBtn = (Button) findViewById(R.id.submitGuessBtn);
        TextView guessedLetters = (TextView) findViewById(R.id.guessedLetters);

        submitGuessBtn.setClickable(false);

        if (win) {
            guessedLetters.setText("You Won! \n\n" + guessedLetters.getText());
        } else {
            guessedLetters.setText("Sorry, you lose! \n" + "The word was " +
                    currentWord + "\n\n" + guessedLetters.getText());
        }

    }

    private void updateGallows() {
        ImageView gallowsImg = (ImageView) findViewById(R.id.gallowsImg);

        gallowsImgNum++;

        switch (gallowsImgNum) {
            case 1:
                gallowsImg.setImageResource(R.drawable.hangman1);
                break;
            case 2:
                gallowsImg.setImageResource(R.drawable.hangman2);
                break;
            case 3:
                gallowsImg.setImageResource(R.drawable.hangman3);
                break;
            case 4:
                gallowsImg.setImageResource(R.drawable.hangman4);
                break;
            case 5:
                gallowsImg.setImageResource(R.drawable.hangman5);
                break;
            case 6:
                gallowsImg.setImageResource(R.drawable.hangman6);
                break;
            case 7:
                gallowsImg.setImageResource(R.drawable.hangman7);
                break;
        }
    }
}
