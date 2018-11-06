INSERT INTO accounts VALUES (3, 11111, 'RUB', 100000);
INSERT INTO accounts VALUES (5, 22222, 'EUR', 200);
INSERT INTO accounts VALUES (12, 33333, 'USD', 5000);

INSERT INTO customers VALUES (1, 'Vasily', 5);
INSERT INTO customers VALUES (2, 'Ilya', 3);
INSERT INTO customers VALUES (3, 'Dmitry', 12);

insert into currency_rates values (1, 'USD', 'RUB', 68.0);
insert into currency_rates values (2, 'USD', 'EUR', 0.86);
insert into currency_rates values (3, 'USD', 'USD', 1.0);

insert into currency_rates values (4, 'EUR', 'RUB', 79.46);
insert into currency_rates values (5, 'EUR', 'EUR', 1.0);
insert into currency_rates values (6, 'EUR', 'USD', 1.17);

insert into currency_rates values (7, 'RUB', 'RUB', 1.0);
insert into currency_rates values (8, 'RUB', 'EUR', 0.01);
insert into currency_rates values (9, 'RUB', 'USD', 0.02);