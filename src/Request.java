//package org.example;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    public RequestTypes type;
    public LocalDateTime createdDate;
    public String problemName;
    public String description;
    public String userFrom;
    public String userTo;

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
}
