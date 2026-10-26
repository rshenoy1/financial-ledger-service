-- Create corporate accounting schema baseline
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_holder VARCHAR(100) NOT NULL,
    balance NUMERIC(18, 4) NOT NULL,
    version BIGINT NOT NULL
);

-- Index critical high-throughput paths to optimize parallel retrieval parameters
CREATE INDEX idx_accounts_holder ON accounts(account_holder);