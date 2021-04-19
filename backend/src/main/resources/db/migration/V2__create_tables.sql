CREATE TABLE IF NOT EXISTS attr.attractions (
    id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL,
    description varchar(300),
    place varchar(50)
);

CREATE TABLE IF NOT EXISTS attr.images (
    id SERIAL PRIMARY KEY,
    path varchar(100) NOT NULL,
    name varchar(50) NOT NULL,
    attraction_id integer
);

ALTER TABLE attr.images
ADD CONSTRAINT attractions_images
FOREIGN KEY (attraction_id) REFERENCES attr.attractions(id)
ON DELETE CASCADE;