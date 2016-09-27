package serveur;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;


public class Serveur{

	

	public static void main(String[] argv) {
		
		Thread t = new Thread();
		try {
			LocateRegistry.createRegistry(8090);
			Vente salonVente = new Vente();
			Naming.bind("//localhost:8090/enchere", salonVente); // //host:port/name
			
			
			
	
	
		while(true){
			System.out.println(salonVente.getListeAcheteurs());
			t.sleep(2000);
		
		}
	
	
		}
		
		catch(Exception e) { 
			e.getMessage();
			System.out.println(e.getMessage());
			}

	}}
	
