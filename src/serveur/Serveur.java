package serveur;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import client.Acheteur;




public class Serveur{

	private final static int port = 8090;


	public static void main(String[] argv) {
		
		Donnees bdd = new Donnees();
		bdd.initObjets();
		
		
		
		try {
			System.out.println("@ IP : " + InetAddress.getLocalHost());
			
			VenteImpl vente = new VenteImpl(new ArrayList<Acheteur>(), bdd.getListeObjets().get(0));
			LocateRegistry.createRegistry(port);
			Naming.bind("//localhost:"+port+"/enchere", vente);
			
			/*
			//Securite pour l acces distant
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());
			}
			*/


			

	
		while(true){
			
			System.out.println("nbr acheteurs : " + vente.getListeAcheteurs().size());
			System.out.println("etat vente : " + vente.getEtatVente());
			System.out.println("prix : " + vente.getObjet().getPrixCourant());
			Thread.sleep(5000);
		
		}
			
		} catch(RemoteException | InterruptedException | MalformedURLException | UnknownHostException | AlreadyBoundException e){
			e.printStackTrace();
		}		
	}	
}
	
