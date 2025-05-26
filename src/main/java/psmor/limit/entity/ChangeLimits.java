package psmor.limit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="change_limits", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeLimits {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lim_id")
    private Long limId;
    @Column(name = "tran_id")
    private UUID transactionId;
    @Column(name = "change_limit")
    private double changeLimit;
}
