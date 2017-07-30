package cz.pribula.meteorites.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Meteorite extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private String nameType;
    private double mass;
    private String timestamp;
    private String latitude;
    private String longitude;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() { return type;  }

    public void setType(String type) { this.type = type; }
}
