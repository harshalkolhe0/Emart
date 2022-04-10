package hk.dnos;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Main2Activity extends Fragment /*implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleApiClient.OnConnectionFailedListener */ {

    private Button showmap;

    /*
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "123";
    private static final int PROXIMITY_RADIUS = 1500;
    private GoogleMap mMap;
    private Button search;
    private boolean permission = false;
    private GoogleApiClient apiClient;
    private android.support.v7.widget.Toolbar mtoolbar;
    private hk.dnos.GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    private PlaceAutocompleteFragment autocompleteFragment;

    private FirebaseAuth f_auth;
    private FirebaseFirestore fs;

    private List<hk.dnos.LocationModel> shoploc;
    private LocationAdapter adapter;
    private List<GeoPoint> shopPoint;
    private List<String> Ownername,Shopname;



    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

*/

    public Main2Activity() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_main2, container, false);

        showmap = v.findViewById(R.id.showmap);

        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), notification.class);
                startActivity(intent);

            }
        });


/*
        mtoolbar=v.findViewById(R.id.Map_toolbar);

        f_auth=FirebaseAuth.getInstance();
        fs=FirebaseFirestore.getInstance();

        Shopname=new ArrayList<>();
        Ownername=new ArrayList<>();
        shopPoint=new ArrayList<>();

        //     adapter=new LocationAdapter(shoploc,mMap);
        // Construct a hk.dnos.GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(Objects.requireNonNull(this.getActivity()), null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this.getActivity(), null);
/*
        apiClient = new GoogleApiClient
                .Builder(this.getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this.getActivity(), this)
                .build();

*/

/*
        requestPermission();


        if (isServiceOk() && permission) {

            SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);



        }


        */

        return v;
    }
}
