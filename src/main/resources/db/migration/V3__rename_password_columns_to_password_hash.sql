ALTER TABLE admins RENAME COLUMN password TO password_hash;
ALTER TABLE users RENAME COLUMN password TO password_hash;