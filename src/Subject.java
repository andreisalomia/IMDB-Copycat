public interface Subject {
    void registerObserver(User observer);
    void notifyObservers(String type);
    void removeObserver(User observer);
}
