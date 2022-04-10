package hk.dnos;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "123";
    private static final int PROXIMITY_RADIUS = 1500;
    private GoogleMap mMap;
    private Button search;
    private boolean permission = false;
    private GoogleApiClient apiClient;
    private android.support.v7.widget.Toolbar mtoolbar;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    private PlaceAutocompleteFragment autocompleteFragment;

    private FirebaseAuth f_auth;
    private FirebaseFirestore fs;

    private List<LocationModel> shoploc;
    private LocationAdapter adapter;
    private List<GeoPoint> shopPoint;
    private List<String> Ownername,Shopname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //search = findViewById(R.id.findplacebtn);
        mtoolbar=findViewById(R.id.Map_toolbar);


        f_auth=FirebaseAuth.getInstance();
        fs=FirebaseFirestore.getInstance();

        Shopname=new ArrayList<>();
        Ownername=new ArrayList<>();
        shopPoint=new ArrayList<>();

        //     adapter=new LocationAdapter(shoploc,mMap);
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        apiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();



        requestPermission();


        if (isServiceOk() && permission) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        }
    }


    //request permission from user
    private void requestPermission() {


        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            permission = true;

        } else {
            permission = true;
            // Write you code here if permission already given.

        }

    }

    @Override
    public  void onStart() {

        // Add a marker in Sydney, Australia, and move the camera.
        super.onStart();

    }

    //creates map
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermission();
            return;
        }
        else {

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    String emailstr= marker.getSnippet();

                    Bundle data=new Bundle();
                    data.putString("email",emailstr);


                    Intent intent=new Intent(MapsActivity.this,MenuCardActivity.class);
                    intent.putExtras(data);
                    startActivity(intent);

                }
            });


            /*
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    return true;
                }
            });
*/

            mMap.setOnMyLocationClickListener(this);
            mMap.setOnMyLocationButtonClickListener(this);

            mMap.getUiSettings().setCompassEnabled(true);

            mMap.getUiSettings().setMapToolbarEnabled(true);

            mMap.getUiSettings().setZoomControlsEnabled(true);

            mMap.getCameraPosition();

            //    loadNearByPlaces(18.451582,73.884945);

        }


        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        //place autocomplete fragment
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                // mMap.clear();


                LatLng searchplace=new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);
                mMap.addMarker(new MarkerOptions().title(place.getName().toString()).position(searchplace).visible(true).snippet(place.getAddress().toString()));
                mMap.addCircle(new CircleOptions().center(searchplace).radius(10).fillColor(Color.BLUE));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(searchplace));

                Log.i(TAG, "Place: " + place.getName());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });




/*
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(MapsActivity.this);

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {

                    Log.i(TAG, "An error occurred: " + e.getMessage());
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {

                    Log.i(TAG, "An error occurred: " + e.getMessage());

                    // TODO: Handle the error.
                }

            }
        });

*/

        fs.collection("shops").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e("Error: " + e.getMessage(), "Error");
                } else {

                    for (DocumentSnapshot doc : Objects.requireNonNull(queryDocumentSnapshots).getDocuments()) {

                        shopPoint.add((GeoPoint) doc.getGeoPoint("Location"));
                        Ownername.add(doc.getString("OwnerName"));
                        Shopname.add(doc.getString("ShopName"));



                        Toast.makeText(MapsActivity.this,"doc found: "+doc.getString("OwnerName")+ doc.getString("ShopEmail"),Toast.LENGTH_LONG).show();

                        LatLng pos = new LatLng(Objects.requireNonNull(doc.getGeoPoint("Location")).getLatitude(), Objects.requireNonNull(doc.getGeoPoint("Location")).getLongitude());

                        com.google.android.gms.maps.model.MarkerOptions marker=new MarkerOptions().position(pos).title(doc.getString("ShopName")).snippet(doc.getString("ShopEmail"));
                        mMap.addMarker(marker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                        mMap.getMaxZoomLevel();

                        mMap.getFocusedBuilding();


                    }

                }
            }
        });


/*

        LatLng sydney = new LatLng(18.451582, 73.884945);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in kondhwa").snippet("Balaji stores"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getMaxZoomLevel();

        mMap.getFocusedBuilding();
*/

    }


    //checking compatibility of  google play services of user`s device
    public boolean isServiceOk() {

        int avaible = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);

        if (avaible == ConnectionResult.SUCCESS) {
            Toast.makeText(this.getApplicationContext(), "google play services working", Toast.LENGTH_LONG).show();

            return true;
        }

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(avaible))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());

            builder.setMessage("invalid google play services").setTitle("ERROR");

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            AlertDialog alertDialog = builder.create();


        }
        else
        {
            Toast.makeText(this.getApplicationContext(), "We cant make map request ", Toast.LENGTH_LONG).show();

        }
        return false;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }





    @Override
    public boolean onMyLocationButtonClick() {

        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "connection failed:\n" + connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();

    }
}

