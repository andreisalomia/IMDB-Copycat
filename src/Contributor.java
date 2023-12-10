
public class Contributor<T extends Comparable<T>> extends Staff<T> implements RequestsManager {
    public Contributor(Information info, AccountType type, String username, int experience) {
        super(info, type, username, experience);
    }

    @Override
    public void createRequest(Request r) {
        IMDB imdb = IMDB.getInstance();
        imdb.requests.add(r);
        Parser.writeRequests();
        if (r.type == RequestTypes.ACTOR_ISSUE || r.type == RequestTypes.MOVIE_ISSUE) {
            for (User user : imdb.users) {
                if (user.username.equals(r.userTo)) {
                    if (user instanceof Admin) {
                        ((Admin) user).userRequests.add(r);
                    } else if (user instanceof Contributor) {
                        ((Contributor) user).userRequests.add(r);
                    }
                    break;
                }
            }
        }
        if(r.type == RequestTypes.OTHERS || r.type == RequestTypes.DELETE_ACCOUNT) {
            IMDB.RequestsHolder.addRequest(r);
        }
    }

    @Override
    public void removeRequest(Request r) {
        // remove request using Request class
    }
}
