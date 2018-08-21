package com.planner.family.therapist.password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.planner.family.therapist.home.HomeActivity;
import com.planner.family.therapist.R;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button restorePasswordButton;
    private Button okButton;
    private EditText passwordEditText;

    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_activity);
        restorePasswordButton = findViewById(R.id.restore_password_button);
        restorePasswordButton.setOnClickListener(this);
        okButton = findViewById(R.id.ok_button);
        passwordEditText = findViewById(R.id.password_edit_text);
        okButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean passwordWasSet = prefs.getBoolean(getString(R.string.pref_password_was_set), false);
        if(!passwordWasSet) {
            Intent createPasswordIntent = new Intent(this, PasswordCreateActivity.class);
            createPasswordIntent.putExtra("password_was_set", false);
            startActivity(createPasswordIntent);
        } else {
            password = prefs.getString("password", null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restore_password_button:
                Intent restorePasswordIntent = new Intent(this, PasswordRestoreActivity.class);
                startActivity(restorePasswordIntent);
                break;
            case R.id.ok_button:
                if (isPasswordGood()) {
                    Intent homeIntent = new Intent(this, HomeActivity.class);
                    startActivity(homeIntent);
                    ActivityCompat.finishAffinity(PasswordActivity.this);
                } else {
                    Toast.makeText(this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isPasswordGood() {
        return passwordEditText.getText().toString().equals(password);
    }
}
