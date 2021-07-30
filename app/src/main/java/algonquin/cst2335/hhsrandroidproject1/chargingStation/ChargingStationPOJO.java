package algonquin.cst2335.hhsrandroidproject1.chargingStation;

/**
 * @author Hui Lyu
 * @version 1.0
 */
public class ChargingStationPOJO {

    /**
     * unique identifier for a car charging station
     */
    private long id;
    /**
     * title of a car charging station
     */
    private String title;
    /**
     * latitude of a car charging station
     */
    private double latitude;
    /**
     * longitude of a car charging station
     */
    private double longitude;
    /**
     * contact phone of a car charging station
     */
    private String phone;

    /**
     * Constructor to create a car charging station
     *
     * @param id        unique identifier for a station
     * @param title     name of the station
     * @param latitude  latitude of a car charging station
     * @param longitude longitude of a car charging station
     * @param phone     contact phone of a station
     */
    public ChargingStationPOJO(long id, String title, double latitude, double longitude, String phone) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }

    /**
     * Constructor to create a car charging station
     *
     * @param title     name of the station
     * @param latitude  latitude of a car charging station
     * @param longitude longitude of a car charging station
     * @param phone     contact phone of a station
     */
    public ChargingStationPOJO(String title, double latitude, double longitude, String phone) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }

    /**
     * no-argument constructor for a car charging station
     */
    public ChargingStationPOJO() {
    }

    /**
     * Method returns a latitude of a car charging station
     *
     * @return latitude of a station
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Method sets a latitude of a car charging station
     *
     * @param latitude latitude of a station
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Method returns a longitude of a car charging station
     *
     * @return longitude of a station
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Method sets a longitude of a car charging station
     *
     * @param longitude longitude of a station
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Method returns id of a charging station
     *
     * @return id unique identifier of a station
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Method returns a title of a car charging station
     *
     * @return title of a station
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method sets a title of a car charging station
     *
     * @param title name of a station
     */

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method returns a phone number of a car charging station
     *
     * @return contact phone of a station
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Method sets a phone number for a car charging station
     *
     * @param phone phone number of a station
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
