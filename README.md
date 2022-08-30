### Before:

CREATE TABLE `house_subscribe`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `house_id` int(11) NOT NULL COMMENT '房源id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户描述',
  `status` int(2) NOT NULL COMMENT '预约状态:  1-已预约   2-房东确认  3-看房完成 ',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `last_update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '记录更新时间',
  `order_time` datetime(0) NULL DEFAULT NULL COMMENT '预约时间',
  `telephone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `admin_id` int(11) NOT NULL COMMENT '房源发布者id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_on_user_and_house`(`house_id`, `user_id`) USING BTREE COMMENT '用户和房子唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '预约看房信息表' ROW_FORMAT = Dynamic;


### After:

预约看房信息表(house_subscribe):

| 字段名          | 数据类型   | 说明                        |
| --------------- | ---------  | --------------------------- |
| id              | bigint     | 主键                        |
| house_id        | bigint     | 房源id                      |
| user_id         | bigint     |  用户id                     |
| description     | varchar(255)    | 用户描述               |
| status          | int(2)     | 预约状态:  1-已预约   2-房东确认  3-看房完成     |
| create_time     | datetime   | 数据创建时间                |
| last_update_time| datetime   | 记录更新时间                |
| order_time      | datetime   | 预约时间                    |
| telephone       | varchar(255)    | 联系电话               |
| admin_id        | bigint     |  用户id                     |


### Description:


目的： 解析 SQL 文件 生成 markdown 表格

前置条件： 数据库已经存在或文件已经存在 ，SQL 基于标准导出的 SQL 文件中的内容，通过 mysqldump 导出语句保存成 sql 文件 【绝对路径】

输出结果： 保存 字段名   字段类型  字段描述

要求： 尽少依赖第三方、需要排除特殊字符，仅保存字段、【类型[长度]】、说明/注释等信息
 

 ### 使用方法
 1. 如果只存在数据库文件，先导入到数据库中  （目的：统一SQL文件的内容）
    mysql -uroot -e"source your db.sql"
 2. 如果已经存在数据库  执行打好的 Jar 包或执行 MVN 命令:
    java -jar 库名 用户名 [密码]
    或者
    mvn compile exec:java -Dexec.mainClass=org.lanqiao.MainProcess -Dexec.args="库名 用户名 [密码]"
  
