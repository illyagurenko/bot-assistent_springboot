CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    tg_id BIGINT NOT NULL UNIQUE,
    username VARCHAR(255),
    timezone VARCHAR(50) DEFAULT 'UTC',
    bot_state VARCHAR(50) DEFAULT 'IDLE'
);

CREATE INDEX idx_users_tg_id ON users(tg_id);

CREATE TABLE schedules (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    day_of_week VARCHAR(255) NOT NULL,
    notification_time TIME NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE dota_profiles (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        dota_account_id BIGINT NOT NULL
);