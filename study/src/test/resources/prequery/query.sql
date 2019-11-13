CREATE VIEW dev_type AS
    SELECT
        SUBSTRING_INDEX(SUBSTRING_INDEX(DevType, ';', numbers.n), ';', - 1) AS dev_type,
        REPLACE(LEFT(YearsCodingProf, 2), '-', '') AS period
    FROM
    (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
    UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
    UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20) AS
        numbers
            INNER JOIN
        survey_results_public ON CHAR_LENGTH(DevType) - CHAR_LENGTH(REPLACE(DevType, ';', '')) >= numbers.n - 1
    WHERE
        NOT YearsCodingProf = 'NA';