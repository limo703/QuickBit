package quickbit.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class NewsModel {

    private final String title;
    private final String content;
    private final String link;
    private final LocalDateTime createdAt;

    @JsonCreator
    public NewsModel(
        @JsonProperty("title") String title,
        @JsonProperty("content") String content,
        @JsonProperty("link") String link,
        @JsonProperty("createdAt") LocalDateTime createdAt
    ) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
