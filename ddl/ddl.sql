CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL UNIQUE,
  `password_hash` varchar(255) NOT NULL,
  `display_name` varchar(255),
  `avatar` text,
  `header` text,
  `note` text,
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `content` varchar(140) NOT NULL,
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
);

CREATE TABLE `follow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `followee_id` bigint(20) NOT NULL,
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  FOREIGN KEY (`followee_id`) REFERENCES `account` (`id`),
  UNIQUE KEY (`account_id`, `followee_id`),
  INDEX (`account_id`),
  INDEX (`followee_id`)
);

CREATE TABLE `media` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `url` text NOT NULL,
  `description` varchar(420),
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `status_medias` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status_id` bigint(20) NOT NULL,
  `media_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  FOREIGN KEY (`media_id`) REFERENCES `media` (`id`),
  INDEX (`status_id`),
  INDEX (`media_id`)
);

INSERT INTO `account` (id, username, password_hash, display_name, avatar, header) VALUES
(1, 'yt8492', 'password', 'mayamito', 'https://avatars.githubusercontent.com/u/39693306?v=4', 'https://pbs.twimg.com/profile_banners/972404402425245697/1690337648/1500x500'),
(2, 'mitohato', 'password', 'mito', 'https://avatars.githubusercontent.com/u/19385268?v=4', 'https://pbs.twimg.com/profile_banners/3036286802/1597331435/1500x500');

INSERT INTO `status` (account_id, content) VALUES
(1, 'Hello World!'),
(2, 'Hello Yatter!');
