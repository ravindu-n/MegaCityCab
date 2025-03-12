/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


console.log("✅ customer.js Loaded");

// API URLs
const VEHICLE_URL = "http://localhost:8080/MegaCityCabBackend/api/vehicles";
const BOOKING_URL = "http://localhost:8080/MegaCityCabBackend/api/bookings";
const FARE_PER_KM = 2.50;

// ✅ Estimate Distance (Dummy calculation — replace with real later)
function estimateDistance(pickup, dropoff) {
    if (!pickup || !dropoff)
        return 0;
    return Math.floor(Math.random() * 10) + 1;
}

// ✅ Calculate Fare & Distance
function calculateEstimatedFare() {
    const pickup = document.getElementById("pickup_location")?.value.trim();
    const dropoff = document.getElementById("dropoff_location")?.value.trim();

    if (pickup && dropoff) {
        const distance = estimateDistance(pickup, dropoff);
        const fare = (distance * FARE_PER_KM).toFixed(2);

        document.getElementById("distance").value = distance;
        document.getElementById("fare").value = fare;
    } else {
        document.getElementById("distance").value = "";
        document.getElementById("fare").value = "";
    }
}

// ✅ Fetch Available Vehicles
async function fetchAvailableVehiclesForBooking() {
    const dropdown = document.getElementById("vehicle");
    if (!dropdown)
        return;

    try {
        const response = await fetch(`${VEHICLE_URL}/available`);
        const vehicles = await response.json();

        dropdown.innerHTML = `<option value="">Select a Vehicle</option>`;
        vehicles.forEach(v => {
            const option = document.createElement("option");
            option.value = v.id;
            option.textContent = `${v.model} (${v.licensePlate})`;
            dropdown.appendChild(option);
        });
    } catch (error) {
        console.error("❌ Error fetching vehicles:", error);
    }
}

// ✅ Book Ride (Booking form submit)
document.addEventListener("DOMContentLoaded", function () {
    const bookingForm = document.getElementById("bookingForm");
    if (bookingForm) {
        bookingForm.addEventListener("submit", async function (e) {
            e.preventDefault();

            const customer_id = parseInt(localStorage.getItem("userId")); // ✅ Fix: Retrieve correct key
            const pickup_location = document.getElementById("pickup_location").value.trim();
            const dropoff_location = document.getElementById("dropoff_location").value.trim();
            const pickup_time = document.getElementById("pickup_time").value;
            const fare = parseFloat(document.getElementById("fare").value);
            const distance = parseFloat(document.getElementById("distance").value);
            const vehicle_id = parseInt(document.getElementById("vehicle").value);

            // ✅ Validation
            if (!customer_id || !pickup_location || !dropoff_location || !pickup_time || isNaN(fare) || isNaN(vehicle_id) || isNaN(distance)) {
                alert("❌ Please fill all fields correctly and ensure fare is calculated.");
                return;
            }

            const booking = {
                customer_id,
                pickup_location,
                dropoff_location,
                pickup_time,
                fare,
                distance,
                vehicle_id,
                bStatus: "Pending"
            };
            console.log("🚕 Booking Data Sent:", booking);

            try {
                const res = await fetch(`${BOOKING_URL}/create`, {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(booking)
                });

                const data = await res.json();
                if (res.ok) {
                    alert(data.message);
                    window.location.href = "customer_bookings.html";
                } else {
                    console.error("❌ Backend error:", data);
                    alert(data.message || "❌ Failed to book ride.");
                }
            } catch (error) {
                console.error("❌ Booking error:", error);
                alert("❌ Failed to book ride. Please try again later.");
            }
        });
    }
});

// ✅ Fetch Customer Bookings
async function fetchCustomerBookings() {
    const userId = localStorage.getItem("userId");
    try {
        const response = await fetch(`${BOOKING_URL}/customer/${userId}`);
        const bookings = await response.json();
        const tableBody = document.getElementById("customerBookingsTable")?.querySelector("tbody");

        if (!tableBody)
            return;

        tableBody.innerHTML = ""; // Clear table

        bookings.forEach(b => {
            const row = `
                <tr>
                    <td>${b.Id}</td>
                    <td>${b.pickup_location}</td>
                    <td>${b.dropoff_location}</td>
                    <td>${b.pickup_time}</td>
                    <td>${b.distance.toFixed(2)} km</td>
                    <td>${b.fare.toFixed(2)} $</td>
                    <td>${b.bStatus}</td>
                </tr>`;
            tableBody.innerHTML += row;
        });

    } catch (error) {
        console.error("❌ Error fetching bookings:", error);
    }
}
window.fetchCustomerBookings = fetchCustomerBookings;
window.calculateEstimatedFare = calculateEstimatedFare;
