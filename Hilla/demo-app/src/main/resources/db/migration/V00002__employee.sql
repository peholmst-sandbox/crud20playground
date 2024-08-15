CREATE TYPE gender AS ENUM ('MALE', 'FEMALE', 'OTHER');

CREATE TABLE employee
(
    employee_id        BIGSERIAL   NOT NULL,
    opt_lock_ver       BIGINT      NOT NULL,
    public_employee_id VARCHAR(21) NOT NULL,
    first_name         VARCHAR(100),
    middle_name        VARCHAR(100),
    last_name          VARCHAR(100),
    preferred_name     VARCHAR(100),
    birth_date         DATE,
    gender             GENDER,
    dietary_notes      TEXT,
    home_address       JSONB,
    work_phone         VARCHAR(20),
    mobile_phone       VARCHAR(20),
    home_phone         VARCHAR(20),
    work_email         VARCHAR(320),
    PRIMARY KEY (employee_id),
    UNIQUE (public_employee_id)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON employee TO demo_app_user;

CREATE TABLE employee_audit_log
(
    employee_audit_log_id BIGSERIAL                NOT NULL,
    employee_id           BIGINT                   NOT NULL,
    action                VARCHAR(100)             NOT NULL,
    action_timestamp      TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id               VARCHAR(100),
    user_ip_address       VARCHAR(39),
    PRIMARY KEY (employee_audit_log_id)
    -- Cannot use foreign key because the audit log should remain even after records have been deleted
);

GRANT SELECT, INSERT on employee_audit_log TO demo_app_user;
