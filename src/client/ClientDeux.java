package client;

import java.rmi.RemoteException;

public class ClientDeux {

	
	public static void main(String[] argv) throws Exception{
		try {
			Client c = new Client("Tutu");
			c.inscription();
			while(c.getEtat() == EtatClient.ATTENTE) {System.out.print("");} // Attente de l'appel de objetVendu() du serveur
			c.encherir(300);
			System.out.println("Tutu ne rencherit pas -> il perd la deuxieme manche avec Toto après 30sc.");
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
