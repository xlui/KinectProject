# 使用 sqlite 数据库的例子
import sqlite3

database_name = 'sample.db'

# 连接数据库
connect = sqlite3.connect(database_name)
cursor = connect.cursor()

# 删除已经存在的测试表
try:
    cursor.execute("DROP TABLE COMPANY")
except sqlite3.OperationalError as e:
    print(e)

# 创建表
print("Create table: company")
cursor.execute("""CREATE TABLE COMPANY(
  ID INT PRIMARY KEY NOT NULL,
  NAME TEXT NOT NULL,
  AGE INT NOT NULL,
  ADDRESS CHAR(50),
  SALARY REAL);""")
connect.commit()

# 向表中插入数据
print("Insert data into table: company")
cursor.execute("INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) \
      VALUES (1, 'Paul', 32, 'California', 20000.00 )")
cursor.execute("INSERT INTO COMPANY VALUES (2, 'Allen', 25, 'Texas', 15000.00 )")
cursor.execute("INSERT INTO COMPANY VALUES (3, 'Teddy', 23, 'Norway', 20000.00 )")
cursor.execute("INSERT INTO COMPANY VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 )")
connect.commit()

# 查询
print("Now query data from table: company")
results = cursor.execute("SELECT ID, NAME, ADDRESS, SALARY FROM COMPANY")
print("Now, data in database is:")
for row in results:
    print("ID =", row[0])
    print("Name =", row[1])
    print("Address =", row[2])
    print("Salary =", row[3])
    print()

# 更新
print("Now update data in table: company")
cursor.execute("UPDATE COMPANY SET SALARY=25000.00 WHERE ID=1")
connect.commit()
results = cursor.execute("SELECT ID, NAME, ADDRESS, SALARY FROM COMPANY")
print("Now, data in database is:")
for row in results:
    print("ID =", row[0])
    print("Name =", row[1])
    print("Address =", row[2])
    print("Salary =", row[3])
    print()

# 删除
print('Now delete data from table: company')
cursor.execute('DELETE FROM COMPANY WHERE ID=2')
connect.commit()
results = cursor.execute("SELECT ID, NAME, ADDRESS, SALARY FROM COMPANY")
print("Now, data in database is:")
for row in results:
    print("ID =", row[0])
    print("Name =", row[1])
    print("Address =", row[2])
    print("Salary =", row[3])
    print()

# 关闭
connect.close()
cursor.close()
print("Close connect to database.")
