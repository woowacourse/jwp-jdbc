-- 데이터를 jwp_jdbc 데이터베이스에 모두 import 완료한 상황이라고 가정
use jwp_jdbc;

-- Hobby 통계처리를 빠르게 하기 위한 전처리
ALTER TABLE survey_results_public
CHANGE COLUMN Respondent Respondent INT(6) NOT NULL,
CHANGE COLUMN Hobby Hobby CHAR(3) NOT NULL,
ADD INDEX Hobby (Hobby);

-- 개발자 유형을 나타내는 DEV_TYPES 테이블 만들기
DROP TABLE IF EXISTS DEV_TYPES;
CREATE TABLE DEV_TYPES (
    id INT(2) NOT NULL AUTO_INCREMENT,
    DevType VARCHAR(50),
    PRIMARY KEY (id),
    UNIQUE KEY (DevType)
) SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(survey_results_public.DevType, ';', numbers.n), ';', -1) AS DevType
FROM (
    SELECT 1 AS n
    UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
    UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13
    UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17
    UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20
    ) AS numbers INNER JOIN survey_results_public
ON CHAR_LENGTH(survey_results_public.DevType) - CHAR_LENGTH(REPLACE(survey_results_public.DevType, ';', '')) >= numbers.n - 1
WHERE YearsCodingProf != 'NA';

-- 통계 처리를 수월하게 하기 위해 값을 추출해서 별도의 테이블을 생성
DROP TABLE IF EXISTS YEARS_PROFESSIONAL_BY_DEV_TYPE;
CREATE TABLE YEARS_PROFESSIONAL_BY_DEV_TYPE (
    Respondent int NOT NULL,
    YearsProfessional int(2) NOT NULL,
    DevTypeId int(2) NOT NULL,
    FOREIGN KEY (DevTypeId) REFERENCES DEV_TYPES (id)
) SELECT result.Respondent, result.YearsProfessional, DEV_TYPES.id AS DevTypeId
FROM (
     SELECT Respondent,
            substring(replace(YearsCodingProf, 'NA', '0'), 1, 2) + 0                                 as YearsProfessional,
            SUBSTRING_INDEX(SUBSTRING_INDEX(survey_results_public.DevType, ';', numbers.n), ';', -1) AS DevType
     FROM (
         SELECT 1 AS n
         UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
         UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
         UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13
         UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17
         UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20) AS numbers
              INNER JOIN survey_results_public ON CHAR_LENGTH(survey_results_public.DevType) -
                                                  CHAR_LENGTH(REPLACE(survey_results_public.DevType, ';', '')) >=
                                                  numbers.n - 1
     WHERE YearsCodingProf != 'NA'
     ) AS result
INNER JOIN DEV_TYPES ON result.DevType = DEV_TYPES.DevType;