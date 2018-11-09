# Server

## Development Environment
This server is built on Node.js using the Express routing framework.

### Install Node.js and npm
What are Node.js and npm you might ask? Well here it is, straight from the horse's mouth (https://nodejs.org/en/):
> Node.js® is a JavaScript runtime built on Chrome's V8 JavaScript engine. Node.js uses an event-driven, non-blocking I/O model that makes it lightweight and efficient. Node.js' package ecosystem, npm, is the largest ecosystem of open source libraries in the world.

#### Ubuntu
Execute the following commands from the terminal.
```
sudo apt-get update
sudo apt-get install nodejs
sudo apt-get install npm
```

#### Windows or Mac
Get it here: https://nodejs.org/en/download/

### Install Dependencies
Navigate to the root directory of the project and execute the following commands from the terminal.
```
npm install
```

### Running the Server
This one is fairly straight-forward. Navigate to the folder containing `server.js` and execute the following command:
```node server.js```
The server is now accessible on localhost:8080!
### Testing
Once the the server is running, we can use Curl to do some basic HTTP calls from the command line.  Postman (https://www.getpostman.com/) is also a great option. Plus, it has a GUI!

#### Ex. To Test Sign-in
```
curl -X POST -H "Content-Type: application/json" -d '{"alias":"YOUR_ALIAS","password":"YOUR_PASSWORD","scan":"YOUR_SCAN"}' http://localhost:8080/signin
```

#### Ex. To Test Authentication Following a Sign-in
```
curl -X POST -H "Authorization: Bearer {YOUR TOKEN HERE}" http://localhost:8080/test_signin
```