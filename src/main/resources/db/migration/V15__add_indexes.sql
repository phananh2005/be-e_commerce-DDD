-- Order indexes
CREATE INDEX idx_order_created_at ON orders(created_at);

-- User indexes
CREATE INDEX idx_user_full_name ON users(full_name);