package hk.dnos;

import android.app.Fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Addmenu extends android.support.v4.app.Fragment {

    Button checkmenu,uploaditem;
    EditText name,price;
    String n="name" ;
    String p="price";
    //String [] item = new String[50];

    String namestr;
    String priceint;
    String emailStr;
    private FirebaseAuth f_auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    static int i=0;


    public Addmenu() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_addmenu, container, false);

        name=v.findViewById(R.id.name);
        price=v.findViewById(R.id.price);
        //checkmenu=v.findViewById(R.id.menucheck);
        uploaditem=v.findViewById(R.id.uploaditem);

        //f_auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        f_auth = FirebaseAuth.getInstance();

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
                user =f_auth.getCurrentUser();
                emailStr=user.getEmail();
                //emailStr=getIntent().getStringExtra("email");
                firestore.collection("shops").document(emailStr).collection("MenuCard").document().set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "data uploaded successfully", Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(getContext(), "cannot upload data", Toast.LENGTH_LONG).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), "cannot upload data+" + e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

                //checkmenu.setOnClickListener(new View.OnClickListener() {
                 //   @Override
                //    public void onClick(View v) {
                //        Intent intent=new Intent(getContext(),main2retail.class);
                //        startActivity(intent);
                //    }
                //});
            }
        });

        return v;
    }
}
