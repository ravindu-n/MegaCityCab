/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


console.log("✅ driver.js Loaded");

// ✅ API Base URL
const DRIVER_API_URL = "http://localhost:8080/MegaCityCabBackend/api/drivers";
const DRIVER_BOOKING_URL = "http://localhost:8080/MegaCityCabBackend/api/bookings";

// ✅ Fetch Driver Profile Details
async function fetchDriverDetails(driverId) {
    try {
        const response = await fetch(`${DRIVER_API_URL}/${driverId}`); // GET /drivers/{id}
        if (!response.ok) {
            throw new Error("Failed to fetch driver details");
        }

        const driver = await response.json();
        console.log("✅ Driver Data:", driver);

        // Display in HTML
        document.getElementById("name").textContent = driver.dName;
        document.getElementById("nic").textContent = driver.nic || "N/A"; // If email exists
        document.getElementById("phone").textContent = driver.phone;
        document.getElementById("licenseNumber").textContent = driver.licenseNumber;

    } catch (error) {
        console.error("❌ Error fetching driver details:", error);
        alert("❌ Failed to load driver profile. Please try again later.");
    }
}

// ✅ Fetch Driver Bookings
async function fetchDriverBookings(userId) {
    try {
        const response = await fetch(`${DRIVER_BOOKING_URL}/driver/${userId}`);
        const bookings = await response.json();
        const tableBody = document.querySelector('#driverTripsTable tbody');
        tableBody.innerHTML = "";
        bookings.forEach(b => {
            const isPending = b.bStatus === "Pending"; // ✅ Check if booking is pending

            const row = `
                <tr>
                    <td>${b.Id}</td>
                    <td>${b.customerName}</td>
                    <td>${b.pickup_location}</td>
                    <td>${b.dropoff_location}</td>
                    <td>${b.pickup_time}</td>
                    <td>${b.fare} $</td>
                    <td>${b.bStatus}</td>
                    <td>
                        ${isPending ? `
                            <button onclick="acceptBooking(${b.Id})">Accept</button>
                            <button onclick="rejectBooking(${b.Id})">Reject</button>
                        ` : ''}
                    </td>
                </tr>`;
            tableBody.innerHTML += row;
        });
    } catch (error) {
        console.error("❌ Error fetching driver bookings:", error);
    }
}

// ✅ Accept Booking
async function acceptBooking(bookingId) {
    await updateBookingStatus(bookingId, "Confirmed");
}

// ✅ Reject Booking
async function rejectBooking(bookingId) {
    await updateBookingStatus(bookingId, "Cancelled");
}

// ✅ Update Booking Status
async function updateBookingStatus(bookingId, status) {
    try {
        const res = await fetch(`${DRIVER_BOOKING_URL}/updateStatus/${bookingId}`, {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({bStatus: status})
        });

        const data = await res.json();
        if (res.ok) {
            alert(data.message);
            fetchDriverBookings(localStorage.getItem("userId")); // ✅ Refresh list after update
        } else {
            console.error("❌ Backend error:", data);
            alert(data.message || "❌ Failed to update booking status.");
        }
    } catch (error) {
        console.error("❌ Error updating booking status:", error);
        alert("❌ Failed to update status. Please try again later.");
    }
}

// ✅ Make functions available globally if needed
window.fetchDriverDetails = fetchDriverDetails;
window.fetchDriverBookings = fetchDriverBookings;
window.acceptBooking = acceptBooking;
window.rejectBooking = rejectBooking;
