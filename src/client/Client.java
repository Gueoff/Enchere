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
	private long chrono;
	
	protected Client(String pseudo) throws RemoteException {
		super();
		try {
			serveur = (ServeurVente) Naming.lookup("//e103c04.ifsic.univ-rennes1.fr:8090/additionneur");
		} catch (MalformedURLException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serveur.inscriptionAcheteur(pseudo, this);
		this.pseudo = pseudo;
		etat = EtatClient.attente;
		// chrono = ?
	}

	@Override
	public void nouvelleSoumission(String objet, int prix) {
		try {
			//serveur.addObjet(objet, prix);
		}
		catch(Exception e){
			System.out.println("erreur");
		}
		
	}

	@Override
	public void objetVendu() {
		etat = EtatClient.vendu;
		
	}

	@Override
	public void nouveauPrix(int prix) throws RemoteException {
		serveur.rencherir(prix, this);
	}
	
	public static void main(String[] argv){
		try {
			ServeurVente c = (ServeurVente) Naming.lookup("//e103c04.ifsic.univ-rennes1.fr:8090/additionneur");
		}
		catch(Exception e){
			System.out.println("erreur");
		}
	}

}
