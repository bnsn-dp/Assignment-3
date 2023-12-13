package backend;

import java.util.HashMap;

public class Root extends Group{
	private HashMap<String, Client> everyone;
	public static Root instance;

	public static Root getInstance(){
		if(instance == null){
			instance = new Root();
		}
		return instance;
	}

	private Root(){
		super("root");
		everyone = new HashMap<String, Client>();
		everyone.put(this.getID(), (Client) this);
	}
}
