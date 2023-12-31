import React, { useEffect, useState } from 'react';
import { deleteRoom, getAllRooms } from '../utils/ApiFunctions';
import { Col, Row } from 'react-bootstrap';
import { FaEdit, FaEye, FaPlus, FaTrashAlt } from "react-icons/fa"
import RoomFilter from '../common/RoomFilter';
import RoomPaginator from '../common/RoomPaginator';
import { Link } from "react-router-dom"

const ExistingRooms = () => {
    const [rooms, setRooms] = useState([{ id: "", roomType: "", roomPrice: "" }]);
    const [currentPage, setCurrentPage] = useState(1);
    const [roomsPerPage, setRoomsPerPage] = useState(5);
    const [isLoading, setIsLoading] = useState(false);
    const [filteredRooms, setFilteredRooms] = useState([{ id: "", roomType: "", roomPrice: "" }]);
    const [selectedRoomType, setSelectedRoomType] = useState('');
    const [errorMsg, setErrorMsg] = useState('');
    const [successMsg, setSuccessMsg] = useState('');

    useEffect(() => {
        fetchRooms();
    }, []);

    const fetchRooms = async () => {
        setIsLoading(true);
        try {
            const result = await getAllRooms();
            setRooms(result);
            setFilteredRooms(result);
            setIsLoading(false);
        } catch (error) {
            setErrorMsg(error.message);
            setIsLoading(false);
        }
    };

    useEffect(() => {
        if (selectedRoomType === '') {
            setFilteredRooms(rooms);
        } else {
            const filteredRooms = rooms.filter(
                (room) => room.roomType === selectedRoomType
            );
            setFilteredRooms(filteredRooms);
        }
        setCurrentPage(1);
    }, [rooms, selectedRoomType]);

    const handlePaginationClick = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handleDelete = async (roomId) => {
        try {
            const result = await deleteRoom(roomId)
            if (result === "") {
                setSuccessMsg(`Room No ${roomId} was delete`)
                fetchRooms()
            } else {
                console.error(`Error deleting room : ${result.message}`)
            }
        } catch (error) {
            setErrorMsg(error.message)
        }
        setTimeout(() => {
            setSuccessMsg("")
            setErrorMsg("")
        }, 3000)
    }

    const calculateTotalPages = (totalRooms) => {
        return Math.ceil(totalRooms.length / roomsPerPage);
    };
    const indexOfLastRoom = currentPage * roomsPerPage;
    const indexOfFirstRoom = indexOfLastRoom - roomsPerPage;
    const currentRooms = filteredRooms.slice(indexOfFirstRoom, indexOfLastRoom);

    return (
        <>
        <div className="container col-md-8 col-lg-6">
				{successMsg && <p className="alert alert-success mt-5">{successMsg}</p>}

				{errorMsg && <p className="alert alert-danger mt-5">{errorMsg}</p>}
			</div>
            {isLoading ? (
                <p>Loading existing rooms</p>
            ) : (
                <>
                    <section className="mt-5 mb-5 container">
                        <div className="d-flex justify-content-between mb-3 mt-5">
                            <h2>Existing Rooms</h2>
                        </div>

                        <Row>
                            <Col md={6} className="mb-2 md-mb-0">
                                <RoomFilter
                                    data={rooms}
                                    setFilteredData={setFilteredRooms}
                                    setSelectedRoomType={setSelectedRoomType}
                                />
                            </Col>

                            <Col md={6} className="d-flex justify-content-end">
								<Link to={"/addRoom"}>
									<FaPlus /> Add Room
								</Link>
							</Col>
                        </Row>

                        <table className="table table-bordered table-hover">
                            <thead>
                                <tr className="text-center">
                                    <th>Sr.No</th>
                                    <th>Room No.</th>
                                    <th>Room Type</th>
                                    <th>Room Price</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>

                            <tbody>
                                {currentRooms.map((room, index) => (
                                    <tr key={room.id} className="text-center">
                                        <input
                                            type="hidden"
                                            name="roomId"
                                            id="roomId"
                                            value={room.id}
                                        />
                                        <td>{(currentPage - 1) * roomsPerPage + index + 1}</td>
                                        <td>{room.id}</td>
                                        <td>{room.roomType}</td>
                                        <td>{room.roomPrice}</td>
                                        <td className="">
                                            <Link to={`/editRoom/${room.id}`}>
                                                <span className="btn btn-info btn-sm  m-1">
                                                    <FaEye />
                                                </span>
                                                <span className="btn btn-warning btn-sm m-1">
                                                    <FaEdit />
                                                </span>
                                            </Link> 
                                            <button
                                                className="btn btn-danger btn-sm m-1"
                                                onClick={() => handleDelete(room.id)}>
                                                <FaTrashAlt />
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>

                        <RoomPaginator
                            currentPage={currentPage}
                            totalPages={calculateTotalPages(filteredRooms)}
                            onPageChange={handlePaginationClick}
                        />
                    </section>
                </>
            )}
        </>
    );
};

export default ExistingRooms;
