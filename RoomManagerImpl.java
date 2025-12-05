import java.rmi.*;
import java.util.*;

public class RoomManagerImpl extends java.rmi.server.UnicastRemoteObject implements RoomManager {
    // Room configurations
    private static class RoomType {
        int initialCount;
        int currentCount;
        int price;
        List<String> bookedGuests;
        String displayName;
        
        RoomType(int count, int price, String displayName) {
            this.initialCount = count;
            this.currentCount = count;
            this.price = price;
            this.bookedGuests = new ArrayList<>();
            this.displayName = displayName;
        }
    }
    
    // Room type definitions
    private static Map<String, RoomType> rooms = new HashMap<>();
    private static Set<String> allGuestNames = new HashSet<>(); // Use Set to avoid duplicates
    
    static {
        // Initialize room types
        rooms.put("single", new RoomType(5, 55000, "Single"));
        rooms.put("double", new RoomType(6, 75000, "Double"));
        rooms.put("triple", new RoomType(2, 80000, "Triple"));
    }
    
    public RoomManagerImpl() throws RemoteException {
        super();
    }
    
    public String list() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("AVAILABLE ROOMS\n");
        
        for (RoomType room : rooms.values()) {
            sb.append(String.format("%d %s room(s) available for %d DT per night\n",
                                  room.currentCount, room.displayName, room.price));
        }
        
        return sb.toString();
    }
    
    public String book(String type, String guestName) throws RemoteException {
        String roomKey = type.toLowerCase();
        
        // Validate room type
        if (!rooms.containsKey(roomKey)) {
            return "Error: Invalid room type. Available: Single, Double, Triple";
        }
        
        RoomType room = rooms.get(roomKey);
        
        // Check availability
        if (room.currentCount <= 0) {
            return String.format("Error: No %s rooms available", room.displayName);
        }
        
        // Check if guest already booked ANY room (case-insensitive)
        for (String existingGuest : allGuestNames) {
            if (existingGuest.equalsIgnoreCase(guestName)) {
                return String.format("Error: Guest '%s' has already booked a room", guestName);
            }
        }
        
        // Book the room
        room.currentCount--;
        room.bookedGuests.add(guestName);
        allGuestNames.add(guestName);
        
        return String.format("Success: %s room booked for %s. Price: %d DT",
                           room.displayName, guestName, room.price);
    }
    
    public List<String> guests() throws RemoteException {
        // Return as list for RMI compatibility
        return new ArrayList<>(allGuestNames);
    }
    
    public String revenue() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("REVENUE REPORT\n");
        
        int totalRevenue = 0;
        
        for (RoomType room : rooms.values()) {
            int bookedCount = room.initialCount - room.currentCount;
            int roomRevenue = bookedCount * room.price;
            totalRevenue += roomRevenue;
            
            sb.append(String.format("%s: %d booked × %d DT = %d DT\n",
                                  room.displayName, bookedCount, room.price, roomRevenue));
        }
        sb.append(String.format("TOTAL REVENUE: %d DT\n", totalRevenue));
        sb.append(String.format("TOTAL GUESTS: %d\n", allGuestNames.size()));
        
        return sb.toString();
    }
    
    // ADMIN METHODS 
    
    public String getStats() throws RemoteException {
        int totalCapacity = 0;
        int totalBooked = 0;
        int totalAvailable = 0;
        
        for (RoomType room : rooms.values()) {
            totalCapacity += room.initialCount;
            totalBooked += (room.initialCount - room.currentCount);
            totalAvailable += room.currentCount;
        }
        
        double occupancyRate = totalCapacity > 0 ? (totalBooked * 100.0) / totalCapacity : 0;
        
        return String.format(
            "=== HOTEL STATISTICS ===\n" +
            "Total Capacity: %d rooms\n" +
            "Booked: %d rooms\n" +
            "Available: %d rooms\n" +
            "Occupancy Rate: %.1f%%\n" +
            "Total Guests: %d\n" +
            "Total Revenue: %d DT",
            totalCapacity, totalBooked, totalAvailable, occupancyRate,
            allGuestNames.size(), calculateTotalRevenue()
        );
    }
    
    public String resetSystem() throws RemoteException {
        // Reset all rooms to initial state
        for (RoomType room : rooms.values()) {
            room.currentCount = room.initialCount;
            room.bookedGuests.clear();
        }
        allGuestNames.clear();
        
        return "System reset successfully. All bookings cleared.";
    }
    
    public String addRooms(String type, int count, int price) throws RemoteException {
        String roomKey = type.toLowerCase();
        
        if (!rooms.containsKey(roomKey)) {
            return "Error: Room type doesn't exist. Create it first.";
        }
        
        RoomType room = rooms.get(roomKey);
        room.initialCount += count;
        room.currentCount += count;
        room.price = price; // Update price
        
        return String.format("Added %d %s rooms. New capacity: %d at %d DT each",
                           count, room.displayName, room.initialCount, price);
    }
    
    private int calculateTotalRevenue() {
        int total = 0;
        for (RoomType room : rooms.values()) {
            total += (room.initialCount - room.currentCount) * room.price;
        }
        return total;
    }
}