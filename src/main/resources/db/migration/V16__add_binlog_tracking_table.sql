CREATE TABLE binlog_tracking (
    id INT PRIMARY KEY,
    binlog_filename VARCHAR(255) NOT NULL,
    binlog_position BIGINT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO binlog_tracking (id, binlog_filename, binlog_position) VALUES (1, '', 0);
