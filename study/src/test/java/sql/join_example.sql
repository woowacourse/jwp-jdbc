SELECT c.category_id, c.name, s.sales, r.product_id
FROM categories AS c
INNER JOIN category_sales AS s ON c.category_id =  s.category_id
INNER JOIN product_sale_ranking AS r ON c.category_id = r.category_id;


SELECT c.category_id, c.name, s.sales
FROM categories AS c
LEFT JOIN category_sales AS s ON c.category_id =  s.category_id
LEFT JOIN product_sale_ranking as p on c.category_id = p.category_id
GROUP BY c.category_id, c.name, s.sales, s.sales;

SELECT dt, COUNT(order_id), SUM(purchase_amount), AVG(purchase_amount)
FROM example.purchase_log
GROUP BY dt;