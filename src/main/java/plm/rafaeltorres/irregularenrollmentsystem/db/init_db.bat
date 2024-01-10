
set PATH=%PATH%;"C:\Program Files\MySQL\MySQL Server 8.2\bin"

mysql -u root -p --verbose -e "create user if not exists 'oop'@'localhost' identified by 'oop';grant all privileges on *.* to 'oop'@'localhost' with grant option;flush privileges;create database if not exists enrollment_system;use enrollment_system;source enrollment_system.sql";

timeout 5