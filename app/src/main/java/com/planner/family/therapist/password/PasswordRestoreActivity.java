package com.planner.family.therapist.password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.planner.family.therapist.R;

public class PasswordRestoreActivity extends AppCompatActivity implements View.OnClickListener {

    Button checkAnswerButton;
    TextView questionTextView;
    EditText answerEditText;

    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_restore_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        checkAnswerButton = findViewById(R.id.check_answer_button);
        checkAnswerButton.setOnClickListener(this);
        questionTextView = findViewById(R.id.question_text_view);
        answerEditText = findViewById(R.id.answer_edit_text);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String question = prefs.getString("security_question", null);
        answer = prefs.getString("security_answer", null);
        questionTextView.setText(question);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_answer_button:
                if (isAnswerGood()) {
                    Intent createPasswordIntent = new Intent(this, PasswordCreateActivity.class);
                    startActivity(createPasswordIntent);
                } else {
                    Toast.makeText(this, "Answer is incorrect", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isAnswerGood() {
        return answerEditText.getText().toString().equals(answer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
