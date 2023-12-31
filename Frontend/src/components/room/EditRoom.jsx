import React, { useEffect, useState } from 'react'
import { getRoomById, updateRoom } from '../utils/ApiFunctions';
import { Link, useParams } from "react-router-dom"

const EditRoom = () => {
    const [room, setRoom] = useState({
        photo: null,
        roomType: "",
        roomPrice: ""
    });
    const [imagePreview, setImagePreview] = useState("");
    const [successMsg, setSuccessMsg] = useState("");
    const [errorMsg, setErrorMsg] = useState("");
    const { roomId } = useParams()

    const handleImageChange = (e) => {
        const selectedImage = e.target.files[0];
        setRoom({ ...room, photo: selectedImage });
        setImagePreview(URL.createObjectURL(selectedImage));
    }

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setRoom({ ...room, [name]: value })
    }

    useEffect(() => {
        const fetchRoom = async () => {
            try {
                const roomData = await getRoomById(roomId)
                setRoom(roomData)
                setImagePreview(`http://localhost:9091/images/_${roomData.photoPath}`)
            } catch (error) {
                console.error(error)
            }
        }

        fetchRoom()
    }, [roomId])

    const handleSubmit = async (e) => {
        e.preventDefault()

        try {
            const response = await updateRoom(roomId, room)
            if (response.status === 200) {
                setSuccessMsg("Room updated successfully!")
                const updatedRoomData = await getRoomById(roomId)
                setRoom(updatedRoomData)
                setImagePreview(`http://localhost:9091/images/_${roomData.photoPath}`)
                setErrorMsg("")
            } else {
                setErrorMsg("Error updating room")
            }
        } catch (error) {
            console.error(error)
            setErrorMsg(error.message)
        }
    }
    return (
        <div className="container mt-5 mb-5">
            <h3 className="text-center mb-5 mt-5">Edit Room</h3>
            <div className="row justify-content-center">
                <div className="col-md-8 col-lg-6">
                    {successMsg && (
                        <div className="alert alert-success" role="alert">
                            {successMsg}
                        </div>
                    )}
                    {errorMsg && (
                        <div className="alert alert-danger" role="alert">
                            {errorMsg}
                        </div>
                    )}
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="roomType" className="form-label hotel-color">
                                Room Type
                            </label>
                            <input
                                type="text"
                                className="form-control"
                                id="roomType"
                                name="roomType"
                                value={room.roomType}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="roomPrice" className="form-label hotel-color">
                                Room Price
                            </label>
                            <input
                                type="number"
                                className="form-control"
                                id="roomPrice"
                                name="roomPrice"
                                value={room.roomPrice}
                                onChange={handleInputChange}
                            />
                        </div>

                        <div className="mb-3">
                            <label htmlFor="photo" className="form-label hotel-color">
                                Photo
                            </label>
                            <input
                                required
                                type="file"
                                className="form-control"
                                id="photo"
                                name="photo"
                                onChange={handleImageChange}
                            />
                            {imagePreview && (
                                <img
                                    src={imagePreview}
                                    alt="Room preview"
                                    style={{ maxWidth: "400px", maxHeight: "400" }}
                                    className="mt-3"
                                />
                            )}
                        </div>
                        <div className="d-grid gap-2 d-md-flex mt-2">
                            <Link to={"/existingRooms"} className="btn btn-outline-info ml-5">
                                back
                            </Link>
                            <button type="submit" className="btn btn-outline-warning">
                                Edit Room
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default EditRoom