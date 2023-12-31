import React from 'react'
import '../node_modules/bootstrap/dist/css/bootstrap.min.css'
import '/node_modules/bootstrap/dist/js/bootstrap.min.js'
import './App.css'
import AddRoom from './components/room/AddRoom.jsx'
import ExistingRooms from './components/room/ExistingRooms.jsx'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Home from './components/home/Home.jsx'
import EditRoom from './components/room/EditRoom.jsx'
import NavBar from './components/layout/NavBar.jsx'
import Footer from './components/layout/Footer.jsx'
import RoomListing from './components/room/RoomListing.jsx'
import Admin from './components/admin/Admin.jsx'
import Checkout from './components/bookings/Checkout.jsx'
import BookingSuccess from './components/bookings/BookingSuccess.jsx'
import Bookings from './components/bookings/Bookings.jsx'
import FindBooking from './components/bookings/FindBooking.jsx'
import Login from './components/auth/Login.jsx'
import Registration from './components/auth/Registration.jsx'
import Profile from './components/auth/Profile.jsx'
import { AuthProvider } from './components/auth/AuthProvider.jsx'

function App() {

  return (
    <AuthProvider>
      <main>
        <Router>
          <NavBar />
          <Routes>
            <Route path='/' element={<Home />} />
            <Route path='/editRoom/:roomId' element={<EditRoom />} />
            <Route path='/existingRooms' element={<ExistingRooms />} />
            <Route path='/addRoom' element={<AddRoom />} />
            <Route path="/bookRoom/:roomId" element={<Checkout />}/>
            <Route path='/browseAllRooms' element={<RoomListing />} />
            <Route path='/admin' element={<Admin />} />
            <Route path="/bookingSuccess" element={<BookingSuccess />} />
						<Route path="/existingBookings" element={<Bookings />} />
						<Route path="/findBooking" element={<FindBooking />} />
            <Route path="/login" element={<Login />} />
						<Route path="/register" element={<Registration />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/logout" element={<FindBooking />} />
          </Routes>
         
        </Router>
        <Footer />
      </main>
      </AuthProvider>
  )
}

export default App
