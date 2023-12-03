import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    RequestTypes type;
    LocalDateTime createdDate;
    String problemName;
    String description;
    String userFrom;
    String userTo;

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
}
