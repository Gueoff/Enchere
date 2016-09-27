package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import serveur.ServeurVente;

public class Client  extends UnicastRemoteObject  implements Acheteur{

	private static final long serialVersionUID = 1L;
	private ServeurVente serveur;
	private String pseudo;
	private EtatClient etat;
	private Chrono chrono;
	
	protected Client(String pseudo) throws RemoteException {
		super();
		this.pseudo = pseudo;
		etat = EtatClient.attente;
		chrono = new Chrono(60000); // Chrono d'1min
		
		// Initialisation du serveur
		try {
			serveur = (ServeurVente) Naming.lookup("//172.16.134.156:8090/enchere");
			serveur.inscriptionAcheteur(pseudo, this);
		} catch (Exception e) {
			System.out.println("Connexion au serveur " + "172.16.134.156:8090/enchere" + " refusé.");
		}
	}

	public String getPseudo() {
		return pseudo;
	}
	
	@Override
	public void nouvelleSoumission(String objet, int prix) throws RemoteException {
		try {
			//serveur.addObjet(objet, prix);
		}
		catch(Exception e){
			System.out.println("erreur");
		}
		
	}

	@Override
	public void objetVendu() throws RemoteException{
		etat = EtatClient.vendu;
		chrono.start();
	}

	@Override
	public void nouveauPrix(int prix) throws RemoteException {
		if(!chrono.getFini() & etat != EtatClient.attente) {
			serveur.rencherir(prix, this);
			etat = EtatClient.rencherir;
		}
	}
	
	public static void main(String[] argv){
		try {
			Client c = new Client("toto");
			c.objetVendu();
			int cpt = 0;
			while(true) {
				System.out.println( cpt + " " + c.chrono.getFini());
				Thread.sleep(1000);
				cpt++;
			}
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}

<<<<<<< HEAD
	@Override
	public String getPseudo() {
		// TODO Auto-generated method stub
		return this.pseudo;
	}
=======

>>>>>>> 79c3e087b45434770ebc12658dd38977bc0e620d

}
