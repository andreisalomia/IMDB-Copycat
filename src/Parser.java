//package org.example;
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
                double avgRating;
                if (jsonObject.get("averageRating") == null)
                    jsonObject.put("averageRating", 0.0);
                else
                    avgRating = (Double) jsonObject.get("averageRating");
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
            for (Object element : jsonArray) {
                JSONObject userJson = (JSONObject) element;

                String username = (String) userJson.get("username");
                String userType = ((String) userJson.get("userType"));
                Object experienceObj = userJson.get("experience");
                long experience = -1;

                if (experienceObj != null) {
                    if (experienceObj instanceof Number) {
                        experience = ((Number) experienceObj).longValue();
                    } else if (experienceObj instanceof String) {
                        experience = Long.parseLong((String) experienceObj);
                    }
                }

                JSONObject informationJson = (JSONObject) userJson.get("information");
                User<?> user = UserFactory.createUser(AccountType.valueOf(userType), username,
                        parseCredentials((JSONObject) informationJson.get("credentials")),
                        (String) informationJson.get("name"),
                        (String) informationJson.get("country"),
                        Integer.parseInt(informationJson.get("age").toString()),
                        ((String) informationJson.get("gender")).charAt(0),
                        LocalDate.parse((String) informationJson.get("birthDate"), DateTimeFormatter.ISO_DATE),
                        (int) experience);

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

    private static void reqHolder(Request request) {
        if (request.type == RequestTypes.OTHERS || request.type == RequestTypes.DELETE_ACCOUNT || request.userTo.equals("admin")) {
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
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(new FileReader(filePath));

            for (Object element : jsonArray) {
                JSONObject userJson = (JSONObject) element;
                String username = (String) userJson.get("username");

                if (tUser.username.equals(username)) {
                    userJson.remove("favoriteProductions");

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
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(new FileReader(filePath));

            for (Object element : jsonArray) {
                JSONObject userJson = (JSONObject) element;
                String username = (String) userJson.get("username");

                if (tUser.username.equals(username)) {
                    userJson.remove("favoriteActors");

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
            JSONArray jsonArray = new JSONArray();
            for (Request request : IMDB.getInstance().requests) {
                JSONObject requestJson = new JSONObject();
                requestJson.put("type", request.type.name());
                requestJson.put("createdDate", request.createdDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                if (request.type == RequestTypes.ACTOR_ISSUE || request.type == RequestTypes.MOVIE_ISSUE) {
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
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(new FileReader(filePath));

            for (Object element : jsonArray) {
                JSONObject productionJson = (JSONObject) element;
                String title = (String) productionJson.get("title");

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

    public static void addActor(Actor actor) {
        String filePath = "src/actors.json";

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));

            JSONArray jsonArray;

            if (obj instanceof JSONArray) {
                jsonArray = (JSONArray) obj;
            } else {
                jsonArray = new JSONArray();
            }

            JSONObject actorJson = new JSONObject();

            actorJson.put("name", actor.name);

            JSONArray performancesJson = new JSONArray();
            for (Actor.Pair<String, Actor.Type> performance : actor.filmography) {
                JSONObject performanceJson = new JSONObject();
                performanceJson.put("title", performance.name);
                performanceJson.put("type", performance.type.toString());
                performancesJson.add(performanceJson);
            }
            actorJson.put("performances", performancesJson);

            actorJson.put("biography", actor.biography);

            jsonArray.add(actorJson);

            try (FileWriter fileWriter = new FileWriter(filePath)) {

                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void addSeries(Series series) {
        String filePath = "src/production.json";

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));

            JSONArray jsonArray;

            if (obj instanceof JSONArray) {
                jsonArray = (JSONArray) obj;
            } else {
                jsonArray = new JSONArray();
            }

            JSONObject seriesJson = new JSONObject();
            seriesJson.put("title", series.title);
            seriesJson.put("type", "Series");

            seriesJson.put("releaseYear", series.releaseYear);
            seriesJson.put("numSeasons", series.numberOfSeasons);

            JSONObject seasonsJson = new JSONObject();
            for (Map.Entry<String, List<Episode>> entry : series.episodes.entrySet()) {
                String seasonName = entry.getKey();
                JSONArray seasonEpisodesJson = new JSONArray();

                for (Episode episode : entry.getValue()) {
                    JSONObject episodeJson = new JSONObject();
                    episodeJson.put("episodeName", episode.title);
                    episodeJson.put("duration", episode.duration + " minutes");
                    seasonEpisodesJson.add(episodeJson);
                }

                seasonsJson.put(seasonName, seasonEpisodesJson);
            }
            seriesJson.put("seasons", seasonsJson);

            seriesJson.put("directors", series.directors);
            seriesJson.put("actors", series.actors);
            seriesJson.put("genres", series.genres);

            JSONArray genresJson = new JSONArray();
            for (Genre genre : series.genres) {
                genresJson.add(genre.toString());
            }
            seriesJson.put("genres", genresJson);

            seriesJson.put("ratings", new JSONArray());
            seriesJson.put("plot", series.description);

            jsonArray.add(seriesJson);

            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void addMovie(Movie movie) {
        String filePath = "src/production.json";

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));

            JSONArray jsonArray;

            if (obj instanceof JSONArray) {
                jsonArray = (JSONArray) obj;
            } else {
                jsonArray = new JSONArray();
            }

            JSONObject movieJson = new JSONObject();
            movieJson.put("title", movie.title);
            movieJson.put("type", "Movie");

            movieJson.put("duration", movie.duration + " minutes");
            movieJson.put("releaseYear", movie.releaseYear);

            movieJson.put("directors", movie.directors);
            movieJson.put("actors", movie.actors);

            JSONArray genresJson = new JSONArray();
            for (Genre genre : movie.genres) {
                genresJson.add(genre.toString());
            }
            movieJson.put("genres", genresJson);

            movieJson.put("ratings", new JSONArray());
            movieJson.put("description", movie.description);

            jsonArray.add(movieJson);

            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void removeProduction(Production p) {
        String filePath = "src/production.json";

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));

            if (obj instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) obj;

                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject productionJson = (JSONObject) jsonArray.get(i);
                    String title = (String) productionJson.get("title");

                    if (title.equals(p.title)) {
                        jsonArray.remove(i);

                        try (FileWriter fileWriter = new FileWriter(filePath)) {
                            fileWriter.write(jsonArray.toJSONString());
                            fileWriter.flush();
                        }

                        return;
                    }
                }
                System.out.println("Production not found.");
            } else {
                System.out.println("Invalid JSON file structure.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void removeActor(Actor actor) {
        String filePath = "src/actors.json";

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));

            if (obj instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) obj;

                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject actorJson = (JSONObject) jsonArray.get(i);
                    String actorName = (String) actorJson.get("name");

                    if (actorName.equals(actor.name)) {
                        jsonArray.remove(i);

                        try (FileWriter fileWriter = new FileWriter(filePath)) {
                            fileWriter.write(jsonArray.toJSONString());
                            fileWriter.flush();
                        }

                        System.out.println("Actor removed successfully.");
                        return;
                    }
                }
                System.out.println("Actor not found.");
            } else {
                System.out.println("Invalid JSON file structure.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void updateContributions(Staff user) {
        String filePath = "src/accounts.json";
        IMDB imdb = IMDB.getInstance();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filePath));

            JSONArray jsonArray;

            if (obj instanceof JSONArray) {
                jsonArray = (JSONArray) obj;
            } else {
                jsonArray = new JSONArray();
            }

            JSONObject userJson = null;
            for (Object jsonObj : jsonArray) {
                JSONObject jsonUser = (JSONObject) jsonObj;
                String username = (String) jsonUser.get("username");
                if (username.equals(user.username)) {
                    userJson = jsonUser;
                    break;
                }
            }

            if (userJson != null) {
                userJson.remove("actorsContribution");
                userJson.remove("productionsContribution");

                JSONArray actorContributions = new JSONArray();
                JSONArray productionContributions = new JSONArray();

                for (Object contribution : user.contributions) {
                    if (contribution instanceof Actor) {
                        actorContributions.add(((Actor) contribution).name);
                    } else if (contribution instanceof Production) {
                        productionContributions.add(((Production) contribution).title);
                    }
                }

                userJson.put("actorsContribution", actorContributions);
                userJson.put("productionsContribution", productionContributions);
            }

            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void writeProductions(List<Production> productions) {
        String filePath = "src/production.json";
        JSONArray jsonArray = new JSONArray();

        for (Production production : productions) {
            JSONObject productionJson = new JSONObject();

            productionJson.put("type", production instanceof Movie ? "MOVIE" : "SERIES");
            productionJson.put("title", production.title);
            productionJson.put("releaseYear", production instanceof Movie ? ((Movie) production).releaseYear : ((Series) production).releaseYear);

            productionJson.put("directors", production.directors);
            productionJson.put("actors", production.actors);

            JSONArray genresJson = new JSONArray();
            for (Genre genre : production.genres) {
                genresJson.add(genre.toString());
            }
            productionJson.put("genres", genresJson);

            JSONArray ratingsJson = new JSONArray();
            for (Rating rating : production.ratings) {
                JSONObject ratingJson = new JSONObject();
                ratingJson.put("username", rating.username);
                ratingJson.put("rating", rating.rating);
                ratingJson.put("comment", rating.review);
                ratingsJson.add(ratingJson);
            }
            productionJson.put("ratings", ratingsJson);

            productionJson.put("plot", production.description);

            if (production instanceof Movie) {
                Movie movie = (Movie) production;
                productionJson.put("duration", movie.duration + " minutes");
            } else if (production instanceof Series) {
                Series series = (Series) production;
                productionJson.put("numSeasons", series.numberOfSeasons);

                JSONObject seasonsJson = new JSONObject();
                for (Map.Entry<String, List<Episode>> entry : series.episodes.entrySet()) {
                    String seasonName = entry.getKey();
                    JSONArray seasonEpisodesJson = new JSONArray();

                    for (Episode episode : entry.getValue()) {
                        JSONObject episodeJson = new JSONObject();
                        episodeJson.put("episodeName", episode.title);
                        episodeJson.put("duration", episode.duration + " minutes");
                        seasonEpisodesJson.add(episodeJson);
                    }

                    seasonsJson.put(seasonName, seasonEpisodesJson);
                }
                productionJson.put("seasons", seasonsJson);
            }

            jsonArray.add(productionJson);
        }

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonArray.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeActors(List<Actor> actors) {
        String filePath = "src/actors.json";
        JSONArray jsonArray = new JSONArray();

        for (Actor actor : actors) {
            JSONObject actorJson = new JSONObject();

            actorJson.put("name", actor.name);
            actorJson.put("biography", actor.biography);

            JSONArray performancesJson = new JSONArray();
            for (Actor.Pair<String, Actor.Type> performance : actor.filmography) {
                JSONObject performanceJson = new JSONObject();
                performanceJson.put("title", performance.name);
                if (performance.type == Actor.Type.MOVIE)
                    performanceJson.put("type", "Movie");
                else
                    performanceJson.put("type", "Series");
                performancesJson.add(performanceJson);
            }
            actorJson.put("performances", performancesJson);

            jsonArray.add(actorJson);
        }

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonArray.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addUserToJson(User user) {
        String filePath = "src/accounts.json";
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray userArray = (JSONArray) obj;

            JSONObject userJson = new JSONObject();
            userJson.put("username", user.username);
            userJson.put("userType", user.accountType.name());
            userJson.put("experience", user.experience);

            JSONObject informationJson = new JSONObject();
            informationJson.put("credentials", user.userInformation.getCredentials().toJSON());
            informationJson.put("name", user.userInformation.name);
            informationJson.put("country", user.userInformation.country);
            informationJson.put("age", user.userInformation.age);
            informationJson.put("gender", String.valueOf(user.userInformation.gender));
            informationJson.put("birthDate", user.userInformation.birthDate.toString());

            userJson.put("information", informationJson);

            userJson.put("notifications", user.notifications);

            JSONArray favProductions = new JSONArray();
            JSONArray favActors = new JSONArray();

            for (Object favorite : user.favorites) {
                if (favorite instanceof Production) {
                    favProductions.add(((Production) favorite).title);
                } else if (favorite instanceof Actor) {
                    favActors.add(((Actor) favorite).name);
                }
            }

            userJson.put("favoriteProductions", favProductions);
            userJson.put("favoriteActors", favActors);

            if (user instanceof Contributor<?> || user instanceof Admin<?>) {
                JSONArray productionsContributions = new JSONArray();
                JSONArray actorsContributions = new JSONArray();

                if (user instanceof Contributor) {
                    for(Object contribution : ((Contributor) user).contributions) {
                        if (contribution instanceof Production) {
                            productionsContributions.add(((Production) contribution).title);
                        } else if (contribution instanceof Actor) {
                            actorsContributions.add(((Actor) contribution).name);
                        }
                    }
                } else if (user instanceof Admin) {
                    for(Object contribution : ((Admin) user).contributions) {
                        if (contribution instanceof Production) {
                            productionsContributions.add(((Production) contribution).title);
                        } else if (contribution instanceof Actor) {
                            actorsContributions.add(((Actor) contribution).name);
                        }
                    }
                }

                userJson.put("productionsContribution", productionsContributions);
                userJson.put("actorsContribution", actorsContributions);
            }

            userArray.add(userJson);

            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(userArray.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateLists() {
        IMDB imdb = IMDB.getInstance();
        imdb.actors = Parser.parseActors("src/actors.json");
        imdb.productions = Parser.parseProductions("src/production.json");
        imdb.requests = Parser.parseRequests("src/requests.json");
        imdb.users = Parser.parseUsers("src/accounts.json");
    }

    public static void removeUser(User user) {
        String filePath = "src/accounts.json";
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray userArray = (JSONArray) obj;

            Iterator<JSONObject> iterator = userArray.iterator();
            while (iterator.hasNext()) {
                JSONObject userJson = iterator.next();
                String username = (String) userJson.get("username");
                if (username.equals(user.username)) {
                    iterator.remove();
                    break;
                }
            }

            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(userArray.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateNotifications(User user) {
        String filePath = "src/accounts.json";
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;

            for (Object element : jsonArray) {
                JSONObject userJson = (JSONObject) element;
                String username = (String) userJson.get("username");

                if (username.equals(user.username)) {
                    userJson.put("notifications", user.notifications);

                    try (FileWriter fileWriter = new FileWriter(filePath)) {
                        fileWriter.write(jsonArray.toJSONString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateExperience(User user) {
        String filePath = "src/accounts.json";
        try (FileReader reader = new FileReader(filePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;

            for (Object element : jsonArray) {
                JSONObject userJson = (JSONObject) element;
                String username = (String) userJson.get("username");

                if (username.equals(user.username)) {
                    userJson.put("experience", user.experience);

                    try (FileWriter fileWriter = new FileWriter(filePath)) {
                        fileWriter.write(jsonArray.toJSONString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
