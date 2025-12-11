import java.rmi.*; 

public interface RoomManager extends Remote{
	public String book(String a, String b)
		throws RemoteException;
	public String list()
		throws RemoteException;
	public String guests()
		throws RemoteException; 
	public String getStats()
		throws RemoteException;
	public String resetSystem()
		throws RemoteException;
    public String addRooms(String type, int count, int price)
        throws RemoteException;
	public String revenue()
		throws RemoteException;



} 