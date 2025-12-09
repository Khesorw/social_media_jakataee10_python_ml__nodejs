CREATE TABLE IF NOT EXISTS test_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO test_user (username, email) VALUES
('ali', 'ali@example.com'),
('sara', 'sara@example.com'),
('mike', 'mike@example.com'),
('linda', 'linda@example.com'),
('john', 'john@example.com');
