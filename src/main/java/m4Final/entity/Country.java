package m4Final.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "country", schema = "world")
public class Country {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "code_2", nullable = false)
    private String code2;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "continent", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Continent continent;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "surface_area", nullable = false)
    private BigDecimal surfaceArea;

    @Column(name = "indep_year")
    private Short indepYear;
    @Column(name = "population", nullable = false)
    private Integer population;
    @Column(name = "life_expectancy")
    private BigDecimal lifeExpectancy;
    @Column(name = "gnp")
    private BigDecimal gnp;
    @Column(name = "gnpo_id")
    private BigDecimal gnpoId;
    @Column(name = "local_name", nullable = false)
    private String localName;
    @Column(name = "government_form", nullable = false)
    private String governmentForm;
    @Column(name = "head_of_state")
    private String headOfState;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capital")
    private City capital;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Set<CountryLanguage> languages;




}
