CREATE TABLE login_token (
    id bigserial primary key,
    email varchar(255) NOT NULL,
    token varchar(255) NOT NULL UNIQUE,
    created timestamp default NULL
);
