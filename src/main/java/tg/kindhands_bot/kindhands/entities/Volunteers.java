package tg.kindhands_bot.kindhands.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "volunteers")
public class Volunteers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String name;
    private Boolean free; //свободен ли волонтер. true, если да

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteers that = (Volunteers) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(name, that.name) && Objects.equals(free, that.free);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, name, free);
    }

    @Override
    public String toString() {
        return "Volunteers{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", name='" + name + '\'' +
                ", free=" + free +
                '}';
    }
}
