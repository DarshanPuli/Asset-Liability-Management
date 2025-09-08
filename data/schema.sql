BEGIN
EXECUTE IMMEDIATE 'DROP TABLE scenario_rate_change CASCADE CONSTRAINTS';
EXECUTE IMMEDIATE 'DROP TABLE scenario CASCADE CONSTRAINTS';
EXECUTE IMMEDIATE 'DROP TABLE deposits CASCADE CONSTRAINTS';
EXECUTE IMMEDIATE 'DROP TABLE loans CASCADE CONSTRAINTS';
EXECUTE IMMEDIATE 'DROP TABLE liabilities CASCADE CONSTRAINTS';
EXECUTE IMMEDIATE 'DROP TABLE assets CASCADE CONSTRAINTS';
EXCEPTION
    WHEN OTHERS THEN
        NULL; -- Ignore errors if tables do not exist
END;
/

CREATE TABLE loans (
                       id VARCHAR2(64) PRIMARY KEY,
                       borrower VARCHAR2(255) NOT NULL,
                       credit_rating NUMBER(2) NOT NULL,
                       probability_of_default NUMBER(8,5) NOT NULL,
                       loss_given_default NUMBER(8,5) NOT NULL,
                       has_defaulted NUMBER(1) DEFAULT 0 NOT NULL, -- no BOOLEAN in Oracle
                       default_date DATE,
                       last_payment_date DATE,
                       CONSTRAINT fk_loan_asset FOREIGN KEY (id) REFERENCES assets(id) ON DELETE CASCADE,
                       CONSTRAINT chk_credit_rating CHECK (credit_rating BETWEEN 1 AND 10),
                       CONSTRAINT chk_probability_default CHECK (probability_of_default BETWEEN 0 AND 1),
                       CONSTRAINT chk_loss_given_default CHECK (loss_given_default BETWEEN 0 AND 1)
);


CREATE TABLE liabilities (
                             id VARCHAR2(64) PRIMARY KEY,
                             name VARCHAR2(255) NOT NULL,
                             principal_amount NUMBER(18,2) NOT NULL,
                             interest_rate NUMBER(8,5) NOT NULL,
                             maturity_date DATE NOT NULL,
                             currency VARCHAR2(16) NOT NULL,
                             is_fixed_rate NUMBER(1) DEFAULT 1,
                             type VARCHAR2(32) NOT NULL
);


CREATE TABLE scenario (
                          name VARCHAR2(128) PRIMARY KEY,
                          description VARCHAR2(512),
                          liquidity_shock_factor NUMBER(8,5),
                          default_rate_increase NUMBER(8,5)
);

CREATE TABLE scenario_rate_change (
                                      scenario_name VARCHAR2(128),
                                      currency VARCHAR2(16),
                                      rate_change NUMBER(8,5),
                                      PRIMARY KEY (scenario_name, currency),
                                      CONSTRAINT fk_scenario_rate FOREIGN KEY (scenario_name) REFERENCES scenario(name) ON DELETE CASCADE
);


INSERT INTO assets (id, name, principal_amount, interest_rate, maturity_date, currency, is_fixed_rate, type) VALUES
                                                                                                                 ('L001', 'Home Loan - Alice Brown', 250000.00, 0.045, DATE '2033-01-01', 'USD', 1, 'loan'),
                                                                                                                 ('L002', 'Auto Loan - Bob Davis', 30000.00, 0.065, DATE '2027-06-15', 'USD', 1, 'loan'),
                                                                                                                 ('L003', 'Business Loan - Gamma Inc', 500000.00, 0.075, DATE '2030-12-31', 'USD', 1, 'loan'),
                                                                                                                 ('L004', 'Student Loan - Carol Smith', 45000.00, 0.035, DATE '2032-09-01', 'USD', 1, 'loan'),

                                                                                                                 ('B001', 'Corporate Bond - Acme Corp', 1000000.00, 0.035, DATE '2031-12-31', 'USD', 1, 'bond'),
                                                                                                                 ('B002', 'Government Bond - US Treasury', 2000000.00, 0.020, DATE '2035-06-30', 'USD', 1, 'bond'),
                                                                                                                 ('B003', 'Municipal Bond - NY Metro', 750000.00, 0.028, DATE '2033-03-15', 'USD', 1, 'bond'),

                                                                                                                 ('C001', 'Cash Reserve', 41250.00, 0.000, NULL, 'USD', 1, 'cash');

INSERT INTO liabilities (id, name, principal_amount, interest_rate, maturity_date, currency, is_fixed_rate, type) VALUES
                                                                                                                      ('L001', 'Fixed Term Deposit', 300000.00, 0.02000, DATE '2026-01-01', 'USD', 1, 'deposit'),
                                                                                                                      ('L002', 'Corporate Debt', 1000000.00, 0.03000, DATE '2029-11-15', 'EUR', 1, 'debt'),
                                                                                                                      ('L003', 'Retail Savings Account', 200000.00, 0.01000, DATE '2025-12-31', 'USD', 0, 'savings'),
                                                                                                                      ('L004', 'Wholesale Funding', 500000.00, 0.02500, DATE '2027-06-30', 'GBP', 1, 'debt'),
                                                                                                                      ('L005', 'Short-term Note', 150000.00, 0.01800, DATE '2025-10-15', 'USD', 0, 'note'),
                                                                                                                      ('L006', 'Municipal Bond', 400000.00, 0.02200, DATE '2028-03-20', 'EUR', 1, 'bond'),
                                                                                                                      ('L007', 'Callable Deposit', 250000.00, 0.01900, DATE '2026-09-01', 'USD', 1, 'deposit'),
                                                                                                                      ('L008', 'Overnight Repo', 100000.00, 0.01200, DATE '2025-09-07', 'USD', 0, 'repo'),
                                                                                                                      ('L009', 'Subordinated Debt', 350000.00, 0.02800, DATE '2030-12-31', 'GBP', 1, 'debt'),
                                                                                                                      ('L010', 'Foreign Currency Deposit', 180000.00, 0.02100, DATE '2027-11-11', 'EUR', 1, 'deposit');

INSERT INTO loans (id, borrower, credit_rating, probability_of_default, loss_given_default) VALUES
                                                                                                ('L001', 'Alice Brown', 8, 0.003, 0.250),
                                                                                                ('L002', 'Bob Davis', 6, 0.010, 0.400),
                                                                                                ('L003', 'Gamma Inc', 7, 0.005, 0.300),
                                                                                                ('L004', 'Carol Smith', 9, 0.002, 0.150);

INSERT INTO deposits (id, depositor, is_withdrawable) VALUES
                                                          ('L001', 'Alice Smith', 0),
                                                          ('L003', 'Bob Johnson', 1),
                                                          ('L007', 'Carlos Perez', 0),
                                                          ('L010', 'Diana Lee', 1),
                                                          ('L002', 'Eve Kim', 1),
                                                          ('L004', 'Frank Wang', 0),
                                                          ('L005', 'Grace Liu', 1),
                                                          ('L006', 'Henry Chan', 0),
                                                          ('L008', 'Ivy Patel', 1),
                                                          ('L009', 'Jack Ma', 0);

INSERT INTO scenario (name, description, liquidity_shock_factor, default_rate_increase) VALUES
                                                                                            ('Baseline', 'Normal economic conditions', 1.00000, 0.00000),
                                                                                            ('Stress_2020', 'Pandemic-induced stress scenario', 0.75000, 0.01500),
                                                                                            ('Inflation_Surge', 'Sudden inflation spike', 0.90000, 0.00500),
                                                                                            ('Recession', 'Global recession scenario', 0.70000, 0.02000),
                                                                                            ('Boom', 'Economic boom scenario', 1.20000, -0.00500),
                                                                                            ('Energy_Crisis', 'Energy price shock', 0
