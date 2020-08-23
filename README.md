# Shotbow Playercount API

## Docker

You can build the image locally using Docker by running the following Docker command:
```
docker build -t shotbow-playercount-api .
```

You can run the built image using Docker by running the following Docker command:
```
docker run --name shotbow-playercount-api --restart always -d -e SPRING_DATASOURCE_URL=<URL> -e SPRING_DATASOURCE_USERNAME=<USERNAME> -e SPRING_DATASOURCE_PASSWORD=<PASSWORD> -p 8080:8080 shotbow-playercount-api
```

More detailed information coming soon...
