package cz.inspire.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@Entity
@Table(name="nastaveni_json")
public class NastaveniJsonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String key;

    @Column
    private String value;
}
