# Financial Aggregator

A personal learning project built to understand and practice **microservices architecture** in a real-world scenario.

The system tracks currency exchange rates, evaluates user-defined price alerts, and delivers email notifications.

Its purpose is to automate monitoring of volatile exchange markets and provide timely, event-driven notifications, reducing the need for manual tracking and enabling faster decision-making.

All wired together through asynchronous messaging between independently deployable services.


## How it works

```
ExchangeRate API (external)
        │ HTTP / RestClient
        ▼
┌─────────────────────┐
│   market-service    │  :8080  - fetches & stores exchange rates
└─────────────────────┘
        │ RabbitMQ  →  rates.updated
        ▼
┌─────────────────────┐
│   alert-service     │  :8081  - evaluates user-defined rate alerts
└─────────────────────┘
        │ RabbitMQ  →  alert.triggered
        ▼
┌─────────────────────┐
│   notify-service    │  :8082  - sends email notifications
└─────────────────────┘
```

**market-service** polls the [ExchangeRate API](https://www.exchangerate-api.com/) every 12 hours, persists the rates, and publishes a `rates.updated` event to RabbitMQ.

**alert-service** consumes `rates.updated`, checks all active user-defined alerts, and publishes an `alert.triggered` event for any threshold that has been crossed.

**notify-service** consumes `alert.triggered`, sends real email notifications via Gmail SMTP, and persists the notification in its own database.

Each service is **completely independent** - separate codebase, separate database, communicating only through RabbitMQ events.

### Example Email Notification

**Subject:** `[Alert] EUR is above threshold 0.20`

**Body:**

> **Alert triggered!**
>
> - Currency: **EUR**  
> - Threshold: **0.20**  
> - Current rate: **0.2357**  
> - Difference: **0.0357**  
> - Direction: **above threshold**  
> - Triggered at: `2026-04-27T12:10:08.103480Z`


## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 |
| Messaging | RabbitMQ 3.12 |
| Database | PostgreSQL 16 (one per service) |
| Email (local development) | MailHog |
| Email (production) | Gmail SMTP |
| Infrastructure | Docker Compose |
| Build | Maven |
| Boilerplate | Lombok |
| Validation | Bean Validation |


## API Reference

### market-service - `:8080`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/rates/{currency}?date=YYYY-MM-DD` | Get stored rates for a currency on a given date |

Tracked currencies: `EUR`, `USD`, `GBP`, `CHF`, `CZK`, `JPY`

### alert-service - `:8081`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/alerts` | Create a new rate alert |
| `GET` | `/api/v1/alerts?currencyCode={code}` | List alerts for a currency |
| `DELETE` | `/api/v1/alerts/{id}` | Delete an alert |

**Example request body:**
```json
{
  "currencyCode": "EUR",
  "thresholdRate": 4.30,
  "higher": true
}
```

- `higher: true` - fires when the rate goes **above** the threshold
- `higher: false` - fires when the rate goes **below** the threshold

### notify-service - `:8082`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/notifications` | List all notification history |
| `GET` | `/api/v1/notifications?currency={code}` | Filter by currency |

## Repository Structure

```
financial-aggregator/
├── market-service/     # rate fetching & storage
├── alert-service/      # alert evaluation
├── notify-service/     # email notifications
└── docker-compose.yml
```

## Tests

Each service includes unit and controller tests to ensure correctness of business logic and API behavior.  
Mockito is used for mocking dependencies, and Spring WebMVC Test is used for lightweight controller testing.

## AI

The core application code and architecture were designed and implemented by me.  
An AI agent (Sonnet 4.5) was used as a supporting tool for learning, discussing design decisions, refining code, and generating unit tests.

