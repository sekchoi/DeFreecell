set PjtHome=%~dp0
set PjtHome=%PjtHome:~0,-1%
cd /d %PjtHome%

set JDK=d:\wbin\jdk17
set JAR=DeFreecell
set JfwHome=%PjtHome%

del %JfwHome%\%JAR%.jar
cd /D %JfwHome%\classes

%JDK%\bin\jar cvf %JfwHome%\%JAR%.jar META-INF quixote
