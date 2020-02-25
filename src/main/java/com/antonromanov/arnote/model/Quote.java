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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wishes_seq_gen")
    @SequenceGenerator(name = "wishes_seq_gen", sequenceName ="arnote.wishes_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "wish", nullable = true, length = 255)
    private String wish;

    @Column(name = "price", nullable = true)
    private Integer price;

    @Column(name = "priority", nullable = true)
    private Integer priority;

    @Column(name = "archive", nullable = true)
    private Boolean ac;

    @Column(name = "description", nullable = true, length = 1024)
    private String description;

    @Column(name = "url", nullable = true, length = 1024)
    private String url;

    @ManyToOne(cascade = CascadeType.ALL)
    private LocalUser user;

    @Column
    private Integer priorityGroup;

    @Column
    private Integer priorityGroupOrder;

    @Column
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column
    @Temporal(TemporalType.DATE)
    private Date realizationDate;

    @Column
    private Boolean realized;

    @Lob
    private byte[] wishPicture;


    public Quote(String wish, Integer price, Integer priority, Integer priorityGroup, Boolean ac, String description, String url, LocalUser user, Date creationDate) {
        this.wish = wish;
        this.price = price;
        this.priority = priority;
        this.priorityGroup = priorityGroup;
        this.ac = ac;
        this.description = description;
        this.url = url;
        this.user = user;
        this.creationDate = creationDate;
    }

    public Quote(long id, String wish, int price, int priority, boolean ac, String description, String url, LocalUser user) {

        this.wish = wish;
        this.price = price;
        this.priority = priority;
        this.ac = ac;
        this.description = description;
        this.url = url;
        this.user = user;
        this.id = id;

    }

    public Quote(String wish, int price, int priority, boolean archive, String description, String url, LocalUser user) {
        this.wish = wish;
        this.price = price;
        this.priority = priority;
        this.priorityGroup = null;
        this.ac = archive;
        this.description = description;
        this.url = url;
        this.user = user;
    }
}

