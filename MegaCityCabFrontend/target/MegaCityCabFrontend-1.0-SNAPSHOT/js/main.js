/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

/*
 * MegaCityCabFrontend - JavaScript for User Authentication & Booking System
 */

// Base URLs for API endpoints
const BASE_USER_URL = "http://localhost:8080/MegaCityCabBackend/api/users";
const BASE_DRIVER_URL = "http://localhost:8080/MegaCityCabBackend/api/drivers";
const VEHICLES_API = "http://localhost:8080/MegaCityCabBackend/api/vehicles";
const BASE_VEHICLE_URL = "http://localhost:8080/MegaCityCabBackend/api/vehicles";

// ✅ DOM Ready for Login and Registration
document.addEventListener("DOMContentLoaded", () => {
    console.log("✅ Main.js Loaded");

    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");

    if (loginForm)
        loginForm.addEventListener("submit", handleLogin);
    if (registerForm) {
        registerForm.addEventListener("submit", handleRegistration);
        toggleFields();
        fetchAvailableVehiclesForDriver();
    }
});

// ✅ Fully Fixed Login Handler
async function handleLogin(event) {
    event.preventDefault();

    const identifier = document.getElementById("identifier").value.trim();
    const password = document.getElementById("password").value.trim();

    try {
        // ✅ First attempt - Customer/Admin Login
        let userResponse = await fetch(`${BASE_USER_URL}/login`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: identifier, pword: password})
        });

        if (userResponse.ok) {
            const user = await userResponse.json();
            alert("✅ Login Successfully!");
            handleSuccessfulLogin(user, user.urole);
            return;
        } else {
            console.log("Customer/Admin login failed. Trying Driver login...");
        }

        // ✅ Second attempt - Driver Login
        let driverResponse = await fetch(`${BASE_DRIVER_URL}/login`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({dName: identifier, pword: password})
        });

        if (driverResponse.ok) {
            const driver = await driverResponse.json();
            alert("✅ Login Successfully!");
            handleSuccessfulLogin(driver, "Driver");
            return;
        } else {
            console.log("Driver login failed.");
        }

        // ❌ If both fail
        alert("❌ Invalid credentials. Please try again.");

    } catch (error) {
        console.error("❌ Error during login:", error);
        alert("❌ Server error during login. Please try again later.");
    }
}

function handleSuccessfulLogin(user, role) {
    console.log("✅ Logged in:", user);
    localStorage.setItem("userId", user.id);
    localStorage.setItem("role", role);

    if (role === "Admin")
        window.location.href = "admin_dashboard.html";
    else if (role === "Driver")
        window.location.href = "driver_dashboard.html";
    else
        window.location.href = "user_dashboard.html"; // Customer
}

// ✅ Handle Registration
async function handleRegistration(event) {
    event.preventDefault();

    const role = document.getElementById("role").value;
    const password = document.getElementById("password").value;

    if (role === "Driver") {
        const driverData = {
            dName: document.getElementById("dName").value,
            licenseNumber: document.getElementById("licenseNumber").value,
            pword: password,
            phone: document.getElementById("phone").value,
            nic: document.getElementById("nic").value,
            vehicleId: document.getElementById("vehicle").value || null,
            dStatus: "Available"
        };

        console.log("🚕 Driver Data:", driverData);

        try {
            const response = await fetch(`${BASE_DRIVER_URL}/create`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(driverData)
            });

            const result = await response.json(); // Get message from backend

            if (response.ok) {
                alert("✅ Driver registered successfully!");
                window.location.href = "login.html";
            } else {
                // ✅ User-friendly alert, NO console error
                alert(result.message || "❌ Failed to register driver.");
                // console.error("❌ Error:", result); // ❌ REMOVE or comment this
            }

        } catch (error) {
            // ✅ Handle any unexpected error gracefully
            alert("❌ Failed to register driver. (Vehicle might be already assigned.)");
            // console.error("❌ Server Error:", error); // ❌ REMOVE or comment this
        }

    } else {
        const customerData = {
            username: document.getElementById("username").value,
            email: document.getElementById("email").value,
            pword: password,
            phone: document.getElementById("phone").value,
            nic: document.getElementById("nic").value,
            address: document.getElementById("address").value,
            urole: "Customer"
        };

        console.log("👤 Customer Data:", customerData);

        const response = await fetch(`${BASE_USER_URL}/create`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(customerData)
        });

        const result = await response.json();
        if (response.ok) {
            alert("✅ Customer registered successfully!");
            window.location.href = "login.html";
        } else {
            console.error("❌ Error:", result);
            alert(result.message || "❌ Failed to register customer.");
        }
    }
}

// ✅ Toggle fields based on role selection
function toggleFields() {
    const role = document.getElementById("role").value;
    const isDriver = role === "Driver";

    // Customer fields
    const username = document.getElementById("username");
    const email = document.getElementById("email");
    const address = document.getElementById("address");

    // Driver fields
    const dName = document.getElementById("dName");
    const licenseNumber = document.getElementById("licenseNumber");
    const vehicle = document.getElementById("vehicle");
    const vehicleLabel = document.getElementById("vehicleLabel");

    // Toggle display
    username.style.display = isDriver ? "none" : "block";
    email.style.display = isDriver ? "none" : "block";
    address.style.display = isDriver ? "none" : "block";

    dName.style.display = isDriver ? "block" : "none";
    licenseNumber.style.display = isDriver ? "block" : "none";
    vehicle.style.display = isDriver ? "block" : "none";
    vehicleLabel.style.display = isDriver ? "block" : "none";

    // Manage required attributes
    if (isDriver) {
        username.removeAttribute("required");
        email.removeAttribute("required");
        address.removeAttribute("required");
        dName.setAttribute("required", "required");
        licenseNumber.setAttribute("required", "required");
    } else {
        username.setAttribute("required", "required");
        email.setAttribute("required", "required");
        address.setAttribute("required", "required");
        dName.removeAttribute("required");
        licenseNumber.removeAttribute("required");
    }
}

// ✅ Check User Session (Reusable for other pages)
function checkUserSession() {
    const userId = localStorage.getItem("userId");
    if (!userId) {
        alert("❌ You are not logged in!");
        window.location.href = "login.html";
    }
}

// ✅ Logout Function (Reusable)
function logoutUser() {
    localStorage.removeItem("userId");
    localStorage.removeItem("role");
    alert("✅ Logged out successfully!");
    window.location.href = "login.html";
}

// Expose global functions
window.toggleFields = toggleFields;
window.checkUserSession = checkUserSession;
window.logoutUser = logoutUser;

// ✅ Fetch User Profile
async function fetchUserDetails(userId) {
    try {
        const response = await fetch(`${BASE_USER_URL}/${userId}`);
        if (!response.ok)
            throw new Error("Failed to fetch user details.");

        const user = await response.json();
        document.getElementById("username").textContent = user.username;
        document.getElementById("email").textContent = user.email;
        document.getElementById("phone").textContent = user.phone;
        document.getElementById("address").textContent = user.address;
    } catch (error) {
        console.error("❌ Error fetching user details:", error);
        alert("❌ Failed to load profile. Please try again later.");
    }
}

// ✅ Fetch Available Vehicles for Driver Assignment
async function fetchAvailableVehicles() {
    const vehicleDropdown = document.getElementById("vehicle");
    if (!vehicleDropdown)
        return; // Stop if dropdown is not found

    try {
        const response = await fetch("http://localhost:8080/MegaCityCabBackend/api/vehicles/");
        if (!response.ok)
            throw new Error("Failed to fetch vehicles from server");

        const vehicles = await response.json();

        // Clear existing options
        vehicleDropdown.innerHTML = `<option value="">No Vehicle Assigned</option>`;

        // Loop through and add only vehicles that are available and not assigned
        vehicles.forEach(vehicle => {
            if (vehicle.vStatus === "Available") { // Check if vehicle is available
                const option = document.createElement("option");
                option.value = vehicle.id; // ID will be sent to backend
                option.textContent = `${vehicle.model} - ${vehicle.licensePlate}`; // Show model & plate
                vehicleDropdown.appendChild(option); // Add to dropdown
            }
        });

        console.log("✅ Vehicles loaded:", vehicles); // Debug log

    } catch (error) {
        console.error("❌ Error loading vehicles:", error);
        alert("❌ Failed to load available vehicles. Please try again later.");
    }
}

// ✅ Fetch Unassigned Vehicles for Driver Registration (For register.html)
async function fetchAvailableVehiclesForDriver() {
    const dropdown = document.getElementById("vehicle");
    if (!dropdown)
        return;

    try {
        const response = await fetch(`${VEHICLES_API}/availableForDriver`);
        const vehicles = await response.json();

        dropdown.innerHTML = ""; // Clear dropdown

        if (vehicles.length === 0) {
            // 🚫 No vehicles available
            const option = document.createElement("option");
            option.value = ""; // No value
            option.textContent = "No vacancies for drivers"; // Message
            option.disabled = true; // Make it unselectable
            dropdown.appendChild(option);
        } else {
            // ✅ Vehicles available
            const defaultOption = document.createElement("option");
            defaultOption.value = "";
            defaultOption.textContent = "No Vehicle Assigned (Optional)";
            dropdown.appendChild(defaultOption);

            vehicles.forEach(v => {
                const option = document.createElement("option");
                option.value = v.id;
                option.textContent = `${v.model} (${v.license_plate})`;
                dropdown.appendChild(option);
            });
        }

    } catch (error) {
        console.error("❌ Error fetching available vehicles for drivers:", error);
    }
}

function openUserGuideModal() {
    document.getElementById("userGuideModal").style.display = "block";
}

function closeUserGuideModal() {
    document.getElementById("userGuideModal").style.display = "none";
}

// Close modal when clicking outside the modal content
window.onclick = function (event) {
    const modal = document.getElementById("userGuideModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

// Function to dynamically fetch and display vehicles on index.html
async function fetchAndDisplayVehicles() {
    const container = document.getElementById("vehicleList");
    container.innerHTML = "<p>Loading vehicles...</p>";

    try {
        const response = await fetch(`${BASE_VEHICLE_URL}/`);
        if (!response.ok)
            throw new Error("Failed to fetch vehicles");

        const vehicles = await response.json();
        console.log("🚗 Vehicles Fetched:", vehicles);

        if (vehicles.length === 0) {
            container.innerHTML = "<p>No vehicles available at the moment.</p>";
            return;
        }

        container.innerHTML = ""; // Clear loading message

        vehicles.forEach(vehicle => {
            const vehicleCard = document.createElement("div");
            vehicleCard.className = "vehicle-card";

            // ✅ Proper sanitization to match file names
            const imageName = vehicle.model.replace(/[^a-zA-Z0-9 ]/g, '').replace(/\s+/g, '_');
            const imagePath = `images/${imageName}.jpg`;

            vehicleCard.innerHTML = `
                <img src="${imagePath}" alt="${vehicle.model}" onerror="this.onerror=null;this.src='images/no_image.jpg';">
                <h3>${vehicle.model} (${vehicle.vType})</h3>
                <p><strong>Capacity:</strong> ${vehicle.capacity} persons</p>
            `;
            container.appendChild(vehicleCard);
        });

    } catch (error) {
        console.error("❌ Error fetching vehicles:", error);
        container.innerHTML = "<p>Failed to load vehicles. Please try again later.</p>";
    }
}

// Modal control
function openUserGuideModal() {
    document.getElementById("userGuideModal").style.display = "block";
}

function closeUserGuideModal() {
    document.getElementById("userGuideModal").style.display = "none";
}

window.fetchAndDisplayVehicles = fetchAndDisplayVehicles;
window.openUserGuideModal = openUserGuideModal;
window.closeUserGuideModal = closeUserGuideModal;

function togglePassword() {
    const passwordField = document.getElementById('password');
    const showPasswordCheckbox = document.getElementById('showPassword');
    if (showPasswordCheckbox.checked) {
        passwordField.type = 'text';
    } else {
        passwordField.type = 'password';
    }
}
