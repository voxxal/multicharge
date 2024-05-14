javac -d target/common -cp jars/* common/*.java
javac -d target/server -cp jars/*:target/common server/*.java
javac -d target/client -cp jars/*:target/common client/*.java
java -cp target/server:jars/*:target/common Server &
SERV_PID = $!
java -cp target/client:jars/*:target/common Client &
CLIENT_PID = $!
read -p "" < /dev/tty
echo $SERV_PID
echo $CLIENT_PID
kill -9 $SERV_PID
kill -9 $CLIENT_PID
