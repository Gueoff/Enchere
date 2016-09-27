package serveur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import client.Acheteur;

public class Vente extends UnicastRemoteObject implements ServeurVente{
	
	private static final long serialVersionUID = 1L;
	private List<Acheteur> listeAcheteurs = new ArrayList<Acheteur>();
	private Objet objet;
	private Acheteur acheteurCourant;
	private EtatVente etatVente;
	
	
	protected Vente() throws RemoteException {
		super();
	}
	
	public Vente(List<Acheteur> listeAcheteurs, Objet objet)
			throws RemoteException {
		super();
		this.listeAcheteurs = listeAcheteurs;
		this.objet = objet;
		this.etatVente = EtatVente.attente;
	}

	

	

	public List<Acheteur> getListeAcheteurs() {
		return listeAcheteurs;
	}

	public void setListeAcheteurs(List<Acheteur> listeAcheteurs) {
		this.listeAcheteurs = listeAcheteurs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
		
	}

	public Objet getObjet() {
		return objet;
	}

	public void setObjet(Objet objet) {
		this.objet = objet;
	}

	public Acheteur getAcheteurCourant() {
		return acheteurCourant;
	}

	public void setAcheteurCourant(Acheteur acheteurCourant) {
		this.acheteurCourant = acheteurCourant;
	}
	

	public EtatVente getEtatVente() {
		return etatVente;
	}

	public void setEtatVente(EtatVente etatVente) {
		this.etatVente = etatVente;
	}

	@Override
	public void inscriptionAcheteur(String login, Acheteur acheteur) {
		for(Acheteur each : listeAcheteurs){
			if(each.getPseudo().equals(login) || each.getPseudo().equals(acheteur.getPseudo())){
				System.out.println("user deja pris");
			}
		}
		
		listeAcheteurs.add(acheteur);
		
	}

	@Override
	public int rencherir(int nouveauPrix, Acheteur acheteur) {
		if(this.objet.getPrixCourant() < nouveauPrix){
			this.objet.setPrixCourant(nouveauPrix);
			this.acheteurCourant = acheteur;
		}
		else{
			System.out.println("prix non valide");
		}
		
		return nouveauPrix;
	}

	
	
	@Override
	public int tempsEcoule(Acheteur acheteur) {
		// TODO Auto-generated method stub
		return 0;
	}

}
