CREATE TABLE IF NOT EXISTS products (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        name VARCHAR(120) NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(19,2) NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT chk_products_price_nonneg CHECK (price >= 0),
    CONSTRAINT chk_products_quantity_nonneg CHECK (quantity >= 0)
    ) ENGINE=InnoDB;

SET @idx_exists := (
  SELECT COUNT(1)
  FROM information_schema.statistics
  WHERE table_schema = DATABASE()
    AND table_name = 'products'
    AND index_name = 'idx_products_name'
);

SET @sql := IF(@idx_exists = 0,
               'CREATE INDEX idx_products_name ON products(name)',
               'SELECT 1');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @row_count := (SELECT COUNT(*) FROM products);
SET @ins_sql := IF(@row_count = 0, '
  INSERT INTO products (name, description, price, quantity) VALUES
    (''Laptop'', ''15-inch gaming laptop with 16GB RAM and 512GB SSD'', 1500.00, 10),
    (''Phone'', ''Latest smartphone with AMOLED display'', 800.00, 20),
    (''Tablet'', ''10-inch Android tablet with stylus support'', 500.00, 15),
    (''Headphones'', ''Noise-cancelling wireless headphones'', 200.00, 30),
    (''Monitor'', ''27-inch 4K UHD monitor'', 350.00, 12),
    (''Keyboard'', ''Mechanical keyboard with RGB lighting'', 120.00, 25),
    (''Mouse'', ''Wireless ergonomic mouse'', 60.00, 40),
    (''Smartwatch'', ''Fitness smartwatch with heart rate monitor'', 180.00, 18),
    (''Camera'', ''Mirrorless digital camera with 24MP sensor'', 1200.00, 8),
    (''Printer'', ''All-in-one wireless printer'', 250.00, 6)
', 'SELECT 1');

PREPARE ins FROM @ins_sql;
EXECUTE ins;
DEALLOCATE PREPARE ins;
