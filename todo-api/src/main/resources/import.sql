CREATE TABLE IF NOT EXISTS todoitems(
    id SERIAL PRIMARY KEY,
    taskname VARCHAR(50),
    status BOOLEAN
);