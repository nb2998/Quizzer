package com.apps.nishtha.quizzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class InstructionsActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        final String q=getIntent().getStringExtra("question");
        Log.d(TAG, "onCreate: instructions "+q);
        Button btnStartQuiz=findViewById(R.id.btnStartQuiz);
        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InstructionsActivity.this, QuestionActivity.class);
                intent.putExtra("ques",q);
                startActivity(intent);
            }
        });
    }
}