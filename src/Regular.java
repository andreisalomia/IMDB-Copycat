//package org.example;

import java.util.Comparator;
import java.util.TreeSet;

public class Regular<T extends Comparable<T>> extends User<T> implements RequestsManager {
    public Regular(Information info, AccountType type, String username, int experience) {
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
                        r.registerObserver((Admin) user);
                        r.notifyObservers("new_request");
                    } else if (user instanceof Contributor) {
                        ((Contributor) user).userRequests.add(r);
                        r.registerObserver((Contributor) user);
                        r.notifyObservers("new_request");
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
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput(r.description + "\n");
        imdb.requests.remove(r);
        Parser.writeRequests();
        if (r.type == RequestTypes.ACTOR_ISSUE || r.type == RequestTypes.MOVIE_ISSUE) {
            for (User user : imdb.users) {
                if (user.username.equals(r.userTo)) {
                    if (user instanceof Admin) {
                        ((Admin) user).userRequests.remove(r);
                        r.removeObserver((Admin) user);
                    } else if (user instanceof Contributor) {
                        ((Contributor) user).userRequests.remove(r);
                        r.removeObserver((Contributor) user);
                    }
                    break;
                }
            }
        }
        if(r.type == RequestTypes.OTHERS || r.type == RequestTypes.DELETE_ACCOUNT) {
            IMDB.RequestsHolder.removeRequest(r);
        }
    }

    public void addRating(Rating r, Production p) {
        IMDB imdb = IMDB.getInstance();
        boolean alreadyRated = false;
        for(Production prod : imdb.productions) {
            if(prod.equals(p)) {
//                If user has already rated this production, remove the old rating
                for(Rating rating : prod.ratings) {
                    if(rating.username.equals(this.username)) {
                        prod.ratings.remove(rating);
                        alreadyRated = true;
                        break;
                    }
                }
                prod.ratings.add(r);
                prod.averageRating = Production.calculateAverageRating(prod.ratings);
                Parser.writeRatings();
                if(!alreadyRated)
                    this.updateExperience(new ReviewStrategy().calculateExperience());
                break;
            }
        }
    }
}