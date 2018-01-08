package com.apps.nishtha.quizzer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int totalQues=10;
    public static final int totalCategories=4;
    private static final String TAG = "TAG";
    TextView tvQuestion, tvPoints, tvCountdown;
    Button[] btnOptions = new Button[totalCategories];
    Integer[] btnOptionIds = {R.id.btnOption1, R.id.btnOption2, R.id.btnOption3, R.id.btnOption4};
    int correctAnsPos = 0;
    ProgressBar progressBar;
    ArrayList<QnA> qnAArrayList;
    int countOfQues = 0;
    CountDownTimer myTimer = new MyTimer(10000, 1000);
    int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setTitle(R.string.ready)
                .setMessage(R.string.instructions)
                .setNegativeButton(R.string.alert_go_back

                        , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton(R.string.alert_start, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGame();
                    }
                })
                .create();
        alertDialog.show();

        tvQuestion = findViewById(R.id.tvQuestion);
        tvPoints = findViewById(R.id.tvPoints);
        tvCountdown = findViewById(R.id.tvCountdown);

        for (int i = 0; i < totalCategories; i++) {
            btnOptions[i] = findViewById(btnOptionIds[i]);
            btnOptions[i].setOnClickListener(this);
        }
        progressBar = findViewById(R.id.progressbar);
        progressBar.setProgress(100);
    }

    private void startGame() {
        final Handler handler = new Handler();
        final AtomicInteger atomicInteger = new AtomicInteger(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tvCountdown.bringToFront();
                tvCountdown.setText(Integer.toString(atomicInteger.get()));
                if (atomicInteger.getAndDecrement() >= 1) {
                    handler.postDelayed(this, 1000);
                } else {
                    tvCountdown.setVisibility(View.GONE);
                    try {
                        qnAArrayList = (ArrayList<QnA>) getIntent().getSerializableExtra("qna");
                        if (countOfQues < totalQues) {
                            generateRandomQuestion(qnAArrayList);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onCreate: question act " + e.getLocalizedMessage());
                    }
                }
            }
        };
        runnable.run();
    }

    private void generateRandomQuestion(ArrayList<QnA> qnAArrayList) {
        Random random = new Random();
        if (qnAArrayList.size() > 0) {
            resetUi();
            countOfQues++;
            final QnA qnA = qnAArrayList.get(random.nextInt(qnAArrayList.size()));
            updateUi(qnA);

        } else {
            Toast.makeText(QuestionActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        }

    }

    private void resetUi() {
        progressBar.setProgress(100);
        myTimer.start();

        for (int i = 0; i < totalCategories; i++) {
            btnOptions[i].setClickable(true);
            btnOptions[i].setBackgroundResource(android.R.drawable.btn_default);
        }
    }

    private void updateUi(QnA qnA) {
        myTimer.start();
        tvQuestion.setText("Question " + countOfQues + ": " + qnA.getText());
        if (qnA.getAnswers().size() > 3) {
            for (int i = 0; i < totalCategories; i++) {
                btnOptions[i].setText(String.valueOf(i + 1) + ". " + qnA.getAnswers().get(i).getText());
                if (qnA.getAnswers().get(i).getCorrect().equals("true")) {
                    correctAnsPos = i;
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.quit_message)
                .setMessage(getApplicationContext().getResources().getString(R.string.no_partial_points_message))
                .setPositiveButton("Yes, Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myTimer.cancel();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog.show();

    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < totalCategories; i++) {
            if (view.getId() == btnOptionIds[i]) {
                myTimer.cancel();
                if (i == correctAnsPos) {
                    btnOptions[i].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    tvPoints.setText("Points: " + (++points));
//                    btnOptions[i].getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_light),PorterDuff.Mode.MULTIPLY);
                    Toast.makeText(QuestionActivity.this, "Correct answer!", Toast.LENGTH_SHORT).show();
                    makeOtherButtonsUnclickable();
                    generateQuesOrResult();
                } else {
                    btnOptions[i].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    btnOptions[correctAnsPos].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
//                    btnOptions[i].getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_light),PorterDuff.Mode.MULTIPLY);
//                    btnOptions[correctAnsPos].getBackground().setColorFilter(getResources().getColor(android.R.color.holo_green_light), PorterDuff.Mode.MULTIPLY);
                    Toast.makeText(QuestionActivity.this, "Wrong answer!", Toast.LENGTH_SHORT).show();
                    makeOtherButtonsUnclickable();
                    generateQuesOrResult();
                }

            }
        }
    }

    private void makeOtherButtonsUnclickable() {
        for (int i = 0; i < totalCategories; i++) {
            btnOptions[i].setClickable(false);
        }
    }

    private class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            progressBar.setProgress((int) l / 100);
        }

        @Override
        public void onFinish() { // TODO: 26/12/17 bug solved but correct answer to be shown
            Toast.makeText(QuestionActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
            btnOptions[correctAnsPos].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            if (countOfQues < totalQues && qnAArrayList != null) {
//                Handler handler=new Handler();
//                Runnable runnable=new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "run: inside run timer on finish ");
//                        generateRandomQuestion(qnAArrayList);
//                    }
//                };
//                Log.d(TAG, "onFinish: outside onFInish");
//                handler.postDelayed(runnable,3000);
                generateRandomQuestion(qnAArrayList);
            } else if (countOfQues == totalQues) {
                // TODO: 5/1/18 Display result
                Intent intent=new Intent(QuestionActivity.this,ResultActivity.class);
                intent.putExtra("scoreResult",points);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        }
    }

    private void generateQuesOrResult() {
        if(countOfQues==totalQues){
            // TODO: 5/1/18 Display result
            Intent intent=new Intent(QuestionActivity.this,ResultActivity.class);
            intent.putExtra("scoreResult",points);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        if(countOfQues<totalQues &&qnAArrayList!=null){
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateRandomQuestion(qnAArrayList);
                }
            },3000);
        }
    }

}