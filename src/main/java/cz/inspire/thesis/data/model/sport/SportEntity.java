package cz.inspire.thesis.data.model.sport;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.Color;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="sport")
public class SportEntity {
    @Id
    private String id;
    @Column
    private String type;
    @Column
    private String zboziId;
    @Column
    private String skladId;
    @Column
    private Integer sazbaJednotek;
    @Column
    private Boolean sazbaNaOsobu;
    @Column
    private Integer sazbaNaCas;
    @Column
    private Boolean uctovatZalohu;
    @Column
    private int podSportyCount;
    @Column
    private List<SazbaStorna> sazbyStorna;
    @Column
    private Integer minDelkaRezervace;
    @Column
    private Integer maxDelkaRezervace;
    @Column
    private Boolean objednavkaZaplniObjekt;
    @Column
    private Integer delkaRezervaceNasobkem;
    @Column
    private Color barvaPopredi;
    @Column
    private Color barvaPozadi;
    @Column
    private Boolean zobrazitText;
    /**
     * Setter in old bean look's like this -> public abstract void setViditelnyWeb(boolean uctovatZalohu);
     * Maybe it's only a mistake
     */
    @Column
    private Boolean viditelnyWeb;
    @Column
    private Integer navRezervaceOffset;
    @Column
    private Integer delkaHlavniRez;
    @Column
    private Integer minimalniPocetOsob;
    @Column
    private Integer minutyPredVyhodnocenimKapacity;
    @Column
    private Integer maximalniPocetOsobNaZakaznika;
}
