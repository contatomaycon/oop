INSERT IGNORE INTO brand (name) VALUES
('Toyota'),
('Honda'),
('Ford'),
('Chevrolet'),
('Volkswagen'),
('BMW'),
('Mercedes-Benz'),
('Audi'),
('Nissan'),
('Hyundai'),
('Fiat'),
('Renault');

INSERT IGNORE INTO model (name, brand_id)
SELECT 'Corolla', id FROM brand WHERE name = 'Toyota'
UNION ALL SELECT 'Hilux', id FROM brand WHERE name = 'Toyota'
UNION ALL SELECT 'Civic', id FROM brand WHERE name = 'Honda'
UNION ALL SELECT 'HR-V', id FROM brand WHERE name = 'Honda'
UNION ALL SELECT 'Ranger', id FROM brand WHERE name = 'Ford'
UNION ALL SELECT 'Onix', id FROM brand WHERE name = 'Chevrolet'
UNION ALL SELECT 'Tracker', id FROM brand WHERE name = 'Chevrolet'
UNION ALL SELECT 'Polo', id FROM brand WHERE name = 'Volkswagen'
UNION ALL SELECT '320i', id FROM brand WHERE name = 'BMW'
UNION ALL SELECT 'C180', id FROM brand WHERE name = 'Mercedes-Benz'
UNION ALL SELECT 'A3', id FROM brand WHERE name = 'Audi'
UNION ALL SELECT 'Kicks', id FROM brand WHERE name = 'Nissan'
UNION ALL SELECT 'HB20', id FROM brand WHERE name = 'Hyundai'
UNION ALL SELECT 'Strada', id FROM brand WHERE name = 'Fiat'
UNION ALL SELECT 'Duster', id FROM brand WHERE name = 'Renault';

INSERT INTO vehicle (model_id, year, color, price, mileage, status)
SELECT m.id, 2022, 'Branco', 98000.00, 25000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Toyota' AND m.name = 'Corolla'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2022 AND v.color = 'Branco'
      AND v.price = 98000.00 AND v.mileage = 25000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2020, 'Prata', 85000.00, 52000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Toyota' AND m.name = 'Corolla'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2020 AND v.color = 'Prata'
      AND v.price = 85000.00 AND v.mileage = 52000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2021, 'Preto', 188000.00, 41000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Toyota' AND m.name = 'Hilux'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2021 AND v.color = 'Preto'
      AND v.price = 188000.00 AND v.mileage = 41000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2022, 'Azul', 108000.00, 22000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Honda' AND m.name = 'Civic'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2022 AND v.color = 'Azul'
      AND v.price = 108000.00 AND v.mileage = 22000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2021, 'Cinza', 132000.00, 36000, 'SOLD'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Honda' AND m.name = 'HR-V'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2021 AND v.color = 'Cinza'
      AND v.price = 132000.00 AND v.mileage = 36000 AND v.status = 'SOLD'
  )
UNION ALL SELECT m.id, 2022, 'Prata', 220000.00, 28000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Ford' AND m.name = 'Ranger'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2022 AND v.color = 'Prata'
      AND v.price = 220000.00 AND v.mileage = 28000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2023, 'Vermelho', 82000.00, 18000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Chevrolet' AND m.name = 'Onix'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2023 AND v.color = 'Vermelho'
      AND v.price = 82000.00 AND v.mileage = 18000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2022, 'Branco', 126000.00, 30000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Chevrolet' AND m.name = 'Tracker'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2022 AND v.color = 'Branco'
      AND v.price = 126000.00 AND v.mileage = 30000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2021, 'Preto', 79000.00, 47000, 'DISCONTINUED'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Volkswagen' AND m.name = 'Polo'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2021 AND v.color = 'Preto'
      AND v.price = 79000.00 AND v.mileage = 47000 AND v.status = 'DISCONTINUED'
  )
UNION ALL SELECT m.id, 2023, 'Azul', 265000.00, 15000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'BMW' AND m.name = '320i'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2023 AND v.color = 'Azul'
      AND v.price = 265000.00 AND v.mileage = 15000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2022, 'Prata', 245000.00, 24000, 'SOLD'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Mercedes-Benz' AND m.name = 'C180'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2022 AND v.color = 'Prata'
      AND v.price = 245000.00 AND v.mileage = 24000 AND v.status = 'SOLD'
  )
UNION ALL SELECT m.id, 2021, 'Branco', 198000.00, 35000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Audi' AND m.name = 'A3'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2021 AND v.color = 'Branco'
      AND v.price = 198000.00 AND v.mileage = 35000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2023, 'Cinza', 118000.00, 12000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Nissan' AND m.name = 'Kicks'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2023 AND v.color = 'Cinza'
      AND v.price = 118000.00 AND v.mileage = 12000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2022, 'Preto', 76000.00, 28000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Hyundai' AND m.name = 'HB20'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2022 AND v.color = 'Preto'
      AND v.price = 76000.00 AND v.mileage = 28000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2021, 'Branco', 103000.00, 49000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Fiat' AND m.name = 'Strada'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2021 AND v.color = 'Branco'
      AND v.price = 103000.00 AND v.mileage = 49000 AND v.status = 'AVAILABLE'
  )
UNION ALL SELECT m.id, 2022, 'Verde', 112000.00, 33000, 'AVAILABLE'
FROM model m JOIN brand b ON b.id = m.brand_id
WHERE b.name = 'Renault' AND m.name = 'Duster'
  AND NOT EXISTS (
    SELECT 1 FROM vehicle v
    WHERE v.model_id = m.id AND v.year = 2022 AND v.color = 'Verde'
      AND v.price = 112000.00 AND v.mileage = 33000 AND v.status = 'AVAILABLE'
  );
