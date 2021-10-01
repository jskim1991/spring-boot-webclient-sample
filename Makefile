server-app:
	cd server && mvn clean install
client-app:
	cd client && mvn clean package
tests: server-app client-app
