import java.io.*;
import java.net.*;
import java.rmi.Naming;

public class WebServer {
    private static RoomManager hotelService;
    
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Hotel Web Server");
        
        try {
            hotelService = (RoomManager) Naming.lookup("rmi://localhost:1099/Hotel");
            System.out.println("Connected to RMI server");
        } catch (Exception e) {
            System.out.println("RMI Connection failed: " + e.getMessage());
            return;
        }
        
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Web server: http://localhost:8080/");

        
        while (true) {
            Socket client = server.accept();
            new Thread(() -> handleClient(client)).start();
        }
    }
    
    static void handleClient(Socket client) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            
            String request = in.readLine();
            if (request == null) {
                client.close();
                return;
            }
            
            System.out.println("Request: " + request);
            
            String[] parts = request.split(" ");
            if (parts.length < 2) {
                client.close();
                return;
            }
            
            String method = parts[0];
            String path = parts[1];
            
            // Pages statiques
            if (path.equals("/") || path.equals("/index.html")) {
                serveFile(out, "index.html");
            }
            else if (path.equals("/book.html")) {
                serveFile(out, "book.html");
            }
            else if (path.equals("/admin.html")) {
                serveFile(out, "admin.html");
            }
            else if (path.equals("/room-availability.html")) {
    serveFile(out, "room-availability.html");
}
            else if (path.equals("/admin-login.html")) {  
            serveFile(out, "admin-login.html");}
            else if (path.equals("/client.html")) {
                serveFile(out, "index.html"); 
            }
            
            // Actions CLIENT (comme HotelClient.java)
            else if (path.equals("/client/list")) {
                handleClientList(out);
            }
            else if (method.equals("POST") && path.equals("/client/book")) {
                handleClientBook(in, out);
            }
            
            // Actions ADMIN (comme HotelAdmin.java)
            else if (path.equals("/admin/stats")) {
                handleAdminStats(out);
            }
            else if (path.equals("/admin/guests")) {
                handleAdminGuests(out);
            }
            else if (path.equals("/admin/revenue")) {
                handleAdminRevenue(out);
            }
            else if (path.equals("/admin/list")) {
                handleAdminList(out);
            }
            else if (method.equals("POST") && path.equals("/admin/add")) {
                handleAdminAdd(in, out);
            }
            else if (method.equals("POST") && path.equals("/admin/reset")) {
                handleAdminReset(in, out);
            }
            else {
                serve404(out);
            }
            
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   //CLIENT ACTIONS
    
    static void handleClientList(PrintWriter out) throws Exception {
        sendHtmlPage(out, "Available Rooms", hotelService.list(), "/");
    }
    
    static void handleClientBook(BufferedReader in, PrintWriter out) throws Exception {
        String body = readRequestBody(in);
        String[] params = body.split("&");
        
        String roomType = "";
        String guestName = "";
        
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                
                if (key.equals("roomType")) roomType = value;
                if (key.equals("guestName")) guestName = value;
            }
        }
        
        String result = hotelService.book(roomType, guestName);
        sendHtmlPage(out, "Booking Result", result, "/book.html");
    }
    
    //ADMIN ACTIONS
    
    static void handleAdminStats(PrintWriter out) throws Exception {
        sendHtmlPage(out, "Hotel Statistics", hotelService.getStats(), "/admin.html");
    }
    
    static void handleAdminGuests(PrintWriter out) throws Exception {
        sendHtmlPage(out, "Guest List", hotelService.guests(), "/admin.html");
    }
    
    static void handleAdminRevenue(PrintWriter out) throws Exception {
        sendHtmlPage(out, "Hotel Revenue", hotelService.revenue(), "/admin.html");
    }
    
    static void handleAdminList(PrintWriter out) throws Exception {
        sendHtmlPage(out, "Available Rooms", hotelService.list(), "/admin.html");
    }
    
    static void handleAdminAdd(BufferedReader in, PrintWriter out) throws Exception {
    String body = readRequestBody(in);
    String[] params = body.split("&");
    
    String type = "";
    String displayName = "";
    String countStr = "";
    String maxGuestsStr = "";
    String priceStr = "";
    String description = "";
    
    for (String param : params) {
        String[] keyValue = param.split("=");
        if (keyValue.length == 2) {
            String key = URLDecoder.decode(keyValue[0], "UTF-8");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            
            if (key.equals("type")) type = value;
            if (key.equals("displayName")) displayName = value;
            if (key.equals("count")) countStr = value;
            if (key.equals("maxGuests")) maxGuestsStr = value;
            if (key.equals("price")) priceStr = value;
            if (key.equals("description")) description = value;
        }
    }
    
    try {
        int count = Integer.parseInt(countStr);
        int price = Integer.parseInt(priceStr);
        int maxGuests = maxGuestsStr.isEmpty() ? 2 : Integer.parseInt(maxGuestsStr);
        
        // Create a formatted string with all room details
        String roomDetails = String.format(
            "type=%s|displayName=%s|count=%d|maxGuests=%d|price=%d|description=%s",
            type, displayName, count, maxGuests, price, description
        );
        
        // Call the existing method (will need to modify addRooms to parse this)
        String result = hotelService.addRooms(roomDetails, count, price);
        
        sendHtmlPage(out, "Add Rooms Result", result, "/admin.html");
    } catch (NumberFormatException e) {
        sendError(out, "Invalid number format", "/admin.html");
    }
}
    
    static void handleAdminReset(BufferedReader in, PrintWriter out) throws Exception {
        String body = readRequestBody(in);
        String[] params = body.split("&");
        
        String confirm = "";
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                if (key.equals("confirm")) confirm = value;
            }
        }
        
        String result;
        if ("yes".equalsIgnoreCase(confirm)) {
            result = hotelService.resetSystem();
        } else {
            result = "Reset cancelled. Type 'yes' to confirm.";
        }
        
        sendHtmlPage(out, "System Reset", result, "/admin.html");
    }
    
    // ==================== HELPER METHODS ====================
    
    static void serveFile(PrintWriter out, String filename) throws IOException {
        File file = new File(filename);
        
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println();
        
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }
            reader.close();
        } else {
            out.println("<h1>File not found: " + filename + "</h1>");
        }
    }
    
    static void sendHtmlPage(PrintWriter out, String title, String content, String backLink) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<html><head><title>" + title + "</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 40px; background: #f5f5f5; }");
        out.println(".container { max-width: 800px; margin: auto; background: white; padding: 30px; border-radius: 10px; }");
        out.println("h1 { color: #333; }");
        out.println(".result { background: #f0f8ff; padding: 20px; margin: 20px 0; border-radius: 8px; }");
        out.println(".btn { display: inline-block; padding: 10px 20px; margin-top: 20px; background: #4CAF50; color: white; text-decoration: none; border-radius: 5px; }");
        out.println("</style></head><body>");
        out.println("<div class='container'>");
        out.println("<h1>" + title + "</h1>");
        out.println("<div class='result'><pre>" + content + "</pre></div>");
        out.println("<a class='btn' href='" + backLink + "'>Back</a>");
        out.println("</div></body></html>");
    }
    
    static void sendError(PrintWriter out, String message, String backLink) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<html><body>");
        out.println("<h1>Error</h1>");
        out.println("<p>" + message + "</p>");
        out.println("<br><a href='" + backLink + "'>Back</a>");
        out.println("</body></html>");
    }
    
    static String readRequestBody(BufferedReader in) throws IOException {
        int contentLength = 0;
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.substring(15).trim());
            }
        }
        
        char[] bodyChars = new char[contentLength];
        in.read(bodyChars, 0, contentLength);
        return new String(bodyChars);
    }
    
    static void serve404(PrintWriter out) {
        out.println("HTTP/1.1 404 Not Found");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<h1>404 - Page Not Found</h1>");
        out.println("<a href='/'>Go Home</a>");
    }
}