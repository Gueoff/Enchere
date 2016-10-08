package serveur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import client.Acheteur;

public class VenteImpl extends UnicastRemoteObject implements Vente{
	
	private static final long serialVersionUID = 1L;
	private List<Acheteur> listeAcheteurs = new ArrayList<Acheteur>();
	private Map<Acheteur, Integer> enchereTemp = new HashMap<Acheteur, Integer>();
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
		for(Acheteur each : listeAcheteurs){
			if(each.getPseudo().equals(login) || each.getPseudo().equals(acheteur.getPseudo())){
				throw new Exception("Login deja pris");
			}
		}
		if(this.etatVente.equals(EtatVente.TERMINE) || this.etatVente.equals(EtatVente.ENCHERISSEMENT)){
			throw new Exception("La vente ne peut pas etre rejointe");
		}
		if (!donnees.estInscrit(login)){
			donnees.inscription(login, acheteur);
		}
		if(this.listeAcheteurs.size() >= 1 && this.etatVente.equals(EtatVente.ATTENTE)){
			listeAcheteurs.add(acheteur);
			this.etatVente = EtatVente.ENCHERISSEMENT;
			notifyAll();
		}
	}

	
	@Override
	public synchronized int rencherir(int nouveauPrix, Acheteur acheteur) throws Exception{
		
		if(this.objet.getPrixCourant() >= nouveauPrix){
			throw new Exception("Prix non valide");
		}
		this.enchereTemp.put(acheteur, nouveauPrix);
		
		//On a reçu toutes les encheres
		if(this.enchereTemp.size() == this.listeAcheteurs.size()){
			System.out.println("on a recu toutes les encheres");
			Set<Acheteur> cles = this.enchereTemp.keySet();
			Iterator<Acheteur> it = cles.iterator();
			
			while (it.hasNext()){
				Acheteur cle = it.next();
				Integer valeur = this.enchereTemp.get(cle);
				
				if(valeur > this.objet.getPrixCourant()){
					this.objet.setPrixCourant(valeur);
					this.acheteurCourant = cle;	
			   }
			}
			this.enchereTemp.clear();
			for(Acheteur ach : this.listeAcheteurs){
				ach.notify();
			}
			
			
		}else{
			System.out.println("en attente de reponse...");
			acheteur.wait();
		}
		
		return objet.getPrixCourant();
	}

	
	
	@Override
	public synchronized int tempsEcoule(Acheteur acheteur) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void ajouterObjet(Objet objet, Acheteur acheteur) throws RemoteException {
		try {
			this.donnees.ajouterArticle(objet, acheteur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



}
