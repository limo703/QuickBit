package quickbit.dbcore.entity;

import quickbit.dbcore.entity.base.UuidTimedEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Image extends UuidTimedEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    private String filePath;
    private String originalFileName;

    public String getFilePath() {
        return filePath;
    }

    public Image setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public Image setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Image setUser(User user) {
        this.user = user;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Image setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
