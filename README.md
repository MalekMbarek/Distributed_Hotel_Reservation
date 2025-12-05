# Hotel Reservation System (RMI Project)

Project Files

1. HotelServer.java
Purpose: The main server that starts the RMI registry and makes the hotel service available to clients.
What it does:

Starts the RMI registry on port 1099
Creates an instance of RoomManagerImpl (the actual hotel logic)
Binds it to the name "Hotel" in the RMI registry
Keeps the server running so clients can connect

2. RoomManager.java (Interface)
Purpose: Defines the "contract" - all the methods that clients can call remotely.
What it contains:

Method signatures (no implementation)
Both regular client methods AND admin methods
Ensures both server and client agree on what methods exist

Methods:

list() - Show available rooms
book(roomType, guestName) - Book a room
guests() - List all guests
revenue() - Show revenue
getStats() - Admin: show statistics
resetSystem() - Admin: reset all bookings
addRooms(type, count, price) - Admin: add more rooms

3. RoomManagerImpl.java
Purpose: The actual implementation of all hotel logic (runs on the server).
What it does:

Contains ALL business logic for the hotel
Manages room inventory, prices, guest lists
Handles bookings, revenue calculation
Implements both client and admin features
Uses RMI to receive calls from clients

4. HotelClient.java
Purpose: Regular user client for guests to interact with the hotel.
What it does:

Connects to the RMI server
Provides 2 simple commands for guests (list,book)
Used by regular customers to book rooms

5. HotelAdmin.java
Purpose: Administrator client with special privileges.
What it does:

Connects to same RMI server as HotelClient
Has access to admin-only methods
Used by hotel staff/management

How the System Works

Server starts first: HotelServer → runs RMI registry + hotel logic
Client connects: HotelClient or HotelAdmin connects to server
Method calls: Client calls methods defined in RoomManager interface
Execution: Methods run on server (RoomManagerImpl)
Results returned: Server sends results back to client

Setup & Running

Compile:

javac *.java

Run (3 terminals needed):

**Terminal 1 - RMI Registry**
rmiregistry
**Terminal 2 - Server**
java HotelServer

**Terminal 3 - Client (or open more terminals)**
java HotelClient list
java HotelAdmin stats
