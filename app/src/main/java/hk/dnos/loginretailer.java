package hk.dnos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Objects;

public class loginretailer extends AppCompatActivity {


    private   android.support.v7.widget.Toolbar mtoolbar;

    EditText email,pass;
    Button signin;
    private ProgressDialog pdialog;

    TextView forgetPass;

    FirebaseAuth f_auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginretailer);
        //Progress Dialog
        pdialog= new ProgressDialog(this);
        forgetPass=findViewById(R.id.forgetpassword);
        //Toolbar
        mtoolbar = findViewById(R.id.log_toolbar);
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("DNOS");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //textviews
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        signin=findViewById(R.id.signin);

        f_auth=FirebaseAuth.getInstance();
        // Radio button

        //signuptxt=findViewById(R.id.register);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Intent intent=new Intent(login.this,forgetpassword.class);
                //  startActivity(intent);

            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email1 = email.getText().toString();
                String pass1= pass.getText().toString();

                pdialog.setTitle("Logging User");
                pdialog.setMessage("Please wait while Logging in");
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();

                if(!email1.isEmpty() && !pass1.isEmpty()) {

                    LoggingIn(email1, pass1);
                }
                else{

                    if(email1.isEmpty()) {

                        email.setError("Email required");
                        email.setHint("please enter email id");
                        email.setHintTextColor(Color.RED);
                    }

                    if(pass1.isEmpty())
                    {
                        email.setError("password required");
                        pass.setHint("please enter password");
                        pass.setHintTextColor(Color.RED);

                    }
                }

            }
        });
    }
    private void LoggingIn(String email1, String pass1)
    {
        f_auth.signInWithEmailAndPassword(email1, pass1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithEmail:success");
                            pdialog.dismiss();
                            FirebaseUser user =f_auth.getCurrentUser();

                            Toast.makeText(loginretailer.this, ("logging in "+ user.getUid()),
                                    Toast.LENGTH_SHORT).show();

                            Intent log_int=new Intent(loginretailer.this,main2retail.class);
                            log_int.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(log_int);
                            finish();

                            // updateUI(user);
                        }
                        else
                        {
                            pdialog.hide();
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(loginretailer.this, "Email or password is invalid",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
