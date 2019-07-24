all : build build_old

build:
	cd final && javac -d out -cp processing-core.jar:out src/*.java

run:
	cd final && java -cp processing-core.jar:out VirtualWorld

build_old:
	cd given_code && javac -d  out -cp processing-core.jar:out src/*.java

run_old:
	cd given_code && java -cp processing-core.jar:out VirtualWorld

clean:
	rm -rf final/out/*.class
	rm -rf given_code/out/*.class




