package quickbit.core.model;

import java.time.LocalDateTime;

public class NewsModel {

    private String title;
    private String content;
    private String link;
    private LocalDateTime createdAt;

    public String getTitle() {
        return title;
    }

    public NewsModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NewsModel setContent(String content) {
        this.content = content;
        return this;
    }

    public String getLink() {
        return link;
    }

    public NewsModel setLink(String link) {
        this.link = link;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public NewsModel setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
