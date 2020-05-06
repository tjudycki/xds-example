# IPF IHE XDS SpringBoot Example

This is a simple tutorial that shows how to use IPF to support IHE XDS using SpringBoot.

## How to use it

1. Clone or download this project
2. Run ``startup.sh`` or ``startup.bat`` file
3. Open http://localhost:9091/services/ 

You need Java 1.8 with this version of IPF.

Change port in `src/main/resources/application.properties` if you need.


## How to test it

Download client for testing https://www.soapui.org/downloads/soapui.html

Run SoapUI, File -> Import project -> open examples/XDS-soapui-project.xml

In this project there are ready requests for XDS:
- ITI-41 Provide and Register with in-line document
- ITI-41 Provide and Register with attached document
- ITI-18 Stored Query
- ITI-43 Retrieve 

The same requests are stored in `examples` directory as XML files.

## Project description

This project shows first, minimal set of XDS transactions: store documents into repository, find documents, retrieve documents. For each transaction there is a processor class, mostly self explanatory, that handle document's attributes and content.
  

## Maven, SpringBoot, Logback for beginners

This is short and simple user's manual for absolute beginners.

### Maven & SpringBoot

Script `startup` contains just one command:
`mvn spring-boot:run`
that would download all necessary JARs, compile project and run in on embedded Tomcat. Other useful mvn options are:
clean - delete all compiled/built stuff
compile - compile project
install - create JAR/WAR (explained below)
dependency:sources - download JARs with sources (if available) but you need to remove already downloaded JARs from local repository - usually .m2 directory
You may pass several options in proper order, eg:
`mvn dependency:sources clean install`

Running `mvn install` will create a JAR in target directory. If you need WAR then open pom.xml and change 
	<packaging>jar</packaging>
into
	<packaging>war</packaging>
This time `mvn install` will create a WAR in target directory.

SpringBoot application creates an executable JAR, to use it:
`java -jar target/xds-example-0.0.1-SNAPSHOT.jar`

### Logback

Have a look at `src/main/resources/logback.xml` to modify logging level and output. Definition like:
    <logger name="org.openehealth.ipf.examples.xds" level="debug" additivity="false">
        <appender-ref ref="STDOUT"/>
    </logger>
means: for all classes in org.openehealth.ipf.examples.xds package set log level to debug and append log messages to stdout. You may change log level and put FILE instead of STDOUT to redirect log messages to file.
