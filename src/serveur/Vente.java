package serveur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Vente extends UnicastRemoteObject implements ServeurVente{
	
	protected Vente() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<InterfaceAcheteur> listeAcheteurs = new ArrayList<InterfaceAcheteur>();

	@Override
	public void inscriptionAcheteur(String login, InterfaceAcheteur acheteur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int rencherir(int nouveauPrix, InterfaceAcheteur acheteur) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int tempsEcoule(InterfaceAcheteur acheteur) {
		// TODO Auto-generated method stub
		return 0;
	}

}
