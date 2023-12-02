import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    private RequestTypes type;
    private LocalDateTime createdDate;
    private String title;
    private String description;
    private String userFrom;
    private String userTo;

    LocalDateTime date = LocalDateTime.now();
    String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    public Request(RequestTypes type, String title, String description, String userFrom, String userTo) {
        this.type = type;
        this.createdDate = LocalDateTime.parse(formattedDate);
        this.title = title;
        this.description = description;
        this.userFrom = userFrom;
        this.userTo = userTo;
    }
}
