package com.green_station_20.problem4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login, register;
    private EditText etEmail, etPass;
    private DbHelper db;
    private Session session;

    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadLocale();
        db = new DbHelper(this);
        session = new Session(this);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnReg);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        login.setOnClickListener(this);
        register.setOnClickListener(this);


        if (session.loggedin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        aSwitch = findViewById(R.id.switchId);
        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        aSwitch.setChecked(sharedPreferences.getBoolean("value", true));
        aSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()) {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    aSwitch.setChecked(true);
                    setLocale("bn");
                    recreate();


                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    aSwitch.setChecked(false);
                    setLocale("en");
                    recreate();
                }
            }
        });

    }

    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("My_Lang", "");
        setLocale(language);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnReg:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            default:

        }
    }

    private void login() {
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        if (db.getUser(email, pass)) {
            session.setLoggedin(true);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong email/password", Toast.LENGTH_SHORT).show();
        }
    }


}
