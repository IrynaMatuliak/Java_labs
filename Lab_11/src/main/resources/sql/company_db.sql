CREATE DATABASE company_db;
USE company_db;

CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    name CHAR(30) NOT NULL,
    phone CHAR(15)
);

CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    last_name VARCHAR(20) NOT NULL,
    first_name VARCHAR(10) NOT NULL,
    position VARCHAR(20),
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

CREATE TABLE tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    task_description VARCHAR(50) NOT NULL,
    employee_id INT,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

INSERT INTO departments (name, phone) VALUES 
('IT Department', '+380501234567'),
('HR Department', '+380501234568'),
('Finance Department', '+380501234569'),
('Marketing Department', '+380501234570'),
('Sales Department', '+380501234571'),
('Operations Department', '+380501234572'),
('R&D Department', '+380501234573'),
('Support Department', '+380501234574');

INSERT INTO employees (last_name, first_name, position, department_id) VALUES 
('Smith', 'John', 'Software Developer', 1),
('Johnson', 'Mary', 'HR Manager', 2),
('Williams', 'David', 'Financial Analyst', 3),
('Brown', 'Sarah', 'QA Engineer', 1),
('Davis', 'Michael', 'Marketing Manager', 4),
('Miller', 'Jennifer', 'Sales Manager', 5),
('Wilson', 'James', 'Operations Manager', 6),
('Moore', 'Elizabeth', 'Research Scientist', 7),
('Taylor', 'Robert', 'Support Specialist', 8),
('Anderson', 'Lisa', 'Senior Developer', 1),
('Thomas', 'Daniel', 'Recruitment Manager', 2),
('Jackson', 'Patricia', 'Senior Accountant', 3),
('White', 'Ann', 'Marketing Analyst', 4),
('Harris', 'Susan', 'Sales Executive', 5),
('Martin', 'Paul', 'Logistics Manager', 6),
('Thompson', 'Karen', 'Data Scientist', 7),
('Garcia', 'Matthew', 'Technical Support', 8),
('Martinez', 'Nancy', 'DevOps Engineer', 1),
('Robinson', 'Kevin', 'HR Specialist', 2),
('Clark', 'Amanda', 'Finance Manager', 3);

INSERT INTO tasks (task_description, employee_id) VALUES 
('Develop new web app module', 1),
('Test software functionality', 4),
('Employee performance review', 2),
('Prepare financial report', 3),
('Social media campaign', 5),
('Manage sales team targets', 6),
('Optimize operations', 7),
('Research product features', 8),
('Handle customer issues', 9),
('Code review and optimization', 10),
('Screen job applicants', 11),
('Process payroll expenses', 12),
('Analyze market trends', 13),
('Contact potential clients', 14),
('Coordinate logistics', 15),
('Analyze big data insights', 16),
('Resolve complaints', 17),
('Setup CI/CD pipeline', 18),
('Design benefits package', 19),
('Audit financial statements', 20),
('Implement database system', 1),
('Write unit tests', 4),
('Team building event', 2),
('Budget planning', 3),
('SEO optimization', 13),
('Client presentation', 14),
('Inventory management', 15),
('ML model development', 16),
('Software installation', 17),
('Cloud deployment', 18),
('Salary analysis', 19),
('Tax compliance docs', 20),
('Mobile app development', 10),
('Security testing', 4),
('Training program dev', 11);
