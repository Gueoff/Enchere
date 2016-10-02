package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Acheteur extends Remote {
	public void nouvelleSoumission(String objet, String description, int prix) throws RemoteException;
	
	/**
	 * 
	 * @param gagnant Client qui a gagné l'enchère
	 * @throws RemoteException
	 */
	public void objetVendu(Client gagnant) throws RemoteException;
	
	public void nouveauPrix(int prix) throws RemoteException, Exception;
	public String getPseudo() throws RemoteException;

}
