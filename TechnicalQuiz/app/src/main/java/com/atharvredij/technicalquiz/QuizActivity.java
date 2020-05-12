package com.atharvredij.technicalquiz;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    // All the required views
    private TextView timerTextView, scoreTextView, questionTextView;
    private Button button0, button1, button2, button3;
    private ProgressBar circularProgressBar;

    // Firebase objects
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private Random random;

    // This will keep track of questions asked
    private int[] questionsAsked = new int[50];
    private int count=0;

    private int chosenQuestion;
    private int locationOfCorrectAnswer;

    private int score=0;

    private static final String TAG = "QuizActivity";

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        random = new Random();

        // Initialization of all the views

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        //scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        questionTextView = (TextView) findViewById(R.id.questionTextView);

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        circularProgressBar = (ProgressBar) findViewById(R.id.timerProgressBar);

        mProgressDialog = new ProgressDialog(this);

        // setting up firebase
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("question");

        // Timer code
        circularProgressBar.setMax(10);
        CountDownTimer timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {
                timerTextView.setText("" + (int)l/1000);
                circularProgressBar.setProgress((int)l/1000);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("0");
                circularProgressBar.setProgress(circularProgressBar.getMax());
                Toast.makeText(getApplicationContext(),"We are done",Toast.LENGTH_SHORT).show();
                stopQuiz();
            }
        };
        timer.start();

        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        newQuestion();
    }

    // creates a new question
    private void newQuestion() {
        chosenQuestion = questionChooser();
        questionsAsked[count] = chosenQuestion;

        DatabaseReference mCurrentQuesRef = mDatabaseReference.child("question-" + chosenQuestion);
        mCurrentQuesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"\n Question = " + dataSnapshot.child("ques").getValue(String.class)
                        + "\n Ans1 = " + dataSnapshot.child("ans-1").getValue(String.class)
                        + "\n Ans2 = " + dataSnapshot.child("ans-2").getValue(String.class)
                        + "\n Ans3 = " + dataSnapshot.child("ans-3").getValue(String.class)
                        + "\n Ans4 = " + dataSnapshot.child("ans-4").getValue(String.class));


                String ques = dataSnapshot.child("ques").getValue(String.class);
                String ans1 = dataSnapshot.child("ans-1").getValue(String.class);
                String ans2 = dataSnapshot.child("ans-2").getValue(String.class);
                String ans3 = dataSnapshot.child("ans-3").getValue(String.class);
                String ans4 = dataSnapshot.child("ans-4").getValue(String.class);

                ArrayList<String> list = new ArrayList<>();
                list.add(ans2);
                list.add(ans3);
                list.add(ans4);

                locationOfCorrectAnswer = random.nextInt(4);
                questionTextView.setText(ques);

                int incorrectAnswerLocation = 0;
                // For every question this will hold answers (both correct and incorrect)
                String[] answersArray = new String[5];

                // This for loop fills answersArray with strings
                // which are to be displayed on button
                for (int i = 0; i < 4; i++) {
                    if (i == locationOfCorrectAnswer) {
                        answersArray[i] = ans1;
                    } else {
                        incorrectAnswerLocation = random.nextInt(3);

                        while (displayAnswerNotRepeat(answersArray, incorrectAnswerLocation, list)) {
                            incorrectAnswerLocation = random.nextInt(3);
                        }

                        answersArray[i] = list.get(incorrectAnswerLocation);
                    }
                }

                button0.setText(answersArray[0]);
                button1.setText(answersArray[1]);
                button2.setText(answersArray[2]);
                button3.setText(answersArray[3]);

                if(mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // This method chooses question
    private int questionChooser() {
        // TODO: change bound to be dynamic
        int x = random.nextInt(8);

        // not including zero because it is default value of
        // array and will cause lot of problems as it is already
        // in questions asked it will not be taken anyway
        while (checker(x) || x == 0) {
            x = random.nextInt(8);
        }

        return x;
    }

    // This is a helper method of questionChooser()
    // It will check if current question was asked previously
    private boolean checker(int num) {
        for (int n : questionsAsked) {
            if (num == n) {
                return true;
            }
        }
        return false;
    }

    // This method checks that displayed answers i.e. A,B,C,D are distinct
    public boolean displayAnswerNotRepeat(String[] arr, int num, ArrayList<String> list) {
        for (String s : arr) {
            if (s == list.get(num)) {
                return true;
            }
        }
        return false;
    }

    // This method is invoked when button is clicked
    public void answerClicked(View view) {
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
            score++;
        } else {
            Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT).show();
        }

        count++;
        //scoreTextView.setText(score + "/" + count);
        newQuestion();
    }

    private void stopQuiz() {
        button0.setEnabled(false);
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
    }
}
