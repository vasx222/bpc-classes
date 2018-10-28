CREATE TABLE customers (
  id         INTEGER IDENTITY(1, 1) PRIMARY KEY,
  name       VARCHAR(30),
  account_id integer(10)
);

CREATE TABLE accounts (
  id             INTEGER IDENTITY(1, 1) PRIMARY KEY,
  account_number integer(10),
  currency       VARCHAR(3),
  amount         integer(20)
);

CREATE TABLE currency_rates (
  id         INTEGER IDENTITY(1, 1) PRIMARY KEY,
  currency1  VARCHAR(3),
  currency2  VARCHAR(3),
  multiplier FLOAT
);