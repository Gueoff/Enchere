package serveur;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;


public class Serveur{

	

	public static void main(String[] argv) {
		
		try {
		LocateRegistry.createRegistry(8090);
		Vente salonVente = new Vente();
		
		Naming.bind("//localhost:8090/enchere", salonVente); // //host:port/name
		
		}
		
		catch(Exception e) { System.out.println("erreur");}
		}

	
	
	
	
	
	public void inscriptionAcheteur(String login, InterfaceAcheteur acheteur) {
		// TODO Auto-generated method stub
		
	}

	public int rencherir(int nouveauPrix, InterfaceAcheteur acheteur) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int tempsEcoule(InterfaceAcheteur acheteur) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
