javac -d target/common -cp jars/* common/*.java
javac -d target/server -cp jars/*:target/common server/*.java
javac -d target/client -cp jars/*:target/common client/*.java
java -cp target/server:jars/*:target/common Server &
java -XstartOnFirstThread -cp target/client:jars/*:target/common Client &

read -p "" < /dev/tty

pkill -P $$