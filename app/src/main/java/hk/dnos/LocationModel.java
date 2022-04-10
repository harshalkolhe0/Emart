package hk.dnos;

import com.google.firebase.firestore.GeoPoint;

public class LocationModel {

    GeoPoint Location,ShopName,OwnerName;

    public LocationModel() {
    }



    public LocationModel(GeoPoint location, GeoPoint shopName, GeoPoint ownerName) {
        Location = location;
        ShopName = shopName;
        OwnerName = ownerName;
    }

    public GeoPoint getLocation() {
        return Location;
    }

    public void setLocation(GeoPoint location) {
        Location = location;
    }

    public GeoPoint getShopName() {
        return ShopName;
    }

    public void setShopName(GeoPoint shopName) {
        ShopName = shopName;
    }

    public GeoPoint getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(GeoPoint ownerName) {
        OwnerName = ownerName;
    }
}
