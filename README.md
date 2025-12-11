# Distributed Hotel Reservation System

A distributed hotel reservation platform built using **Java RMI** combined with a **custom Web Server** that serves a full web interface for guests and administrators. The system supports room browsing, booking, availability checks, and administrative management.

---

## . Overview

This project implements a complete distributed application for hotel reservation using:

* **Java RMI** for communication between server and clients
* **A lightweight HTTP Web Server** (custom Java implementation) to expose a web-based UI
* **HTML pages** for the user and admin interfaces

The system allows users to:

* View room availability
* Book rooms
* View booking confirmation

Admins can:

* View all rooms and their status
* Check revenue
* Manage availability and system state

---

## . Architecture

### **Main components**

* **RoomManager (Interface)**: Declares remote methods.
* **RoomManagerImpl**: Contains full hotel logic (rooms, guests, booking, pricing, revenue, attributes).
* **HotelServer**: Hosts the RMI service and registers RoomManager.
* **WebServer**: Custom HTTP server that:

  * handles GET/POST requests
  * interacts with RoomManager via RMI
  * serves the HTML UI
* **HTML Frontend**:

  * `index.html` – main menu
  * `book.html` – booking page
  * `room-availability.html` – room status
  * `admin-login.html` – login portal
  * `admin.html` – admin dashboard

---

## . Updated Room Types

Room types have been expanded and now include several attributes instead of the initial basic listing.

Each room now has:

* **Type** (Single, Double, Suite, Deluxe, etc.)
* **Price**
* **Capacity**
* **Availability**
* **Description**
---

## . How the System Works

### 1 RMI Layer

The RMI layer manages all hotel operations:

* Listing rooms
* Booking rooms
* Tracking revenue
* Tracking guests
* Admin operations

`HotelServer.java` starts the registry and binds the RoomManager service.

### 2 Web Server Layer

The web server:

* Compiles and serves HTML pages
* Handles `/book`, `/rooms`, `/admin` routes
* Converts HTTP requests into RMI method calls

Example:

* User submits booking → POST to WebServer → WebServer calls `RoomManager.bookRoom()` via RMI.

---

## . Running the System

### **Windows (Batch Script Provided)**

A ready script is included to launch everything automatically.

Or run manually:

### Step 1: Start RMI Registry

```
rmiregistry
```

Leave it running.

### Step 2: Start HotelServer

```
javac *.java
java HotelServer
```

### Step 3: Start Web Server

```
javac WebServer.java
java WebServer
```

### Step 4: Open the Web Interface

Open in browser:

```
http://localhost:8080
```

---

## . Features

### **Guest Features**

* View all rooms and attributes
* Check real-time availability
* Submit bookings

### **Admin Features**

* Login authentication
* View all reservations
* View room state
* Revenue tracking

---

## . Repository Structure

```
Distributed_Hotel_Reservation/
│── HotelServer.java
│── RoomManager.java
│── RoomManagerImpl.java
│── HotelClient.java
│── HotelAdmin.java
│── WebServer.java
│── index.html
│── admin.html
│── admin-login.html
│── book.html
│── room-availability.html
│── *.class
└── README.md
```


## Authors

**Malek Mbarek**
**Amal Messaoudi**
