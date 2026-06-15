INSERT INTO tb_category (name) VALUES
                                  ('Electronics'),
                                  ('Computers'),
                                  ('Smartphones'),
                                  ('Gaming'),
                                  ('Home Appliances'),
                                  ('Books'),
                                  ('Fashion'),
                                  ('Sports');

INSERT INTO tb_product (name, description, price, stock) VALUES
                                                           ('Gaming Laptop', 'High-performance laptop for gaming and development', 1499.99, 15),
                                                           ('Mechanical Keyboard', 'RGB mechanical keyboard with blue switches', 89.90, 50),
                                                           ('Wireless Mouse', 'Ergonomic wireless mouse with rechargeable battery', 39.99, 80),
                                                           ('Smartphone X', 'Flagship smartphone with OLED display', 999.99, 30),
                                                           ('Gaming Headset', 'Surround sound headset with noise cancellation', 79.99, 40),
                                                           ('4K Monitor', '27-inch UHD monitor for productivity and gaming', 329.99, 20),
                                                           ('Java Programming Book', 'Comprehensive guide to Java and Spring Boot', 49.90, 100),
                                                           ('Running Shoes', 'Lightweight running shoes for daily training', 119.99, 25);

INSERT INTO tb_product_category (product_id, category_id) VALUES
                                                           (1, 2),
                                                           (1, 4),
                                                           (2, 2),
                                                           (3, 2),
                                                           (4, 1),
                                                           (4, 3),
                                                           (5, 4),
                                                           (6, 2),
                                                           (7, 6),
                                                           (8, 8);