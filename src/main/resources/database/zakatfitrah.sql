CREATE TABLE zakat_fitrah_editions
(
    id                UUID   NOT NULL UNIQUE,
    year              INT    NOT NULL UNIQUE,
    start_date        BIGINT NOT NULL UNIQUE,
    end_date          BIGINT UNIQUE,
    amount_per_person FLOAT  NOT NULL,
    PRIMARY KEY (id, year)
);

CREATE TABLE zakat_fitrah_applicants
(
    id                  UUID PRIMARY KEY,
    institution_name    VARCHAR(255) NOT NULL,
    institution_address VARCHAR(500),
    received_time       BIGINT       NOT NULL,
    given_time          BIGINT,
    given_amount        FLOAT,
    zakat_id            UUID REFERENCES zakat_fitrah_editions (id)
);

CREATE TABLE zakat_fitrah_recipients
(
    id           UUID PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    address      VARCHAR(500),
    given_time   BIGINT,
    given_amount FLOAT,
    zakat_id     UUID REFERENCES zakat_fitrah_editions (id)
);

CREATE TABLE zakat_fitrah_payers
(
    id                     UUID PRIMARY KEY,
    name                   VARCHAR(255) NOT NULL,
    address                VARCHAR(500),
    submitted_time         BIGINT       NOT NULL,
    total_people           INT          NOT NULL,
    total_amount           FLOAT        NOT NULL,
    excess_amount          FLOAT        NOT NULL,
    less_amount            FLOAT        NOT NULL,
    excess_amount_returned BOOLEAN      NOT NULL,
    zakat_id               UUID REFERENCES zakat_fitrah_editions (id)
);
