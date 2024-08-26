package uk.me.g4dpz.fcdwrawdata.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "payload")
public class Payload implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hex_text")
    private String hexText;

    @Column(name = "created_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToMany
    @JoinColumn(name="payload_id")
    public Set<HexFrame> hexFrames;

    public Payload() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHexText() {
        return hexText;
    }

    public void setHexText(String hexText) {
        this.hexText = hexText;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Set<HexFrame> getHexFrames() {
        return hexFrames;
    }

    public void setHexFrames(Set<HexFrame> hexFrames) {
        this.hexFrames = hexFrames;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "id=" + id +
                ", hexText='" + hexText + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
