-- Create User table
CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      first_name VARCHAR(45) NOT NULL,
                      last_name VARCHAR(45) NOT NULL,
                      email VARCHAR(100) NOT NULL UNIQUE,
                      password VARCHAR(60) NOT NULL,
                      role VARCHAR(45) NOT NULL
);

-- Create Blog Posts table
CREATE TABLE blog_posts (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            title VARCHAR(254) NOT NULL,
                            content MEDIUMTEXT NOT NULL,
                            created_at DATE NOT NULL,
                            user_id BIGINT NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES user(id)
);
