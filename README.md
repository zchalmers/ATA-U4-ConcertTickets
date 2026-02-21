# Concert Ticket Management System

A full-stack web application for managing concert tickets with features for creating concerts, reserving tickets, and purchasing tickets. Built as part of the Amazon Technical Academy Unit 4 project.

## 🎯 Features

- **Concert Management**: Create, update, view, and delete concerts
- **Ticket Reservation System**: Reserve tickets with time-based expiration
- **Ticket Purchasing**: Purchase tickets with concurrent request handling
- **Caching Layer**: Optimized performance with Redis-based caching
- **Asynchronous Processing**: Background tasks for handling expired reservations
- **REST API**: Full RESTful API for all operations
- **Frontend Interface**: Interactive web UI for managing concerts and tickets

## 🏗️ Architecture

### Backend
- **Framework**: Spring Boot
- **Database**: AWS DynamoDB
- **Caching**: Redis (via Spring Cache)
- **Build Tool**: Gradle
- **Language**: Java

### Frontend
- **Framework**: Vanilla JavaScript
- **Module Bundler**: Webpack
- **HTTP Client**: Axios
- **Notifications**: Toastify.js

## 📋 Prerequisites

- Java 11 or higher
- Node.js and npm
- Gradle
- DynamoDB Local (for development)
- Redis (for caching)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/zchalmers/ATA-U4-ConcertTickets.git
cd ATA-U4-ConcertTickets
```

### 2. Backend Setup

#### Set up the environment
```bash
./setupEnvironment.sh
```

#### Start local DynamoDB
```bash
./local-dynamodb.sh
```

#### Build the application
```bash
./gradlew build
```

#### Run the backend
```bash
./gradlew bootRun
```

The backend API will be available at `http://localhost:5001`

### 3. Frontend Setup

```bash
cd Frontend
npm install
npm start
```

The frontend will be available at `http://localhost:8080`

## 🔌 API Endpoints

### Concerts
- `GET /concerts` - Get all concerts
- `GET /concerts/{concertId}` - Get a specific concert
- `POST /concerts` - Create a new concert
- `PUT /concerts` - Update a concert
- `DELETE /concerts/{concertId}` - Delete a concert

### Reserved Tickets
- `GET /reservedtickets` - Get all reserved tickets
- `GET /reservedtickets/{concertId}` - Get reserved tickets for a concert
- `POST /reservedtickets` - Reserve a ticket
- `PUT /reservedtickets` - Update a reservation
- `DELETE /reservedtickets/{concertId}/{ticketId}` - Cancel a reservation

### Purchased Tickets
- `GET /purchasedtickets` - Get all purchased tickets
- `GET /purchasedtickets/{concertId}` - Get purchased tickets for a concert
- `POST /purchasedtickets` - Purchase a ticket

## 📦 Project Structure

```
ATA-U4-ConcertTickets/
├── Application/           # Spring Boot backend application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/     # Java source code
│   │   │   └── resources/ # Application configuration
│   │   └── test/         # Unit and integration tests
├── Frontend/             # JavaScript frontend
│   ├── src/
│   │   ├── css/         # Stylesheets
│   │   ├── pages/       # HTML pages
│   │   └── api/         # API client code
├── buildScripts/         # Build automation scripts
├── IntegrationTests/     # Integration test suite
└── Utilities/            # Shared utilities
```

## 🧪 Testing

### Run backend tests
```bash
./gradlew test
```

### Run integration tests
```bash
./gradlew integrationTest
```

## 💡 Usage Examples

### Creating a Concert
```javascript
// Frontend API call
POST /concerts
{
  "name": "Summer Music Festival",
  "date": "2024-07-15",
  "ticketBasePrice": 75.00
}
```

### Reserving a Ticket
```javascript
POST /reservedtickets
{
  "concertId": "concert-uuid",
  "ticketCount": 2
}
```

### Purchasing Tickets
```javascript
POST /purchasedtickets
{
  "concertId": "concert-uuid",
  "ticketId": "ticket-uuid",
  "pricePaid": 75.00
}
```

## 🔧 Configuration

Application configuration can be found in:
- `Application/src/main/resources/application.properties`

Key configuration options:
- Server port
- DynamoDB endpoint
- Cache settings
- Scheduler intervals

## 🛠️ Technologies Used

**Backend:**
- Spring Boot
- Spring Web (REST API)
- Spring Cache (Caching layer)
- Spring Scheduled Tasks
- AWS DynamoDB SDK
- Gradle

**Frontend:**
- JavaScript (ES6+)
- Webpack
- Axios
- Toastify.js
- HTML5/CSS3

**Development Tools:**
- SpotBugs (Static analysis)
- JUnit (Testing)
- DynamoDB Local

## 📝 Development Notes

- The application uses asynchronous processing to handle expired ticket reservations
- Caching is implemented to optimize database queries for frequently accessed concerts
- The frontend uses Webpack for module bundling and hot-reloading during development
- Integration tests require DynamoDB Local to be running

## 🤝 Contributing

This is an educational project. If you're working on similar coursework, please maintain academic integrity.

## 📄 License

This project is part of the Amazon Technical Academy curriculum.

## 🙏 Acknowledgments

- Amazon Technical Academy
- Kenzie Academy
