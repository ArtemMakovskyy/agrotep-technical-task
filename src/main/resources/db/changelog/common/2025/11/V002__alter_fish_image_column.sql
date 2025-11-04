-- liquibase formatted sql
-- logicalFilePath: db/changelog/common/2025/11/V002__alter_fish_image_column.sql

-- changeset artem:002 splitStatements:false
ALTER TABLE fish
    CHANGE COLUMN image_file_name image_file_names VARCHAR(1020) DEFAULT NULL;
