package com.antonromanov.elvl.model;

import javax.persistence.*;
import com.antonromanov.elvl.dto.QuoteDto;
import lombok.*;

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

    public Quote(String isin, Double bid, Integer ask) {
        this.isin = isin;
        this.bid = bid;
        this.ask = ask;
    }

    public static Quote $fromDto(QuoteDto dto){
        return new Quote(dto.getIsin(), dto.getBid(), dto.getAsk());
    }
}

