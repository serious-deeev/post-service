-- добавляем тестовых пользователей
INSERT INTO user_post_storage.users(id, username, email, created_at)
VALUES (100, 'john', 'john@mail.ru', now()),
       (200, 'mark', 'test@gmail.com', now());

-- добавляем тестовые данные по постам
INSERT INTO user_post_storage.posts(id, user_id, title, content, is_reserved)
VALUES (1, 100, 'Как много есть и не потолстеть', 'Bla bla bla', false),
       (2, 200, 'Как мало есть и не похудеть', 'Bla bla bla', false);
