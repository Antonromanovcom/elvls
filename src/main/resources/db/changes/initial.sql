--changeset a.romanov:1

create sequence elvls.quotes_id_seq;

create table elvls.QUOTE
(
    ID BIGINT PRIMARY KEY NOT NULL,
    ASK DOUBLE PRECISION,
    BID DOUBLE PRECISION,
    ISIN VARCHAR(12)
);

