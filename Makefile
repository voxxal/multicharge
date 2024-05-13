all:
	javac -d ./target/server server/*.java
	javac -d ./target/client -cp client/jars/* client/*.java
	java -cp target/server Server &
	java -cp target/client:client/jars/* Client &
	# java -cp target/client:client/jars/* Client &
