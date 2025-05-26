package psmor.limit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="limits", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Limits {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "current_limit")
    private double currentLimit;
}
