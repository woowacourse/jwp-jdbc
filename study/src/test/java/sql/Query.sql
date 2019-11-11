DROP TABLE numbers;
DROP TABLE DevType;
DROP TABLE Result;

# numbers 테이블 생성
CREATE TABLE numbers (
	n int, primary key(n)
);

# numbers 데이터 추가
INSERT INTO numbers values (1), (2), (3), (4), (5), (6), (7), (8), (9), (10);
INSERT INTO numbers values (11), (12), (13), (14), (15), (16), (17), (18), (19), (20);
INSERT INTO numbers values (21), (22), (23), (24), (25), (26), (27), (28), (29), (30);

# develop type 테이블 생성
# 구분자(세미콜론 ;)로 develop type 나누기
CREATE TABLE develop_type
	SELECT
		substring_index(substring_index(DevType, ';', numbers.n), ';', -1) as dev
	FROM numbers
		INNER JOIN survey_results_public as srp
		ON char_length(srp.DevType) - char_length(replace(srp.DevType, ';', '')) >= numbers.n -1
	WHERE YearsCodingProf != 'NA'
	GROUP BY dev;

# YearsCodingProf 컬럼 추가한 result 테이블 생성
CREATE TABLE result
	SELECT dev, YearsCodingProf as years
	FROM survey_results_public
		INNER JOIN develop_type
		ON survey_results_public.DevType like concat('%', develop_type.dev, '%');

# 인덱스 설정
CREATE INDEX idx_dev_years ON result(dev, years);

# Mission - 1
SELECT Hobby, round(count(*) * 100 / (SELECT count(*) FROM survey_results_public), 1) as percent
FROM survey_results_public
GROUP BY Hobby;

# Mission - 2
SELECT dev, round(avg(years), 1) as years
FROM result
WHERE years <> 'NA' && dev <> 'NA'
GROUP BY dev;