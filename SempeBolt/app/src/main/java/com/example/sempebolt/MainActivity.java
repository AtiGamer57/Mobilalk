package com.example.sempebolt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 420;
    private static final String TAG = MainActivity.class.getName();
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    EditText emailEditText;
    EditText passwordEditText;

    Button button3;
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
        button3 = findViewById(R.id.buttonLogIn2);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.buttonLogIn);

        animation = AnimationUtils.loadAnimation(this, R.anim.text_anim2);

        textView = findViewById(R.id.editTextTextPersonName2);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        emailEditText.startAnimation(animation);
        passwordEditText.startAnimation(animation);
        textView.startAnimation(animation);
        button.startAnimation(animation);
        button2.startAnimation(animation);
        button3.startAnimation(animation);

    }


    @Override
    protected void onResume() {
        super.onResume();
        emailEditText.startAnimation(animation);
        passwordEditText.startAnimation(animation);
        textView.startAnimation(animation);
        button.startAnimation(animation);
        button2.startAnimation(animation);
        button3.startAnimation(animation);
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

    public void loginWithGoogle(View view) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "goggle sign in success");
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                e.printStackTrace();
                Log.w(TAG, "google sign in failed");
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
}