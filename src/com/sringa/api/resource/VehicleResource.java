package com.sringa.api.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.traccar.Context;
import org.traccar.api.BaseObjectResource;

import com.sringa.data.model.Vehicle;
import com.sringa.database.VechicleManager;

@Path("vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleResource extends BaseObjectResource<Vehicle> {

    public VehicleResource() {
        super(Vehicle.class);
    }

    @GET
    public Collection<Vehicle> get(@QueryParam("userId") Long userId) {

        VechicleManager vehicleManager = Context.getVehicleManager();
        Set<Long> result = new HashSet<>();
        // for admin users
        if (null != userId && Context.getPermissionsManager().isAdmin(getUserId())) {
            result = vehicleManager.getManagedItems(userId);
        } else {
            // for non admin users
            Context.getPermissionsManager().checkManager(getUserId());
            result = vehicleManager.getManagedItems(getUserId());
        }
        return vehicleManager.getItems(result);
    }
}
