import java.rmi.Naming;

public class HotelServer{
    public HotelServer(){
        try{
            //HotelManager creates HotelManagerImpl object
 
            RoomManager h = new RoomManagerImpl();
 
        Naming.rebind("rmi://localhost:1099/Hotel", h);
        }catch(Exception e){
            System.out.println("Error: _" + e);
        } 
    } 
       public static void main(String args[]){
       new HotelServer();
       } 
    }

