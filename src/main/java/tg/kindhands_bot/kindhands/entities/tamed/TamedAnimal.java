package tg.kindhands_bot.kindhands.entities.tamed;

import tg.kindhands_bot.kindhands.entities.Animal;
import tg.kindhands_bot.kindhands.entities.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TamedAnimal {

    @Id
    @GeneratedValue
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
    private int numReports = 30;
    private int numReportsSent = 0;
    private LocalDate dateAdoption;
    private LocalDate dateLastReport;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public int getNumReports() {
        return numReports;
    }

    public void setNumReports(int numReports) {
        this.numReports = numReports;
    }

    public int getNumReportsSent() {
        return numReportsSent;
    }

    public void setNumReportsSent(int numReportsSent) {
        this.numReportsSent = numReportsSent;
    }

    public LocalDate getDateAdoption() {
        return dateAdoption;
    }

    public void setDateAdoption(LocalDate dateAdoption) {
        this.dateAdoption = dateAdoption;
    }

    public LocalDate getDateLastReport() {
        return dateLastReport;
    }

    public void setDateLastReport(LocalDate dateLastReport) {
        this.dateLastReport = dateLastReport;
    }

    @Override
    public String toString() {
        return "TamedAnimal{" +
                "id=" + id +
                ", user=" + user +
                ", animal=" + animal +
                ", numReports=" + numReports +
                ", numReportsSent=" + numReportsSent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TamedAnimal that = (TamedAnimal) o;
        return id == that.id && numReports == that.numReports && numReportsSent == that.numReportsSent && Objects.equals(user, that.user) && Objects.equals(animal, that.animal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, animal, numReports, numReportsSent);
    }
}
