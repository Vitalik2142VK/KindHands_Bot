package tg.kindhands_bot.kindhands.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "volunteers")
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean adopted = false; //принят ли волонтер. true, если да
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public boolean getAdopted() {
        return adopted;
    }

    public void setAdopted(boolean adopted) {
        this.adopted = adopted;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return Objects.equals(id, volunteer.id) && Objects.equals(adopted, volunteer.adopted) && Objects.equals(phone, volunteer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, adopted, phone);
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", free=" + adopted +
                ", phone='" + phone + '\'' +
                '}';
    }
}
