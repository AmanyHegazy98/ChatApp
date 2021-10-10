package com.amany.chat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amany.chat.R;
import com.amany.chat.data.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    //ui
    private Button btn_signUp;
    private TextView login;
    private EditText emailText, passwordText, userNameText;
    String email_pattern = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    ProgressDialog progressDialog;


    //firebase database
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // bind view
        setContentView(R.layout.activity_sign_up);
        btn_signUp = findViewById(R.id.button);
        login = findViewById(R.id.sing_in);
        emailText = findViewById(R.id.editText1);
        userNameText = findViewById(R.id.editText4);
        passwordText = findViewById(R.id.editText2);
        progressDialog = new ProgressDialog(this);


        btn_signUp.setOnClickListener(v -> {
            PerforAuth();


        });
        login.setOnClickListener(v -> {
            Intent intent2 = new Intent(SignUp.this, Login.class);
            startActivity(intent2);

        });


    }

    private void PerforAuth() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String userName = userNameText.getText().toString();


        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)||TextUtils.isEmpty(email)){
            Toast.makeText(this,"All filleds are required ",Toast.LENGTH_LONG).show();

        }

        if (!email.matches(email_pattern)) {
            emailText.setError("please,enter email context right");
        } else if (password.isEmpty() || password.length() < 6) {
            passwordText.setError("please,Enter the password correctly");
        } else {
            progressDialog.setMessage("please,wait while Registration..");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        mUser = mAuth.getCurrentUser();

                        String userid = mUser.getUid();
                        //init database && search from users
                        reference = FirebaseDatabase.getInstance().getReference("User").child(userid);
                        /*HashMap<String,String> hashMap=new HashMap<>();
                        hashMap.put("id",userid);
                        hashMap.put("name",userName);
                        hashMap.put("image","default");*/

                       // insert user information
                        UserModel userModel =new UserModel(userid,"default",userName,"","online","");
                        reference.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    sendUserToNextActivity();
                                    finish();
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUp.this, "Registration failed: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });


        }


    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(SignUp.this, WelcomePage.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}