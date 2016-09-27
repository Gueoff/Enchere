package serveur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import client.Acheteur;

public class Vente extends UnicastRemoteObject implements ServeurVente{
	
	protected Vente() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Acheteur> listeAcheteurs = new ArrayList<Acheteur>();
	
	

	public List<Acheteur> getListeAcheteurs() {
		return listeAcheteurs;
	}

	public void setListeAcheteurs(List<Acheteur> listeAcheteurs) {
		this.listeAcheteurs = listeAcheteurs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void inscriptionAcheteur(String login, Acheteur acheteur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int rencherir(int nouveauPrix, Acheteur acheteur) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int tempsEcoule(Acheteur acheteur) {
		// TODO Auto-generated method stub
		return 0;
	}

}
