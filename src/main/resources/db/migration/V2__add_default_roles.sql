USE priceline;

INSERT INTO role (uid, name, default_role) values (UUID(), 'Developer', true);
INSERT INTO role (uid, name, default_role) values (UUID(), 'Product Owner', false);
INSERT INTO role (uid, name, default_role) values (UUID(), 'Tester', false);