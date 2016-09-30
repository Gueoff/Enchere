package serveur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import client.Acheteur;

public class VenteImpl extends UnicastRemoteObject implements Vente{
	static Object lock = new Object();
	
	private static final long serialVersionUID = 1L;
	private List<Acheteur> listeAcheteurs = new ArrayList<Acheteur>();
	private Objet objet;
	private Acheteur acheteurCourant;
	private EtatVente etatVente;
	private Donnees donnees;
	
	
	protected VenteImpl() throws RemoteException {
		super();
	}
	
	public VenteImpl(List<Acheteur> listeAcheteurs, Objet objet)
			throws RemoteException {
		super();
		this.listeAcheteurs = listeAcheteurs;
		this.objet = objet;
		this.etatVente = EtatVente.ATTENTE;
		this.donnees = new Donnees();
		donnees.initObjets();
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
	public void inscriptionAcheteur(String login, Acheteur acheteur) throws Exception{
		synchronized(lock){
			for(Acheteur each : listeAcheteurs){
				if(each.getPseudo().equals(login) || each.getPseudo().equals(acheteur.getPseudo())){
					throw new Exception("Login deja pris");
				}
			}
		}
		if(this.etatVente.equals(EtatVente.TERMINE) || this.etatVente.equals(EtatVente.ENCHERISSEMENT)){
			throw new Exception("La vente ne peut pas etre rejointe");
		}
		if(this.listeAcheteurs.size() >= 1 && this.etatVente.equals(EtatVente.ATTENTE)){
			this.etatVente = EtatVente.ENCHERISSEMENT;
		}
		if (!donnees.estInscrit(login)){
			donnees.inscription(login, acheteur);
		}
		listeAcheteurs.add(acheteur);
		
	}

	
	@Override
	public synchronized int rencherir(int nouveauPrix, Acheteur acheteur) throws Exception{
		
		if(this.objet.getPrixCourant() >= nouveauPrix){
			throw new Exception("Prix non valide");
		}
		
		this.objet.setPrixCourant(nouveauPrix);
		this.acheteurCourant = acheteur;		
		return nouveauPrix;
	}

	
	
	@Override
	public synchronized int tempsEcoule(Acheteur acheteur) {
		// TODO Auto-generated method stub
		return 0;
	}



}
