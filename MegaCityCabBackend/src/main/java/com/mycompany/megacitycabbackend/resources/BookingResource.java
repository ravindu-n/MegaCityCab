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
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ravin
 */
@Path("bookings")
public class BookingResource {

    private final Gson gson = new Gson();

    // ✅ Add a New Booking
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(String json) {
        Bookings booking = gson.fromJson(json, Bookings.class);
        int bookingId = BookingOperations.addBooking(booking);
        if (bookingId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Booking created successfully\", \"id\": " + bookingId + "}")
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to create booking\"}")
                .build();
    }

    // ✅ Get All Bookings
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookings() {
        List<Map<String, Object>> bookingsList = new ArrayList<>();

        String query = "SELECT b.Id, u.username AS customerName, b.pickup_location, b.dropoff_location, b.bStatus "
                + "FROM bookings b "
                + "JOIN users u ON b.customer_id = u.Id "
                + "WHERE u.urole = 'Customer'";  // ✅ Ensures only customers appear

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> booking = new HashMap<>();
                booking.put("id", rs.getInt("Id"));
                booking.put("customerName", rs.getString("customerName")); // ✅ Get only Customers
                booking.put("pickupLocation", rs.getString("pickup_location"));
                booking.put("dropoffLocation", rs.getString("dropoff_location"));
                booking.put("bStatus", rs.getString("bStatus"));

                bookingsList.add(booking);
            }

            return Response.ok(new Gson().toJson(bookingsList)).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to fetch bookings\"}")
                    .build();
        }
    }

    // ✅ Delete Booking by ID
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBooking(@PathParam("id") int id) {
        int deleted = BookingOperations.deleteBooking(id);
        if (deleted > 0) {
            return Response.ok("{\"message\": \"Booking deleted successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete booking\"}")
                .build();
    }

    // ✅ Update Booking Status
    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBookingStatus(@PathParam("id") int id, String json) {
        String newStatus = gson.fromJson(json, String.class);

        if (!newStatus.matches("Pending|Confirmed|Completed|Cancelled")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid status. Allowed values: Pending, Confirmed, Completed, Cancelled\"}")
                    .build();
        }

        boolean updated = BookingOperations.updateBookingStatus(id, newStatus);
        if (updated) {
            return Response.ok("{\"message\": \"Booking status updated successfully\"}").build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update booking status\"}")
                .build();
    }
}
