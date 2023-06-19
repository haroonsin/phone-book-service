package ae.phonecheckers.phone;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "booking")
public class Booking extends PanacheEntity {

    // @ManyToOne(fetch = FetchType.LAZY)
    // private Phone phone;

    // @Column(nullable = false)
    // private String bookedBy;

    // @Column(nullable = false)
    // private LocalDateTime bookedAt;
}