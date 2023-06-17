CREATE TABLE zakat_fitrah_editions
(
    id                VARCHAR(50) PRIMARY KEY,
    year              INT NOT NULL UNIQUE,
    start_date        INT NOT NULL UNIQUE,
    end_date          INT UNIQUE,
    amount_per_person INT NOT NULL
);

CREATE TABLE zakat_fitrah_applicants
(
    id                  VARCHAR(50) PRIMARY KEY,
    institution_name    VARCHAR(255) NOT NULL,
    institution_address VARCHAR(500),
    received_time       INT          NOT NULL,
    given_time          INT,
    given_amount        INT,
    zakat_id            VARCHAR(50) REFERENCES zakat_fitrah_editions (id)
);

CREATE TABLE zakat_fitrah_recipients
(
    id           VARCHAR(50) PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    address      VARCHAR(500),
    given_time   INT,
    given_amount INT,
    zakat_id     VARCHAR(50) REFERENCES zakat_fitrah_editions (id)
);

CREATE TABLE zakat_fitrah_payers
(
    id                     VARCHAR(50) PRIMARY KEY,
    name                   VARCHAR(255) NOT NULL,
    address                VARCHAR(500),
    submitted_time         INT          NOT NULL,
    total_people           INT          NOT NULL,
    total_amount           INT          NOT NULL,
    excess_amount          INT          NOT NULL,
    less_amount            INT          NOT NULL,
    excess_amount_returned BOOLEAN      NOT NULL,
    zakat_id               VARCHAR(50) REFERENCES zakat_fitrah_editions (id)
);