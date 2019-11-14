-- JOIN 학습 1 --
ALTER TABLE category_sales ADD INDEX idx_category_sales_id (category_id);
ALTER TABLE categories ADD INDEX idx_categories_id (category_id);
ALTER TABLE product_sale_ranking ADD INDEX idx_product_sale_ranking_id (category_id, product_id);

SELECT * FROM product_sale_ranking;

ALTER TABLE product_sale_ranking DROP INDEX idx_product_sale_ranking_id;

SELECT cs.category_id, ca.name, cs.sales, ps.product_id as sale_product
FROM category_sales cs
INNER JOIN categories ca ON cs.category_id = ca.category_id
INNER JOIN product_sale_ranking ps ON cs.category_id = ps.category_id;

-- JOIN 학습 2 --
SELECT *
FROM categories ca
LEFT JOIN category_sales cs ON cs.category_id = ca.category_id
LEFT JOIN product_sale_ranking ps ON ps.sales NOT IN (20000, 10000) AND ca.category_id = ps.category_id;

-- JOIN 학습 3 --
ALTER TABLE products_20161201 ADD INDEX idx_product_id (product_id);
ALTER TABLE products_20170101 ADD INDEX idx_product_id_2 (product_id);

SELECT p1.*
FROM products_20161201 p1
LEFT JOIN products_20170101 p2 ON p1.product_id = p2.product_id
WHERE p2.product_id IS NULL;

-- GROUP BY 학습 --
ALTER TABLE purchase_log ADD INDEX idx_purchase_log (dt);
ALTER TABLE purchase_log DROP INDEX idx_purchase_log;

SELECT dt, count(*) purchase_count, SUM(purchase_amount) total_amount, AVG(purchase_amount) avg_amount
FROM purchase_log
GROUP BY dt;

ALTER TABLE nested_set_model ADD INDEX idx_lft (lft, rgt);

SELECT n1.id, CONCAT(REPEAT(" ", COUNT(*) - 1), n1.name) name, n1.lft, n1.rgt, COUNT(*) - 1 depth
FROM nested_set_model n1
JOIN nested_set_model n2 ON n1.lft BETWEEN n2.lft AND n2.rgt
GROUP BY n1.id;

-- 미션 1단계 --
ALTER TABLE survey_results_public ADD INDEX idx_hobby (hobby);

SELECT ROUND(s1.yes / (s1.yes + s2.not_yes) * 100, 1) yes, ROUND(s2.not_yes / (s1.yes + s2.not_yes) * 100, 1) no
FROM (SELECT COUNT(*) yes FROM survey_results_public WHERE hobby='YES') s1
JOIN (SELECT COUNT(*) not_yes FROM survey_results_public WHERE hobby='NO') s2;

ALTER TABLE survey_results_public DROP INDEX idx_hobby;

-- 미션 2단계 --
SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(devtype, ';', n), ';', -1) dev_type, ROUND(AVG((SUBSTRING_INDEX(YearsCodingProf, '-', 1))), 1) years_of_prof
FROM survey_results_public
JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
            UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
            UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12
            UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16
            UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20) numbers
ON CHAR_LENGTH(devtype) - CHAR_LENGTH(REPLACE(devtype, ';', '')) >= n - 1
WHERE devtype <> 'NA' AND YearsCodingProf <> 'NA'
GROUP BY dev_type;