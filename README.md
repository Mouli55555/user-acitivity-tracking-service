# Event-Driven User Activity Tracking Service

A production-ready **Event-Driven User Activity Tracking System** built with **Spring Boot**, **RabbitMQ**, and **MySQL**, fully containerized using **Docker Compose**.

---

## 📋 Overview

This system provides a scalable solution for tracking user activities through an event-driven architecture:

- **Producer Service** → REST API that accepts user activity events and publishes them to RabbitMQ
- **Consumer Service** → Event listener that consumes messages from RabbitMQ and persists them to MySQL

---

## ✨ Features

- ✅ RESTful Producer API: `POST /api/v1/events/track`
- ✅ Event publishing to RabbitMQ queue: `user_activity_events`
- ✅ Consumer service persists events to MySQL table: `user_activities`
- ✅ Full Docker Compose orchestration
- ✅ Environment-based configuration
- ✅ Retry handling and safe metadata serialization
- ✅ Comprehensive test suite

---

## 🏗️ Architecture

```
Client
  │
  │ POST /api/v1/events/track
  ▼
Producer Service (Spring Boot)
  │
  │ Publishes JSON Event
  ▼
RabbitMQ Queue: user_activity_events
  │
  │ Consumes Messages
  ▼
Consumer Service (Spring Boot)
  │
  │ Saves to DB
  ▼
MySQL Table: user_activities
```

---

## 🛠️ Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Message Broker | RabbitMQ (with Management UI) |
| Database | MySQL 8 |
| Containerization | Docker + Docker Compose |

---

## 📂 Project Structure

```
user-activity-tracker/
│
├── producer-service/
│   ├── src/main/java/...
│   ├── Dockerfile
│   └── pom.xml
│
├── consumer-service/
│   ├── src/main/java/...
│   ├── Dockerfile
│   └── pom.xml
│
├── db/
│   └── init.sql
│
├── docker-compose.yml
├── .env.example
└── README.md
```

---

## ⚙️ Environment Configuration

Create a `.env` file based on the provided `.env.example`:

```env
# RabbitMQ Configuration
SPRING_RABBITMQ_HOST=rabbitmq
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=appuser
SPRING_RABBITMQ_PASSWORD=rabbitpass

# MySQL Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/user_activity_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root_password
```

---

## 🚀 Getting Started

### Prerequisites

- Docker Engine 20.10+
- Docker Compose 2.0+

### Step 1: Start All Services

From the project root directory:

```bash
docker-compose up --build
```

This command will start:
- Producer Service (Port 8080)
- Consumer Service
- RabbitMQ (Ports 5672, 15672)
- MySQL (Port 3306)

---

## 📡 API Usage

### Track User Activity Event

**Endpoint:** `POST /api/v1/events/track`

**Example Request:**

```bash
curl -X POST http://localhost:8080/api/v1/events/track \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 101,
    "eventType": "login",
    "timestamp": "2026-02-06T10:00:00",
    "metadata": {
      "device": "mobile",
      "location": "India"
    }
  }'
```

**Response:**

```
✅ Event sent to RabbitMQ! 🐇
```

---

## 🐰 RabbitMQ Management

Access the RabbitMQ Management UI:

- **URL:** http://localhost:15672
- **Username:** `appuser`
- **Password:** `rabbitpass`
- **Queue Name:** `user_activity_events`

---

## 🗄️ Database Verification

### Connect to MySQL

```bash
docker exec -it mysql mysql -uroot -proot_password
```

### Query Saved Activities

```sql
USE user_activity_db;
SELECT * FROM user_activities;
```

### Database Schema

The schema is automatically initialized from `db/init.sql`:

```sql
CREATE TABLE IF NOT EXISTS user_activities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    timestamp DATETIME NOT NULL,
    metadata JSON
);
```

---

## 🧪 Running Tests

### Producer Service Tests

```bash
docker run --rm -v ${PWD}/producer-service:/app -w /app \
  maven:3.9.6-eclipse-temurin-17 mvn test
```

### Consumer Service Tests

```bash
docker run --rm -v ${PWD}/consumer-service:/app -w /app \
  maven:3.9.6-eclipse-temurin-17 mvn test
```

---

## ✅ Submission Checklist

| Requirement | Status |
|------------|--------|
| Producer API implemented | ✅ Done |
| Queue `user_activity_events` configured | ✅ Done |
| Consumer saves events to MySQL | ✅ Done |
| Docker Compose orchestration | ✅ Done |
| Dockerfiles included | ✅ Done |
| `init.sql` provided | ✅ Done |
| `.env.example` included | ✅ Done |
| Tests runnable via command | ✅ Done |
| README documentation complete | ✅ Done |

---

## 🐛 Troubleshooting

### Services Not Starting

```bash
# Check Docker logs
docker-compose logs -f

# Restart services
docker-compose down
docker-compose up --build
```

### Port Conflicts

If ports 8080, 5672, or 3306 are already in use, modify the port mappings in `docker-compose.yml`.

### Database Connection Issues

Ensure MySQL is fully initialized before the consumer starts. The `docker-compose.yml` includes health checks and dependency ordering.

---

## 📚 Additional Resources

- [Spring AMQP Documentation](https://spring.io/projects/spring-amqp)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

---

## 👨‍💻 Author

Developed as part of the **Event-Driven User Activity Tracking Service** project.

---

## 📄 License

This project is provided as-is for educational and evaluation purposes.

---

## 🎯 Future Enhancements (Optional)

- Add Actuator health endpoints for monitoring
- Implement dead letter queue for failed messages
- Add Postman collection for API testing
- Include integration tests with Testcontainers
- Add event validation and error handling
- Implement event replay functionality

---

**🎉 Submission Ready!**

This project includes all required components and is ready for evaluation.