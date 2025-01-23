CREATE TABLE IF NOT EXISTS expenses (
                                        id BIGINT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        amount DECIMAL(10, 2) NOT NULL
);

INSERT INTO expenses (id, name, amount) VALUES
                                              (1, 'Coffee', 5.0),
                                              (2, 'Lunch', 15.0),
                                              (3, 'Taxi', 30.0);