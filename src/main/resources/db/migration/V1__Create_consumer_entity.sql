CREATE TABLE consumer_entity
(
    id                       VARCHAR(255) PRIMARY KEY,
    domain                   VARCHAR(255) NOT NULL,
    "package"                VARCHAR(255) NOT NULL,
    org                      VARCHAR(255) NOT NULL,
    version                  VARCHAR(255) NOT NULL,
    shared                   BOOLEAN      NOT NULL,
    limits_cpu               VARCHAR(255) NOT NULL,
    limits_memory            VARCHAR(255) NOT NULL,
    requests_cpu             VARCHAR(255) NOT NULL,
    requests_memory          VARCHAR(255) NOT NULL,
    resources                TEXT[],
    writeable_resources      TEXT[],
    cache_disabled_resources TEXT[]
);
