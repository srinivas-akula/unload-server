package com.sringa.database;

import java.util.HashSet;
import java.util.Set;

import org.traccar.Context;
import org.traccar.database.BaseObjectManager;
import org.traccar.database.DataManager;
import org.traccar.database.ManagableObjects;

import com.sringa.data.model.Vehicle;

public class VechicleManager extends BaseObjectManager<Vehicle> implements ManagableObjects {

    public VechicleManager(DataManager dataManager) {
        super(dataManager, Vehicle.class);
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
}
