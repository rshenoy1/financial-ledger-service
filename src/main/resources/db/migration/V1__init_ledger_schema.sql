-- Create explicit primary key generator sequence
CREATE SEQUENCE accounts_id_seq START WITH 1 INCREMENT BY 1;

-- Create corporate accounting schema baseline mapping to the generator sequence
CREATE TABLE accounts (
    id BIGINT PRIMARY KEY DEFAULT nextval('accounts_id_seq'),
    account_holder VARCHAR(100) NOT NULL,
    balance NUMERIC(18, 4) NOT NULL,
    version BIGINT NOT NULL
);

-- Index critical high-throughput paths to optimize parallel retrieval parameters
CREATE INDEX idx_accounts_holder ON accounts(account_holder);