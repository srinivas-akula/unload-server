package org.traccar.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.traccar.model.Location;

public class LocationsManager {

    private Map<String, GeoLocation> locations = new HashMap<>();

    public LocationsManager(DataManager dataManager) {

        try {
            final Collection<Location> locations = dataManager.getObjects(Location.class);
            for (Location loc : locations) {
                String cityState = loc.getCity() + "-" + loc.getState();
                GeoLocation gl = new GeoLocation(loc.getLatitude(), loc.getLongitude());
                this.locations.put(cityState, gl);
            }
        } catch (SQLException e) {

        }
    }

    public GeoLocation getLocation(String city, String state) {
        Objects.requireNonNull(city);
        Objects.requireNonNull(state);
        return locations.get(city + "-" + state);
    }

    public class GeoLocation {
        private final double lat;
        private final double lng;

        public GeoLocation(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}
