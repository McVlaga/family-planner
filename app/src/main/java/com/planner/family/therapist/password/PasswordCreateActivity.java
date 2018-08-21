package com.planner.family.therapist.password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.planner.family.therapist.R;

public class PasswordCreateActivity extends AppCompatActivity implements View.OnClickListener {

    Button savePasswordButton;
    Spinner securityQuestionSpinner;

    EditText passwordFirstEditText;
    EditText passwordSecondEditText;
    EditText answerEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_create_activity);
        savePasswordButton = findViewById(R.id.save_password_button);
        savePasswordButton.setOnClickListener(this);
        securityQuestionSpinner = findViewById(R.id.security_questions_spinner);
        passwordFirstEditText = findViewById(R.id.password_first_edit_text);
        passwordSecondEditText = findViewById(R.id.password_second_edit_text);
        answerEditText = findViewById(R.id.answer_edit_text);
        securityQuestionSpinner.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.security_questions, R.layout.security_question_spinner_item));
        Intent intent = getIntent();
        boolean passwordWasSet = intent.getBooleanExtra("password_was_set", true);
        if (passwordWasSet) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_password_button:
                if (isInputGood()) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(getString(R.string.pref_password_was_set), true);
                    edit.putString("password", passwordFirstEditText.getText().toString());
                    edit.putString("security_question", securityQuestionSpinner.getSelectedItem().toString());
                    edit.putString("security_answer", answerEditText.getText().toString());
                    edit.apply();
                    Intent intent = new Intent(this, PasswordActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private boolean isInputGood() {
        if (passwordFirstEditText.getText().length() == 0) {
            Toast.makeText(this, "Type a password please", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordSecondEditText.getText().length() == 0) {
            Toast.makeText(this, "Confirm password please", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwordFirstEditText.getText().toString().equals(passwordSecondEditText.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordFirstEditText.getText().length() < 6) {
            Toast.makeText(this, "Your password should have at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (answerEditText.getText().length() == 0) {
            Toast.makeText(this, "Answer the chosen security question please", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
