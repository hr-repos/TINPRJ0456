cd back-end
mvn clean package
docker build -t scr-api .

cd ..
docker-compose -p scr up -d
