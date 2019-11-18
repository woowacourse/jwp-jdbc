CREATE TABLE DEV_DATA (
	id INT NOT NULL auto_increment primary key,
    dev_type VARCHAR(100),
    year int(2),
    Respondent int(11),
    CONSTRAINT DEV_TYPE_fk FOREIGN KEY (Respondent) references survey_results_public (Respondent)
);

INSERT INTO DEV_DATA (dev_type, year, Respondent)
SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(s.DevType, ';', numbers.n), ';', - 1), if(s.YearsCodingProf = '30 or more years', 30, substring_index(s.YearsCodingProf, '-', 1)), s.Respondent
FROM
(SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL
SELECT 11 n UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20) as numbers
JOIN survey_results_public as s
ON CHAR_LENGTH(s.DevType) - CHAR_LENGTH(REPLACE(s.DevType, ';', '')) >= numbers.n - 1
WHERE s.YearsCodingProf != 'NA';

CREATE INDEX idx_hobby ON survey_results_public (hobby);
CREATE INDEX idx_dev_type ON DEV_DATA (Dev_type, year);