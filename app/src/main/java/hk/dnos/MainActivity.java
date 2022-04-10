package hk.dnos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    static int ord=0;
    private   android.support.v7.widget.Toolbar mtoolbar;
    FirebaseAuth f_auth;

    private BottomNavigationView nav;
    private FrameLayout frameLayout;


    private Homefragment homeFragment;
    private Accfragment accFragment;
    private Cartfragment cartFragment;

    String emailStr;

    private GoogleApiClient gac;

    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        f_auth=FirebaseAuth.getInstance();

        //toolbar
        mtoolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("DNOS");

        //fragment
        homeFragment=new Homefragment();
        accFragment= new Accfragment();
        cartFragment=new Cartfragment();


        //nav
        nav=findViewById(R.id.bottom_navigation);
        frameLayout=findViewById(R.id.mainFrame);




        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        gac=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.HomeNav:

                        setFragment(homeFragment);
                        return true;

                    case R.id.CartNav:
                        setFragment(cartFragment);
                        return true;

                    case R.id.AccountNav:
                        setFragment(accFragment);
                        return true;

                    default:    return false;

                }

            }
        });

    }

    private void setFragment(Fragment fragment) {

        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commit();

    }

    //*************************************************************************
    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser f_user=f_auth.getCurrentUser();

        if(f_user==null )
        {
            sendToStart();
        }
    }


    //******************************************************************8
    @Override

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }



    //*****************************************************************
    //MenuItems
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout_menu) {
            revokeAccess();
            signOut();
            /*
            Toast.makeText(MainActivity.this, ("Logging out" + f_auth.getCurrentUser().toString()), Toast.LENGTH_LONG).show();

            FirebaseAuth.getInstance().signOut();

            sendToStart();
            */
        }

        return true;
    }


    private void signOut() {

        Auth.GoogleSignInApi.signOut(gac).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

                Toast.makeText(MainActivity.this,"signed out",Toast.LENGTH_LONG).show();
                sendToStart();
            }
        });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(gac).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast.makeText(MainActivity.this,"Access Revoked",Toast.LENGTH_LONG).show();
            }
        });
    }

    //******************************************************************************8
    private void sendToStart() {
        Intent startIntent=new Intent(MainActivity.this,Customerorretailer.class);
        startActivity(startIntent);
        finish();
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this.getApplicationContext(),"connection Failed",Toast.LENGTH_LONG).show();
    }

}
