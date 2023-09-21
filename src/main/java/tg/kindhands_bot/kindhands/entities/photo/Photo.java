package tg.kindhands_bot.kindhands.entities.photo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Photo {
    @Id
    @GeneratedValue
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return fileSize == photo.fileSize && Objects.equals(filePath, photo.filePath) && Objects.equals(mediaType, photo.mediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, fileSize, mediaType);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", filePath='" + filePath +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType;
    }
}
