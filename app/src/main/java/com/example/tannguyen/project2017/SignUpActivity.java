package com.example.tannguyen.project2017;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword, edtConfirmPass;
    TextView txtGoSignin;
    Button btnSignUp;
    //ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPass = (EditText) findViewById(R.id.edtConfirmPassword);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignUp = (Button) findViewById(R.id.btnSingUp);
        txtGoSignin = (TextView) findViewById(R.id.txtGotoSignin);
        firebaseAuth = firebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPass = edtConfirmPass.getText().toString();

                if (TextUtils.isEmpty(email)){
                    edtEmail.requestFocus();
                    Toast.makeText(SignUpActivity.this,"Please enter the email",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    edtPassword.requestFocus();
                    Toast.makeText(SignUpActivity.this,"Please enter the email",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(confirmPass)){
                    edtConfirmPass.requestFocus();
                    Toast.makeText(SignUpActivity.this,"Please confirm the email",Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(confirmPass)){
                    Toast.makeText(SignUpActivity.this,"Enter exactly password you typed before",Toast.LENGTH_LONG).show();
                    return;
                }
                //progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email,password).
                        addOnCompleteListener(SignUpActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Toast.makeText(SignUpActivity.this,
                                                "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                            //progressBar.setVisibility(View.GONE);
                                        }
                                        else {
                                            Toast.makeText(SignUpActivity.this, "Successful" + task.getResult(),
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                });
            }
        });
        txtGoSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            }
        });
    }
}
