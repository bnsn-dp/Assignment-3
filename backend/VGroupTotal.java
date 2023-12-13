package backend;

import backend.Group;
import backend.User;
import backend.Client;

public class VGroupTotal implements Visitor {

	@Override
	public int visitClient(Client client) {
		int count = 0;
		if(client.getClass() == User.class){
			count += visitUser(client);
		}
		else if(client.getClass() == Group.class){
			count += visitGroup(client);
		}
		return count;
	}

	@Override
	public int visitUser(Client user) {
		return 0;
	}

	@Override
	public int visitGroup(Client group) {
		int count = 0;
		for(Client instance : ((Group) group).getMembers().values()){
			if(instance.getClass() == Group.class){
				count += 1;
			}
			count += visitUser(instance);
		}
		return count;
	}

}
