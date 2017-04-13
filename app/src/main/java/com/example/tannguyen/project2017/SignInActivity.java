package com.example.tannguyen.project2017;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnSignin;
    TextView txtGoSignup, txtResetPassword;
    FirebaseAuth auth;
    ProgressBar progress;
    public static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SharedPreferences pre=getSharedPreferences
                ("account",MODE_PRIVATE);
        String check_user=pre.getString("user", "");
        if(!check_user.equals("")) {
            ProgressDialog progressDialog=new ProgressDialog(SignInActivity.this);
            progressDialog.setMessage("Sign In...");
            progressDialog.show();
            user=check_user;
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            progressDialog.cancel();
            return;
        }
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSignin = (Button) findViewById(R.id.btnSignin);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        txtGoSignup = (TextView) findViewById(R.id.txtGotoSignup);
        txtResetPassword = (TextView) findViewById(R.id.txtFogotPassword);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressdialog = new ProgressDialog(SignInActivity.this);
                progressdialog.setMessage("Check Account...");
                progressdialog.show();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignInActivity.this,
                            "Please enter the email", Toast.LENGTH_LONG).show();
                    progressdialog.cancel();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this,
                            "Please enter the password", Toast.LENGTH_LONG).show();
                    progressdialog.cancel();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(SignInActivity.this,
                            "Password musts be larger than 6 character", Toast.LENGTH_LONG).show();
                    progressdialog.cancel();
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progress.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this,
                                            "Log in is unsuccessful" + task.isSuccessful(), Toast.LENGTH_LONG).show();
                                    progressdialog.cancel();

                                } else {
                                    Toast.makeText(SignInActivity.this,
                                            "Log in is successful!" + task.getException(), Toast.LENGTH_LONG).show();
                                    user=edtEmail.getText().toString().split("@")[0];
                                    SharedPreferences pre=getSharedPreferences
                                            ("account", MODE_PRIVATE);
                                    SharedPreferences.Editor editor=pre.edit();
                                    editor.putString("user", user);
                                    editor.commit();
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    progressdialog.cancel();
                                }
                            }
                        }
                );
            }
        });

        txtGoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        txtResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ResetActivity.class));
            }
        });
    }
}

