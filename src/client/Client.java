package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import serveur.Objet;
import serveur.Vente;

public class Client extends UnicastRemoteObject  implements Acheteur {

	private static final long serialVersionUID = 1L;
	private static final String adresseServeur = "172.16.134.145:8090/enchere";
	private Vente serveur;
	private String pseudo;
	private EtatClient etat;
	private Chrono chrono;
	
	public Client(String pseudo) throws RemoteException {
		super();
		this.pseudo = pseudo;
		this.etat = EtatClient.ATTENTE;
		this.chrono = new Chrono(60000); // Chrono d'1min
		
		// Connexion au serveur
		try {
			serveur = (Vente) Naming.lookup("//" + adresseServeur);
			serveur.inscriptionAcheteur(pseudo, this);
			System.out.println("Connexion au serveur " + adresseServeur + " réussi. " + pseudo + " est ajouté à la liste d'acheteurs.");
		} catch (Exception e) {
			System.out.println("Connexion au serveur " + adresseServeur + " impossible.");
			e.printStackTrace();
		}
	}

	@Override
	public String getPseudo() throws RemoteException{
		return pseudo;
	}
	
	@Override
	public void nouvelleSoumission(String nom, String description, int prix) throws RemoteException {
		//serveur.addObjet(new Objet(nom, description, prix));
	}

	@Override
	public void objetVendu() throws RemoteException{
		etat = EtatClient.TERMINE;
		chrono.start();
	}

	@Override
	public void nouveauPrix(int prix) throws RemoteException, Exception {
		if(!chrono.getFini() & etat != EtatClient.ATTENTE) {
			serveur.rencherir(prix, this);
			etat = EtatClient.RENCHERI;
			System.out.println("Vous avez tente de rencherir de " + prix +"euros.");
		}
	}
	

	public Chrono getChrono() {
		return chrono;
	}

	public void setChrono(Chrono chrono) {
		this.chrono = chrono;
	}

}
