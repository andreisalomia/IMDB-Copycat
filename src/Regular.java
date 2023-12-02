public class Regular<T extends Comparable<T>> extends User<T> implements RequestsManager {
    public Regular(Information info, AccountType type, String username) {
        super(info, type, username);
    }

    @Override
    public void createRequest(Request r) {
        // generate request using Request class
    }

    @Override
    public void removeRequest(Request r) {
        // remove request using Request class
    }

    public void addRating(Rating r, Production p) {
        // add rating to movie
    }
}