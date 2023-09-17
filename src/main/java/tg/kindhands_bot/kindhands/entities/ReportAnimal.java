package tg.kindhands_bot.kindhands.entities;

import tg.kindhands_bot.kindhands.enums.ReportStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class ReportAnimal {
    @Id
    @GeneratedValue
    private long id;
    //private TamedAnimal tamedAnimal;
    //private PhotoReportAnimal photo;
    private String description;
    private LocalDate date;
    private int reportNumber;
    private ReportStatus reportStatus = ReportStatus.ON_INSPECTION;

    // для тестирования, удалить после добавления TamedAnimal
    private long chatId;

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

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}
