ALTER DATABASE jwp_jdbc DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE numbers
(
    number int not null,
    primary key (number)
);

INSERT INTO numbers
VALUES (1),
       (2),
       (3),
       (4),
       (5),
       (6),
       (7),
       (8),
       (9),
       (10),
       (11),
       (12),
       (13),
       (14),
       (15),
       (16),
       (17),
       (18),
       (19),
       (20);

CREATE TABLE DEV_TYPE
SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(srp.DevType, ';', numbers.number), ';', -1) name
FROM numbers
         INNER JOIN survey_results_public AS srp
                    ON CHAR_LENGTH(srp.DevType) - CHAR_LENGTH(REPLACE(srp.DevType, ';', '')) >= numbers.number - 1
GROUP BY name;

CREATE INDEX idx_dev_name ON DEV_TYPE (name);
CREATE INDEX idx_hobby ON survey_results_public (Hobby);
CREATE INDEX idx_devType ON survey_results_public (DevType);
CREATE INDEX idx_dev_years ON survey_results_public (DevType, YearsCodingProf);