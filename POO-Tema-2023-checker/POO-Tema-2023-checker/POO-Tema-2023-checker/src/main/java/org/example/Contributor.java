public class Contributor<T extends Comparable<T>> extends Staff<T> implements RequestsManager {
    public Contributor(Information info, AccountType type, String username, int experience) {
        super(info, type, username, experience);
    }

    @Override
    public void createRequest(Request r) {
        // generate request using Request class
    }

    @Override
    public void removeRequest(Request r) {
        // remove request using Request class
    }
}
