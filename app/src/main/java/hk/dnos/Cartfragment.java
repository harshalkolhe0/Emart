package hk.dnos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import static hk.dnos.registerretailermenu.i;

public class Cartfragment extends Fragment {

    FirebaseFirestore fs;
    FirebaseAuth f_auth;
    TextView menuName,menuPrice;
    List<String> shopemails;
    List<cartMenuModel> menuData;

    cartMenuAdapter adapter;

    public static int total=0;
    String TAG="1222";
    RecyclerView recyclerView;
    String email;
    String emailStr;
    private Button buybtn;
    public static int ii=12;

    orderno or;


    public  static String menuShop;
    public Cartfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.activity_cartfragment, container, false);

        f_auth=FirebaseAuth.getInstance();
        fs=FirebaseFirestore.getInstance();

        buybtn=v.findViewById(R.id.buy);

        menuName=v.findViewById(R.id.OrderName);
        menuPrice=v.findViewById(R.id.OrderPrice);

        menuData=new ArrayList<>();



        Bundle d= Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //  totalprice.setText(Objects.requireNonNull(d).getString("TotalPrice"));

        recyclerView=v.findViewById(R.id.cartRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter=new cartMenuAdapter(menuData);

        recyclerView.setAdapter(adapter);

        email=Objects.requireNonNull(f_auth.getCurrentUser()).getEmail();
        Toast.makeText(getActivity(), "Current user: " + email, Toast.LENGTH_LONG).show();


        shopemails=new ArrayList<>();


        fs.collection("orderPlaced").document(email).collection("ShopEmails").orderBy("date", Query.Direction.DESCENDING).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                for (DocumentChange doc: Objects.requireNonNull(queryDocumentSnapshots).getDocumentChanges())
                {
                    if (doc.getType().equals(DocumentChange.Type.ADDED))
                    {
                        shopemails.add(doc.getDocument().getId().toString());
                        Toast.makeText(getActivity(), "email fethed: " + doc.getDocument().getId().toString(), Toast.LENGTH_LONG).show();

                    }
                }
                //String p=shopemails.get(0);
                //int t=p.indexOf("shopEmail");

                //t=t+10;
                //String em=p.substring(t,p.length()-1);

                fs.collection("Orders").document(email).collection("order")
                        .document(shopemails.get(0)).collection("menu").orderBy("date", Query.Direction.DESCENDING).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {


                    @Override
                    public void onEvent(@Nullable final QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        List<String> random;
                        random=new ArrayList<>();
                        for (DocumentChange doc: Objects.requireNonNull(queryDocumentSnapshots).getDocumentChanges())
                        {
                            if (doc.getType().equals(DocumentChange.Type.ADDED))
                            {
                                random.add(doc.getDocument().getId().toString());
                                //Toast.makeText(getActivity(), "email fethed: " + doc.getDocument().getId().toString(), Toast.LENGTH_LONG).show();

                            }
                        }

                        fs.collection("Orders").document(email).collection("order")
                                .document(shopemails.get(0)).collection("menu").document(random.get(0)).collection("menues").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                int flag=1;
                                if(task.isSuccessful())
                                {
                                    QuerySnapshot docu=task.getResult();
                                    for (DocumentChange doc : Objects.requireNonNull(docu).getDocumentChanges()) {

                                        if (doc.getType().equals(DocumentChange.Type.ADDED)) {

                                            cartMenuModel model = doc.getDocument().toObject(cartMenuModel.class);
                                            menuData.add(model);

                                            if(flag==1)
                                            {
                                                //String s=doc.getDocument().toString();
                                                //String q=s.substring(s.length()-12,s.length());
                                                //String p=q.substring(q.length()/2,q.length());
                                                //String r=p.substring(p.length()/2,p.length());
                                                //int a=q.indexOf("total");
                                                //int b=q.indexOf("}");
                                                //String res=q.substring(a+6,q.length());
                                                total=MenuAdapter.sum;

                                            }

                                          //  Toast.makeText(getActivity(), "data: " +doc.getDocument().getData() , Toast.LENGTH_LONG).show();

                                        }

                                    }
                                    adapter.notifyDataSetChanged();
                                }


                            }
                        });


                    }

                });
            }
        });






        //emailStr=getIntent().getStringExtra("email");
/*
        fs.collection("orderPlaced").document(email).collection("ShopEmails")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        for (DocumentChange doc: Objects.requireNonNull(queryDocumentSnapshots).getDocumentChanges())
                        {
                            if (doc.getType().equals(DocumentChange.Type.ADDED))
                            {
                                shopemails.add(doc.getDocument().getId());
                                Toast.makeText(getActivity(), "email fethed: " + doc.getDocument().getId().toString(), Toast.LENGTH_LONG).show();

                            }
                        }

                       // Map<String, Object> data = new HashMap<>();
                        //emailStr=getIntent().getStringExtra("email");






                        if(shopemails.size()>0)

                        {
                            for (int i = 0;i<shopemails.size();i++ )


                                fs.collection("Orders").document(email).collection("order")
                                        .document(shopemails.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                String kk,ll;
                                                kk=document.getData().toString();
                                                int a,b;
                                                a=kk.indexOf('=');
                                                b=kk.indexOf('}');
                                                ll=kk.substring(a+1,b);
                                                //ii++;
                                                ii=Integer.parseInt(kk.replaceAll("[\\D]",""));

                                                //ii= Integer.parseInt(kk.substring(a+1,b));
                                                Toast.makeText(getActivity(),"value of ii "+ ii,Toast.LENGTH_LONG).show();
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }

                                    }
                                });
                                */

/*
                                fs.collection("Orders").document(email).collection("order")
                                        .document(shopemails.get(i)).collection("menu").document(String.valueOf(ii)).collection("menues")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

/*
                        for(DocumentSnapshot doc: Objects.requireNonNull(queryDocumentSnapshots).getDocuments())
                        {

                        }
  */
/*
                                                for (DocumentChange doc : Objects.requireNonNull(queryDocumentSnapshots).getDocumentChanges()) {

                                                    if (doc.getType().equals(DocumentChange.Type.ADDED)) {

                                                        cartMenuModel model = doc.getDocument().toObject(cartMenuModel.class);
                                                        menuData.add(model);

                                                        Toast.makeText(getActivity(), "data: " +doc.getDocument().getData() , Toast.LENGTH_LONG).show();


                                                    }


                                                }


                                                adapter.notifyDataSetChanged();

                                                //       totalprice.setText(cartMenuAdapter.Totalpraice);
                                            }
                                        });
        */
/*
                            fs.collection("Orders").document(email).collection("order")
                                    .document(shopemails.get(i)).collection("menu").document().collection("menues").orderBy("date", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    //document = task.getResult();
                                                    String k = document.getData().toString();
                                                    //menuData.add(model);

                                                    cartMenuModel model = document.toObject(cartMenuModel.class);
                                                    menuData.add(model);

                                                    //int a, b;
                                                    //a=k.indexOf("total");

                                                    //b=k.indexOf(',',a+1);
                                                    //a=a+6;
                                                    //total=total+Integer.parseInt(k.substring(a,b));

                                                    //Toast.makeText(getActivity(), "data: " + k.substring(a,b), Toast.LENGTH_LONG).show();
                                                }
                                                adapter.notifyDataSetChanged();

                                            }

                                        }


                                    });
                                    */
           /*
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"You dont have any order",Toast.LENGTH_LONG).show();
                        }



                    }

                });

*/



        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fs.collection("Orders").document("user_"+email).collection("order")
                        .document("shop_"+ MenuCardActivity.shopEmail).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getActivity(),"order served ",Toast.LENGTH_LONG).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"failed tp serve order ",Toast.LENGTH_LONG).show();

                    }
                });


                //Bundle data = new Bundle();
                //Map<String,Object> da=new HashMap<>();
                //da.put("total",total);

                //data.putString("TotalPrice", String.valueOf(cartMenuAdapter.Totalpraice));

                Bundle da=new Bundle();
                da.putString("total",String.valueOf(total));
                Intent intent = new Intent(getActivity(), BillActivity.class);
                intent.putExtras(da);
                startActivity(intent);
            }
        });

        return v;
    }



}

