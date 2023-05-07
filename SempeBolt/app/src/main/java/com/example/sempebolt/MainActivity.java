package com.example.sempebolt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private FirebaseAuth auth;

    EditText emailEditText;
    EditText passwordEditText;

    Button button2;
    Button button;

    TextView textView;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);

        button2 = findViewById(R.id.button);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.buttonLogIn);

        animation = AnimationUtils.loadAnimation(this, R.anim.text_anim2);

        textView = findViewById(R.id.editTextTextPersonName2);


        emailEditText.startAnimation(animation);
        passwordEditText.startAnimation(animation);
        textView.startAnimation(animation);
        button.startAnimation(animation);
        button2.startAnimation(animation);

    }


    @Override
    protected void onResume() {
        super.onResume();
        emailEditText.startAnimation(animation);
        passwordEditText.startAnimation(animation);
        textView.startAnimation(animation);
        button.startAnimation(animation);
        button2.startAnimation(animation);
    }


    public void login(View view) {
        Log.i(TAG, "Login function got called");

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Log in was successful");
                    goToShop();
                } else {
                    Log.d(TAG, "Log in was UNsuccessful");

                }
            }
        });

    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToShop() {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }
}