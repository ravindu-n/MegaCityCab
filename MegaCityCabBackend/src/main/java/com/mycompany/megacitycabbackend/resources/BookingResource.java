/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.megacitycabbackend.resources;

import Booking.BookingOperations;
import Booking.Bookings;
import DB.DatabaseOperation;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;

/**
 *
 * @author ravin
 */
@Path("bookings")
public class BookingResource {

    private final Gson gson = new Gson();

    // ✅ Add a New Booking (Auto-assign driver if available for selected vehicle)
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(String json) {
        Bookings booking = gson.fromJson(json, Bookings.class);

        if (booking.getCustomerId() == 0 || booking.getVehicleId() == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid booking data.\"}")
                    .build();
        }

        int bookingId = BookingOperations.addBooking(booking);
        if (bookingId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Booking created successfully\", \"id\": " + bookingId + "}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to create booking\"}")
                    .build();
        }
    }

    // ✅ Get All Bookings (For Admin view)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookings() {
        List<Bookings> bookings = BookingOperations.getAllBookings();
        return Response.ok(gson.toJson(bookings)).build();
    }

    // ✅ Get Customer's Bookings (For Customer Dashboard)
    @GET
    @Path("/customer/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingsByCustomer(@PathParam("customerId") int customerId) {
        List<Bookings> bookings = BookingOperations.getBookingsByCustomer(customerId);
        return Response.ok(gson.toJson(bookings)).build();
    }

    // ✅ Get Driver's Bookings (For Driver Dashboard)
    @GET
    @Path("/driver/{driverId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingsByDriver(@PathParam("driverId") int driverId) {
        List<Bookings> bookings = BookingOperations.getBookingsByDriver(driverId);
        return Response.ok(gson.toJson(bookings)).build();
    }

    // ✅ Update Booking Status (Driver can Accept or Cancel)
    @PUT
    @Path("/updateStatus/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBookingStatus(@PathParam("id") int id, String json) {
        JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
        String newStatus = jsonObject.getString("bStatus");

        boolean success = BookingOperations.updateBookingStatus(id, newStatus);
        if (success) {
            return Response.ok("{\"message\":\"Booking status updated successfully.\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Failed to update booking status.\"}")
                    .build();
        }
    }

    // ✅ Delete Booking (Optional, Admin functionality)
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBooking(@PathParam("id") int id) {
        int rowsDeleted = BookingOperations.deleteBooking(id);
        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"Booking deleted successfully.\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to delete booking. Please try again.\"}")
                    .build();
        }
    }

    // ✅ Private helper to find available driver assigned to a specific vehicle
    private Integer findAvailableDriver(int vehicleId) {
        String query = "SELECT Id FROM drivers WHERE dStatus = 'Available' AND vehicle_id = ? LIMIT 1";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Id"); // Found available driver
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No available driver found
    }
}
