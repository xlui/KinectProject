# test to read and write data to mysql database
# success
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


# insert
sql = 'insert into test1 values (4, 40)'
cursor.execute(sql)
connect.commit()
print(type(cursor.fetchall()))
print(len(cursor.fetchall()))
print('成功插入', cursor.rowcount, '条语句')

# query
sql_query = 'select value from test1 where id=2'
cursor.execute(sql_query)
for row in cursor.fetchall():
    print("id: 2\nvalue: ", row)
print("共查出", cursor.rowcount, '条数据')


cursor.close()
connect.close()

