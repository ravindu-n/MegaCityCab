/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.megacitycabbackend.resources;

import Driver.DriverOperations;
import Driver.Drivers;
import com.google.gson.Gson;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author ravin
 */
@Path("drivers")
public class DriverResource {
    private final Gson gson = new Gson();

    // ✅ Add a New Driver
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDriver(String json) {
        Drivers driver = gson.fromJson(json, Drivers.class);
        int driverId = DriverOperations.addDriver(driver);
        if (driverId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Driver added successfully\", \"id\": " + driverId + "}")
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to add driver\"}")
                .build();
    }

    // ✅ Get All Drivers
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDrivers() {
        List<Drivers> drivers = DriverOperations.getAllDrivers();
        return Response.ok(gson.toJson(drivers)).build();
    }

    // ✅ Delete Driver by ID
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDriver(@PathParam("id") int id) {
        int deleted = DriverOperations.deleteDriver(id);
        if (deleted > 0) {
            return Response.ok("{\"message\": \"Driver deleted successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete driver\"}")
                .build();
    }

    // ✅ Update Driver Status (Available, On Trip, Inactive)
    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDriverStatus(@PathParam("id") int id, String json) {
        String status = gson.fromJson(json, String.class);

        if (!status.matches("Available|On Trip|Inactive")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid status. Allowed values: Available, On Trip, Inactive\"}")
                    .build();
        }

        boolean updated = DriverOperations.updateDriverStatus(id, status);
        if (updated) {
            return Response.ok("{\"message\": \"Driver status updated successfully\"}").build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update driver status\"}")
                .build();
    }

    // ✅ Assign Vehicle to Driver
    @PUT
    @Path("/{id}/assign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignVehicle(@PathParam("id") int driverId, String json) {
        int vehicleId = gson.fromJson(json, Integer.class);

        boolean assigned = DriverOperations.assignVehicleToDriver(driverId, vehicleId);
        if (assigned) {
            return Response.ok("{\"message\": \"Vehicle assigned successfully to driver\"}").build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to assign vehicle to driver\"}")
                .build();
    }
}
