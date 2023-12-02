public class Episode {
    private String title;
    private int duration;

    public Episode(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() { return title; }
    public int getDuration() { return duration; }
}
