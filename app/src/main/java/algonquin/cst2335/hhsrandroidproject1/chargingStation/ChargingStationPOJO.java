package algonquin.cst2335.hhsrandroidproject1.chargingStation;

public class ChargingStationPOJO {

    private long id;

    private String title;

    private double latitude;

    private double longitude;

    private String phone;

    public ChargingStationPOJO(long id, String title, double latitude, double longitude, String phone) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }

    public ChargingStationPOJO(String title, double latitude, double longitude, String phone) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }
    public ChargingStationPOJO(){}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
