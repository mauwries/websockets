package nl.minvenj.nfi.websockets.db.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class ForensicCase {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Setter
    private String forensicCaseNumber;
    @Setter
    private String sin;
    @Setter
    private String status;
    @Setter
    private String username;

    protected ForensicCase() {
    }

    public ForensicCase(final String forensicCaseNumber, final String sin, final String status, final String username) {
        this.forensicCaseNumber = forensicCaseNumber;
        this.sin = sin;
        this.status = status;
        this.username = username;
    }
}
