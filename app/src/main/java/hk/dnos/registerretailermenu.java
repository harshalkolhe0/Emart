package hk.dnos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class registerretailermenu extends AppCompatActivity {

    Button checkmenu,uploaditem;
    EditText name,price;
    String n="name" ;
    String p="price";
    //String [] item = new String[50];

    String namestr;
    String priceint;
    String emailStr;
    private FirebaseFirestore firestore;
    static int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerretailermenu);

        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        checkmenu=findViewById(R.id.menucheck);
        uploaditem=findViewById(R.id.uploaditem);

        //f_auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        uploaditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namestr = name.getText().toString();
                priceint = price.getText().toString();


                //item[i] = namestr + "    " + priceint;
                name.setText("");
                price.setText("");
                Map<String, Object> data = new HashMap<>();
                data.put("menuName", namestr);
                data.put("menuPrice", priceint);
                i++;
                emailStr=getIntent().getStringExtra("email");
                firestore.collection("shops").document(emailStr).collection("MenuCard").document().set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(registerretailermenu.this, "data uploaded successfully", Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(registerretailermenu.this, "cannot upload data", Toast.LENGTH_LONG).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(registerretailermenu.this, "cannot upload data+" + e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

                checkmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(registerretailermenu.this,Customerorretailer.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }
    String returnnewn(int i)
        {
        String m=n+i;
        return m;
    }
    String returnnewp(int i)
    {
        String m=n+i;
        return m;
    }
}
