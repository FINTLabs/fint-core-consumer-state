CREATE TABLE consumer_entity
(
    id              VARCHAR(255) PRIMARY KEY,
    domain          VARCHAR(255) NOT NULL,
    package         VARCHAR(255) NOT NULL,
    org             VARCHAR(255) NOT NULL,
    version         VARCHAR(255) NOT NULL,
    shared          BOOLEAN      NOT NULL,
    limits_cpu      VARCHAR(255),
    limits_memory   VARCHAR(255),
    requests_cpu    VARCHAR(255),
    requests_memory VARCHAR(255)
);

CREATE TABLE consumer_entity_resources
(
    consumer_entity_id VARCHAR(255) NOT NULL,
    element            VARCHAR(255) NOT NULL,
    PRIMARY KEY (consumer_entity_id, element),
    FOREIGN KEY (consumer_entity_id) REFERENCES consumer_entity (id)
);

CREATE TABLE consumer_entity_writeable_resources
(
    consumer_entity_id VARCHAR(255) NOT NULL,
    element            VARCHAR(255) NOT NULL,
    PRIMARY KEY (consumer_entity_id, element),
    FOREIGN KEY (consumer_entity_id) REFERENCES consumer_entity (id)
);

CREATE TABLE consumer_entity_cache_disabled_resources
(
    consumer_entity_id VARCHAR(255) NOT NULL,
    element            VARCHAR(255) NOT NULL,
    PRIMARY KEY (consumer_entity_id, element),
    FOREIGN KEY (consumer_entity_id) REFERENCES consumer_entity (id)
);
