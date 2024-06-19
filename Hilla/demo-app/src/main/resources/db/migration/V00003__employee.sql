CREATE TYPE gender AS ENUM ('M', 'F', 'O');

CREATE TABLE employment_type
(
    employment_type_id        BIGSERIAL    NOT NULL,
    public_employment_type_id VARCHAR(21),
    employment_type_name      VARCHAR(100) NOT NULL,
    -- TODO Add more employment type columns
    PRIMARY KEY (employment_type_id),
    UNIQUE (public_employment_type_id)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON employment_type TO demo_app_user;

CREATE TABLE employment_type_audit_log
(
    employment_type_audit_log_id BIGSERIAL    NOT NULL,
    employment_type_id           BIGINT       NOT NULL,
    action                       VARCHAR(100) NOT NULL,
    action_timestamp             TIMESTAMP    NOT NULL,
    user_id                      VARCHAR(100) NOT NULL,
    user_ip_address              VARCHAR(39),
    PRIMARY KEY (employment_type_audit_log_id)
    -- Cannot use foreign key because the audit log should remain even after records have been deleted
);

GRANT SELECT, INSERT on employment_type_audit_log TO demo_app_user;

CREATE TABLE employee
(
    employee_id        BIGSERIAL   NOT NULL,
    public_employee_id VARCHAR(21) NOT NULL,
    first_name         VARCHAR(100),
    middle_name        VARCHAR(100),
    last_name          VARCHAR(100),
    date_of_birth      DATE,
    gender             GENDER,
    dietary_notes      TEXT,
    street_address     VARCHAR(100),
    city               VARCHAR(100),
    province           VARCHAR(100),
    postal_code        VARCHAR(10),
    country            VARCHAR(100),
    work_phone         VARCHAR(20),
    mobile_phone       VARCHAR(20),
    home_phone         VARCHAR(20),
    work_email         VARCHAR(320),
    employment_type_id BIGINT      NOT NULL,
    location_id        BIGINT      NOT NULL,
    manager_id         BIGINT,
    PRIMARY KEY (employee_id),
    UNIQUE (public_employee_id),
    FOREIGN KEY (employment_type_id) REFERENCES employment_type (employment_type_id),
    FOREIGN KEY (location_id) REFERENCES location (location_id),
    FOREIGN KEY (manager_id) REFERENCES employee (employee_id)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON employee TO demo_app_user;

CREATE TABLE employee_audit_log
(
    employee_audit_log_id BIGSERIAL    NOT NULL,
    employee_id           BIGINT       NOT NULL,
    action                VARCHAR(100) NOT NULL,
    action_timestamp      TIMESTAMP    NOT NULL,
    user_id               VARCHAR(100) NOT NULL,
    user_ip_address       VARCHAR(39),
    PRIMARY KEY (employee_audit_log_id)
    -- Cannot use foreign key because the audit log should remain even after records have been deleted
);

GRANT SELECT, INSERT on employee_audit_log TO demo_app_user;
