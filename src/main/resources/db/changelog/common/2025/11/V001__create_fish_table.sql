-- liquibase formatted sql
-- logicalFilePath: db/changelog/common/2025/11/V001__create_fish_table.sql

-- changeset artem:001
DROP TABLE IF EXISTS fish;

-- changeset artem:002
CREATE TABLE fish (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      catch_date DATETIME(6),
                      name VARCHAR(255),
                      price DOUBLE NOT NULL,
                      image_file_name VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
