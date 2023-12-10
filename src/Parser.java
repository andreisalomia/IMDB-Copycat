import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Parser {

    private static Map<String, List<Episode>> parseEpisodes(JSONObject jsonObject) {
        Map<String, List<Episode>> episodesMap = new HashMap<>();
        JSONObject seasonsObject = (JSONObject) jsonObject.get("seasons");

        for (Object seasonKey : seasonsObject.keySet()) {
            String season = (String) seasonKey;
            JSONArray episodesArray = (JSONArray) seasonsObject.get(season);
            List<Episode> episodeList = new ArrayList<>();

            for (Object episodeObj : episodesArray) {
                JSONObject episode = (JSONObject) episodeObj;
                String episodeName = (String) episode.get("episodeName");
                String duration = (String) episode.get("duration");
                String numericDuration = duration.replaceAll("[^0-9]", "");
                Episode episodeInstance = new Episode(episodeName, Integer.parseInt(numericDuration));
                episodeList.add(episodeInstance);
            }

            episodesMap.put(season, episodeList);
        }

        return episodesMap;
    }

    public static List<Actor> parseActors(String filePath) {
        JSONParser parser = new JSONParser();
        List<Actor> actors = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            // Iterate through elements in the array
            for (Object element : jsonArray) {
                JSONObject jsonObject = (JSONObject) element;
                JSONArray performances = (JSONArray) jsonObject.get("performances");
                List<Actor.Pair<String, Actor.Type>> filmography = new ArrayList<>();
                for (Object performance : performances) {
                    JSONObject perfObj = (JSONObject) performance;
                    String title = (String) perfObj.get("title");
                    String type = (String) perfObj.get("type");
                    Actor.Type perfType = type.equals("Movie") ? Actor.Type.MOVIE : Actor.Type.SERIES;
                    filmography.add(new Actor.Pair<>(title, perfType));
                }
                Actor actor = new Actor((String) jsonObject.get("name"), filmography, (String) jsonObject.get("biography"));
                actors.add(actor);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        } catch (ParseException e) {
            System.out.println("Parse Exception");
        }
        return actors;
    }

    public static List<Request> parseRequests(String filePath) {
        List<Request> requests = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            // Iterate through elements in the array
            for (Object element : jsonArray) {
                JSONObject jsonObject = (JSONObject) element;
                String type = (String) jsonObject.get("type");
                String createdDate = (String) jsonObject.get("createdDate");
                String userFrom = (String) jsonObject.get("username");
                String userTo = (String) jsonObject.get("to");
                String description = (String) jsonObject.get("description");
                if (jsonObject.get("movieTitle") != null) {
                    String movieTitle = (String) jsonObject.get("movieTitle");
                    Request request = new Request(RequestTypes.valueOf(type), createdDate, userFrom, movieTitle, userTo, description);
                    requests.add(request);
                    reqHolder(request);
                } else if (jsonObject.get("actorName") != null) {
                    String actorName = (String) jsonObject.get("actorName");
                    Request request = new Request(RequestTypes.valueOf(type), createdDate, userFrom, actorName, userTo, description);
                    requests.add(request);
                    reqHolder(request);
                } else {
                    Request request = new Request(RequestTypes.valueOf(type), createdDate, userFrom, userTo, description);
                    requests.add(request);
                    reqHolder(request);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        } catch (ParseException e) {
            System.out.println("Parse Exception");
        }
        return requests;
    }

    public static List<Production> parseProductions(String filePath) {
        List<Production> productions = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            // Iterate through elements in the array
            for (Object element : jsonArray) {
                JSONObject jsonObject = (JSONObject) element;
                String type = ((String) jsonObject.get("type")).toUpperCase();
                String title = (String) jsonObject.get("title");
                int releaseYear;
                if (jsonObject.get("releaseYear") != null)
                    releaseYear = ((Long) jsonObject.get("releaseYear")).intValue();
                else
                    releaseYear = 0;
                JSONArray directors = (JSONArray) jsonObject.get("directors");
                List<String> directorsList = new ArrayList<>();
                for (Object director : directors) {
                    directorsList.add((String) director);
                }
                JSONArray actors = (JSONArray) jsonObject.get("actors");
                List<String> actorsList = new ArrayList<>();
                for (Object actor : actors) {
                    actorsList.add((String) actor);
                }
                JSONArray genres = (JSONArray) jsonObject.get("genres");
                List<Genre> genresList = new ArrayList<>();
                for (Object genre : genres) {
                    String genreString = ((String) genre).toUpperCase();
                    genresList.add(Genre.valueOf(genreString));
                }
                JSONArray ratings = (JSONArray) jsonObject.get("ratings");
                List<Rating> ratingsList = new ArrayList<>();
                for (Object rating : ratings) {
                    JSONObject ratingObj = (JSONObject) rating;
                    String username = (String) ratingObj.get("username");
                    int ratingValue = ((Long) ratingObj.get("rating")).intValue();
                    String comment = (String) ratingObj.get("comment");
                    Rating rating1 = new Rating(username, (int) ratingValue, comment);
                    ratingsList.add(rating1);
                }
                String description = (String) jsonObject.get("plot");
                double avgRating = (Double) jsonObject.get("averageRating");
                if (type.equals("MOVIE")) {
                    String duration = (String) jsonObject.get("duration");
                    String numericDuration = duration.replaceAll("[^0-9]", "");
                    Movie movie = new Movie(title, directorsList, actorsList, genresList, ratingsList, description, Integer.parseInt(numericDuration), releaseYear);
                    productions.add(movie);
                } else {
                    int numberSeasons = ((Long) jsonObject.get("numSeasons")).intValue();
                    Map<String, List<Episode>> episodes = parseEpisodes(jsonObject);
                    Series series = new Series(title, directorsList, actorsList, genresList, ratingsList, description, releaseYear, numberSeasons, episodes);
                    productions.add(series);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        } catch (ParseException e) {
            System.out.println("Parse Exception");

        }
        return productions;
    }

    public static List<User> parseUsers(String filePath) {
        List<User> users = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            // Iterate through elements in the array
            for (Object element : jsonArray) {
                JSONObject userJson = (JSONObject) element;

                String username = (String) userJson.get("username");
                String userType = ((String) userJson.get("userType"));
                Object experienceObj = userJson.get("experience");
                long experience = -1;  // Default value for undefined experience

                if (experienceObj != null) {
                    if (experienceObj instanceof Number) {
                        experience = ((Number) experienceObj).longValue();
                    } else if (experienceObj instanceof String) {
                        experience = Long.parseLong((String) experienceObj);
                    }
                }

                JSONObject informationJson = (JSONObject) userJson.get("information");
                User<?> user = userFactory(AccountType.valueOf(userType), username,
                        parseCredentials((JSONObject) informationJson.get("credentials")),
                        (String) informationJson.get("name"),
                        (String) informationJson.get("country"),
                        Integer.parseInt(informationJson.get("age").toString()),
                        ((String) informationJson.get("gender")).charAt(0),
                        LocalDate.parse((String) informationJson.get("birthDate"), DateTimeFormatter.ISO_DATE),
                        (int) experience);

                // extract from the json file the users notifications
                JSONArray notifications = (JSONArray) userJson.get("notifications");
                if (notifications != null) {
                    for (Object notification : notifications) {
                        String notif = (String) notification;
                        user.notifications.add(notif);
                    }
                }

                JSONArray favProductions = (JSONArray) userJson.get("favoriteProductions");
                JSONArray favActors = (JSONArray) userJson.get("favoriteActors");
                if (favProductions != null) {
                    for (Object production : favProductions) {
                        String title = (String) production;
                        for (Production p : IMDB.getInstance().productions) {
                            if (p.title.equals(title)) {
                                user.favorites.add(p);
                            }
                        }
                    }
                }
                if (favActors != null) {
                    for (Object actor : favActors) {
                        String name = (String) actor;
                        for (Actor a : IMDB.getInstance().actors) {
                            if (a.name.equals(name)) {
                                user.favorites.add(a);
                            }
                        }
                    }
                }

                if (user instanceof Contributor<?> || user instanceof Admin<?>) {
                    // extract from the json file the users productionsContributions and actorsContributions
                    JSONArray productionsContributions = (JSONArray) userJson.get("productionsContribution");
                    JSONArray actorsContributions = (JSONArray) userJson.get("actorsContribution");
                    if (productionsContributions != null) {
                        for (Object production : productionsContributions) {
                            String title = (String) production;
                            for (Production p : IMDB.getInstance().productions) {
                                if (p.title.equals(title)) {
                                    if (user instanceof Contributor) {
                                        ((Contributor) user).contributions.add(p);
                                    } else if (user instanceof Admin) {
                                        ((Admin) user).contributions.add(p);
                                    }
                                }
                            }
                        }
                    }
                    if (actorsContributions != null) {
                        for (Object actor : actorsContributions) {
                            String name = (String) actor;
                            for (Actor a : IMDB.getInstance().actors) {
                                if (a.name.equals(name)) {
                                    if (user instanceof Contributor) {
                                        ((Contributor) user).contributions.add(a);
                                    } else if (user instanceof Admin) {
                                        ((Admin) user).contributions.add(a);
                                    }
                                }
                            }
                        }
                    }
                }
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        } catch (ParseException e) {
            System.out.println("Parse Exception");
        }
        return users;
    }

    private static User<?> userFactory(AccountType type, String username, Credentials credentials, String name, String country, int age, char gender, LocalDate birthDate, int experience) {
        User.Information userInformation = new User.Information(credentials, name, country, age, gender, birthDate);

        switch (type) {
            case Regular:
                return new Regular<>(userInformation, AccountType.Regular, username, experience);
            case Contributor:
                return new Contributor<>(userInformation, AccountType.Contributor, username, experience);
            case Admin:
                return new Admin<>(userInformation, AccountType.Admin, username, experience);
            default:
                throw new IllegalArgumentException("InvalidUserException");
        }
    }

    private static void reqHolder(Request request) {
        if (request.type == RequestTypes.OTHERS || request.type == RequestTypes.DELETE_ACCOUNT) {
            IMDB.RequestsHolder.addRequest(request);
        }
    }

    private static Credentials parseCredentials(JSONObject credentialsJSon) {
        String email = (String) credentialsJSon.get("email");
        String password = (String) credentialsJSon.get("password");
        return new Credentials(email, password);
    }

    public static <T extends Comparable<T>> void insertFavProduction(User tUser, Comparable production) {
        String filePath = "src/accounts.json";

        try {
            // Read existing users from the JSON file
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(new FileReader(filePath));

            // Find the user in the JSONArray and update the favoriteProductions
            for (Object element : jsonArray) {
                JSONObject userJson = (JSONObject) element;
                String username = (String) userJson.get("username");

                if (tUser.username.equals(username)) {
                    // Delete existing favoriteProductions
                    userJson.remove("favoriteProductions");

                    // Add the productions from user.favorites to favoriteProductions array
                    JSONArray favoriteProductions = new JSONArray();
                    for (Object favProduction : tUser.favorites) {
                        if (favProduction instanceof Production) {
                            String productionTitle = ((Production) favProduction).title;
                            favoriteProductions.add(productionTitle);
                        }
                    }

                    userJson.put("favoriteProductions", favoriteProductions);
                    break;
                }
            }

            // Write the updated JSON array back to the file
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }


    public static <T extends Comparable<T>> void insertFavActor(User tUser, Comparable production) {
        String filePath = "src/accounts.json";

        try {
            // Read existing users from the JSON file
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(new FileReader(filePath));

            // Find the user in the JSONArray and update the favoriteProductions
            for (Object element : jsonArray) {
                JSONObject userJson = (JSONObject) element;
                String username = (String) userJson.get("username");

                if (tUser.username.equals(username)) {
                    // Delete existing favoriteProductions
                    userJson.remove("favoriteActors");

                    // Add the productions from user.favorites to favoriteProductions array
                    JSONArray favoriteActors = new JSONArray();
                    for (Object favActor : tUser.favorites) {
                        if (favActor instanceof Actor) {
                            String actorTitle = ((Actor) favActor).name;
                            favoriteActors.add(actorTitle);
                        }
                    }

                    userJson.put("favoriteActors", favoriteActors);
                    break;
                }
            }

            // Write the updated JSON array back to the file
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    public static void writeRequests() {
        String filePath = "src/requests.json";

        try {
            // Convert requests to JSON array
            JSONArray jsonArray = new JSONArray();
            for (Request request : IMDB.getInstance().requests) {
                JSONObject requestJson = new JSONObject();
                requestJson.put("type", request.type.name());
                requestJson.put("createdDate", request.createdDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                if(request.type == RequestTypes.ACTOR_ISSUE || request.type == RequestTypes.MOVIE_ISSUE) {
                    //check if it's a movie title or a actor name use a for to check all lists of productions and actors
                    for (Production production : IMDB.getInstance().productions) {
                        if (production.title.equals(request.problemName)) {
                            requestJson.put("movieTitle", request.problemName);
                            break;
                        }
                    }
                    for (Actor actor : IMDB.getInstance().actors) {
                        if (actor.name.equals(request.problemName)) {
                            requestJson.put("actorName", request.problemName);
                            break;
                        }
                    }
                }
                requestJson.put("description", request.description);
                requestJson.put("username", request.userFrom);
                requestJson.put("to", request.userTo);
                jsonArray.add(requestJson);
            }

            // Write the JSON array to the file
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeRatings() {
        IMDB imdb = IMDB.getInstance();
        String filePath = "src/production.json";

        try {
            // Read existing productions from the JSON file
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(new FileReader(filePath));

            // Update the ratings in the JSONArray
            for (Object element : jsonArray) {
                JSONObject productionJson = (JSONObject) element;
                String title = (String) productionJson.get("title");

                // Find the production in the JSONArray and remove existing ratings
                for (Production production : imdb.productions) {
                    if (production.title.equals(title)) {
                        productionJson.remove("ratings");
                        productionJson.put("ratings", convertRatingsToJsonArray(production.ratings));
                        productionJson.remove("averageRating");
                        productionJson.put("averageRating", production.averageRating);
                        break;
                    }
                }
            }

            // Write the updated JSON array back to the file
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray convertRatingsToJsonArray(List<Rating> ratings) {
        JSONArray ratingsArray = new JSONArray();
        for (Rating rating : ratings) {
            JSONObject ratingJson = new JSONObject();
            ratingJson.put("username", rating.username);
            ratingJson.put("rating", rating.rating);
            ratingJson.put("comment", rating.review);

            ratingsArray.add(ratingJson);
        }
        return ratingsArray;
    }
}
