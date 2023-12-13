package backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class User extends Client implements Observed{
	private static final List<String> POSITIVE_WORDS = Arrays.asList("good", "great", "pog", "awesome", "swag");
	private HashMap<String, Observer> followers;
	private HashMap<String, Observed> following;
	private ArrayList<String> feed;
	private String tweet;
	private int positiveMessageCount;
	private long createdTime;
	private long updatedTime;

	public User(String id){
		super(id);
		createdTime = System.currentTimeMillis();
		followers = new HashMap<String, Observer>();
		followers.put(this.getID(), this);
		following = new HashMap<String, Observed>();
		feed = new ArrayList<String>();
		
	}

	public HashMap<String, Observer> getFollowers(){
		return followers;
	}

	public HashMap<String, Observed> getFollowing(){
		return following;
	}

	public ArrayList<String> getFeed(){
		return feed;
	}

	public void postTweet(String contents){
		tweet = contents;
		this.setMessageCount(this.getMessageCount() + 1);
		if(isPositive(contents)){
			positiveMessageCount += 1;
		}
		updatedTime = System.currentTimeMillis();
		System.out.print(getID() + ": ");
		System.out.println(updatedTime);
		notifyObservers();
	}

	public String getTweet(){
		return this.tweet;
	}

	public int getPositiveMessageCount(){
		return positiveMessageCount;
	}

	public long getCreatedTime(){
		return this.createdTime;
	}

	@Override
	public void update(Observed observed) {
		feed.add(0, (((User) observed).getID() + ": " + ((User) observed).getTweet()));
	}

	@Override
	public boolean contains(String id) {
		return this.getID().equals(id);
	}

	@Override
	public int getUserCount() {
		return 1;
	}

	@Override
	public int getGroupCount() {
		return 0;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitUser(this);
	}

	@Override
	public void attach(Observer observer) {
		addFollower(observer);
	}

	@Override
	public void notifyObservers() {
		for(Observer instance : followers.values()){
			instance.update(this);
		}
	}

	private void addFollower(Observer client){
		this.getFollowers().put(((Client) client).getID(), client);
		((User) client).addToFollowing(this);
	}

	private void addToFollowing(Observed client){
		if(client.getClass() == User.class){
			getFollowing().put(((Client) client).getID(), client);
		}
	}

	private boolean isPositive(String contents){
		boolean positive = false;
		for(String word : POSITIVE_WORDS){
			if(contents.contains(word)){
				positive = true;
			}
		}
		return positive;
	}
}
