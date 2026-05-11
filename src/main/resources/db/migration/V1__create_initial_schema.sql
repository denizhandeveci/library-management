CREATE TABLE admins
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    version      BIGINT       NOT NULL DEFAULT 0,
    created      DATETIME(6)  NOT NULL,
    updated      DATETIME(6)  NOT NULL,
    deleted      DATETIME(6)  NULL,

    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NULL,
    address      VARCHAR(255) NULL,
    password     VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_admins_email UNIQUE (email)
);

CREATE TABLE users
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    version      BIGINT       NOT NULL DEFAULT 0,
    created      DATETIME(6)  NOT NULL,
    updated      DATETIME(6)  NOT NULL,
    deleted      DATETIME(6)  NULL,

    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NULL,
    address      VARCHAR(255) NULL,
    password     VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE books
(
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    version                 BIGINT       NOT NULL DEFAULT 0,
    created                 DATETIME(6)  NOT NULL,
    updated                 DATETIME(6)  NOT NULL,
    deleted                 DATETIME(6)  NULL,

    title                   VARCHAR(255) NOT NULL,
    num_of_total_copies     INT          NOT NULL,
    num_of_copies_available INT          NOT NULL,
    author                  VARCHAR(255) NOT NULL,
    isbn                    VARCHAR(255) NOT NULL,
    genre                   VARCHAR(255) NULL,
    cover_image_url         VARCHAR(255) NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_books_isbn UNIQUE (isbn)
);

CREATE TABLE loans
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    version     BIGINT      NOT NULL DEFAULT 0,
    created     DATETIME(6) NOT NULL,
    updated     DATETIME(6) NOT NULL,
    deleted     DATETIME(6) NULL,

    book_id     BIGINT      NOT NULL,
    user_id     BIGINT      NOT NULL,
    loan_date   DATE        NOT NULL,
    due_date    DATE        NOT NULL,
    return_date DATE        NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_loans_book
        FOREIGN KEY (book_id)
            REFERENCES books (id),

    CONSTRAINT fk_loans_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

CREATE TABLE reservations
(
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    version          BIGINT      NOT NULL DEFAULT 0,
    created          DATETIME(6) NOT NULL,
    updated          DATETIME(6) NOT NULL,
    deleted          DATETIME(6) NULL,

    reservation_date DATE        NOT NULL,
    user_id          BIGINT      NOT NULL,
    book_id          BIGINT      NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_reservations_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT fk_reservations_book
        FOREIGN KEY (book_id)
            REFERENCES books (id)
);

CREATE TABLE reviews
(
    id      BIGINT        NOT NULL AUTO_INCREMENT,
    version BIGINT        NOT NULL DEFAULT 0,
    created DATETIME(6)   NOT NULL,
    updated DATETIME(6)   NOT NULL,
    deleted DATETIME(6)   NULL,

    book_id BIGINT        NOT NULL,
    user_id BIGINT        NOT NULL,
    rating  INT           NOT NULL,
    comment VARCHAR(1000) NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_reviews_book
        FOREIGN KEY (book_id)
            REFERENCES books (id),

    CONSTRAINT fk_reviews_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);