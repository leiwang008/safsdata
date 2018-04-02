REM The safsdatamodel.jar contains the model class files in package org.safs.data.model; it is packaged from the project SAFS 'Core'.
REM Please install safsdatamodel.jar to your local maven repository, so that your project can be compiled correctly by maven.
mvn install:install-file -Dfile=safsdatamodel.jar -DgroupId=org.safs.data.model -DartifactId=safsdatamodel -Dversion=1.0 -Dpackaging=jar