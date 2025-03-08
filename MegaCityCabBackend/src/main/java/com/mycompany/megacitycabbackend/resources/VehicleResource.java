/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.megacitycabbackend.resources;

import Vehicle.VehicleOperations;
import Vehicle.Vehicles;
import com.google.gson.Gson;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author ravin
 */
@Path("vehicles")
public class VehicleResource {

    private final Gson gson = new Gson();

    // ✅ Add a New Vehicle
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVehicle(String json) {
        Vehicles vehicle = gson.fromJson(json, Vehicles.class);
        int vehicleId = VehicleOperations.addVehicle(vehicle);
        if (vehicleId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Vehicle added successfully\", \"id\": " + vehicleId + "}")
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to add vehicle\"}")
                .build();
    }

    // ✅ Get All Vehicles
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllVehicles() {
        List<Vehicles> vehicles = VehicleOperations.getAllVehicles();
        return Response.ok(gson.toJson(vehicles)).build();
    }

    @GET
    @Path("/available")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableVehicles() {
        try {
            List<Vehicles> vehicles = VehicleOperations.getAvailableVehicles();
            if (vehicles == null || vehicles.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"No available vehicles found\"}")
                        .build();
            }
            return Response.ok(new Gson().toJson(vehicles)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Error fetching vehicles: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // ✅ Delete Vehicle by ID
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVehicle(@PathParam("id") int id) {
        int deleted = VehicleOperations.deleteVehicle(id);
        if (deleted > 0) {
            return Response.ok("{\"message\": \"Vehicle deleted successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete vehicle\"}")
                .build();
    }

    // ✅ Update Vehicle Status (Available, Booked, Maintenance)
    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateVehicleStatus(@PathParam("id") int id, String json) {
        String status = gson.fromJson(json, String.class);

        if (!status.matches("Available|Booked|Maintenance")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid status. Allowed values: Available, Booked, Maintenance\"}")
                    .build();
        }

        boolean updated = VehicleOperations.updateVehicleStatus(id, status);
        if (updated) {
            return Response.ok("{\"message\": \"Vehicle status updated successfully\"}").build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update vehicle status\"}")
                .build();
    }
}
