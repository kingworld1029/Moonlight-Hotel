import React, { useEffect, useState } from 'react'
import { getAllRoomTypes } from '../utils/ApiFunctions';

const RoomTypeSelector = ({ handleRoomInputsChange, newRoom }) => {
    const [roomTypes, setRoomTypes] = useState([""]);
    const [showNewRoomTypeInput, setShowNewRoomTypeInput] = useState(false);
    const [newRoomType, setNewRoomType] = useState("");

    useEffect(() => {
        getAllRoomTypes().then((data) => {
            setRoomTypes(data)
        })
    }, [])

    const handleNewRoomTypeInputChange = (e) => {
        setNewRoomType(e.target.value);
    }

    const handleAddNewRoomType = () => {
        if (newRoomType !== "") {
            setRoomTypes([...roomTypes, newRoomType])
            setNewRoomType("");
            setShowNewRoomTypeInput(false);
        }
    }

    return (
        <>
            { (
                <div>
                    <select required className="form-select" id="roomType" name="roomType" value={newRoom.roomType} onChange={(e) => {
                        if (e.target.value === "Add New") {
                            setShowNewRoomTypeInput(true)
                        } else {
                            handleRoomInputsChange(e)
                        }
                    }}>
                        <option value="">Select a Role Type</option>
                        <option value={"Add New"}>Add New</option>
                        {roomTypes.map((type, index) => (
                            <option key={index} value={type}>{type}</option>
                        ))}
                    </select>
                    {showNewRoomTypeInput && (
                        <div className='mt-2'>
                            <div className='input-group'>
                                <input className='form-control' type='text' placeholder='Enter a New Room Type'
                                    onChange={handleNewRoomTypeInputChange} value={newRoomType} />
                                <button className='btn btn-hotel' type='button' onClick={handleAddNewRoomType}>
                                    Add</button>
                            </div>
                        </div>

                    )}
                </div>
            )}
        </>
    )
}

export default RoomTypeSelector