package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignActivity extends AppCompatActivity {

    private static final String TAG = "SignActivity";
    private boolean loginModeActive;

    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Button buttonSignUp;
    private TextView textViewTapToLogin;

    FirebaseDatabase database;
    DatabaseReference usersDatabaseReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersDatabaseReferences = database.getReference().child("users");

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        buttonSignUp = findViewById(R.id.signUpButton);
        textViewTapToLogin = findViewById(R.id.textViewTapToLogIn);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignActivity.this, UserListActivity.class));
        }

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpUser(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim());

            }
        });

    }

    private void loginSignUpUser(String email, String password) {
        if (loginModeActive) {
            if (editTextPassword.getText().toString().trim().length() < 7) {
                Toast.makeText(this, "password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            } else if (editTextEmail.getText().toString().trim().equals("")) {
                Toast.makeText(this, "please input your email", Toast.LENGTH_SHORT).show();

            }else{mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(SignActivity.this, UserListActivity.class);
                                intent.putExtra("userName", editTextName.getText().toString().trim());
                                startActivity(intent);
                                //     updateUI(user);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //  updateUI(null);
                            }
                        }
                    });}

        } else {
            if (!editTextPassword.getText().toString().trim().equals(editTextRepeatPassword.getText().toString().trim())) {
                Toast.makeText(this, "password don't match", Toast.LENGTH_SHORT).show();
            } else if (editTextPassword.getText().toString().trim().length() < 7) {
                Toast.makeText(this, "password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            } else if (editTextEmail.getText().toString().trim().equals("")) {
                Toast.makeText(this, "please input your email", Toast.LENGTH_SHORT).show();

            } else {

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    createUser(user);
                                    //   updateUI(user);
                                    Intent intent = new Intent(SignActivity.this, UserListActivity.class);
                                    intent.putExtra("userName", editTextName.getText().toString().trim());
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // updateUI(null);
                                }
                            }
                        });
            }
        }
    }

    private void createUser(FirebaseUser fbUser) {
        User user = new User();
        user.setId(fbUser.getUid());
        user.setEmail(fbUser.getEmail());
        user.setName(editTextName.getText().toString().trim());

        usersDatabaseReferences.push().setValue(user);

    }

    public void toggleLoginMode(View view) {
        if (loginModeActive) {
            loginModeActive = false;
            buttonSignUp.setText("Sing Up");
            textViewTapToLogin.setText("Or, log in");
            editTextRepeatPassword.setVisibility(View.VISIBLE);
        } else {
            loginModeActive = true;
            buttonSignUp.setText("Log In");
            textViewTapToLogin.setText("Or, Sign Up");
            editTextRepeatPassword.setVisibility(View.GONE);
        }
    }
}