CREATE DATABASE IF NOT EXISTS expensetracker;

-- Create database for Expense Tracker / User Service
CREATE DATABASE IF NOT EXISTS consumerservice;

-- Optional: Grant all privileges to the root user (usually default, but safe to have)
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';