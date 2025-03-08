/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.megacitycabbackend.resources;

import Driver.DriverOperations;
import Driver.Drivers;
import User.UserOperations;
import User.Users;
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

        // First, create the user entry
        Users newUser = new Users();
        newUser.setUsername(driver.getdName());
        newUser.setEmail(driver.getPhone() + "@megacitycab.com"); // Dummy email since drivers might not have emails
        newUser.setPword("default123"); // Default password, should be changed later
        newUser.setUrole("Driver");
        newUser.setPhone(driver.getPhone());
        newUser.setNic(driver.getNic());

        int userId = UserOperations.addAccount(newUser);
        if (userId < 0) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to create user account for driver\"}")
                    .build();
        }

        // Now, create the driver entry
        int driverId = DriverOperations.addDriver(driver, userId);
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
        System.out.println("🔹 GET Request Received: Fetching All Drivers"); // Debugging Log

        List<Drivers> drivers = DriverOperations.getAllDrivers();
        return Response.ok(new Gson().toJson(drivers)).build();
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

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriverById(@PathParam("id") int id) {
        Drivers driver = DriverOperations.getDriverById(id); // Fetch from drivers table
        if (driver != null) {
            return Response.ok(driver).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Driver not found\"}")
                .build();
    }

    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriverByUserId(@PathParam("userId") int userId) {
        Drivers driver = DriverOperations.getDriverByUserId(userId);
        if (driver != null) {
            return Response.ok(driver).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Driver not found for this user\"}")
                .build();
    }

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public Response testAPI() {
        return Response.ok("Driver API is working!").build();
    }
}
