package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Acheteur extends Remote {

	public void nouvelleSoumission(String objet, int prix);
	public void objetVendu();
	public void nouveauPrix(int prix) throws RemoteException;
}
