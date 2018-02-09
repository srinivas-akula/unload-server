/*
 * Copyright 2015 - 2017 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.traccar.api.resource;

import org.traccar.Context;
import org.traccar.api.BaseObjectResource;
import org.traccar.database.UsersManager;
import org.traccar.model.ManagedUser;
import org.traccar.model.User;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource extends BaseObjectResource<User> {

    public UserResource() {
        super(User.class);
    }

    @GET
    public Collection<User> get(@QueryParam("userId") long userId) throws SQLException {
        UsersManager usersManager = Context.getUsersManager();
        Set<Long> result = null;
        if (Context.getPermissionsManager().isAdmin(getUserId())) {
            if (userId != 0) {
                result = usersManager.getUserItems(userId);
            } else {
                result = usersManager.getAllItems();
            }
        } else if (Context.getPermissionsManager().getUserManager(getUserId())) {
            result = usersManager.getManagedItems(getUserId());
        } else {
            throw new SecurityException("Admin or manager access required");
        }
        return usersManager.getItems(result);
    }

    @Override
    @PermitAll
    @POST
    public Response add(User entity) throws SQLException {
        if (!Context.getPermissionsManager().isAdmin(getUserId())) {
            Context.getPermissionsManager().checkUserUpdate(getUserId(), new User(), entity);
            if (Context.getPermissionsManager().getUserManager(getUserId())) {
                Context.getPermissionsManager().checkUserLimit(getUserId());
            } else {
                Context.getPermissionsManager().checkRegistration(getUserId());
                entity.setDeviceLimit(
                        Context.getConfig().getInteger("users.defaultDeviceLimit", -1));
                int expirationDays = Context.getConfig().getInteger("users.defaultExpirationDays");
                if (expirationDays > 0) {
                    entity.setExpirationTime(new Date(
                            System.currentTimeMillis() + (long) expirationDays * 24 * 3600 * 1000));
                }
            }
        }
        Context.getUsersManager().addItem(entity);
        if (Context.getPermissionsManager().getUserManager(getUserId())) {
            Context.getDataManager().linkObject(User.class, getUserId(), ManagedUser.class,
                    entity.getId(), true);
        }
        Context.getUsersManager().refreshUserItems();
        return Response.ok(entity).build();
    }

    @PermitAll
    @Path("register")
    @POST
    public Response updatePwd(User entity) throws Exception {

        if (null == entity.getSalt() || null == entity.getHashedPassword()
                || null == entity.getPhone()) {
            throw new Exception("Phone or password is missing.");
        }
        User user = Context.getUsersManager().getByPhone(entity.getPhone());
        if (null == user) {
            throw new Exception("No User exists with phone: " + entity.getPhone());
        }
        user.setHashedPassword(entity.getHashedPassword());
        user.setSalt(entity.getSalt());
        Context.getUsersManager().updateItem(user);
        return Response.ok(entity).build();
    }

    @PermitAll
    @Path("app")
    @POST
    public Response addNewUser(User entity) throws Exception {

        if (null == entity.getPhone() || null == entity.getMode()
                || null == entity.getHashedPassword() || null == entity.getSalt()) {
            throw new Exception("Phone or Mode or Password is missing.");
        }
        entity.setUserLimit(1);
        final User user = Context.getUsersManager().getByPhone(entity.getPhone());
        final int limit = "D".equals(entity.getMode()) ? 1 : 20;
        if (null != user) {
            user.setDeviceLimit(limit);
            user.setVehiclelimit(limit);
            Context.getUsersManager().updateItem(user);
        } else {
            entity.setDeviceLimit(limit);
            entity.setVehiclelimit(limit);
            Context.getUsersManager().addItem(entity);
        }
        return Response.ok(entity).build();
    }

    @Override
    @Path("{id}")
    @DELETE
    public Response remove(@PathParam("id") String idStr) throws SQLException {

        final Set<Long> userDevices = Context.getDeviceManager().getUserItems(Long.valueOf(idStr));
        super.remove(idStr);
        if (!userDevices.isEmpty()) {
            for (Long id : userDevices) {
                Context.getDeviceManager().removeItem(id);
            }
        }
        return Response.noContent().build();
    }
}
