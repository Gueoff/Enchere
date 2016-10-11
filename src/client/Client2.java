package client;

import java.rmi.RemoteException;

public class Client2 {

	public static void main(String[] args) throws Exception {
		try {
			Client c2 = new Client("tata");
			c2.inscription();

			c2.encherir(400);
			

		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
