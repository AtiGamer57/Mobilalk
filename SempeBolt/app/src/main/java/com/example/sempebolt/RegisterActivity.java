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

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    EditText userNameEditText;
    EditText emailEditText;
    EditText passwordEditText;

    Animation animation;
    TextView textView;
    TextView textView2;
    Button button;
    Button button2;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameEditText = findViewById(R.id.editTextUserName);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);

        auth = FirebaseAuth.getInstance();

        animation = AnimationUtils.loadAnimation(this, R.anim.text_anim);

        textView = findViewById(R.id.editTextTextPersonName2);

        button = findViewById(R.id.buttonLogIn);
        button2 = findViewById(R.id.button);

        emailEditText.startAnimation(animation);
        passwordEditText.startAnimation(animation);
        textView.startAnimation(animation);
        button.startAnimation(animation);
        button2.startAnimation(animation);
        userNameEditText.startAnimation(animation);

    }

    @Override
    protected void onResume() {
        super.onResume();
        emailEditText.startAnimation(animation);
        passwordEditText.startAnimation(animation);
        textView.startAnimation(animation);
        button.startAnimation(animation);
        button2.startAnimation(animation);
        userNameEditText.startAnimation(animation);
    }

    public void register(View view) {
        String userName = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "User created successfuly");
                    goToShop();
                } else {
                    Log.d(TAG, "User created UNsuccessfuly");
                }
            }
        });
    }

    public void goToLogin(View view) {
        finish();
    }

    public void goToShop() {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }
}