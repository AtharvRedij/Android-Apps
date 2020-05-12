package com.atharvredij.braintrainer;

import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button goButton;
    int a,b,locationOfCorrectAnswer;
    ArrayList<Integer> answers = new ArrayList<Integer>();
    TextView resultTextView;
    int noOfQuestion=0;
    int score=0;
    TextView scoreTextView, questionTextView, timerTextView;
    Button button0,button1,button2,button3;
    Button playAgain;
    ConstraintLayout gameLayout;

    public void playAgain(View view) {
        score=0;
        noOfQuestion=0;
        timerTextView.setText("30s");
        resultTextView.setText("");
        playAgain.setVisibility(View.INVISIBLE);
        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);

        newQuestion();
        scoreTextView.setText(score + "/" + noOfQuestion);

        new CountDownTimer(30100,1000) {

            @Override
            public void onTick(long l) {
                timerTextView.setText(l/1000 + "s");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("0s");
                resultTextView.setText("Done! \nYour score is " + score);
                playAgain.setVisibility(View.VISIBLE);
                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
            }
        }.start();
    }

    public void start(View view) {
        goButton.setVisibility(View.GONE);
        playAgain(goButton);
        gameLayout.setVisibility(View.VISIBLE);
    }

    public void newQuestion() {
        Random rand = new Random();

        a=rand.nextInt(21);
        b=rand.nextInt(21);
        locationOfCorrectAnswer = rand.nextInt(4);

        questionTextView.setText(a + " + " + b);

        answers.clear();

        for(int i=0; i<4; i++) {
            if(i == locationOfCorrectAnswer) {
                answers.add(a+b);
            }
            else {
                int wrongAnswer = rand.nextInt(41);
                while(wrongAnswer == a+b) {
                    wrongAnswer = rand.nextInt(41);
                }
                answers.add(wrongAnswer);
            }
        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }

    public void chooseAnswer(View view) {
        noOfQuestion++;
        resultTextView.setVisibility(View.VISIBLE);
        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            resultTextView.setText("Correct");
            score++;
        } else {
            resultTextView.setText("Wrong");
        }

        scoreTextView.setText(score + "/" + noOfQuestion);
        newQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.resultTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        questionTextView = findViewById(R.id.questionTextView);
        timerTextView = findViewById(R.id.timerTextView);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        goButton = findViewById(R.id.goButton);

        playAgain = findViewById(R.id.playAgain);

        resultTextView.setVisibility(View.INVISIBLE);

        gameLayout = findViewById(R.id.gameLayout);
        gameLayout.setVisibility(View.INVISIBLE);

    }
}
