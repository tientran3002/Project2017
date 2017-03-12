package com.example.tannguyen.project2017;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {

    EditText edtEmail;
    Button btnSend;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnSend = (Button) findViewById(R.id.btnSend);
        auth.getInstance();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    edtEmail.requestFocus();
                    Toast.makeText(ResetActivity.this,"Please enter the email",Toast.LENGTH_LONG).show();
                    return;
                }
                auth.sendPasswordResetEmail(email).addOnCompleteListener(ResetActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetActivity.this,
                                    "Successful1"+task.getResult(),Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                            Toast.makeText(ResetActivity.this,
                                    "Unsuccessful1"+task.getException(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
