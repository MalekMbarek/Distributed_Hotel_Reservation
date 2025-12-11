import java.rmi.*;
import java.util.*;

public class RoomManagerImpl extends java.rmi.server.UnicastRemoteObject implements RoomManager {
    // Enhanced RoomType class with more details
    private static class RoomType {
        int initialCount;
        int currentCount;
        int price;
        int maxGuests;
        String displayName;
        String description;
        List<String> bookedGuests;
        
        RoomType(int count, int price, String displayName, int maxGuests, String description) {
            this.initialCount = count;
            this.currentCount = count;
            this.price = price;
            this.displayName = displayName;
            this.maxGuests = maxGuests;
            this.description = description;
            this.bookedGuests = new ArrayList<>();
        }
    }
    
    private static Map<String, RoomType> rooms = new HashMap<>();
    private static Set<String> allGuestNames = new HashSet<>();
    
    static {
        // Initialize with enhanced room types
        rooms.put("single", new RoomType(5,120, "Single Room", 1, 
            "Perfect for solo travelers with essential amenities"));
        rooms.put("double", new RoomType(6, 250, "Double Room", 2, 
            "Ideal for couples with comfortable bedding"));
        rooms.put("triple", new RoomType(2, 330, "Triple Room", 3, 
            "Spacious room suitable for families or small groups"));
    }
    
    public RoomManagerImpl() throws RemoteException {
        super();
    }
    
    public String list() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("AVAILABLE ROOMS\n");
        
        for (RoomType room : rooms.values()) {
            sb.append(String.format("%s:\n", room.displayName));
            sb.append(String.format("  Available: %d of %d rooms\n", room.currentCount, room.initialCount));
            sb.append(String.format("  Price: %d DT per night\n", room.price));
            sb.append(String.format("  Max guests: %d person(s)\n", room.maxGuests));
            sb.append(String.format("  Description: %s\n", room.description));
            sb.append(String.format("  Status: %s\n\n", 
                room.currentCount > 2 ? "Available" : 
                room.currentCount > 0 ? "Limited availability" : "Sold out"));
        }
        
        return sb.toString();
    }
    
    public String book(String type, String guestName) throws RemoteException {
        String roomKey = type.toLowerCase();
        
        if (!rooms.containsKey(roomKey)) {
            return "Invalid room type. Available types: single, double, triple";
        }
        
        RoomType room = rooms.get(roomKey);
        if (room.currentCount <= 0) {
            return String.format("Error: No %s rooms available", room.displayName);
        }
        
        room.currentCount--;
        room.bookedGuests.add(guestName);
        allGuestNames.add(guestName);
        
        return String.format(
            "SUCCESSFUL BOOKING!\n" +
            "Room type: %s\n" +
            "Guest name: %s\n" +
            "Max occupancy: %d guest(s)\n" +
            "Price: %d DT per night\n" +
            "Description: %s\n" +
            "Booking confirmed!",
            room.displayName, guestName, room.maxGuests, room.price, room.description
        );
    }
    
    public String guests() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("GUEST LIST\n");
        
        if(allGuestNames.size() == 0) {
            return "No guests have booked yet";
        } else {
            int guestNumber = 1;
            for(String guest: allGuestNames) {
                sb.append(String.format("%d. %s\n", guestNumber++, guest));
            }
            sb.append(String.format("\nTotal guests: %d\n", allGuestNames.size()));
            
            // Also show room-specific guest counts
            sb.append("\nGuests by Room Type:\n");
            for (RoomType room : rooms.values()) {
                if (!room.bookedGuests.isEmpty()) {
                    sb.append(String.format("  %s: %d guest(s)\n", 
                        room.displayName, room.bookedGuests.size()));
                }
            }
        }
        
        return sb.toString();
    }
    
    public String revenue() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("HOTEL REVENUE REPORT\n");
        
        int totalRevenue = 0;
        int totalBookedRooms = 0;
        
        for (RoomType room : rooms.values()) {
            int bookedCount = room.initialCount - room.currentCount;
            int roomRevenue = bookedCount * room.price;
            totalRevenue += roomRevenue;
            totalBookedRooms += bookedCount;
            
            sb.append(String.format("%s:\n", room.displayName));
            sb.append(String.format("  • Booked rooms: %d\n", bookedCount));
            sb.append(String.format("  • Price per room: %d DT\n", room.price));
            sb.append(String.format("  • Room revenue: %d DT\n", roomRevenue));
            sb.append(String.format("  • Max guests per room: %d\n", room.maxGuests));
            sb.append(String.format("  • Potential guests: %d\n\n", bookedCount * room.maxGuests));
        }
        
        sb.append(String.format("SUMMARY:\n"));
        sb.append(String.format("  • Total booked rooms: %d\n", totalBookedRooms));
        sb.append(String.format("  • Total revenue: %d DT\n", totalRevenue));
        sb.append(String.format("  • Average price per room: %d DT\n", 
            totalBookedRooms > 0 ? totalRevenue / totalBookedRooms : 0));
        
        return sb.toString();
    }
    
    public String getStats() throws RemoteException {
        int totalCapacity = 0;
        int totalBooked = 0;
        int totalAvailable = 0;
        int maxPotentialGuests = 0;
        int currentGuests = 0;
        
        for (RoomType room : rooms.values()) {
            totalCapacity += room.initialCount;
            totalBooked += (room.initialCount - room.currentCount);
            totalAvailable += room.currentCount;
            maxPotentialGuests += room.initialCount * room.maxGuests;
            currentGuests += (room.initialCount - room.currentCount) * room.maxGuests;
        }
        
        double occupancyRate = totalCapacity > 0 ? (totalBooked * 100.0) / totalCapacity : 0;
        double guestOccupancyRate = maxPotentialGuests > 0 ? (currentGuests * 100.0) / maxPotentialGuests : 0;
        
        return String.format(
            "HOTEL STATISTICS\n" +
            "Room Capacity:\n" +
            "  Total rooms: %d\n" +
            "  Booked rooms: %d\n" +
            "  Available rooms: %d\n" +
            "  Occupancy rate: %.1f%%\n\n" +
            "Guest Capacity:\n" +
            "  Max potential guests: %d\n" +
            "  Current guests: %d\n" +
            "  Guest occupancy: %.1f%%\n\n" +
            "Financial:\n" +
            "  Total guests booked: %d\n" +
            "  Total revenue: %d DT\n" +
            "  Average revenue per room: %d DT",
            totalCapacity, totalBooked, totalAvailable, occupancyRate,
            maxPotentialGuests, currentGuests, guestOccupancyRate,
            allGuestNames.size(), calculateTotalRevenue(),
            totalBooked > 0 ? calculateTotalRevenue() / totalBooked : 0
        );
    }
    
    public String resetSystem() throws RemoteException {
        // Reset all rooms to initial state
        for (RoomType room : rooms.values()) {
            room.currentCount = room.initialCount;
            room.bookedGuests.clear();
        }
        allGuestNames.clear();
        
        return "System has been reset.\nAll bookings cleared.\nAll rooms are now available.";
    }
    
public String addRooms(String type, int count, int price, String displayName, 
                      int maxGuests, String description) throws RemoteException {
    String roomKey = type.toLowerCase();
    
    if (!rooms.containsKey(roomKey)) {
        // For new room types, use provided info or defaults
        if (displayName == null || displayName.trim().isEmpty()) {
            displayName = type.substring(0, 1).toUpperCase() + type.substring(1) + " Room";
        }
        
        if (maxGuests <= 0) {
            // Set defaults based on room type name if maxGuests not provided
            if (type.toLowerCase().contains("single")) {
                maxGuests = 1;
            } else if (type.toLowerCase().contains("double")) {
                maxGuests = 2;
            } else if (type.toLowerCase().contains("triple") || type.toLowerCase().contains("family")) {
                maxGuests = 3;
            } else if (type.toLowerCase().contains("suite")) {
                maxGuests = 4;
            } else {
                maxGuests = 2; // Default
            }
        }
        
        if (description == null || description.trim().isEmpty()) {
            // Set default description based on type
            String lowerType = type.toLowerCase();
            if (lowerType.contains("single")) {
                description = "Single occupancy room";
            } else if (lowerType.contains("double")) {
                description = "Double occupancy room";
            } else if (lowerType.contains("triple") || lowerType.contains("family")) {
                description = "Family or group room";
            } else if (lowerType.contains("suite")) {
                description = "Luxury suite";
            } else {
                description = "Standard room";
            }
        }
        
        rooms.put(roomKey, new RoomType(count, price, displayName, maxGuests, description));
        System.out.println("roomadded");
        return String.format(
            "NEW ROOM TYPE CREATED\n" +
            "Type: %s\n" +
            "Display Name: %s\n" +
            "Count: %d rooms\n" +
            "Price: %d DT per night\n" +
            "Max guests: %d person(s)\n" +
            "Description: %s",
            type, displayName, count, price, maxGuests, description
        );
    }
    
    // Existing room type - just update count and price
    RoomType room = rooms.get(roomKey);
    room.initialCount += count;
    room.currentCount += count;
    room.price = price;
    
    return String.format(
        "ROOMS ADDED\n" +
        "Room type: %s\n" +
        "Display Name: %s\n" +
        "Added: %d room(s)\n" +
        "New total: %d rooms\n" +
        "New price: %d DT per night\n" +
        "Max guests per room: %d\n" +
        "Description: %s",
        type, room.displayName, count, room.initialCount, price, 
        room.maxGuests, room.description
    );
}
    
    private int calculateTotalRevenue() {
        int total = 0;
        for (RoomType room : rooms.values()) {
            total += (room.initialCount - room.currentCount) * room.price;
        }
        return total;
    }
}