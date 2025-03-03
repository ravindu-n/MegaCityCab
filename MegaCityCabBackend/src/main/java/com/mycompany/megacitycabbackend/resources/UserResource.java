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

    // ✅ Get All Users
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<Users> users = UserOperations.getAllAccounts();

        // ✅ Remove passwords from response for security
        for (Users user : users) {
            user.setPword(null);
        }

        return Response.ok(gson.toJson(users)).build();
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
        try {
            System.out.println("🔹 Received Login Request: " + json);

            Gson gson = new Gson();
            Users user = gson.fromJson(json, Users.class);

            // Ensure email and password are provided
            if (user.getEmail() == null || user.getPword() == null) {
                System.out.println("❌ Missing email or password");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Email and Password are required!\"}")
                        .build();
            }

            System.out.println("🔹 Checking login for email: " + user.getEmail());

            Users validUser = UserOperations.validateLogin(user.getEmail(), user.getPword());

            if (validUser != null) {
                String trimmedRole = validUser.getUrole().trim();
                System.out.println("✅ User Authenticated: " + validUser.getEmail() + " | Role: " + trimmedRole);

                return Response.ok("{\"message\": \"Login successful!\", \"id\": " + validUser.getId()
                        + ", \"role\": \"" + trimmedRole + "\"}").build();
            } else {
                System.out.println("❌ Invalid Credentials for: " + user.getEmail());
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"message\": \"Invalid email or password!\"}")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error in GlassFish logs
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Internal Server Error!\"}")
                    .build();
        }
    }
}
