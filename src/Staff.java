//package org.example;
import java.util.*;

public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface {
    public List<Request> userRequests;
    public SortedSet<Comparable> contributions;
    public Staff(Information info, AccountType type, String username, int experience) {
        super(info, type, username, experience);
        this.userRequests = new ArrayList<>();
        this.contributions = new TreeSet<>(new NameComparator());
    }

    private class NameComparator implements Comparator<Comparable> {
        @Override
        public int compare(Comparable o1, Comparable o2) {
            String name1 = getName(o1);
            String name2 = getName(o2);
            return name1.compareTo(name2);
        }

        private String getName(Comparable obj) {
            if (obj instanceof Actor) {
                return ((Actor) obj).name;
            } else if (obj instanceof Production) {
                return ((Production) obj).title;
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