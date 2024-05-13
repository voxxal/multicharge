all:
	javac -d target/common common/*.java
	javac -d target/server -cp target/common server/*.java
	javac -d target/client -cp client/jars/*:target/common client/*.java
	java -cp target/server:target/common Server &
	java -cp target/client:client/jars/*:target/common Client &
	# java -cp target/client:client/jars/* Client &
