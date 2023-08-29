package tg.kindhands_bot.kindhands.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String name;
    private Boolean isBlocked;

    public User(Long id, Long chatId, String name, Boolean isBlocked) {
        this.id = id;
        this.chatId = chatId;
        this.name = name;
        this.isBlocked = isBlocked;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(chatId, user.chatId) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name);
    }

    @Override
    public String toString() {
        return "id=" + id + ", chatId=" + chatId + ", name=" + name + ", isBlocked=" + isBlocked;
    }
}
