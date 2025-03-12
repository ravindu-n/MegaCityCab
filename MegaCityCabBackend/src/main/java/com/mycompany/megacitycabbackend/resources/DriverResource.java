/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.megacitycabbackend.resources;

import Driver.DriverOperations;
import Driver.Drivers;
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

    // ✅ Register Driver
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerDriver(Drivers driver) {
        boolean success = DriverOperations.registerDriver(driver);
        if (success) {
            return Response.ok("{\"message\": \"Driver registered successfully!\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Failed to register driver. (Vehicle might be already assigned.)\"}").build();
        }
    }

    // ✅ Driver Login
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginDriver(Drivers loginData) {
        Drivers driver = DriverOperations.loginDriver(loginData.getdName(), loginData.getPword()); // Using dName now
        if (driver != null) {
            return Response.ok(driver).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\": \"Invalid driver name or password.\"}").build();
        }
    }

    // ✅ Get All Drivers
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Drivers> getAllDrivers() {
        return DriverOperations.getAllDrivers();
    }

    // ✅ Get Driver by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriverById(@PathParam("id") int id) {
        Drivers driver = DriverOperations.getDriverById(id);
        if (driver != null) {
            return Response.ok(driver).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Driver not found.\"}").build();
        }
    }

    // ✅ Delete Driver
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDriver(@PathParam("id") int id) {
        boolean success = DriverOperations.deleteDriver(id);
        return success
                ? Response.ok("{\"message\": \"Driver deleted successfully.\"}").build()
                : Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"Failed to delete driver.\"}").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Drivers> getAllDriversDefault() {
        return DriverOperations.getAllDrivers();
    }
}
