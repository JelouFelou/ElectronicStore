WITH inserted_users AS (
    INSERT INTO app_schema.users (username, password, email, role)
        VALUES
            ('admin', '$2a$10$ABC123', 'admin@store.com', 'ADMIN'),
            ('user1', '$2a$10$XYZ456', 'user1@store.com', 'USER')
        ON CONFLICT (username) DO NOTHING
        RETURNING id, username
)

INSERT INTO app_schema.user_details (phone, address, city, first_name, last_name, user_id)
SELECT
    details.phone,
    details.address,
    details.city,
    details.first_name,
    details.last_name,
    u.id  -- Używamy ID użytkownika zamiast nazwy
FROM
    (VALUES
         ('+48123456789', 'Main St 1', 'Warsaw', 'Jan', 'Kowalski', 'admin'),
         ('+48987654321', 'Second St 5', 'Krakow', 'Anna', 'Nowak', 'user1')
    ) AS details(phone, address, city, first_name, last_name, username)
        JOIN
    inserted_users u ON u.username = details.username
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO app_schema.products (name, price, stock, category)
VALUES
    ('Laptop Pro', 4999.99, 10, 'Computers'),
    ('Smartphone X', 2499.50, 25, 'Phones'),
    ('Wireless Headphones', 399.00, 50, 'Accessories')
ON CONFLICT (name) DO NOTHING;