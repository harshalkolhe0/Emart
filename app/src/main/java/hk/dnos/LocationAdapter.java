package hk.dnos;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class LocationAdapter  {

    List<LocationModel> shoploc;
    GoogleMap map;

    public LocationAdapter(List<LocationModel> shoploc, GoogleMap map) {
        this.shoploc = shoploc;
        this.map = map;
    }


}
