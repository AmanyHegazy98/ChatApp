package com.amany.chat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amany.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private Button login;
    private TextView signUp,forgetPassword;
    private EditText emailText, passwordText;
    String email_pattern = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.button);
        signUp = findViewById(R.id.sing_in);
        forgetPassword=findViewById(R.id.forget_password);
        emailText = findViewById(R.id.editText1);
        passwordText = findViewById(R.id.editText2);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        login.setOnClickListener(v -> {
            /*Intent intent = new Intent(Login.this,WelcomePage.class);
            startActivity(intent);*/
            perForLogin();


        });
        forgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, ForgetPassword.class));

        });
        signUp.setOnClickListener(v -> {
            Intent intent2 = new Intent(Login.this, SignUp.class);
            startActivity(intent2);

        });


    }

    private void perForLogin() {


        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (!email.matches(email_pattern)) {
            emailText.setError("please,enter email context right");
        } else if (password.isEmpty() || password.length() < 6) {
            passwordText.setError("please,Enter the password correctly");
        } else {
            progressDialog.setMessage("please,wait while Login..");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Login.this,"Login successful",Toast.LENGTH_LONG).show();

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this,""+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                    }

                }


            });

        }

    }

    private void sendUserToNextActivity() {



        Intent intent=new Intent(Login.this, WelcomePage.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}