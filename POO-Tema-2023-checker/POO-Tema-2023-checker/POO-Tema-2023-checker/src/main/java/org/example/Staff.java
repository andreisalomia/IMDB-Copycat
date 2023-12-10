import java.util.*;

public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface {
    List<Request> userRequests;
    SortedSet<T> contributions;
    public Staff(Information info, AccountType type, String username, int experience) {
        super(info, type, username, experience);
        this.userRequests = new ArrayList<>();
        this.contributions = new TreeSet<>(new NameComparator());
    }

    private class NameComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String name1 = getName(o1);
            String name2 = getName(o2);
            return name1.compareTo(name2);
        }

        private String getName(Object o) {
            if (o instanceof Actor) {
                return ((Actor) o).name;
            }
            else if (o instanceof Production) {
                return ((Production) o).title;
            }
            throw new IllegalArgumentException("Object is not an Actor or Production");
        }
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