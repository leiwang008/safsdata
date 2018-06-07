# safsdata
This SAFS Data Service proejct provides the ability to manage the SAFS data.
There are 2 kinds of data:
1. The runtime-data is something like who runs the test, on which manchine the test runs, with what framework the test runs, with what engines the test runs and the command-line lauches the test.
2. The test-data is the result of the test, the test has been logically divided into 'cycle', 'suite', 'case' and 'step'.

This SAFS Data Service will also be considered as the source of '[Eksprso](http://******/saspedia/Ekspreso) Event'.



[Quick Start]
1. Prepare the database
Install mysql, please don't set any password for user 'root'. Run it on port 3306 (run 'mysqlsd --console --port 3306' on dos console).
2. Import project
This is a maven project, after importing it into an IDE, we need to convert it into a maven project. In Eclipse, user can right-click the project and choose "Configure->Convert To Maven Project".
3. Compile project
Run 'mvn clean compile' to compile it.
4. Run project
In Eclispe, right-click the project and choose "Run As->Spring Boot App".
