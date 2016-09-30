package serveur;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import client.Acheteur;




public class Serveur{

	private final static int port = 8090;


	public static void main(String[] argv) {
		Thread t = new Thread();
		Donnees bdd = new Donnees();
		bdd.initObjets();
		
		
		try {
			VenteImpl vente = new VenteImpl(new ArrayList<Acheteur>(), bdd.getListeObjets().get(0));
			LocateRegistry.createRegistry(port);
	
			/*
			//Securite pour l acces distant
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());
			}
			*/
			

		Naming.bind("//localhost:"+port+"/enchere", vente); // //host:port/name
			

	
		while(true){
			
			t.sleep(2000);
			System.out.println(vente.getEtatVente());
			System.out.println(vente.getObjet().getPrixCourant());
		
		}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} 
		
		
	}	
}
	
