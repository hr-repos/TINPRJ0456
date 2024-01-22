cd front-end
npm run build-only
docker build -t scr-proxy .

cd ..
docker-compose -p scr up -d
