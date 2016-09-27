package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client  extends UnicastRemoteObject  implements Acheteur{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Client() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void nouvelleSoumission(String objet, int prix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objetVendu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nouveauPrix(int prix) {
		// TODO Auto-generated method stub
		
	}

}
