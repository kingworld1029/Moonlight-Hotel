import React, { useState } from 'react'
import { addNewRoom } from '../utils/ApiFunctions';
import RoomTypeSelector from '../common/RoomTypeSelector';
import { Link } from 'react-router-dom';

const AddRoom = () => {
    const [newRoom, setNewRoom] = useState({
        photo: null,
        roomType: "",
        roomPrice: ""
    });
    const [imagePreview, setImagePreview] = useState("");
    const [successMsg, setSuccessMsg] = useState("");
    const [errorMsg, setErrorMsg] = useState("");

    const handleRoomInputChange = (e) => {
        const name = e.target.name;
        let value = e.target.value;
        if (name == "roomPrice") {
            if (!isNaN(value)) {
                value=parseInt(value);
            } else {
                value = "";
            }
        }
        setNewRoom({ ...newRoom, [name]: value })
    }

    const handleImageChange = (e) => {
        const selectedImage = e.target.files[0];
        setNewRoom({ ...newRoom, photo: selectedImage });
        setImagePreview(URL.createObjectURL(selectedImage));
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const success = await addNewRoom(newRoom.photo, newRoom.roomType, newRoom.roomPrice)
            if (success !== undefined) {
                setSuccessMsg("A new Room Added SuccessFully");
                setNewRoom({ photo: null, roomType: "", roomPrice: "" });
                setImagePreview("");
                setErrorMsg("");
            } else {
                setErrorMsg("Error During Adding Room")
            }
        } catch (error) {
            setErrorMsg(error.message)
        }
        setTimeout(() => {
			setSuccessMsg("")
			setErrorMsg("")
		}, 3000)
    }

    return (
        <>
            <section className='container mt-5 mb-5'>
                <div className='row justify-content-center'>
                    <div className='col-md-8 col-lg-6'>
                        <h2 className='mt-5 mb-2'>Add A New Room</h2>

                        {successMsg && (
							<div className="alert alert-success fade show"> {successMsg}</div>
						)}

						{errorMsg && <div className="alert alert-danger fade show"> {errorMsg}</div>}




                        <form onSubmit={handleSubmit}>
                            <div className='mb-3'>
                                <label htmlFor="roomType" className='form-label'>Room Type</label>
                                <div>
                                    <RoomTypeSelector handleRoomInputsChange={handleRoomInputChange} newRoom={newRoom} />
                                </div>
                            </div>
                            <div className='mb-3'>
                                <label htmlFor="roomPrice" className='form-label'>Room Price</label>
                                <input required className='form-control'  id='roomPrice' name='roomPrice' type='number'
                                    value={newRoom.roomPrice} onChange={handleRoomInputChange} />
                            </div>
                            <div className='mb-3'>
                                <label htmlFor="photo" className='form-label'>Photo</label>
                                <input className='form-control' required id='photo' name='photo' type='file'
                                    onChange={handleImageChange} />
                                {imagePreview && (

                                    <img src={imagePreview} alt='Preview Room Photo' style={{ maxWidth: "400px", maxHeight: "400px" }}
                                        className='mb-3' />
                                )
                                }
                            </div>
                            <div className='d-grid gap-2 d-md-flex mt-2'>
                                <Link to={'/existingRooms'} className='btn btn-btn-outline-info'>
                                  Back
                                </Link>
                                <button type='submit' className='btn btn-outline-primary ml-5 '>
                                    Save Room
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </section>
        </>
    )
}

export default AddRoom