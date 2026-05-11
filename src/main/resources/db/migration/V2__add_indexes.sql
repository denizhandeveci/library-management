CREATE INDEX idx_books_deleted ON books (deleted);
CREATE INDEX idx_books_author_deleted ON books (author, deleted);
CREATE INDEX idx_books_genre_deleted ON books (genre, deleted);

CREATE INDEX idx_users_email_deleted ON users (email, deleted);

CREATE INDEX idx_loans_user_deleted_return_date ON loans (user_id, deleted, return_date);
CREATE INDEX idx_loans_book_user_deleted_return_date ON loans (book_id, user_id, deleted, return_date);

CREATE INDEX idx_reservations_book_deleted_reservation_date ON reservations (book_id, deleted, reservation_date, id);
CREATE INDEX idx_reservations_user_deleted ON reservations (user_id, deleted);

CREATE INDEX idx_reviews_book_deleted ON reviews (book_id, deleted);
CREATE INDEX idx_reviews_user_deleted ON reviews (user_id, deleted);