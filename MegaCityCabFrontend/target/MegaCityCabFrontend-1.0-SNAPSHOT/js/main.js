/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

/* 
 * MegaCityCabFrontend - JavaScript for User Authentication & Booking System
 */

document.addEventListener("DOMContentLoaded", function () {
    console.log("Frontend Loaded");
});

// Base API URL
const BASE_URL = "http://localhost:8080/MegaCityCabBackend/api/users";

// ✅ User Login
document.getElementById("loginForm")?.addEventListener("submit", async function (event) {
    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    try {
        const response = await fetch(`${BASE_URL}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, pword: password })
        });

        const data = await response.json();
        console.log("🔹 API Response:", data); // Debugging Response

        if (response.ok && data.role) {
            alert("✅ Login Successful!");

            // Ensure correct role value
            const roleTrimmed = data.role.trim().toLowerCase();
            console.log("🔹 Role received from API:", data.role);
            console.log("🔹 Trimmed Role:", roleTrimmed);

            // Store user ID & role correctly
            localStorage.setItem("userId", data.id);
            localStorage.setItem("userRole", roleTrimmed);

            console.log("🔹 Stored Role in Local Storage:", localStorage.getItem("userRole"));

            // Redirect based on role
            if (roleTrimmed === "admin") {
                console.log("🔹 Redirecting to Admin Dashboard...");
                window.location.href = "admin_dashboard.html";
            } else if (roleTrimmed === "customer") {
                console.log("🔹 Redirecting to User Dashboard...");
                window.location.href = "user_dashboard.html";
            } else {
                console.log("❌ Unknown role detected:", roleTrimmed);
                alert("Invalid role detected. Please contact support.");
            }
        } else {
            alert(data.message || "❌ Invalid email or password!");
        }
    } catch (error) {
        console.error("❌ Login Error:", error);
        alert("❌ Server Error! Try again later.");
    }
});

// ✅ User Registration
document.getElementById("registerForm")?.addEventListener("submit", async function (event) {
    event.preventDefault();

    const user = {
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        pword: document.getElementById("password").value,
        urole: "Customer", // Default role
        phone: document.getElementById("phone").value,
        nic: document.getElementById("nic").value,
        address: document.getElementById("address").value
    };

    try {
        const response = await fetch(`${BASE_URL}/create`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        const data = await response.json();

        if (response.ok) {
            alert("Registration Successful! Please login.");
            window.location.href = "login.html";
        } else {
            alert(data.message || "Registration Failed!");
        }
    } catch (error) {
        console.error("Registration Error:", error);
        alert("Server Error! Try again later.");
    }
});

// ✅ Fetch All Users (Admin Dashboard)
async function fetchUsers() {
    try {
        const response = await fetch("http://localhost:8080/MegaCityCabBackend/api/users/");
        const users = await response.json();
        console.log("✅ Users Fetched:", users);
        return users;
    } catch (error) {
        console.error("❌ Error fetching users:", error);
    }
}

// ✅ Fetch User Details (For Profile Page)
async function fetchUserDetails(userId) {
    try {
        const response = await fetch(`${BASE_URL}/${userId}`);
        const user = await response.json();
        console.log("User Details:", user);
        return user;
    } catch (error) {
        console.error("Error fetching user details:", error);
    }
}

// ✅ Delete User (Admin Only)
async function deleteUser(userId) {
    try {
        const response = await fetch(`${BASE_URL}/${userId}`, {method: "DELETE"});
        const data = await response.json();
        if (response.ok) {
            alert("User deleted successfully!");
            location.reload(); // Refresh page to update user list
        } else {
            alert(data.message || "Failed to delete user!");
        }
    } catch (error) {
        console.error("Error deleting user:", error);
    }
}

// ✅ Logout Function
function logoutUser() {
    localStorage.removeItem("userId");
    localStorage.removeItem("userRole");
    alert("Logged out successfully!");
    window.location.href = "login.html";
}

// ✅ Check If User Is Logged In (Session Handling)
function checkUserSession() {
    const userId = localStorage.getItem("userId");
    if (!userId) {
        alert("You are not logged in!");
        window.location.href = "login.html";
    }
}
