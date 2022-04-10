package hk.dnos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.annotation.Nullable;

public class MenuCardActivity extends AppCompatActivity {


    private FirebaseAuth f_auth;
    private FirebaseFirestore fs;
    public String TAG="1123";
    private CheckBox menuCheck;
    private TextView menuPrice;
    private EditText email,pass;
    private List<MenuModel> menu;
    private android.support.v7.widget.Toolbar mtoolbar;
    private String emailstr;
    private Button Buy;
    private RecyclerView menuRecycleView;
    private MenuAdapter adapter;
    private ProgressDialog pd;
    private AlertDialog.Builder alert;
    private static int ord=0;
    public String price;
    public static int ii=12;
    public orderno or;
    public static List<String> shopEmail=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_card);

        menu=new ArrayList<>();
        menuPrice=findViewById(R.id.MenuPrice);
        menuCheck=findViewById(R.id.MenuCheck);

        alert= new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);

        //Alert Dialogue

        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getApplicationContext());
        }

        pd=new ProgressDialog(this);

        Buy=findViewById(R.id.signin);

        fs= FirebaseFirestore.getInstance();
        f_auth=FirebaseAuth.getInstance();


//
        mtoolbar=findViewById(R.id.MenuCard_toolbar);
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("MenuCard");

        menuRecycleView=findViewById(R.id.MenuRecycle);
        menuRecycleView.setHasFixedSize(true);
        menuRecycleView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        adapter=new MenuAdapter(menu);
        menuRecycleView.setAdapter(adapter);

        Bundle data=getIntent().getExtras();

        emailstr= Objects.requireNonNull(data).getString("email");

        //      emailstr= Objects.requireNonNull(f_auth.getCurrentUser()).getEmail();


        Toast.makeText(this.getApplicationContext(), "retailer email: " + emailstr, Toast.LENGTH_LONG).show();

        fs.collection("shops").document(emailstr)
                .collection("MenuCard").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                {
                    Toast.makeText(MenuCardActivity.this, "Error in fetching menu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                else
                {

                    for(DocumentChange doc: Objects.requireNonNull(queryDocumentSnapshots).getDocumentChanges())
                    {

                        if (doc.getType().equals(DocumentChange.Type.ADDED))
                        {

                            MenuModel model=doc.getDocument().toObject(MenuModel.class);
                            menu.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
            }


        });


        Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.setMessage("wait until Placing order");
                pd.setCanceledOnTouchOutside(false);
                pd.show();


                if (MenuAdapter.flag == 1) {

                    Bundle data = new Bundle();
                    data.putString("TotalPrice", String.valueOf(MenuAdapter.Totalpraice));
                    data.putString("shopEmail",emailstr);

                    //shopEmail.add(emailstr);

                    //MenuAdapter.flag=0;
                    Map<String,Object> shopEmail=new HashMap<>();
                    shopEmail.put("shopEmail",emailstr);
                    Date currentTime = Calendar.getInstance().getTime();
                    shopEmail.put("date",currentTime);

                    fs.collection("orderPlaced").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail()).collection("ShopEmails")
                            .document(emailstr).set(shopEmail,SetOptions.merge())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MenuCardActivity.this, "shop email added: "+emailstr, Toast.LENGTH_LONG).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MenuCardActivity.this, "failed to add email: "+emailstr, Toast.LENGTH_LONG).show();

                        }
                    });


                    //final int[] pp = new int[1];
                    String s=givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect();
                    Map<String,Object> daa=new HashMap<>();

                    //set the dates
                    currentTime = Calendar.getInstance().getTime();
                    daa.put("date",currentTime);
                    fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail()).collection("order").document(emailstr).collection("menu").document(s).set(daa, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(MenuCardActivity.this, "data uploaded successfully", Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(MenuCardActivity.this, "cannot upload data", Toast.LENGTH_LONG).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MenuCardActivity.this, "cannot upload data+" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });


                    for (int i=0;i<MenuAdapter.orderList.size();i++) {

                        Toast.makeText(MenuCardActivity.this, "menu: "+String.valueOf(MenuAdapter.orderList.get(i).values().iterator().next()), Toast.LENGTH_LONG).show();

                        /*
                        fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail())
                                .collection("order").document(emailstr).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        String kk;
                                        kk=document.getData().toString();
                                        ii= Integer.parseInt(kk.substring(4,kk.length()-1));


                                        Map<String,Object> dataa=new HashMap<>();
                                        dataa.put("Orderno",ii);
                                        Map<String,Object> dataaa=new HashMap<>();
                                        dataaa.put("ii",ii+1);

                                        //for setting value of ii
                                        fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail()).collection("order").document(emailstr).set(dataaa, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MenuCardActivity.this, "data uploaded successfully", Toast.LENGTH_LONG).show();


                                                } else {
                                                    Toast.makeText(MenuCardActivity.this, "cannot upload data", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(MenuCardActivity.this, "cannot upload data+" + e.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });

                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }


                               // pp[0] =ii;

                            }


                        });

*/
                        //                      for setting actual menu in menues

                        fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail())
                                .collection("order").document(emailstr).collection("menu").document(s).collection("menues").document(String.valueOf(i)).set(MenuAdapter.orderList.get(i), SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {


                            @Override

                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(MenuCardActivity.this, "Successfully order placed for i "+ii, Toast.LENGTH_LONG).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                if (e != null) {
                                    Toast.makeText(MenuCardActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                        String lmn=givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect();
                        fs.collection("notification").document(emailstr).collection("latest").document(lmn).set(MenuAdapter.orderList.get(i),SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(MenuCardActivity.this, "Order Stored at database ", Toast.LENGTH_LONG).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e != null) {
                                    Toast.makeText(MenuCardActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });


                        /*


                        fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail())
                                .collection("order").document(emailstr).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){

                        });
*/


                        //ii++;
                        /*
                        Map<String,Object> dataa=new HashMap<>();
                        dataa.put("ii",ii+1);
                        fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail()).collection("order").document(emailstr).collection("menu").document(String.valueOf(ii++)).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    pd.hide();
                                    pd.dismiss();
                                    ord++;
                                    //  MainActivity.ord++;
                                    Toast.makeText(MenuCardActivity.this, "Successfully order placed", Toast.LENGTH_LONG).show();


                                } else {
                                    Toast.makeText(MenuCardActivity.this, "cannot upload data", Toast.LENGTH_LONG).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(MenuCardActivity.this, "cannot upload data+" + e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                       */
                    }



                    Intent intent = new Intent(MenuCardActivity.this, MainActivity.class);
                    intent.putExtras(data);
                    //intent.putExtra("email",emailstr);
                    startActivity(intent);



                }
                else
                {

                    Toast.makeText(MenuCardActivity.this,"You have not selected any menu",Toast.LENGTH_LONG).show();

/*

                    builder.setTitle("Insufficient data")
                            .setMessage("Please select menu item")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
*/
                }
            }


        });

    }
    public String givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

}


// Toast.makeText(MenuCardActivity.this, "data added:  "+obj.keySet().iterator().next(), Toast.LENGTH_LONG).show();

                        /*
                        fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail())
                                .collection("order").document(emailstr).collection("menu").document(String.valueOf(i)).set(MenuAdapter.orderList.get(i), SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    pd.hide();
                                    pd.dismiss();
                                    ord++;
                                    //  MainActivity.ord++;
                                    Toast.makeText(MenuCardActivity.this, "Successfully order placed", Toast.LENGTH_LONG).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                if (e != null) {
                                    Toast.makeText(MenuCardActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                        */

/*
                        fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail())
                                .collection("order").document(emailstr).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {


                                //or=documentSnapshot.toObject(orderno.class);
                                //ii=or.ii;
                                //ii++;
                                //or.setIi(ii);
                                String kk;
                                kk=documentSnapshot.getData().toString();
                                ii=Integer.parseInt(kk.substring(4,kk.length()-2));
                                Map<String,Object> dataa=new HashMap<>();
                                dataa.put("Orderno",ii);
                                Map<String,Object> dataaa=new HashMap<>();
                                dataaa.put("ii",ii+1);

                                //for setting value of ii
                                fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail()).collection("order").document(emailstr).set(dataaa, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(MenuCardActivity.this, "data uploaded successfully", Toast.LENGTH_LONG).show();


                                        } else {
                                            Toast.makeText(MenuCardActivity.this, "cannot upload data", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(MenuCardActivity.this, "cannot upload data+" + e.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                });

*/

//for setting order no in menues
/*
                                fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail())
                                        .collection("order").document(emailstr).collection("menu").document(String.valueOf(ii)).collection("menues").document(String.valueOf(ii)).set(dataa, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            pd.hide();
                                            pd.dismiss();
                                            ord++;
                                            //  MainActivity.ord++;
                                            Toast.makeText(MenuCardActivity.this, "Successfully order placed", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        if (e != null) {
                                            Toast.makeText(MenuCardActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
*/
/*                       for setting order no in menues
                                fs.collection("Orders").document(Objects.requireNonNull(f_auth.getCurrentUser()).getEmail()).collection("order").document(emailstr).collection("menu").document(String.valueOf(ii)).collection("menues").document().set(dataa, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            pd.hide();
                                            pd.dismiss();
                                            ord++;
                                            //  MainActivity.ord++;
                                            Toast.makeText(MenuCardActivity.this, "Successfully order placed", Toast.LENGTH_LONG).show();


                                        } else {
                                            Toast.makeText(MenuCardActivity.this, "cannot upload data", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(MenuCardActivity.this, "cannot upload data+" + e.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                });

                                Toast.makeText(MenuCardActivity.this, "Order no  "+ii, Toast.LENGTH_LONG).show();
*/

//     }
// });