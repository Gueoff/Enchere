package serveur;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

import client.Acheteur;


public class Serveur{

	

	public static void main(String[] argv) {
		
		List<Vente> salonsVente = new ArrayList<Vente>();
		Thread t = new Thread();
		Donnees bdd = new Donnees();
		bdd.initObjets();
		
		
		
		try {	
			
			
			
		for(Objet each : bdd.getListeObjets()){
			salonsVente.add(new Vente(new ArrayList<Acheteur>(), each));
			
		}
		
		
			LocateRegistry.createRegistry(8090);
			//Vente salonVente = new Vente();
			Naming.bind("//localhost:8090/enchere", salonsVente.get(0)); // //host:port/name
			
			
			
	
	
		while(true){
			
			if(salonsVente.get(0).getListeAcheteurs().size() >= 1){
				salonsVente.get(0).setEtatVente(EtatVente.encherissement);
			}
			
			
			t.sleep(2000);
			System.out.println(salonsVente.get(0).getEtatVente());
		
		}
	
	
		}
		
		catch(Exception e) { 
			System.out.println(e.getMessage());
			}

	}}
	
