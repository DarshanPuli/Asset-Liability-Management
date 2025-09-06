-- Drop tables if they exist (MySQL style)
DROP TABLE IF EXISTS scenario_rate_change;
DROP TABLE IF EXISTS scenario;
DROP TABLE IF EXISTS deposits;
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS liabilities;
DROP TABLE IF EXISTS assets;

-- ASSETS table
CREATE TABLE assets (
                        id VARCHAR(64) PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        principal_amount DECIMAL(18,2) NOT NULL,
                        interest_rate DECIMAL(8,5) NOT NULL,
                        maturity_date DATE NOT NULL,
                        currency VARCHAR(16) NOT NULL,
                        is_fixed_rate TINYINT(1) DEFAULT 1,
                        type VARCHAR(32) NOT NULL
);

-- LOANS table (extends assets)
CREATE TABLE loans (
                       id VARCHAR(64) PRIMARY KEY,
                       borrower VARCHAR(255) NOT NULL,
                       credit_rating INT NOT NULL,
                       probability_of_default DECIMAL(8,5) NOT NULL,
                       loss_given_default DECIMAL(8,5) NOT NULL,
                       has_defaulted BOOLEAN NOT NULL DEFAULT FALSE,
                       default_date DATE NULL,
                       last_payment_date DATE NULL, -- Added to track last payment for default detection
                       CONSTRAINT fk_loan_asset FOREIGN KEY (id) REFERENCES assets(id) ON DELETE CASCADE,

    -- Add constraints for data validation
                       CONSTRAINT chk_credit_rating CHECK (credit_rating BETWEEN 1 AND 10),
                       CONSTRAINT chk_probability_default CHECK (probability_of_default BETWEEN 0 AND 1),
                       CONSTRAINT chk_loss_given_default CHECK (loss_given_default BETWEEN 0 AND 1)
);

-- LIABILITIES table
CREATE TABLE liabilities (
                             id VARCHAR(64) PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             principal_amount DECIMAL(18,2) NOT NULL,
                             interest_rate DECIMAL(8,5) NOT NULL,
                             maturity_date DATE NOT NULL,
                             currency VARCHAR(16) NOT NULL,
                             is_fixed_rate TINYINT(1) DEFAULT 1,
                             type VARCHAR(32) NOT NULL
);

-- DEPOSITS table (extends liabilities)
CREATE TABLE deposits (
                          id VARCHAR(64) PRIMARY KEY,
                          depositor VARCHAR(255) NOT NULL,
                          is_withdrawable TINYINT(1) DEFAULT 0,
                          CONSTRAINT fk_deposit_liability FOREIGN KEY (id) REFERENCES liabilities(id) ON DELETE CASCADE
);

-- SCENARIO table
CREATE TABLE scenario (
                          name VARCHAR(128) PRIMARY KEY,
                          description VARCHAR(512),
                          liquidity_shock_factor DECIMAL(8,5),
                          default_rate_increase DECIMAL(8,5)
);

-- SCENARIO_RATE_CHANGE table
CREATE TABLE scenario_rate_change (
                                      scenario_name VARCHAR(128),
                                      currency VARCHAR(16),
                                      rate_change DECIMAL(8,5),
                                      PRIMARY KEY (scenario_name, currency),
                                      CONSTRAINT fk_scenario_rate FOREIGN KEY (scenario_name) REFERENCES scenario(name) ON DELETE CASCADE
);


-- ASSETS
INSERT INTO assets (id, name, principal_amount, interest_rate, maturity_date, currency, is_fixed_rate, type) VALUES
                                                                                                                 -- Loans
                                                                                                                 ('L001', 'Home Loan - Alice Brown', 250000.00, 0.045, '2033-01-01', 'USD', 1, 'loan'),
                                                                                                                 ('L002', 'Auto Loan - Bob Davis', 30000.00, 0.065, '2027-06-15', 'USD', 1, 'loan'),
                                                                                                                 ('L003', 'Business Loan - Gamma Inc', 500000.00, 0.075, '2030-12-31', 'USD', 1, 'loan'),
                                                                                                                 ('L004', 'Student Loan - Carol Smith', 45000.00, 0.035, '2032-09-01', 'USD', 1, 'loan'),

                                                                                                                 -- Bonds
                                                                                                                 ('B001', 'Corporate Bond - Acme Corp', 1000000.00, 0.035, '2031-12-31', 'USD', 1, 'bond'),
                                                                                                                 ('B002', 'Government Bond - US Treasury', 2000000.00, 0.020, '2035-06-30', 'USD', 1, 'bond'),
                                                                                                                 ('B003', 'Municipal Bond - NY Metro', 750000.00, 0.028, '2033-03-15', 'USD', 1, 'bond'),

                                                                                                                 -- Cash Reserve (5% of total loans)
                                                                                                                 ('C001', 'Cash Reserve', 41250.00, 0.000, NULL, 'USD', 1, 'cash');

INSERT INTO liabilities (id, name, principal_amount, interest_rate, maturity_date, currency, is_fixed_rate, type) VALUES
                                                                                                                      ('L001', 'Fixed Term Deposit', 300000.00, 0.02000, '2026-01-01', 'USD', 1, 'deposit'),
                                                                                                                      ('L002', 'Corporate Debt', 1000000.00, 0.03000, '2029-11-15', 'EUR', 1, 'debt'),
                                                                                                                      ('L003', 'Retail Savings Account', 200000.00, 0.01000, '2025-12-31', 'USD', 0, 'savings'),
                                                                                                                      ('L004', 'Wholesale Funding', 500000.00, 0.02500, '2027-06-30', 'GBP', 1, 'debt'),
                                                                                                                      ('L005', 'Short-term Note', 150000.00, 0.01800, '2025-10-15', 'USD', 0, 'note'),
                                                                                                                      ('L006', 'Municipal Bond', 400000.00, 0.02200, '2028-03-20', 'EUR', 1, 'bond'),
                                                                                                                      ('L007', 'Callable Deposit', 250000.00, 0.01900, '2026-09-01', 'USD', 1, 'deposit'),
                                                                                                                      ('L008', 'Overnight Repo', 100000.00, 0.01200, '2025-09-07', 'USD', 0, 'repo'),
                                                                                                                      ('L009', 'Subordinated Debt', 350000.00, 0.02800, '2030-12-31', 'GBP', 1, 'debt'),
                                                                                                                      ('L010', 'Foreign Currency Deposit', 180000.00, 0.02100, '2027-11-11', 'EUR', 1, 'deposit');

INSERT INTO loans (id, borrower, credit_rating, probability_of_default, loss_given_default) VALUES
                                                                                                ('L001', 'Alice Brown', 8, 0.003, 0.250),
                                                                                                ('L002', 'Bob Davis', 6, 0.010, 0.400),
                                                                                                ('L003', 'Gamma Inc', 7, 0.005, 0.300),
                                                                                                ('L004', 'Carol Smith', 9, 0.002, 0.150);


-- DEPOSITS (extending liabilities)
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

-- SCENARIOS
INSERT INTO scenario (name, description, liquidity_shock_factor, default_rate_increase) VALUES
                                                                                            ('Baseline', 'Normal economic conditions', 1.00000, 0.00000),
                                                                                            ('Stress_2020', 'Pandemic-induced stress scenario', 0.75000, 0.01500),
                                                                                            ('Inflation_Surge', 'Sudden inflation spike', 0.90000, 0.00500),
                                                                                            ('Recession', 'Global recession scenario', 0.70000, 0.02000),
                                                                                            ('Boom', 'Economic boom scenario', 1.20000, -0.00500),
                                                                                            ('Energy_Crisis', 'Energy price shock', 0.85000, 0.01000),
                                                                                            ('Tech_Bubble', 'Tech sector bubble burst', 0.80000, 0.01200),
                                                                                            ('Housing_Crash', 'Housing market crash', 0.65000, 0.02500),
                                                                                            ('Currency_Crisis', 'Major currency devaluation', 0.60000, 0.03000),
                                                                                            ('Policy_Shift', 'Sudden policy tightening', 0.95000, 0.00800);

-- SCENARIO_RATE_CHANGE
INSERT INTO scenario_rate_change (scenario_name, currency, rate_change) VALUES
                                                                            ('Stress_2020', 'USD', 0.01000),
                                                                            ('Stress_2020', 'EUR', 0.01500),
                                                                            ('Inflation_Surge', 'USD', 0.02500),
                                                                            ('Inflation_Surge', 'EUR', 0.02000),
                                                                            ('Recession', 'USD', -0.01000),
                                                                            ('Recession', 'EUR', -0.01200),
                                                                            ('Boom', 'USD', 0.00500),
                                                                            ('Boom', 'EUR', 0.00400),
                                                                            ('Energy_Crisis', 'USD', 0.01800),
                                                                            ('Energy_Crisis', 'EUR', 0.01700),
                                                                            ('Tech_Bubble', 'USD', 0.01200),
                                                                            ('Tech_Bubble', 'EUR', 0.01100),
                                                                            ('Housing_Crash', 'USD', -0.01500),
                                                                            ('Housing_Crash', 'EUR', -0.01300),
                                                                            ('Currency_Crisis', 'USD', 0.03000),
                                                                            ('Currency_Crisis', 'EUR', 0.02800),
                                                                            ('Policy_Shift', 'USD', 0.00800),
                                                                            ('Policy_Shift', 'EUR', 0.00700);
