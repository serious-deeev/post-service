-- добавляем тестовых пользователей
INSERT INTO user_post_storage.users(id, username, email, created_at)
VALUES (300, 'cole', 'cole@mail.ru', now()),
       (400, 'jane', 'jane@gmail.com', now());

-- добавляем тестовые данные по постам
INSERT INTO user_post_storage.posts(id, user_id, title, content, is_reserved)
VALUES (3, 300, 'Лучшие практики йоги', 'Ole ole ole', false),
       (4, 400, 'Принципы подбора ПК-комплектующих', 'Foo Bar', false);
