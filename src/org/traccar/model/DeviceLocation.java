package org.traccar.model;

public class DeviceLocation {

    private String id;
    private String state;
    private String city;
    private int load;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int loadStatus) {
        this.load = loadStatus;
    }
}
