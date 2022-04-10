package hk.dnos;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class notification extends AppCompatActivity {

    private RecyclerView recycler;
    public String emailStr;
    String TAG="TAG";
    private FirebaseFirestore fs;
    private FirebaseAuth f;
    public List<noteclass> noteData;
    public noteAdapter adapter;

    public notification(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        fs=FirebaseFirestore.getInstance();
        f=FirebaseAuth.getInstance();
        emailStr=f.getCurrentUser().getEmail();
        noteData=new ArrayList<>();


        recycler=findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter=new noteAdapter(noteData);

        recycler.setAdapter(adapter);



        /*
        fs.collection("notification").document(emailStr).collection("latest").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    QuerySnapshot docu=task.getResult();
                    for (DocumentChange doc : Objects.requireNonNull(docu).getDocumentChanges()) {

                        if (doc.getType().equals(DocumentChange.Type.ADDED)) {

                            cartMenuModel model = doc.getDocument().toObject(cartMenuModel.class);
                            menuData.add(model);


                        }

                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });

        */
        /*
        fs.collection("notification").document(emailStr).collection("latest").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                 if(e!=null){
                     Log.d(TAG,"Error"+e.getMessage());
                 }
                 else
                 {
                     for (DocumentChange doc : Objects.requireNonNull(queryDocumentSnapshots).getDocumentChanges()) {

                         if (doc.getType().equals(DocumentChange.Type.ADDED)) {

                             noteclass model = doc.getDocument().toObject(noteclass.class);
                             noteData.add(model);


                             adapter.notifyDataSetChanged();
                         }

                     }

                 }

            }

        });
        */
        fs.collection("notification").document(emailStr).collection("latest").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {
                    QuerySnapshot docu=task.getResult();
                    for (DocumentChange doc : Objects.requireNonNull(docu).getDocumentChanges()) {

                        if (doc.getType().equals(DocumentChange.Type.ADDED)) {

                            noteclass model = doc.getDocument().toObject(noteclass.class);
                            noteData.add(model);


                        }


                    }
                    adapter.notifyDataSetChanged();
                }


            }
        });

        

    }
}
