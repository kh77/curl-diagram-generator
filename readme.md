# 🔄 Curl/Har to Diagram Generator

Transform your curl commands into beautiful visual diagrams with this Spring Boot application. Perfect for API documentation, workflow visualization, and team communication.

## 🚀 Features

- **Multiple Diagram Types**: Sequence diagrams, flowcharts, and network diagrams
- **PlantUML Integration**: High-quality diagram generation
- **REST API**: Easy integration with other tools
- **Curl Parser**: Intelligent parsing of complex curl commands
- **Har file Parser**: Intelligent parsing of complex har file
- **Export Options**: Base64 encoded images and source code

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Spring Boot 3.x

## 🛠️ Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/kh77/curl-diagram-generator.git
   cd curl-diagram-generator
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
    - Web Interface: http://localhost:8080
    - API Base URL: http://localhost:8080/api/diagrams

## 🎯 API Usage

### Generate Single Diagram

**Endpoint:** `POST /api/diagrams/generate`

**Request Body:**
```json
{
  "curlCommands": "your curl commands here",
  "diagramType": "sequence|flowchart|network",
  "title": "Optional diagram title"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/diagrams/generate \
  -H 'Content-Type: application/json' \
  -d '{
    "curlCommands": "curl -X POST https://api.example.com/users -H \"Content-Type: application/json\" -d \"{\\\"name\\\": \\\"John\\\", \\\"email\\\": \\\"john@example.com\\\"}\"\ncurl -X GET https://api.example.com/users/123\ncurl -X PUT https://api.example.com/users/123 -H \"Content-Type: application/json\" -d \"{\\\"name\\\": \\\"John Updated\\\"}\"",
    "diagramType": "sequence",
    "title": "User Management API"
  }'
```

**Response:**
```json
{
  "diagramType": "sequence",
  "plantUmlSource": "@startuml\ntitle User Management API\n...",
  "base64Image": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==",
  "metadata": {
    "commandCount": 3,
    "uniqueHosts": 1
  }
}
```

### Generate All Diagram Types

**Endpoint:** `POST /api/diagrams/generate-all`

**Example:**
```bash
curl -X POST http://localhost:8080/api/diagrams/generate-all \
  -H 'Content-Type: application/json' \
  -d '{
    "curlCommands": "curl -X POST https://auth.example.com/login -d \"username=admin&password=secret\"\ncurl -X GET https://api.example.com/users -H \"Authorization: Bearer token123\"\ncurl -X POST https://api.example.com/orders -d \"userId=1&product=laptop\"",
    "title": "E-commerce API Flow"
  }'
```

**Response:**
```json
{
  "sequence": {
    "diagramType": "sequence",
    "plantUmlSource": "@startuml...",
    "base64Image": "base64_encoded_image_data"
  },
  "flowchart": {
    "diagramType": "flowchart",
    "plantUmlSource": "@startuml...",
    "base64Image": "base64_encoded_image_data",
    "mermaidSource": "flowchart TD..."
  },
  "network": {
    "diagramType": "network",
    "plantUmlSource": "@startuml...",
    "base64Image": "base64_encoded_image_data"
  }
}
```

## 📝 Curl Command Examples

### REST API CRUD Operations
```bash
curl -X POST https://jsonplaceholder.typicode.com/posts \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "My New Post",
    "body": "This is the content of my post",
    "userId": 1
  }'

curl -X GET https://jsonplaceholder.typicode.com/posts/1

curl -X PUT https://jsonplaceholder.typicode.com/posts/1 \
  -H 'Content-Type: application/json' \
  -d '{
    "id": 1,
    "title": "Updated Post Title",
    "body": "Updated content",
    "userId": 1
  }'

curl -X DELETE https://jsonplaceholder.typicode.com/posts/1
```

### Authentication Flow
```bash
curl -X POST https://api.example.com/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "admin",
    "password": "secretpassword"
  }'

curl -X GET https://api.example.com/user/profile \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'

curl -X POST https://api.example.com/auth/refresh \
  -H 'Content-Type: application/json' \
  -d '{
    "refresh_token": "refresh_token_here"
  }'
```

### Microservices Communication
```bash
curl -X POST https://user-service.com/api/users \
  -H 'Authorization: Bearer token123' \
  -d '{"name": "Alice"}'

curl -X POST https://order-service.com/api/orders \
  -H 'Authorization: Bearer token123' \
  -d '{"userId": 1, "product": "laptop"}'

curl -X POST https://payment-service.com/api/payments \
  -H 'Authorization: Bearer token123' \
  -d '{"orderId": 456, "amount": 999.99}'

curl -X POST https://notification-service.com/api/notify \
  -d '{"userId": 1, "message": "Order confirmed"}'
```

## 🧪 Testing the API

### Test Connection
```bash
curl -X GET http://localhost:8080/api/diagrams/test
```
**Expected Response:** `"Diagram service is running!"`

### Generate Sequence Diagram
```bash
curl -X POST http://localhost:8080/api/diagrams/generate \
  -H 'Content-Type: application/json' \
  -d '{
    "curlCommands": "curl -X POST https://api.example.com/users -H '\''Content-Type: application/json'\'' -d '\''{\"name\": \"John\", \"email\": \"john@example.com\"}'\''",
    "diagramType": "sequence",
    "title": "User Creation Flow"
  }'
```

### Generate Flowchart Diagram
```bash
curl -X POST http://localhost:8080/api/diagrams/generate \
  -H 'Content-Type: application/json' \
  -d '{
    "curlCommands": "curl -X GET https://api.example.com/products\ncurl -X POST https://api.example.com/cart/add -d '\''productId=123'\''\ncurl -X POST https://api.example.com/checkout",
    "diagramType": "flowchart",
    "title": "Shopping Flow"
  }'
```

### Generate Network Diagram
```bash
curl -X POST http://localhost:8080/api/diagrams/generate \
  -H 'Content-Type: application/json' \
  -d '{
    "curlCommands": "curl -X POST https://user-service.com/api/register -d '\''name=Alice'\''\ncurl -X POST https://order-service.com/api/orders -d '\''userId=1'\''\ncurl -X POST https://payment-service.com/api/charge -d '\''orderId=123'\''\ncurl -X POST https://inventory-service.com/api/reserve -d '\''productId=456'\''",
    "diagramType": "network",
    "title": "Microservices Architecture"
  }'
```

### Generate From har file, har file is present in the project root `api.har`
```bash
curl --request POST \
    --url http://localhost:8080/api/diagrams/generate-from-har \
    --header 'content-type: multipart/form-data' \
    --form harFile=@harFile \
    --form diagramType=sequence \
    --form 'title=Test API'
```

### Generate All Diagrams at Once
```bash
curl -X POST http://localhost:8080/api/diagrams/generate-all \
  -H 'Content-Type: application/json' \
  -d '{
    "curlCommands": "curl -X POST https://auth.example.com/login -d '\''username=admin&password=secret'\''\ncurl -X GET https://api.example.com/users -H '\''Authorization: Bearer token123'\''\ncurl -X POST https://api.example.com/orders -d '\''userId=1&product=laptop'\''",
    "title": "Complete API Workflow"
  }'
```


## 📊 View Diagram 
-  When you run the project, use http://localhost:8080/ and copy your base64 response and press `View Image` button.


- Search `plant uml editor` in the Google and replace `\n` from the response with `enter line` and replace `\` with `empty` so that diagram will be generated. like https://editor.plantuml.com/
       
       ** Before   
       "plantUmlSource": "@startuml\ntitle My API Flow\nparticipant Client\nparticipant \"auth.com\" as authcom\nparticipant \"api.com\" as apicom\n\nClient -> authcom: POST /login\nauthcom --> Client: Response\n\nClient -> apicom: GET /data\napicom --> Client: Response\n@enduml"
       
       ** After
      @startuml
      title My API Flow
      participant Client
      participant "auth.com" as authcom
      participant "api.com" as apicom
      
      Client -> authcom: POST /login
      authcom --> Client: Response
      
      Client -> apicom: GET /data
      apicom --> Client: Response
      @enduml"




## 📊 Supported Diagram Types

| Type | Description | Best For |
|------|-------------|----------|
| **Sequence** | Shows chronological flow of API calls | API workflows, authentication flows |
| **Flowchart** | Shows process flow with decision points | Business processes, user journeys |
| **Network** | Shows system architecture and connections | Microservices, system architecture |

## 🚀 Advanced Usage

### Batch Processing Multiple Curl Files
```bash
# Process multiple curl command sets
for file in *.curl; do
  curl -X POST http://localhost:8080/api/diagrams/generate \
    -H 'Content-Type: application/json' \
    -d "{\"curlCommands\": \"$(cat $file)\", \"diagramType\": \"sequence\", \"title\": \"$file\"}"
done
```

**Made with ❤️ by Kazim**