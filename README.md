This is a java project based on maven.

Getting Started:
1) git clone https://github.com/rabinlimbu/vorto_vrp.git on your machine
2) load to your IDE by pom.xml.  I use intelliJ.
3) test this project on Java 17.

Execute via command line:
1) cd to a directory of this project on your machine and if you are on current directory on /build
than copy and paste below command to execute. please make sure the file path has -vrp <absolute path to a file> 
and if not you will get error stating its required.
Updated with another argument called super-mode true/false will give better cost based on datasets.
For training-problems, "-sm false" or no "-sm" gives better total cost.
Below are two commands with -sm true or false.  Basically in the same sense, we can add more algorithms with additional optional arguments.
<br/>
$ java -jar vorto_vrp.jar -vrp /Users/rabinlimbu/development/vorto/training-problems/problem20.txt
<br/>
or
<br/>
$ java -jar vorto_vrp.jar -sm true -vrp /Users/rabinlimbu/development/vorto/training-problems/problem20.txt

