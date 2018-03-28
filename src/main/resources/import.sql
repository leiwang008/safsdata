#https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html
#The table's column name is converted into underscore format instead of camel format, be careful with it. 

#Can we insert multiple rows in a single SQL query? NO, this import.sql is going to be executed by Hibernate, which can not handle that format.
#insert into product(name, price) values 
#					('Iron Iron', 5.4),
#					('Baby Car Seat', 86.99);

insert into status(type, description) values ('0', 'GENERIC');
insert into status(type, description) values ('7', 'DEBUG');
insert into status(type, description) values ('256', 'SKIPPED');
insert into status(type, description) values ('1024', 'FAILED');
insert into status(type, description) values ('1025', 'FAILED_OK');
insert into status(type, description) values ('2048', 'PASSED');
insert into status(type, description) values ('4096', 'WARNING');
insert into status(type, description) values ('4097', 'WARNING_OK');
insert into status(type, description) values ('10000', 'CUSTOM');

insert into testcycle(name, tests, failures, skipped, time, timestamp, command_line) values ('VA Test', 10, 2, 1, 2560, '2018-02-22T09:39:15', '"c:\\seleniumplus\\Java\\jre\\bin\\java.exe"  -cp "c:\\seleniumplus\\libs\\seleniumplus.jar;c:\\seleniumplus\\libs\\JSTAFEmbedded.jar;c:\\seleniumplus\\libs\\selenium-server-standalone-3.4.0.jar";bin org.sas.VATest');

insert into testsuite(testcycle_id, name, tests, failures, skipped, time, timestamp) values (1, 'VA Test Suite1', 10, 2, 1, 2560, '2018-02-22T09:39:15');


insert into testcase(testsuite_id, name, time) values (1, 'VA Login', 1000);
insert into testcase(testsuite_id, name, time) values (1, 'VA Manipulate', 1400);
insert into testcase(testsuite_id, name, time) values (1, 'VA Logout', 160);


insert into teststep(testcase_id, status_id, log_message) values (1, 6 , 'VALogin:User Input \'tom\' successful');
insert into teststep(testcase_id, status_id, log_message) values (1, 6 , 'VALogin:Password Input \'******\' successful');
insert into teststep(testcase_id, status_id, log_message) values (1, 6 , 'VALogin:Signin Click successful');
insert into teststep(testcase_id, status_id, log_message) values (2, 3 , 'Dashboard:Dashboard GUIDoesExists skipped');
insert into teststep(testcase_id, status_id, log_message) values (2, 6 , 'Dashboard:ProductList Select \'Iphone\' successful');
insert into teststep(testcase_id, status_id, log_message) values (2, 6 , 'Dashboard:ProductTable GUIDoesExists successful');
insert into teststep(testcase_id, status_id, log_message) values (2, 6 , 'ProductTable:ColumnVersion Click successful');
insert into teststep(testcase_id, status_id, log_message) values (2, 4 , 'ProductTable:ColumnAmount Click failed.');
insert into teststep(testcase_id, status_id, log_message) values (2, 4 , 'ProductTable:ColumnName CaptureGUIImage failed, GUI doesn\'t exist.');
insert into teststep(testcase_id, status_id, log_message) values (3, 6 , 'VALogout:Logout Click successful');
