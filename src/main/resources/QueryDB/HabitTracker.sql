create DATABASE habit_tracker;

-- Enable the UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create table
CREATE TABLE app_users (
                           app_user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           username VARCHAR(255) UNIQUE NOT NULL,
                           email VARCHAR(255) UNIQUE NOT NULL,
                           password VARCHAR(255) NOT NULL,
                           level INT DEFAULT 0,
                           xp INT DEFAULT 0,
                           profile_image TEXT,
                           is_verified BOOLEAN DEFAULT FALSE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE achievements (
                              achievement_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              title VARCHAR(255) NOT NULL,
                              description TEXT,
                              badge TEXT,
                              xp_required INT NOT NULL
);

CREATE TABLE app_user_achievements (
                                       app_user_achievement_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                       app_user_id UUID NOT NULL,
                                       achievement_id UUID NOT NULL,
                                       FOREIGN KEY (app_user_id) REFERENCES app_users(app_user_id) ON DELETE CASCADE,
                                       FOREIGN KEY (achievement_id) REFERENCES achievements(achievement_id) ON DELETE CASCADE
);

CREATE TABLE habits (
                        habit_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        frequency VARCHAR(50),
                        is_active BOOLEAN DEFAULT TRUE,
                        app_user_id UUID NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (app_user_id) REFERENCES app_users(app_user_id) ON DELETE CASCADE
);

CREATE TABLE habit_logs (
                            habit_log_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            log_date DATE DEFAULT CURRENT_DATE NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            xp_earned INT DEFAULT 0,
                            habit_id UUID NOT NULL,
                            FOREIGN KEY (habit_id) REFERENCES habits(habit_id) ON DELETE CASCADE
);





-- OTP table
CREATE TABLE user_verifications(
                                   id serial primary key,
                                   expiry_time time(6),
                                   verified_code varchar(255),
                                   user_id uuid,
                                   CONSTRAINT fk_user FOREIGN KEY (user_id )REFERENCES app_users(app_user_id) ON DELETE CASCADE
);


-- Insert achievement data
INSERT INTO achievements (title, description, badge, xp_required) VALUES
                                                                      ('First Habit Completed', 'Awarded when a user completes their first habit.', 'first_habit_badge.png', 50),
                                                                      ('7-Day Streak', 'Awarded for completing a habit for 7 consecutive days.', '7_day_streak_badge.png', 100),
                                                                      ('30-Day Streak', 'Awarded for completing a habit for 30 consecutive days.', '30_day_streak_badge.png', 200),
                                                                      ('Habit Master', 'Awarded for completing 10 different habits.', 'habit_master_badge.png', 500),
                                                                      ('Perfect Month', 'Awarded for completing a habit every day in a given month.', 'perfect_month_badge.png', 300),
                                                                      ('XP Novice', 'Awarded for earning your first 100 XP.', 'xp_novice_badge.png', 100),
                                                                      ('XP Champion', 'Awarded for earning 500 XP in total.', 'xp_champion_badge.png', 500),
                                                                      ('XP Overlord', 'Awarded for earning 5000 XP in total.', 'xp_overlord_badge.png', 5000),
                                                                      ('7-Day Streak Achievement', 'Awarded when a user completes a habit for 7 consecutive days.', '7_day_streak_achievement.png', 50),
                                                                      ('Level 10 Reached', 'Awarded when a user reaches level 10.', 'level_10_badge.png', 1000);

