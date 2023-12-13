package backend;

public interface Observed {
	public void attach(Observer observer);
    public void notifyObservers();
}
