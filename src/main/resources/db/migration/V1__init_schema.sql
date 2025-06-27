-- Users
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100),
                       created_at TIMESTAMP DEFAULT now()
);

-- Accounts
CREATE TABLE account (
                         id SERIAL PRIMARY KEY,
                         user_id INTEGER NOT NULL ,
                         name VARCHAR(100) NOT NULL,
                         currency VARCHAR(3) DEFAULT 'KZT',
                         balance NUMERIC(14, 2) DEFAULT 0,
                         created_at TIMESTAMP DEFAULT now() ,
                         CONSTRAINT fk_user_id
                             FOREIGN KEY (user_id)
                                 REFERENCES users(id) ON DELETE CASCADE
);

-- Categories
CREATE TABLE category (
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER NOT NULL ,
                          name VARCHAR(100) NOT NULL,
                          type VARCHAR(20) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')) ,
                          CONSTRAINT fk_user_id
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id) ON DELETE CASCADE
);

-- Transactions
CREATE TABLE transaction (
                             id SERIAL PRIMARY KEY,
                             user_id INTEGER NOT NULL ,
                             account_id INTEGER NOT NULL ,
                             amount NUMERIC(14, 2) NOT NULL,
                             type VARCHAR(20) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE', 'TRANSFER')),
                             category_id INTEGER REFERENCES category(id),
                             description TEXT,
                             date DATE NOT NULL,
                             created_at TIMESTAMP DEFAULT now() ,
                             CONSTRAINT fk_user_id
                                 FOREIGN KEY (user_id)
                                     REFERENCES users(id) ON DELETE CASCADE ,
                             CONSTRAINT fk_account_id
                                 FOREIGN KEY (account_id)
                                     REFERENCES account(id) ON DELETE CASCADE ,
                             CONSTRAINT fk_category_id
                                 FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Transfers (связаны с типом 'TRANSFER' в transaction)
CREATE TABLE transfer (
                          id SERIAL PRIMARY KEY,
                          transaction_id INTEGER NOT NULL UNIQUE REFERENCES transaction(id) ON DELETE CASCADE,
                          from_account_id INTEGER NOT NULL REFERENCES account(id),
                          to_account_id INTEGER NOT NULL REFERENCES account(id),
                          amount NUMERIC(14, 2) NOT NULL ,
                          CONSTRAINT fk_transaction_id
                              FOREIGN KEY (transaction_id)
                                  REFERENCES transaction(id) ON DELETE CASCADE ,
                          CONSTRAINT fk_from_account_id
                              FOREIGN KEY (from_account_id) REFERENCES account(id) ,
                          CONSTRAINT fk_to_account_id
                              FOREIGN KEY (to_account_id) REFERENCES account(id)
);
