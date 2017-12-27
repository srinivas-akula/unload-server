package com.sringa.api.resource;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.traccar.api.BaseObjectResource;

import com.sringa.data.model.Vehicle;

@Path("vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleResource extends BaseObjectResource<Vehicle> {

    public VehicleResource() {
        super(Vehicle.class);
    }

    @GET
    public Collection<Vehicle> get(@QueryParam("userId") long userId) {
        return null;

    }

}
