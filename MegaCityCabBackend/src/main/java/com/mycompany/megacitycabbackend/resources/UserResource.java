/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.megacitycabbackend.resources;

import User.UserOperations;
import User.Users;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

    // ✅ Create User
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String json) {
        Users user = gson.fromJson(json, Users.class);
        int userId = UserOperations.addAccount(user);
        if (userId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"User created successfully\", \"id\": " + userId + "}")
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to create user\"}")
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<Users> users = UserOperations.getAllAccounts();
        for (Users user : users) {
            user.setPword(null); // Hide password
        }
        return Response.ok(new Gson().toJson(users)).build();
    }

    // ✅ Get User by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        Users user = UserOperations.getUserById(id);
        if (user != null) {
            user.setPword(null); // Remove password for security
            return Response.ok(new Gson().toJson(user)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"User not found\"}")
                .build();
    }

    // ✅ Delete User
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") int id) {
        int deleted = UserOperations.deleteAccount(id);
        if (deleted > 0) {
            return Response.ok("{\"message\": \"User deleted successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete user\"}")
                .build();
    }

    // ✅ User Login
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateLogin(String json) {
        System.out.println("🔹 Received Login Request: " + json);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class); // Use JsonObject to extract fields

        String email = jsonObject.has("email") ? jsonObject.get("email").getAsString() : null;
        String password = jsonObject.has("password") ? jsonObject.get("password").getAsString() : null; // 🔹 Change to "password"

        if (email == null || password == null) {
            System.out.println("❌ Missing email or password");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Email and Password are required!\"}")
                    .build();
        }

        Users validUser = UserOperations.validateLogin(email, password);

        if (validUser != null) {
            return Response.ok("{\"message\": \"Login successful!\", \"id\": " + validUser.getId()
                    + ", \"urole\": \"" + validUser.getUrole().trim() + "\"}").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\": \"Invalid email or password!\"}")
                    .build();
        }
    }
}
