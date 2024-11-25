package com.example.task_1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WinCertificateActivity extends AppCompatActivity {

    private EditText answerField;
    private Button submitButton;
    private CountDownTimer timer;
    private boolean isTimerStarted = false; // To ensure the timer starts only once

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_certificate);

        TextView questionText = findViewById(R.id.question_text);
        answerField = findViewById(R.id.answer_field);
        submitButton = findViewById(R.id.submit_button);

        questionText.setText("Tell me About Yourself?");

        // Set a focus listener on the text field
        answerField.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !isTimerStarted) { // Check if the field is focused and timer hasn't started
                startCountdown();
                isTimerStarted = true;
            }
        });

        submitButton.setOnClickListener(v -> handleSubmit());
    }

    private void startCountdown() {
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView timerText = findViewById(R.id.timer_text);
                timerText.setText("Time remaining: " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                answerField.setEnabled(false);
                submitButton.setEnabled(true);
                Toast.makeText(WinCertificateActivity.this, "Time's up! Submit your answer.", Toast.LENGTH_SHORT).show();
            }
        };
        timer.start();
    }

    private void handleSubmit() {
        String answer = answerField.getText().toString();
        if (!answer.isEmpty()) {
            Toast.makeText(this, "Answer submitted: " + answer, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No answer provided!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
