CREATE DATABASE IF NOT EXISTS authsvc;

-- Create database for Expense Tracker / User Service
CREATE DATABASE IF NOT EXISTS consumersvc;

CREATE DATABSE IF NOT EXISTS expensesvc;
-- Optional: Grant all privileges to the root user (usually default, but safe to have)
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';