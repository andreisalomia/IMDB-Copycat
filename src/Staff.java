import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface {
    List<Request> userRequests;
    SortedSet<T> productions;
    public Staff(Information info, AccountType type, String username) {
        super(info, type, username);
    }

    @Override
    public void addProductionSystem(Production p) {
        // add a production to the system
    }

    @Override
    public void addActorSystem(Actor a) {
        // add an actor to the system
    }

    @Override
    public void removeProductionSystem(String name) {
        // remove a production from the system
    }

    @Override
    public void removeActorSystem(String name) {
        // remove an actor from the system
    }

    @Override
    public void updateProduction(Production p) {
        // update a production in the system
    }

    @Override
    public void updateActor(Actor a) {
        // update an actor in the system
    }

    public void solveRequest(Request r) {
        // solve a request
    }

}