//package org.example;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request implements Subject{
    public RequestTypes type;
    public LocalDateTime createdDate;
    public String problemName;
    public String description;
    public String userFrom;
    public String userTo;
    public List<User> observers = new ArrayList<>();

    public Request(RequestTypes type, String createdDate, String userFrom, String problemName, String userTo, String description) {
        this.type = type;
        this.createdDate = LocalDateTime.parse(createdDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.problemName = problemName;
        this.description = description;
        this.userFrom = userFrom;
        this.userTo = userTo;
    }

    public Request(RequestTypes type, String createdDate, String userFrom, String userTo, String description) {
        this.type = type;
        this.createdDate = LocalDateTime.parse(createdDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.problemName = null;
        this.description = description;
        this.userFrom = userFrom;
        this.userTo = userTo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request{");
        sb.append("type=").append(type);
        sb.append(", createdDate=").append(createdDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        sb.append(", problemName='").append(problemName).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", userFrom='").append(userFrom).append('\'');
        sb.append(", userTo='").append(userTo).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Request RequestBuilder(User user, String problem, int issueNumber) {
        IMDB imdb = IMDB.getInstance();
        RequestTypes type = RequestTypes.values()[issueNumber - 1];
        String createdDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String userFrom = user.username;
        String description = problem;
//        Search the lists of productions and actors to get actorName or productionName from String problem
        String problemName = null;
        if (type == RequestTypes.ACTOR_ISSUE || type == RequestTypes.MOVIE_ISSUE) {
            for (Actor actor : imdb.actors) {
                if (problem.contains(actor.name)) {
                    problemName = actor.name;
                    break;
                }
            }
            for (Production production : imdb.productions) {
                if (problem.contains(production.title)) {
                    problemName = production.title;
                    break;
                }
            }
        }
        String userTo = null;
        if (type == RequestTypes.DELETE_ACCOUNT || type == RequestTypes.OTHERS) {
            userTo = "ADMIN";
        }
//        Search all contributors/admins lists of added contributions to see if problemName appears
        if (type == RequestTypes.ACTOR_ISSUE || type == RequestTypes.MOVIE_ISSUE) {
            for (User client : imdb.users) {
                if (client instanceof Contributor) {
                    for (Object contribution : ((Contributor) client).contributions) {
                        if (contribution instanceof Actor) {
                            if (((Actor) contribution).name.equals(problemName)) {
                                userTo = client.username;
                                break;
                            }
                        } else if (contribution instanceof Production) {
                            if (((Production) contribution).title.equals(problemName)) {
                                userTo = client.username;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return new Request(type, createdDate, userFrom, problemName, userTo, description);

    }

    @Override
    public void registerObserver(User observer) {
        observers.add(observer);
    }
    @Override
    public void removeObserver(User observer) {
        observers.remove(observer);
    }
    @Override
    public void notifyObservers(String type) {
        IMDB imdb = IMDB.getInstance();
//        notify the contributor/admin that he has received a request
        if(type.equals("new_request")) {
            for (User user : imdb.users) {
                if (user.username.equals(userTo)) {
                    user.update("You received a new request from " + userFrom + " regarding " + problemName + ":\n" + description);
                }
            }
        }
        if(type.equals("request_solved")) {
            for (User user : imdb.users) {
                if (user.username.equals(userFrom)) {
                    user.update("Your request regarding " + problemName + " has been solved.");
                }
            }
        }
        if(type.equals("request_denied")) {
            for (User user : imdb.users) {
                if (user.username.equals(userFrom)) {
                    user.update("Your request regarding " + problemName + " has been denied.");
                }
            }
        }
    }
}
