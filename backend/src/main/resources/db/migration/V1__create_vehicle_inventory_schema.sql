CREATE TABLE brand (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  CONSTRAINT uq_brand_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE model (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  brand_id BIGINT NOT NULL,
  CONSTRAINT fk_model_brand FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE RESTRICT,
  CONSTRAINT uq_model_brand_name UNIQUE (brand_id, name),
  INDEX idx_model_brand (brand_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE vehicle (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  model_id BIGINT NOT NULL,
  year INT NOT NULL,
  color VARCHAR(60) NOT NULL,
  price DECIMAL(12,2) NOT NULL,
  mileage BIGINT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT fk_vehicle_model FOREIGN KEY (model_id) REFERENCES model(id) ON DELETE RESTRICT,
  CONSTRAINT chk_vehicle_year CHECK (year BETWEEN 1900 AND 2100),
  CONSTRAINT chk_vehicle_price CHECK (price >= 0),
  CONSTRAINT chk_vehicle_mileage CHECK (mileage >= 0),
  INDEX idx_vehicle_model (model_id),
  INDEX idx_vehicle_status (status),
  INDEX idx_vehicle_year (year),
  INDEX idx_vehicle_price (price)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
