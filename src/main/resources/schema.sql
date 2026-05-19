-- 各種テーブル削除
DROP TABLE  users CASCADE;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS result;

--users テーブルを作成するクエリ
CREATE TABLE users
(
user_id SERIAL PRIMARY KEY,
   email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    age INTEGER,
    gender INTEGER
);

-- dishes テーブルを作成するクエリ
CREATE TABLE dishes
(
dish_id SERIAL PRIMARY KEY,
name VARCHAR(20) NOT NULL
);

--result テーブルを作成するクエリ
CREATE TABLE result
(
 id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    record_date DATE NOT NULL,
    staple_food INTEGER NOT NULL DEFAULT 0,
    side_dish INTEGER NOT NULL DEFAULT 0,
    main_dish INTEGER NOT NULL DEFAULT 0,
    milk_dish INTEGER NOT NULL DEFAULT 0,
    fruit INTEGER NOT NULL DEFAULT 0,
    detail_memo TEXT,
    achievement INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);