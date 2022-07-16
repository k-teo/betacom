CREATE TABLE item
(
    uuid       UUID NOT NULL,
    owner_uuid UUID NOT NULL,
    name       VARCHAR(255),
    CONSTRAINT pk_item PRIMARY KEY (uuid)
);

-- ALTER TABLE item
--     ADD CONSTRAINT FK_ITEM_ON_OWNER_UUID FOREIGN KEY (owner_uuid) REFERENCES "user" (uuid);

CREATE TABLE user
(
    uuid     UUID NOT NULL,
    login    VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (uuid)
);
