package backend;

import java.util.HashMap;
import backend.Visitor;

public class Group extends Client {
	private HashMap<String, Client> members;

	public Group(String id){
		super(id);
		members = new HashMap<String, Client>();
	}

	public HashMap<String, Client> getMembers(){
		return members;
	}

	public Client addMember(Client client){
		if(!this.contains(client.getID())){
			this.members.put(client.getID(), client);
		}
		return this;
	}

	@Override
	public void update(Observed observed) {
		for(Client instance : members.values()){
			((Observer) instance).update(observed);
		}
	}

	@Override
	public boolean contains(String id) {
		boolean has = false;
		for(Client instance : members.values()){
			if(instance.contains(id)){
				has = true;
			}
		}
		return has;
	}

	@Override
	public int getUserCount() {
		int count = 0;
		for(Client instance : this.members.values()){
			count += instance.getUserCount();
		}
		return count;
	}

	@Override
	public int getGroupCount() {
		int count = 0;
		for(Client instance : this.members.values()){
			if(instance.getClass() == Group.class){
				count += 1;
				count += instance.getGroupCount();
			}
		}
		return count;
	}

	@Override
	public int getMessageCount(){
		int count = 0;
		for(Client instance : this.members.values()){
			count += instance.getMessageCount();
		}
		return count;
	}

	@Override
	public void accept(Visitor visitor) {
		for(Client instance : members.values()){
			instance.accept(visitor);
		}
		visitor.visitGroup(this);
	}
}
