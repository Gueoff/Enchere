package serveur;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Serveur{

	

	public static void main(String[] argv) throws AlreadyBoundException {
		
		
		try {
			int port = 2202;
			//Securite pour l acces distant
			LocateRegistry.createRegistry(port);		 
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());
			}
			
			//Creation de l objet distant
			VenteImpl vente = new VenteImpl();
			String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/Enchere";
			//Naming.rebind(url, vente);
			Naming.bind("//172.16.134.146:"+port+"/enchere", vente); // //host:port/name
			
			
			
			Thread t = new Thread();
			
			while(true){
				
				
				
				t.sleep(2000);
				System.out.println(vente.getEtatVente());
				System.out.println(vente.getObjet().getPrixCourant());
			
			}
			
				
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}	
}
	
