package com.example.mohamedessam.newspaper;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email , pass;
    TextView signup , forggetpassword;
    CheckBox rem;
    SharedPreferences sharedPreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("newspaperlogin.txt",MODE_PRIVATE);
        email= (EditText)findViewById(R.id.user_email);
        pass = (EditText)findViewById(R.id.pass);
        signup = (TextView)findViewById(R.id.signup);
        rem = (CheckBox)findViewById(R.id.remeber);

        if(sharedPreferences.getBoolean("first",true))
        {

            sharedPreferences.edit().putBoolean("first", false).apply();
            sharedPreferences.edit().putString("email", email.getText().toString()).apply();
            sharedPreferences.edit().putString("pass", pass.getText().toString()).apply();
            sharedPreferences.edit().commit();


        }else
        {
            rem.setChecked(!sharedPreferences.getBoolean("first",true));
            email.setText(sharedPreferences.getString("email",""));
            pass.setText(sharedPreferences.getString("pass",""));
        }
        rem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                remeber_me(isChecked);
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),activity_signup.class);
                startActivity(i);
            }
        });

        forggetpassword = (TextView)findViewById(R.id.forgot_password);
        forggetpassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.create();
                dialog.setContentView(R.layout.foreget_password_dialog);
                final EditText useremail = (EditText)dialog.findViewById(R.id.myemail);
                Button button = (Button)dialog.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(useremail.getText().toString().isEmpty())
                        {
                            Toast.makeText(getBaseContext(),"Enter Your Email",Toast.LENGTH_LONG).show();
                        }else
                        {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(useremail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getBaseContext(),"Will Send Password To Email",Toast.LENGTH_LONG).show();
                                        dialog.dismiss();

                                    }else
                                    {
                                        Toast.makeText(getBaseContext(),task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });

                dialog.show();
            }

        });
    }

    public void Login(View view) {

        if(email.getText().toString().isEmpty() && pass.getText().toString().isEmpty())
        {
            Toast.makeText(this,"must complete enter email and password",Toast.LENGTH_LONG).show();

        }else {
            String name = email.getText().toString();
            String password = pass.getText().toString();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(name, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(Login.this, Show_news.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(Login.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email",email.getText().toString());
        outState.putString("pass",pass.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if(savedInstanceState != null)
        {
            email.setText(savedInstanceState.getString("email"));
            pass.setText(savedInstanceState.getString("pass"));
        }
    }

    public void remeber_me(boolean check)
    {



        if(check == true) {
            sharedPreferences.edit().putBoolean("first", false).apply();
            sharedPreferences.edit().putString("email", email.getText().toString()).apply();
            sharedPreferences.edit().putString("pass", pass.getText().toString()).apply();

        }
        sharedPreferences.edit().commit();

        /*
        *  */
    }
}
