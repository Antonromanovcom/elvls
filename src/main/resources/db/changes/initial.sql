--changeset a.romanov:1

create sequence elvls.quotes_id_seq;
create sequence elvls.elvls_id_seq;

create table elvls.QUOTE
(
    ID BIGINT PRIMARY KEY NOT NULL,
    ASK DOUBLE PRECISION,
    BID DOUBLE PRECISION,
    ISIN VARCHAR(12)
);

create table elvls.ELVLS
(
    ID BIGINT PRIMARY KEY NOT NULL,
    ELVLS DOUBLE PRECISION,
    ISIN VARCHAR(12)
);

