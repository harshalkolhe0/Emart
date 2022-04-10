package hk.dnos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class registerretailer extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mtoolbar;
    private EditText name, email, pass, mob, confpass,shopname;
    private ProgressDialog pd;
    private FirebaseAuth f_auth;
    private FirebaseUser f_user;
    String phone;
    private RadioButton rbutton;
    private Button signup;
    private GeoPoint location;
    protected LocationManager locationManager;
    private FirebaseFirestore firestore;
    String nameStr, emailStr, passStr, mobStr, confpassStr,shopstr;
    private FusedLocationProviderClient client;
    private Double lat, lon;
    private Bundle ownerdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerretailer);

        mtoolbar = findViewById(R.id.reg_toolbar);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Step 1");


        pd = new ProgressDialog(this);

        //Edittexts
        name = findViewById(R.id.regName);
        email = findViewById(R.id.regEmail);
        pass = findViewById(R.id.regPass);
        mob = findViewById(R.id.regMob);
        shopname = findViewById(R.id.shopname);
        confpass = findViewById(R.id.regConfpass);
        signup = findViewById(R.id.regSignup);
        f_auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        rbutton = findViewById(R.id.rbutton);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameStr = name.getText().toString();
                emailStr = email.getText().toString();
                passStr = pass.getText().toString();
                mobStr = mob.getText().toString();
                confpassStr = confpass.getText().toString();
                shopstr=shopname.getText().toString();
                phone = mobStr;

                if (validate()) {


                    if (passStr.equals(confpassStr)) {



                        createUser(nameStr, emailStr, mobStr, passStr, confpassStr,shopstr);
                        Intent activity = new Intent(registerretailer.this, registerretailermenu.class);

                        activity.putExtra("email",emailStr);
                        startActivity(activity);

                    } else {
                        confpass.setError("password does not match");
                    }

                } else {

                    if (name.getText().length() == 0) {
                        name.setError("Required");
                        Toast.makeText(registerretailer.this, "Name field is empty", Toast.LENGTH_LONG).show();
                    }
                    if (mob.getText().length() == 0) {
                        mob.setError("Required");
                    }
                    if (email.getText().length() == 0) {
                        email.setError("Required");
                    }
                    if (pass.getText().length() == 0) {
                        pass.setError("Required");
                    }

                    if (confpass.getText().length() == 0) {
                        confpass.setError("Required");
                    }
                    if (shopname.getText().length() == 0) {
                        shopname.setError("Required");
                    }
                }

            }
        });
    }
    private boolean validate() {

        if ((name.getText().length() > 0) && (mob.getText().length() > 0) && (pass.getText().length() > 0) && (email.getText().length() > 0) && (confpass.getText().length() > 0) && (shopname.getText().length() > 0) ) {
            return true;
        } else {
            return false;
        }
    }


    private void createUser(final String nameStr, final String emailStr, final String mobStr, String passStr, String confpassStr,final String shopstr) {


        f_auth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Map<String, Object> data = new HashMap<>();
                    /*
                    data.put("name", nameStr);
                    data.put("email", emailStr);
                    data.put("phone", phone);
                    data.put("latitude",lat);
                    data.put("longitude",lon);
                    data.put("shop",shopstr);
                    */
                    location = new GeoPoint(lat, lon);

                    data.put("OwnerName", nameStr);
                    data.put("ShopName", shopstr);
                    data.put("ShopEmail", emailStr);
                    data.put("ShopPhone", phone);
                    //data.put("OwnerPass", confpassstr);
                    data.put("Location", location);

                    firestore.collection("shops").document(emailStr).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(registerretailer.this, "data uploaded successfully", Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(registerretailer.this, "cannot upload data", Toast.LENGTH_LONG).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(registerretailer.this, "cannot upload data+" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });

                    Toast.makeText(registerretailer.this, "account successfully created", Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(registerretailer.this, "Error in registering", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                return;
            }
            client.getLastLocation().addOnSuccessListener(registerretailer.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location!=null)
                    {
                        lat=location.getLatitude();
                        lon=location.getLongitude();
                        Toast.makeText(registerretailer.this,"Location"+location.getLatitude()+"  "+location.getLongitude(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(registerretailer.this,new String[]{ACCESS_FINE_LOCATION},1);
    }
}
