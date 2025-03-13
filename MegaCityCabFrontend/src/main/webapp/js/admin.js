/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// ✅ Use constants defined in main.js
const USERS_API = BASE_USER_URL;
const DRIVERS_API = BASE_DRIVER_URL;

// ✅ Fetch All Users (Display All, Actions Only for Customers)
async function fetchUsers() {
    try {
        const response = await fetch(`${USERS_API}/all`);
        const users = await response.json();
        console.log("✅ Users Fetched:", users);

        const tableBody = document.getElementById("usersTable").querySelector("tbody");
        tableBody.innerHTML = "";

        users.forEach(user => {
            const row = document.createElement("tr");
            let actions = "Admin";
            if (user.urole === "Customer") {
                actions = `
                    <button onclick="openEditUserModal(${user.id}, '${user.username}', '${user.email}', '${user.phone}', '${user.nic}', '${user.address}')">Edit</button>
                    <button onclick="deleteUser(${user.id})">Delete</button>
                `;
            }
            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>${user.urole}</td>
                <td>${actions}</td>
            `;
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("❌ Error fetching users:", error);
        alert("Failed to load users. Please try again later.");
    }
}

// ✅ Delete Customer
async function deleteUser(userId) {
    if (!confirm("Are you sure you want to delete this customer?"))
        return;
    try {
        const response = await fetch(`${USERS_API}/${userId}`, {method: "DELETE"});
        const data = await response.json();
        if (response.ok) {
            alert("✅ Customer deleted successfully!");
            fetchUsers();
        } else {
            alert(data.message || "❌ Failed to delete customer.");
        }
    } catch (error) {
        console.error("❌ Error deleting user:", error);
        alert("❌ Server error! Please try again later.");
    }
}

// ✅ Open Modal to Edit Customer
function openEditUserModal(id, username, email, phone, nic, address) {
    document.getElementById("editUserId").value = id;
    document.getElementById("editUsername").value = username;
    document.getElementById("editEmail").value = email;
    document.getElementById("editPhone").value = phone;
    document.getElementById("editNic").value = nic;
    document.getElementById("editAddress").value = address;
    document.getElementById("editUserModal").style.display = "block";
}

// ✅ Close Modal
function closeEditUserModal() {
    document.getElementById("editUserModal").style.display = "none";
}

const editUserForm = document.getElementById("editUserForm");

if (editUserForm) {
    editUserForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const userId = document.getElementById("editUserId").value;
        const updatedData = {
            username: document.getElementById("editUsername").value.trim(),
            email: document.getElementById("editEmail").value.trim(),
            phone: document.getElementById("editPhone").value.trim(),
            nic: document.getElementById("editNic").value.trim(),
            address: document.getElementById("editAddress").value.trim()
        };

        console.log("🚀 Updating Customer:", updatedData);

        try {
            const response = await fetch(`${USERS_API}/${userId}`, {
                method: "PUT",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(updatedData)
            });

            const data = await response.json();
            if (response.ok) {
                alert("✅ Customer updated successfully!");
                closeEditUserModal();
                fetchUsers(); // Refresh list
            } else {
                alert(data.message || "❌ Failed to update customer.");
            }
        } catch (error) {
            console.error("❌ Error updating customer:", error);
            alert("❌ Server error! Please try again later.");
        }
    });
}

// ✅ Expose modal functions globally
window.openEditUserModal = openEditUserModal;
window.closeEditUserModal = closeEditUserModal;
window.deleteUser = deleteUser;

// Fetch Vehicles
async function fetchVehicles() {
    try {
        const response = await fetch(`${VEHICLES_API}/`);
        const vehicles = await response.json();

        const tableBody = document.getElementById("vehiclesTable").querySelector("tbody");
        tableBody.innerHTML = "";

        vehicles.forEach(vehicle => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${vehicle.id}</td>
                <td>${vehicle.model}</td>
                <td>${vehicle.make_year}</td>
                <td>${vehicle.license_plate}</td>
                <td>${vehicle.vType}</td>
                <td>${vehicle.capacity}</td>
                <td>${vehicle.vStatus}</td>
                <td>
                    <button onclick="openEditVehicleModal(${vehicle.id}, '${vehicle.model}', '${vehicle.make_year}', '${vehicle.license_plate}', '${vehicle.vType}', '${vehicle.capacity}', '${vehicle.vStatus}')">Edit</button>
                    <button onclick="deleteVehicle(${vehicle.id})">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("❌ Error fetching vehicles:", error);
    }
}

// ✅ Add New Vehicle
const vehicleForm = document.getElementById("vehicleForm");
if (vehicleForm) {
    vehicleForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const vehicleData = {
            model: document.getElementById("model").value.trim(),
            make_year: parseInt(document.getElementById("make_year").value),
            license_plate: document.getElementById("license_plate").value.trim(),
            vType: document.getElementById("vType").value,
            capacity: parseInt(document.getElementById("capacity").value),
            vStatus: "Available"
        };

        console.log("🚗 Vehicle Data to Add:", vehicleData);

        try {
            const response = await fetch(`${VEHICLES_API}/create`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(vehicleData)
            });

            const result = await response.json();

            if (response.ok) {
                alert("✅ Vehicle added successfully!");
                fetchVehicles();
                vehicleForm.reset();
            } else {
                console.error("❌ Add Vehicle Error:", result);
                alert(result.message || "❌ Failed to add vehicle!");
            }

        } catch (error) {
            console.error("❌ Server Error Adding Vehicle:", error);
            alert("❌ Server error! Please try again later.");
        }
    });
}

// Delete Vehicle
async function deleteVehicle(vehicleId) {
    if (!confirm("Are you sure to delete?"))
        return;

    try {
        const response = await fetch(`${VEHICLES_API}/${vehicleId}`, {method: "DELETE"});
        if (response.ok) {
            alert("✅ Vehicle deleted!");
            fetchVehicles();
        } else {
            alert("❌ Failed to delete vehicle.");
        }
    } catch (error) {
        console.error("❌ Error deleting:", error);
    }
}

// ✅ Fetch All Drivers
async function fetchDrivers() {
    try {
        const response = await fetch(`${DRIVERS_API}/`);
        if (!response.ok)
            throw new Error(`Failed to fetch drivers. Status: ${response.status}`);

        const drivers = await response.json();
        console.log("✅ Drivers Fetched:", drivers); // Debugging

        const tableBody = document.getElementById("driversTable").querySelector("tbody");
        tableBody.innerHTML = ""; // Clear previous data

        drivers.forEach(driver => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${driver.id}</td>
                <td>${driver.dName}</td>
                <td>${driver.licenseNumber}</td>
                <td>${driver.phone}</td>
                <td>${driver.dStatus}</td>
                <td>${driver.vehicleId || "No Vehicle Assigned"}</td>
                <td>
                    <button onclick="deleteDriver(${driver.id})">Delete</button>
                </td>
            `;
            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("❌ Error fetching drivers:", error);
        alert("Failed to load drivers. Please try again later.");
    }
}

// ✅ Delete Driver
async function deleteDriver(driverId) {
    if (!confirm("Are you sure you want to delete this driver?"))
        return;

    try {
        const response = await fetch(`${DRIVERS_API}/${driverId}`, {
            method: "DELETE"
        });

        const data = await response.json();
        if (response.ok) {
            alert("✅ Driver deleted successfully!");
            fetchDrivers(); // Refresh list
        } else {
            alert(data.message || "❌ Failed to delete driver!");
        }

    } catch (error) {
        console.error("❌ Error deleting driver:", error);
        alert("❌ Server error! Please try again later.");
    }
}

// ✅ Open Modal to Edit Customer
function openEditVehicleModal(id, model, make_year, license_plate, vType, capacity, vStatus) {
    document.getElementById("editVehicleId").value = id; // ✅ THIS MUST HAVE VALUE
    document.getElementById("editVehiclemodel").value = model;
    document.getElementById("editmake_year").value = make_year;
    document.getElementById("editlicense_plate").value = license_plate;
    document.getElementById("editvType").value = vType;
    document.getElementById("editcapacity").value = capacity;
    document.getElementById("editvStatus").value = vStatus;
    document.getElementById("editVehicleModal").style.display = "block";
}

// ✅ Close Modal
function closeEditVehicleModal() {
    document.getElementById("editVehicleModal").style.display = "none";
}

const editVehicleForm = document.getElementById("editVehicleForm");

if (editVehicleForm) {
    editVehicleForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const vehicleId = document.getElementById("editVehicleId").value;
        const updatedData = {
            model: document.getElementById("editVehiclemodel").value.trim(),
            make_year: document.getElementById("editmake_year").value.trim(),
            license_plate: document.getElementById("editlicense_plate").value.trim(),
            vType: document.getElementById("editvType").value.trim(),
            capacity: document.getElementById("editcapacity").value.trim(),
            vStatus: document.getElementById("editvStatus").value.trim()
        };

        console.log("🚀 Updating Vehicle:", updatedData);

        try {
            const response = await fetch(`${VEHICLES_API}/${vehicleId}`, {
                method: "PUT",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(updatedData)
            });

            const data = await response.json();
            if (response.ok) {
                alert("✅ Vehicle updated successfully!");
                closeEditVehicleModal();
                fetchVehicles(); // Refresh list
            } else {
                alert(data.message || "❌ Failed to update vehicle.");
            }
        } catch (error) {
            console.error("❌ Error updating vehicle:", error);
            alert("❌ Server error! Please try again later.");
        }
    });
}

// ✅ Expose Modal Functions
window.openEditVehicleModal = openEditVehicleModal;
window.closeEditVehicleModal = closeEditVehicleModal;
