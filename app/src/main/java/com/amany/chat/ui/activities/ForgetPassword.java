package com.amany.chat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amany.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    EditText send_email;
    Button btn_reset;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        send_email=findViewById(R.id.send_email);
        btn_reset=findViewById(R.id.btn);
        firebaseAuth=FirebaseAuth.getInstance();
        btn_reset.setOnClickListener(v -> {
            String email= send_email.getText().toString();
            if(email.equals(""))
            {
                Toast.makeText(ForgetPassword.this,"All fileds are required !",Toast.LENGTH_SHORT).show();
            }else {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                            Toast.makeText(ForgetPassword.this,"please check your Email",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgetPassword.this, Login.class));
                        }else {
                           String error= task.getException().getMessage();
                           Toast.makeText(ForgetPassword.this,error,Toast.LENGTH_SHORT).show();
                       }
                    }
                });
            }

        });


    }
}