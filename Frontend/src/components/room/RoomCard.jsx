import React from 'react'
import { Card, Col, Row } from 'react-bootstrap';
import { Link } from 'react-router-dom';
const RoomCard = ({room}) => {
  return (
    <Col key={room.id} className="mb-4" xs={12}>
    <Card>
        <Card.Body className="d-flex flex-wrap align-items-center">
        <div className="flex-shrrink-0 mr-3 mb-3 mb-md-0">
        <Link to={`/bookRoom/${room.id}`}>
							<Card.Img
								variant="top"
								src={`http://localhost:9091/images/_${room.photoPath}`}                                
								alt="Room Photo"
								style={{ width: "100%", maxWidth: "200px", height: "auto" }}
							/>
						</Link>
        </div>
        <div className="flex-grow-1 ml-3 px-5 align-">
						<Card.Title className="hotel-color align-items-center">{room.roomType}</Card.Title>
						<Card.Title className="room-price">{room.roomPrice} / night</Card.Title>
						<Card.Text>Some room information goes here for the guest to read through</Card.Text>
					</div>
					<div className="flex-shrink-0 mt-3">
						<Link to={`/bookRoom/${room.id}`} className="btn btn-hotel btn-sm">
							View / Book Now
						</Link>
					</div>
        </Card.Body>
    </Card>
    </Col>
  )
}

export default RoomCard