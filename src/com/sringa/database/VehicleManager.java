package com.sringa.database;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.traccar.Context;
import org.traccar.database.BaseObjectManager;
import org.traccar.database.DataManager;
import org.traccar.database.ManagableObjects;

import com.sringa.data.model.Vehicle;

public class VehicleManager extends BaseObjectManager<Vehicle> implements ManagableObjects {

    private final Map<String, Long> vehicleNumbers;

    public VehicleManager(DataManager dataManager) {
        super(dataManager, Vehicle.class);
        vehicleNumbers = new ConcurrentHashMap<>();
        for (Vehicle item : getAll()) {
            vehicleNumbers.put(item.getNumber(), item.getId());
        }
    }

    @Override
    public void addItem(Vehicle item) throws SQLException {
        super.addItem(item);
        vehicleNumbers.put(item.getNumber(), item.getId());
    }

    @Override
    protected void updateCachedItem(Vehicle item) {
        Vehicle cached = getById(item.getId());
        super.updateCachedItem(item);
        vehicleNumbers.put(item.getNumber(), item.getId());
        if (!cached.getNumber().equals(item.getNumber())) {
            vehicleNumbers.remove(cached.getNumber());
        }
    }

    @Override
    protected void removeCachedItem(long id) {
        Vehicle cached = getById(id);
        if (null != cached) {
            super.removeCachedItem(id);
            vehicleNumbers.remove(cached.getNumber());
        }
    }

    @Override
    public Set<Long> getUserItems(long userId) {
        if (Context.getPermissionsManager() != null) {
            return Context.getPermissionsManager().getVehiclePermissions(userId);
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public Set<Long> getManagedItems(long userId) {
        Set<Long> result = new HashSet<>();
        result.addAll(getUserItems(userId));
        for (long managedUserId : Context.getUsersManager().getUserItems(userId)) {
            result.addAll(getUserItems(managedUserId));
        }
        return result;
    }

    public Long getIdByNumber(String number) {
        return vehicleNumbers.get(number);
    }
}
