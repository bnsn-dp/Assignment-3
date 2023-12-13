package backend;

import javax.swing.tree.DefaultMutableTreeNode;
import backend.Visitor;

public abstract class Client extends DefaultMutableTreeNode implements Observer{
	private String id;
	private int messageCount;
	
	public Client(String id){
		super(id);
		this.id = id;
		this.setMessageCount(0);
	}

	public String getID(){
		return id;
	}

	public int getMessageCount(){
		return messageCount;
	}

	public void setMessageCount(int count){
		messageCount = count;
	}

	public abstract boolean contains(String id);
    public abstract int getUserCount();
    public abstract int getGroupCount();
	public abstract void accept(Visitor visitor);
}
