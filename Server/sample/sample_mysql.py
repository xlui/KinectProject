#!/usr/bin/env python
# coding:utf-8
# 使用 MySQL 数据库的例子
# create table TABLE_NAME ()
# alter table TABLE_NAME add/change/drop COLUMN
# insert into TABLE_NAME values ()
# update TABLE_NAME set ...
import pymysql

host = "localhost"
username = "kinect"
password = "kinect"

connect = pymysql.connect(host=host, user=username, passwd=password, db='test')
cursor = connect.cursor()


# 如果已经存在测试表，删除；不存在，忽略错误
try:
    sql = "drop table test1"
    cursor.execute(sql)
    connect.commit()
except pymysql.err.InternalError as e:
    print(e)


# 创建表
sql = 'create table test1 (number int primary key, score int)'
cursor.execute(sql)
connect.commit()

# 插入
for i in range(4):
    sql = 'insert into test1 values ({}, {})'.format(i, 10 * i)
    cursor.execute(sql)
connect.commit()
print("type of the result of cursor.fetchall(): ", type(cursor.fetchall()))
print("type of the result of cursor.fetchall(): ", len(cursor.fetchall()))
print('成功插入', cursor.rowcount, '条语句')

# 查询
sql_query = 'select score from test1 where number=2'
cursor.execute(sql_query)
for row in cursor.fetchall():
    print("id: 2\nvalue: ", row)
print("共查出", cursor.rowcount, '条数据')


# 关闭
cursor.close()
connect.close()
