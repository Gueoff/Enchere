package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Acheteur extends Remote {
	public void nouvelleSoumission(String objet, int prix) throws RemoteException;
	public void objetVendu() throws RemoteException;
	public void nouveauPrix(int prix) throws RemoteException;
	public String getPseudo();
}
