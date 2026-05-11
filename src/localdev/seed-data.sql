-- Local development seed data.
-- This file is intentionally NOT part of Flyway migrations.
-- Run manually after starting the DB, and Spring/Flyway has created the schema.

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE reviews;
TRUNCATE TABLE reservations;
TRUNCATE TABLE loans;
TRUNCATE TABLE books;
TRUNCATE TABLE users;
TRUNCATE TABLE admins;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO admins (
    id, version, created, updated, deleted,
    name, email, phone_number, address, password
) VALUES
    (1, 0, NOW(6), NOW(6), NULL,
     'Admin User', 'admin@example.com', '+49 111 111111', 'Admin Street 1, Darmstadt', 'admin123');

INSERT INTO users (
    id, version, created, updated, deleted,
    name, email, phone_number, address, password
) VALUES
      (1, 0, NOW(6), NOW(6), NULL,
       'Alice Müller', 'alice@example.com', '+49 151 11111111', 'Rheinstraße 1, Darmstadt', 'password123'),

      (2, 0, NOW(6), NOW(6), NULL,
       'Bob Schneider', 'bob@example.com', '+49 151 22222222', 'Berliner Allee 2, Darmstadt', 'password123'),

      (3, 0, NOW(6), NOW(6), NULL,
       'Clara Weber', 'clara@example.com', '+49 151 33333333', 'Luisenplatz 3, Darmstadt', 'password123');

INSERT INTO books (
    id, version, created, updated, deleted,
    title, num_of_total_copies, num_of_copies_available,
    author, isbn, genre, cover_image_url
) VALUES
      (1, 0, NOW(6), NOW(6), NULL,
       'Clean Code', 3, 2,
       'Robert C. Martin', '9780132350884', 'Software Engineering', NULL),

      (2, 0, NOW(6), NOW(6), NULL,
       'Effective Java', 2, 1,
       'Joshua Bloch', '9780134685991', 'Software Engineering', NULL),

      (3, 0, NOW(6), NOW(6), NULL,
       'The Hobbit', 4, 4,
       'J. R. R. Tolkien', '9780547928227', 'Fantasy', NULL),

      (4, 0, NOW(6), NOW(6), NULL,
       'Dune', 2, 0,
       'Frank Herbert', '9780441172719', 'Science Fiction', NULL),

      (5, 0, NOW(6), NOW(6), NULL,
       'The Pragmatic Programmer', 2, 2,
       'David Thomas, Andrew Hunt', '9780201616224', 'Software Engineering', NULL);

INSERT INTO loans (
    id, version, created, updated, deleted,
    book_id, user_id, loan_date, due_date, return_date
) VALUES
      -- Alice currently has Clean Code
      (1, 0, NOW(6), NOW(6), NULL,
       1, 1, CURRENT_DATE - INTERVAL 3 DAY, CURRENT_DATE + INTERVAL 11 DAY, NULL),

      -- Bob borrowed Effective Java and already returned it
      (2, 0, NOW(6), NOW(6), NULL,
       2, 2, CURRENT_DATE - INTERVAL 20 DAY, CURRENT_DATE - INTERVAL 6 DAY, CURRENT_DATE - INTERVAL 8 DAY),

      -- Clara currently has Effective Java
      (3, 0, NOW(6), NOW(6), NULL,
       2, 3, CURRENT_DATE - INTERVAL 2 DAY, CURRENT_DATE + INTERVAL 12 DAY, NULL),

      -- Dune is fully loaned out
      (4, 0, NOW(6), NOW(6), NULL,
       4, 1, CURRENT_DATE - INTERVAL 5 DAY, CURRENT_DATE + INTERVAL 9 DAY, NULL),

      (5, 0, NOW(6), NOW(6), NULL,
       4, 2, CURRENT_DATE - INTERVAL 1 DAY, CURRENT_DATE + INTERVAL 13 DAY, NULL);

INSERT INTO reservations (
    id, version, created, updated, deleted,
    reservation_date, user_id, book_id
) VALUES
      -- Clara is first in line for Dune
      (1, 0, NOW(6), NOW(6), NULL,
       CURRENT_DATE - INTERVAL 1 DAY, 3, 4),

      -- Bob has reserved Clean Code
      (2, 0, NOW(6), NOW(6), NULL,
       CURRENT_DATE, 2, 1);

INSERT INTO reviews (
    id, version, created, updated, deleted,
    book_id, user_id, rating, comment
) VALUES
      (1, 0, NOW(6), NOW(6), NULL,
       1, 2, 5, 'Very practical and useful, even if some parts feel opinionated.'),

      (2, 0, NOW(6), NOW(6), NULL,
       3, 1, 5, 'A cozy classic. Great adventure story.'),

      (3, 0, NOW(6), NOW(6), NULL,
       4, 3, 4, 'Excellent worldbuilding, but a bit dense at times.');