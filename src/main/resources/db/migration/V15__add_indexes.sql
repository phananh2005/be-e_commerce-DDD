-- Order indexes
CREATE INDEX idx_order_user_id_created_at ON orders(user_id, created_at);
CREATE INDEX idx_order_created_at ON orders(created_at);

-- User indexes
CREATE INDEX idx_user_full_name ON users(full_name);

