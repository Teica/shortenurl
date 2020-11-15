DROP TABLE IF EXISTS url;

CREATE TABLE url (
  id LONG AUTO_INCREMENT  PRIMARY KEY,
  long_url VARCHAR(300) NOT NULL UNIQUE,
  short_url VARCHAR(200) NOT NULL,
  submit_date DATE NOT NULL
);

INSERT INTO URL VALUES(1, 'https://dnevnik.hr/', 'http://localhost:8080/shortenurl/b', CURRENT_TIMESTAMP());
INSERT INTO URL VALUES(2, 'https://index.hr/', 'http://localhost:8080/shortenurl/c', '2020-11-14');
INSERT INTO URL VALUES(3, 'https://24sata.hr/', 'http://localhost:8080/shortenurl/d', '2020-11-14');
INSERT INTO URL VALUES(4, 'https://hrt.hr/', 'http://localhost:8080/shortenurl/e', '2020-11-14');
INSERT INTO URL VALUES(5, 'https://www.tportal.hr/', 'http://localhost:8080/shortenurl/f', '2020-11-06');
INSERT INTO URL VALUES(6, 'https://stackoverflow.com/', 'http://localhost:8080/shortenurl/g', '2020-11-07');
INSERT INTO URL VALUES(7, 'https://github.com/', 'http://localhost:8080/shortenurl/h', '2020-10-14');
INSERT INTO URL VALUES(8, 'https://jwt.io/', 'http://localhost:8080/shortenurl/i', '2020-10-14');