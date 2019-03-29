CREATE DATABASE seckill;

use seckill;

create table seckill
(
  `seckill_id`  bigint       not null AUTO_INCREMENT COMMENT 'id',
  `name`        varchar(120) not null,
  `number`      int          not null,
  `start_time`  timestamp    not null,
  `end_time`    timestamp    not null,
  `create_time` timestamp    not null default current_timestamp,
  primary key (seckill_id),
  key idx_start_time (start_time),
  key idx_end_time (end_time),
  key idx_create_time (create_time)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  default CHARSET = utf8 COMMENT ='秒杀数据库';

insert into
  seckill(name, number, start_time, end_time)
values
  ('1000 kill iphone', 100, '2019-3-26 00:00:00', '2019-3-27 00:00:00'),
  ('2000 kill iphone2', 100, '2019-3-25 00:00:00', '2019-3-26 00:00:00'),
  ('3000 kill iphone3', 100, '2019-3-26 00:00:00', '2019-3-27 00:00:00'),
  ('4000 kill iphone4', 100, '2019-3-26 00:00:00', '2019-3-27 00:00:00');

create table success_killed
(
  `seckill_id`  bigint    not null,
  `user_phone`  bigint    not null,
  `state`       tinyint   not null default -1,
  `create_time` timestamp not null default current_timestamp,
  primary key (seckill_id, user_phone),/*联合主键 */
  key idx_create_time (create_time)
) ENGINE = InnoDB
  default CHARSET = utf8 COMMENT ='秒杀数据库';


