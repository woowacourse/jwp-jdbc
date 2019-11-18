# 프레임워크 구현
## SQL 
아래의 SQL 을 실행해주세요!

``` SQL
### Problem 1 - Coding as Hobby

CREATE INDEX idx_Hobby ON survey_results_public(Hobby); 

### Problem 2 - Years of Coding Professional by Developer Type

CREATE TABLE IF NOT EXISTS TMP_DEV_TYPE (
	DevType text
); 

INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Engineering manager");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("DevOps specialist");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Desktop or enterprise applications developer");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Embedded applications or devices developer");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Data or business analyst");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("System administrator");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Database administrator");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Full-stack developer");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Back-end developer");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Educator or academic researcher");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Designer");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("QA or test developer");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Front-end developer");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Data scientist or machine learning specialist");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Mobile developer");
INSERT INTO TMP_DEV_TYPE(DevType) VALUES("Game or graphics developer");

SELECT d.DevType AS DevType, ROUND(AVG(s.YearsCodingProf), 1) AS YearsCodingProf
FROM survey_results_public as s, TMP_DEV_TYPE as d 
WHERE s.YearsCodingProf != 'NA' AND s.DevType LIKE CONCAT('%', d.DevType, '%')
GROUP BY d.DevType
ORDER BY YearsCodingProf DESC;
```

