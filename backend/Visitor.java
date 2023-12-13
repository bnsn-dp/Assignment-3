package backend;

import backend.Client;

public interface Visitor {
	public int visitClient(Client client);
	public int visitUser(Client user);
	public int visitGroup(Client group);
}
