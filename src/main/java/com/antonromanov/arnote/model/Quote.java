package com.antonromanov.arnote.model;

import javax.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quote", schema = "elvls", catalog = "postgres")
public class Quote {

    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quotes_seq_gen")
    @SequenceGenerator(name = "quotes_seq_gen", sequenceName ="elvls.quotes_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "isin", length = 12)
    private String isin;

    @Column(name = "bid")
    private Double bid;

    @Column(name = "ask")
    private Integer ask;
}

