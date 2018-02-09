package org.traccar.geolocation;

import java.util.HashMap;
import java.util.Map;

public enum FixedLocation {

    Hyderabad("Hyderabad", 17.553267, 78.476191), Chennai("Chennai", 13.050163,
            80.239213), Bengaluru("Bengaluru", 12.968321, 77.617330), Thiruvananthapuram(
                    "Thiruvananthapuram", 8.507536, 76.941330), Puducherry("Puducherry", 11.942114,
                            79.817304), Vijayawada("Vijayawada", 16.493834, 80.671487), Tirupati(
                                    "Tirupati", 13.624729,
                                    79.420452), Vizag("Vizag", 17.727240, 83.277550);

    private final String place;
    private final double lat;
    private final double lon;

    private static final Map<String, FixedLocation> LOOKUP = new HashMap<>();

    static {
        for (FixedLocation location : FixedLocation.values()) {
            LOOKUP.put(location.getPlace().toLowerCase(), location);
        }
    }

    FixedLocation(String place, double lat, double lon) {
        this.place = place;
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getPlace() {
        return place;
    }

    public static FixedLocation get(String place) {
        return LOOKUP.get(place.toLowerCase());
    }
}
