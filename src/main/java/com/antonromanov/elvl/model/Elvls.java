package com.antonromanov.elvl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

/**
 * Рассчитанный elvls.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "elvls", schema = "elvls", catalog = "postgres")
public class Elvls {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elvls_seq_gen")
    @SequenceGenerator(name = "elvls_seq_gen", sequenceName ="elvls.elvls_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "isin", length = 12)
    private String isin;

    @Column(name = "elvls")
    private Double elvls;

    public Elvls(String isin, Double elvls) {
        this.isin = isin;
        this.elvls = elvls;

    }
}

