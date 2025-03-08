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
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: email, password: password}) // 🔹 Use "password" instead of "pword"
        });

        const data = await response.json();
        console.log("🔹 API Response:", data);

        if (response.ok && data.urole) {
            alert("✅ Login Successful!");

            const roleTrimmed = data.urole.trim().toLowerCase(); // Ensure correct case
            localStorage.setItem("userId", data.id);
            localStorage.setItem("userRole", roleTrimmed);

            console.log("🔹 Stored Role:", localStorage.getItem("userRole"));

            // ✅ Redirect based on role
            if (roleTrimmed === "admin") {
                window.location.href = "admin_dashboard.html";
            } else if (roleTrimmed === "customer") {
                window.location.href = "user_dashboard.html";
            } else if (roleTrimmed === "driver") {
                window.location.href = "driver_dashboard.html";
            } else {
                alert("❌ Invalid role detected. Please contact support.");
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

    const role = document.getElementById("role").value;
    const user = {
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        pword: document.getElementById("password").value,
        phone: document.getElementById("phone").value,
        nic: document.getElementById("nic").value,
        address: document.getElementById("address").value,
        urole: role
    };

    if (role === "Driver") {
        user.licenseNumber = document.getElementById("licenseNumber").value;
        user.vehicleId = document.getElementById("vehicle").value || null;
    }

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
            alert("✅ Registration Successful! Please login.");
            window.location.href = "login.html";
        } else {
            alert(data.message || "❌ Registration Failed!");
        }
    } catch (error) {
        console.error("❌ Registration Error:", error);
        alert("❌ Server Error! Try again later.");
    }
});

// ✅ Fetch All Users (Admin Dashboard)
async function fetchUsers() {
    try {
        const response = await fetch(`${BASE_URL}/`);
        if (!response.ok)
            throw new Error(`HTTP error! Status: ${response.status}`);

        const users = await response.json();
        console.log("✅ Users Fetched:", users);

        if (users.length === 0) {
            console.warn("❌ No users received from API.");
            return;
        }

        const tableBody = document.getElementById("usersTable")?.querySelector("tbody");
        if (!tableBody)
            return;

        tableBody.innerHTML = "";
        users.forEach(user => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>${user.urole}</td>
                <td><button onclick="deleteUser(${user.id})">Delete</button></td>
            `;
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("❌ Error fetching users:", error);
    }
}

// ✅ Fetch User Details
async function fetchUserDetails(userId) {
    console.log("🔹 Fetching User Details for ID:", userId); // Debug log

    try {
        const response = await fetch(`${BASE_URL}/${userId}`);
        if (!response.ok)
            throw new Error("❌ User not found!");

        const user = await response.json();
        console.log("✅ API Response:", user); // Debug log

        document.getElementById("username").textContent = user.username || "N/A";
        document.getElementById("email").textContent = user.email || "N/A";
        document.getElementById("phone").textContent = user.phone || "N/A";
        document.getElementById("address").textContent = user.address || "N/A";

    } catch (error) {
        console.error("❌ Error fetching user details:", error);
    }
}

// ✅ Delete User
async function deleteUser(userId) {
    try {
        const response = await fetch(`${BASE_URL}/${userId}`, {method: "DELETE"});
        const data = await response.json();
        if (response.ok) {
            alert("✅ User deleted successfully!");
            location.reload();
        } else {
            alert(data.message || "❌ Failed to delete user!");
        }
    } catch (error) {
        console.error("❌ Error deleting user:", error);
    }
}

// ✅ Logout Function
function logoutUser() {
    localStorage.removeItem("userId");
    localStorage.removeItem("userRole");
    alert("✅ Logged out successfully!");
    window.location.href = "login.html";
}

// ✅ Check User Session
function checkUserSession() {
    const userId = localStorage.getItem("userId");
    if (!userId) {
        alert("❌ You are not logged in!");
        window.location.href = "login.html";
    }
}

const DRIVERS_API = "http://localhost:8080/MegaCityCabBackend/api/drivers";

// ✅ Fetch All Drivers
async function fetchDrivers() {
    try {
        const response = await fetch(`${DRIVERS_API}/`);
        if (!response.ok)
            throw new Error("Failed to fetch drivers");

        const drivers = await response.json();
        console.log("✅ Drivers Fetched:", drivers);

        const tableBody = document.getElementById("driversTable").querySelector("tbody");
        tableBody.innerHTML = "";

        drivers.forEach(driver => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${driver.id}</td>
                <td>${driver.dName}</td>
                <td>${driver.licenseNumber}</td>
                <td>${driver.phone}</td>
                <td>${driver.dStatus}</td>
                <td>${driver.vehicleId || "No Vehicle"}</td>
                <td>
                    <button onclick="deleteDriver(${driver.id})">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("❌ Error fetching drivers:", error);
    }
}

// ✅ Delete Driver Function
async function deleteDriver(driverId) {
    if (!confirm("Are you sure you want to delete this driver?"))
        return;

    try {
        const response = await fetch(`${DRIVERS_API}/${driverId}`, {method: "DELETE"});
        const data = await response.json();

        if (response.ok) {
            alert("✅ Driver deleted successfully!");
            location.reload();
        } else {
            alert(data.message || "❌ Failed to delete driver!");
        }

    } catch (error) {
        console.error("❌ Error deleting driver:", error);
    }
}

// ✅ Fetch Driver Details
async function fetchDriverDetails() {
    try {
        const userId = localStorage.getItem("userId");
        if (!userId)
            throw new Error("❌ No user ID found in localStorage.");

        const response = await fetch(`${BASE_URL}/${userId}`);
        if (!response.ok)
            throw new Error("❌ Failed to fetch driver details");

        const driver = await response.json();
        console.log("✅ Driver Data Fetched:", driver);

        // Ensure the elements exist before setting content
        if (document.getElementById("name")) {
            document.getElementById("name").textContent = driver.username || "N/A";
        }
        if (document.getElementById("email")) {
            document.getElementById("email").textContent = driver.email || "N/A";
        }
        if (document.getElementById("phone")) {
            document.getElementById("phone").textContent = driver.phone || "N/A";
        }
        if (document.getElementById("licenseNumber")) {
            document.getElementById("licenseNumber").textContent = driver.licenseNumber || "N/A";
        }

    } catch (error) {
        console.error("❌ Error fetching driver details:", error);
    }
}

const BOOKINGS_API = "http://localhost:8080/MegaCityCabBackend/api/bookings";
const VEHICLES_API = "http://localhost:8080/MegaCityCabBackend/api/vehicles";

// ✅ Fetch Available Vehicles
async function fetchAvailableVehicles() {
    try {
        const response = await fetch(`${VEHICLES_API}/`);
        if (!response.ok)
            throw new Error("Failed to fetch vehicles");

        const vehicles = await response.json();
        console.log("✅ Vehicles Fetched:", vehicles);

        const vehicleDropdown = document.getElementById("vehicle");
        vehicleDropdown.innerHTML = `<option value="">Select a Vehicle</option>`;

        vehicles.forEach(vehicle => {
            if (vehicle.vStatus === "Available") {
                const option = document.createElement("option");
                option.value = vehicle.id;
                option.textContent = `${vehicle.model} - ${vehicle.licensePlate}`;
                vehicleDropdown.appendChild(option);
            }
        });

    } catch (error) {
        console.error("❌ Error fetching vehicles:", error);
    }
}

// ✅ Create a New Booking
document.getElementById("bookingForm")?.addEventListener("submit", async function (event) {
    event.preventDefault();

    const userId = localStorage.getItem("userId");
    if (!userId) {
        alert("❌ User not logged in!");
        return;
    }

    const bookingData = {
        customerId: parseInt(userId),
        pickupLocation: document.getElementById("pickupLocation").value,
        dropoffLocation: document.getElementById("dropoffLocation").value,
        pickupTime: document.getElementById("pickupTime").value,
        fare: parseFloat(document.getElementById("fare").value),
        vehicleId: parseInt(document.getElementById("vehicle").value),
        bStatus: "Pending"
    };

    try {
        const response = await fetch(`${BOOKINGS_API}/create`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(bookingData)
        });

        const data = await response.json();
        alert(data.message || "✅ Booking successful!");
        if (response.ok)
            window.location.href = "customer_bookings.html";

    } catch (error) {
        console.error("❌ Booking Error:", error);
        alert("❌ Server Error! Try again later.");
    }
});

// ✅ Fetch All Vehicles
async function fetchVehicles() {
    try {
        const response = await fetch("http://localhost:8080/MegaCityCabBackend/api/vehicles/available");
        if (!response.ok)
            throw new Error("Failed to fetch vehicles");

        const vehicles = await response.json();
        console.log("✅ Vehicles Fetched:", vehicles); // Debugging log

        const tableBody = document.getElementById("vehiclesTable").querySelector("tbody");
        tableBody.innerHTML = ""; // Clear table before adding rows

        vehicles.forEach(vehicle => {
            const row = `<tr>
                <td>${vehicle.id}</td>
                <td>${vehicle.model}</td>
                <td>${vehicle.makeYear}</td>
                <td>${vehicle.licensePlate}</td>
                <td>${vehicle.vType}</td>
                <td>${vehicle.capacity}</td>
                <td>${vehicle.vStatus}</td>
                <td><button onclick="deleteVehicle(${vehicle.id})">Delete</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });

    } catch (error) {
        console.error("❌ Error fetching vehicles:", error);
        alert("Error loading vehicles. Please try again.");
    }
}

// ✅ Add a New Vehicle
document.getElementById("vehicleForm")?.addEventListener("submit", async function (event) {
    event.preventDefault();

    const vehicleData = {
        model: document.getElementById("model").value,
        makeYear: document.getElementById("makeYear").value,
        licensePlate: document.getElementById("licensePlate").value,
        type: document.getElementById("type").value,
        capacity: document.getElementById("capacity").value,
        status: "Available"
    };

    try {
        const response = await fetch(`${VEHICLES_API}/create`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(vehicleData)
        });

        const data = await response.json();
        alert(data.message || "Vehicle added successfully!");
        if (response.ok)
            location.reload();

    } catch (error) {
        console.error("❌ Error adding vehicle:", error);
        alert("Server Error! Try again later.");
    }
});

// ✅ Delete a Vehicle
async function deleteVehicle(vehicleId) {
    if (!confirm("Are you sure you want to delete this vehicle?"))
        return;

    try {
        const response = await fetch(`${VEHICLES_API}/${vehicleId}`, {method: "DELETE"});
        const data = await response.json();

        if (response.ok) {
            alert("✅ Vehicle deleted successfully!");
            location.reload();
        } else {
            alert(data.message || "❌ Failed to delete vehicle!");
        }

    } catch (error) {
        console.error("❌ Error deleting vehicle:", error);
    }
}

// ✅ Fetch Customer's Bookings
async function fetchCustomerBookings() {
    const userId = localStorage.getItem("userId");
    console.log("Fetching bookings for user:", userId);

    try {
        const response = await fetch(`http://localhost:8080/MegaCityCabBackend/api/bookings/customer/${userId}`);

        if (!response.ok) {
            console.warn("No bookings found for this customer.");
            return;
        }

        const bookings = await response.json();
        console.log("API Response:", bookings); // Debugging

        const tableBody = document.getElementById("customerBookingsTable").querySelector("tbody");
        tableBody.innerHTML = "";

        if (bookings.length > 0) {
            bookings.forEach(booking => {
                const row = `<tr>
                    <td>${booking.id}</td>
                    <td>${booking.pickupLocation}</td>
                    <td>${booking.dropoffLocation}</td>
                    <td>${booking.pickupTime}</td>
                    <td>${booking.fare}</td>
                    <td>${booking.bStatus}</td>
                </tr>`;
                tableBody.innerHTML += row;
            });
        } else {
            console.warn("No bookings found for this customer.");
        }
    } catch (error) {
        console.error("Error fetching bookings:", error);
        alert("Error loading bookings. Please try again.");
    }
}

// ✅ Fetch Driver's Bookings
async function fetchDriverBookings(userId) {
    try {
        const response = await fetch(`http://localhost:8080/MegaCityCabBackend/api/bookings/driver/${userId}`);
        if (!response.ok)
            throw new Error("Failed to fetch driver bookings");

        const bookings = await response.json();
        console.log("✅ Driver's Bookings:", bookings);

        const tableBody = document.getElementById("driverTripsTable")?.querySelector("tbody");
        if (!tableBody)
            return;

        tableBody.innerHTML = "";
        bookings.forEach(booking => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${booking.id}</td>
                <td>${booking.customerName}</td>
                <td>${booking.pickupLocation}</td>
                <td>${booking.dropoffLocation}</td>
                <td>${booking.pickupTime}</td>
                <td>${booking.fare}</td>
                <td>${booking.bStatus}</td>
            `;
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("❌ Error fetching driver bookings:", error);
    }
}
