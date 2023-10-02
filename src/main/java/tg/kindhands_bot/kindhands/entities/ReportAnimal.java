package tg.kindhands_bot.kindhands.entities;

import tg.kindhands_bot.kindhands.entities.photo.ReportAnimalPhoto;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;
import tg.kindhands_bot.kindhands.enums.ReportStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class ReportAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate date;
    private String description;
    private int reportNumber;
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus = ReportStatus.ON_INSPECTION;

    @ManyToOne
    @JoinColumn(name = "tamed_animal_id")
    private TamedAnimal tamedAnimal;

    @OneToOne
    private ReportAnimalPhoto photo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public TamedAnimal getTamedAnimal() {
        return tamedAnimal;
    }

    public void setTamedAnimal(TamedAnimal tamedAnimal) {
        this.tamedAnimal = tamedAnimal;
    }

    public ReportAnimalPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(ReportAnimalPhoto photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportAnimal that = (ReportAnimal) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
