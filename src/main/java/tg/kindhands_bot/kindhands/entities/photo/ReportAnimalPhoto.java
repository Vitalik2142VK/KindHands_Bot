package tg.kindhands_bot.kindhands.entities.photo;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "report_animal_photos")
public class ReportAnimalPhoto extends Photo {
    private LocalDateTime timeLastReport;

    public LocalDateTime getTimeLastReport() {
        return timeLastReport;
    }

    public void setTimeLastReport(LocalDateTime timeLastReport) {
        this.timeLastReport = timeLastReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReportAnimalPhoto that = (ReportAnimalPhoto) o;
        return Objects.equals(timeLastReport, that.timeLastReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timeLastReport);
    }

    @Override
    public String toString() {
        return "timeLastReport=" + timeLastReport;
    }
}
