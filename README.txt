====================================================================
=                                                                  =
=                              cups4j                              =
=                                                                  =
====================================================================

The following steps will explain you how to build cups4j.

  As a requirement, you need :

    1. Java JDK (1.6, 1.7) 
    2. maven 3.x (http://maven.apache.org/download.cgi#Installation)
    3. Internet connection to download dependencies

  Next, to build, from command line interpreter :

    mvn package

  This will generate :

    - target/cups4j-${version}.jar : jar with included dependencies
    - target/cups4j-${version}-sources.jar : cups4j sources
    - target/original-cups4j-${version}.jar : jar without dependencies
    - target/site/index.html : generated web site with docs, reports, ...
    - target/site/cups4j.pdf : pdf version of cups4j docs

  You're done.