package org.traccar.model;

public class DeviceLocation {

    private String id;
    private String address;
    private int load;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int loadStatus) {
        this.load = loadStatus;
    }
}
