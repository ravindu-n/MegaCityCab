/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.megacitycabbackend.resources;

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
@Path("users")
public class UserResource {

    private final Gson gson = new Gson();

    // ✅ Register (Customer/Admin)
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(Users user) {
        boolean success = UserOperations.registerUser(user);
        if (success) {
            return Response.ok("{\"message\": \"User registered successfully!\"}").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\": \"Failed to register user.\"}").build();
        }
    }

    // ✅ Login
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(Users loginData) {
        Users user = UserOperations.loginUser(loginData.getEmail(), loginData.getPword());
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\": \"Invalid email or password.\"}").build();
        }
    }

    // ✅ Get All Users (Admin Only)
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> getAllUsers() {
        return UserOperations.getAllUsers();
    }

    // ✅ Get User by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        Users user = UserOperations.getUserById(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"User not found.\"}").build();
        }
    }

    // ✅ Delete User
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") int id) {
        boolean success = UserOperations.deleteUser(id);
        if (success) {
            return Response.ok("{\"message\": \"User deleted successfully.\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"Failed to delete user.\"}").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> getAllUsersDefault() {
        return UserOperations.getAllUsers();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, Users user) {
        boolean success = UserOperations.updateUser(id, user);
        if (success) {
            return Response.ok("{\"message\": \"User updated successfully.\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\": \"Failed to update user.\"}").build();
        }
    }
}
