CREATE TABLE location
(
    location_id        BIGSERIAL    NOT NULL,
    public_location_id VARCHAR(21)  NOT NULL,
    location_name      VARCHAR(100) NOT NULL,
    -- TODO Add more location columns
    PRIMARY KEY (location_id),
    UNIQUE (public_location_id)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON location TO demo_app_user;

CREATE TABLE location_audit_log
(
    location_audit_log_id BIGSERIAL    NOT NULL,
    location_id           BIGINT       NOT NULL,
    action                VARCHAR(100) NOT NULL,
    action_timestamp      TIMESTAMP    NOT NULL,
    user_id               VARCHAR(100) NOT NULL,
    user_ip_address       VARCHAR(39),
    PRIMARY KEY (location_audit_log_id)
    -- Cannot use foreign key because the audit log should remain even after records have been deleted
);

GRANT SELECT, INSERT on location_audit_log TO demo_app_user;
