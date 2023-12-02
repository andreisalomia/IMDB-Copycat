public class Rating {
    private String username;
    private int rating;
    private String review;

    public Rating(String username, int rating, String review) {
        this.username = username;
        this.rating = rating;
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
