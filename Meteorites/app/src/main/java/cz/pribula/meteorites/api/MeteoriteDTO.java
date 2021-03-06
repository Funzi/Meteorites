package cz.pribula.meteorites.api;

import com.cocoahero.android.geojson.Point;
import com.google.gson.annotations.SerializedName;

public class MeteoriteDTO {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;
    @SerializedName("nametype")
    private String nameType;
    @SerializedName("mass")
    private double mass;
    @SerializedName("year")
    private String timestamp;
    @SerializedName("recclass")
    private String type;
    @SerializedName("reclat")
    private String latitude;
    @SerializedName("reclong")
    private String longitude;
    @SerializedName("geolocation_address")
    private String geolocationAddress;
    @SerializedName("geolocation_zip")
    private String geolocationZip;
    @SerializedName("geolocation_city")
    private String geolocationCity;
    @SerializedName("geolocation")
    private Point geolocation;
    @SerializedName("geolocation_state")
    private String geolocationState;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGeolocationAddress() {
        return geolocationAddress;
    }

    public void setGeolocationAddress(String geolocationAddress) {  this.geolocationAddress = geolocationAddress;  }

    public String getGeolocationZip() {
        return geolocationZip;
    }

    public void setGeolocationZip(String geolocationZip) {
        this.geolocationZip = geolocationZip;
    }

    public String getGeolocationCity() {
        return geolocationCity;
    }

    public void setGeolocationCity(String geolocationCity) { this.geolocationCity = geolocationCity;  }

    public Point getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Point geolocation) {
        this.geolocation = geolocation;
    }

    public String getGeolocationState() {
        return geolocationState;
    }

    public void setGeolocationState(String geolocationState) { this.geolocationState = geolocationState;   }

    public String getType() {  return type;  }

    public void setType(String type) {  this.type = type;  }
}
