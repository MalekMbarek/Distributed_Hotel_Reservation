@echo off
echo Hotel Reservation System
echo.

echo 1. Starting RMI Registry
start "RMI Registry" rmiregistry
timeout /t 10

echo 2. Starting Hotel Server
start "Hotel Server" java HotelServer
timeout /t 5

echo 3. Starting Web Server
javac WebServer.java
java WebServer