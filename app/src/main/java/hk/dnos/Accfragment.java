package hk.dnos;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public  class Accfragment extends android.support.v4.app.Fragment
{
    private static final String TODO ="" ;
    private TextView name,email,phone,user;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private Uri uri;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    private CardView changePass;
    private Bitmap img;
    private String sname="",semail,sphone,sadress;

    private ProgressDialog pd;

    public Accfragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_accfragment, container, false);

        pd=new ProgressDialog(this.getContext());
        pd.setTitle("Account");
        pd.setMessage("Please Wait");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        imageView=v.findViewById(R.id.profile_image);
        name=v.findViewById(R.id.username1);
        email=v.findViewById(R.id.email1);
        phone=v.findViewById(R.id.mobile1);
        user=v.findViewById(R.id.Username);
        changePass=v.findViewById(R.id.changePassCard);

        firestore=FirebaseFirestore.getInstance();
        /*
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
*/
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        semail=firebaseUser.getEmail();
        sphone=firebaseUser.getPhoneNumber();


        if(semail!=null)
        {
            email.setText(semail);
        }

        if (sphone!=null)
        {
            if(sphone.length()>0) {
                phone.setText(sphone);
            }
            else
            {
                phone.setText("not provided");
            }
        }

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),forgetpassword.class);
                startActivity(intent);
            }
        });


        DocumentReference docRef = firestore.collection("customer").document("cust_"+firebaseUser.getEmail());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                String suri;

                if (task.isSuccessful())
                {
                    sname=task.getResult().getString("name");
                    //suri=  task.getResult().getString("imgUri");



                    if (sname!=null) {
                        name.setText(sname);
                        user.setText(sname);
                    }

                    /*
                    if(suri!=null) {
                        uri=Uri.parse(suri);

                        Picasso.get().load(uri).into(imageView);

                        pd.hide();
                        pd.dismiss();
                    }
                    */

                }
            }
        });


        return v;
    }

}
