import axios from "axios"

export const api = axios.create({
    baseURL: "http://localhost:9091"
})

export const getHeader = () => {
	const token = localStorage.getItem("token")
	return {
		Authorization: `Bearer ${token}`,
		"Content-Type": "application/json"
	}
}
// This Function Add New Room To The Backend
export async function addNewRoom(photo, roomType, roomPrice) {
    const formData = new FormData();
    formData.append("photo", photo);
    formData.append("roomType", roomType);
    formData.append("roomPrice", roomPrice);
    const response = await api.post("/rooms/addNewRoom", formData, {
		headers: getHeader()
	})
    if (response.status === 201) {
        return true;
    } else {
        return false;
    }
}
// This Function Get All The Room Types From Backend
export async function getAllRoomTypes() {
    try {
        const response = await api.get("/rooms/getAllRoomTypes");
        return response.data;
    } catch (error) {
        throw new Error("Error Fetching All Room Types")
    }
}

// This Function Get All The Rooms From Backend
export async function getAllRooms() {
    try {
        const response = await api.get("/rooms/getAllRooms");
        return response.data
    } catch (error) {
        throw new Error("Error Fetching All Rooms")
    }
}

// This Function Delete  The Rooms Based On RoomId From Backend
export async function deleteRoom(roomId) {
    try {
        const response = await api.delete(`/rooms/deleteRoom/${roomId}`, {
			headers: getHeader()
		})
        return response.data
    } catch (error) {
        throw new Error(`ErrorWhile Deleting The Rooms ${error.message}`)
    }
}

// This Function will Update The Rooms Based On RoomId From Backend
export async function updateRoom(roomId, roomData) {
    try {
        const formData = new FormData();
        formData.append("photo", roomData.photo);
        formData.append("roomType", roomData.roomType);
        formData.append("roomPrice", roomData.roomPrice);
        const response = await api.put(`/rooms/updateRoom/${roomId}`, formData, {
			headers: getHeader()
		})
        return response
    } catch (error) {
        throw new Error(`Error While Updating The Room ${error.message}`)
    }
}

// This Function will Get The Rooms Based On RoomId From Backend
export async function getRoomById(roomId) {
    try {
        const response = await api.get(`/rooms/getRoom/${roomId}`,)
        return response.data
    } catch (error) {
        throw new Error(`ErrorWhile Get The Rooms Based On RoomId ${error.message}`)
    }
}

/* This function saves a new booking to the databse */
export async function saveBooking(roomId, booking) {
	try {
		const response = await api.post(`/bookings/saveBooking/${roomId}`, booking)
		return response.data
	} catch (error) {
		if (error.response && error.response.data) {
			throw new Error(error.response.data)
		} else {
			throw new Error(`Error booking room : ${error.message}`)
		}
	}
}

/* This function gets alll bokings from the database */
export async function getAllBookings() {
	try {
		const result = await api.get("/bookings/getAllBookings", {
			headers: getHeader()
		})
		return result.data
	} catch (error) {
		throw new Error(`Error fetching bookings : ${error.message}`)
	}
}

/* This function get booking by the cnfirmation code */
export async function getBookingByConfirmationCode(confirmationCode) {
	try {
		const result = await api.get(`/bookings/getBookingByConfirmationCode/${confirmationCode}`)
		return result.data
	} catch (error) {
		if (error.response && error.response.data) {
			throw new Error(error.response.data)
		} else {
			throw new Error(`Error find booking : ${error.message}`)
		}
	}
}

/* This is the function to cancel user booking */
export async function cancelBooking(bookingId) {
	try {
		const result = await api.delete(`/bookings/cancelBooking/${bookingId}`)
		return result.data
	} catch (error) {
		throw new Error(`Error cancelling booking :${error.message}`)
	}
}

/* This function gets all availavle rooms from the database with a given date and a room type */
export async function getAvailableRooms(checkInDate, checkOutDate, roomType) {
	const result = await api.get(
		`rooms/availableRooms?checkInDate=${checkInDate}
		&checkOutDate=${checkOutDate}&roomType=${roomType}`
	)
	return result
}

export async function registerUser(registration) {
	try {
		const response = await api.post("/auth/registerUser", registration)
		return response.data
	} catch (error) {
		if (error.reeponse && error.response.data) {
			throw new Error(error.response.data)
		} else {
			throw new Error(`User registration error : ${error.message}`)
		}
	}
}

/* This function login a registered user */
export async function loginUser(login) {
	try {
		const response = await api.post("/auth/login", login)
		if (response.status >= 200 && response.status < 300) {
			return response.data
		} else {
			return null
		}
	} catch (error) {
		console.error(error)
		return null
	}
}

/*  This is function to get the user profile */
export async function getUserProfile(userId, token) {
	try {
		const response = await api.get(`users/profile/${userId}`, {
			headers: getHeader()
		})
		return response.data
	} catch (error) {
		throw error
	}
}

/* This isthe function to delete a user */
export async function deleteUser(userId) {
	try {
		const response = await api.delete(`/users/delete/${userId}`, {
			headers: getHeader()
		})
		return response.data
	} catch (error) {
		return error.message
	}
}

/* This is the function to get a single user */
export async function getUser(userId, token) {
	try {
		const response = await api.get(`/users/${userId}`, {
			headers: getHeader()
		})
		return response.data
	} catch (error) {
		throw error
	}
}

/* This is the function to get user bookings by the user id */
export async function getBookingsByUserId(userId, token) {
	try {
		const response = await api.get(`/bookings/user/${userId}/bookings`, {
			headers: getHeader()
		})
		return response.data
	} catch (error) {
		console.error("Error fetching bookings:", error.message)
		throw new Error("Failed to fetch bookings")
	}
}
