package cz.inspire.thesis.data.model.token;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "token_confirmation")
public class TokenConfirmationEntity {
    @Id
    private String id;
    @Column
    private Date cas;
    @Column
    private String zakaznikId;
    @Column
    private String uzivatelId;
    @Column
    private String tokenId;
    @Column
    private boolean confirmation;
}