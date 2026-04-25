# 🤖 SmartInsight — AI-Powered Analytics Dashboard
### Java Full-Stack Resume Project · Antigravity Skill-Driven Workflow

> **Stack:** Java 21 + Spring Boot 3 · React 19 · LLM API (OpenAI/Gemini) · PostgreSQL · Docker  
> **Goal:** Build a full-stack app where users upload data (CSV/JSON), and an AI backend analyzes it, generates insights, and surfaces them on a live dashboard.  
> **Resume Tagline:** *"Full-stack AI analytics platform using Spring Boot microservices and LLM-powered insight generation"*

---

## 🗺️ What You'll Build

```
[ React Dashboard ]
       ↓ REST API calls
[ Spring Boot Backend ]
   ├── /upload  → parses CSV/JSON
   ├── /analyze → calls LLM API for insight generation
   └── /insights → returns stored AI-generated results
       ↓
[ PostgreSQL ] + [ OpenAI / Gemini API ]
```

Users upload a dataset (e.g., sales data, health stats, any CSV). The backend sends it to an LLM for analysis and returns human-readable insights, anomaly flags, and chart-ready summaries — all displayed live on the React frontend.

---

## ⚙️ Phase 0 — Environment Setup

### Install antigravity-awesome-skills

```bash
# Full library install (recommended)
npx antigravity-awesome-skills --antigravity

# Or clone directly
git clone https://github.com/sickn33/antigravity-awesome-skills.git .agent/skills
```

### Skills to activate for this phase

In Antigravity, use these skills at the start of your session:

```
@senior-fullstack  — full planning + architecture + TDD workflow
@backend-dev-guidelines  — Java/Spring patterns and project structure
@brainstorming  — design your data model and AI prompt strategy before coding
```

### Invoke example in Antigravity

```
Use @brainstorming to help me design the data model and LLM prompt strategy 
for a CSV analytics app where users upload data and get AI insights back.
```

### Prerequisites

| Tool | Version | Install |
|------|---------|---------|
| Java | 21+ | `sdk install java` (via SDKMAN) |
| Maven | 3.9+ | `brew install maven` |
| Node.js | 20+ | `nvm install 20` |
| Docker | Latest | docker.com |
| PostgreSQL | 15+ | via Docker (see Phase 3) |

---

## 🏗️ Phase 1 — Backend: Spring Boot Foundation

### 1.1 Generate the project

Go to [start.spring.io](https://start.spring.io) and select:

- **Project:** Maven
- **Language:** Java 21
- **Spring Boot:** 3.3.x
- **Dependencies:** Spring Web, Spring Data JPA, PostgreSQL Driver, Spring Boot DevTools, Lombok, Validation

```bash
# Unzip and open in your editor
unzip smartinsight.zip && cd smartinsight
```

### 1.2 Project structure to build

```
src/
├── main/java/com/smartinsight/
│   ├── controller/
│   │   ├── UploadController.java      # POST /api/upload
│   │   └── InsightController.java     # GET /api/insights/{id}
│   ├── service/
│   │   ├── DataParserService.java     # CSV/JSON parsing logic
│   │   ├── LLMService.java            # Calls OpenAI/Gemini API
│   │   └── InsightService.java        # Orchestrates analysis + storage
│   ├── model/
│   │   ├── Dataset.java               # JPA entity
│   │   └── Insight.java               # JPA entity
│   ├── repository/
│   │   ├── DatasetRepository.java
│   │   └── InsightRepository.java
│   └── dto/
│       ├── UploadResponse.java
│       └── InsightResponse.java
└── resources/
    └── application.yml
```

### 1.3 Antigravity skill invocations for Phase 1

```
Use @backend-dev-guidelines to scaffold a Spring Boot 3 REST controller 
for file upload that accepts CSV and returns a dataset ID.

Use @code-refactoring to review my DataParserService and apply SOLID principles.

Use @senior-fullstack with TDD — write tests first for InsightService 
before implementing the LLM call logic.
```

### 1.4 Key implementation: LLMService.java

```java
@Service
@RequiredArgsConstructor
public class LLMService {

    private final RestClient restClient;

    @Value("${openai.api.key}")
    private String apiKey;

    public String generateInsights(String datasetSummary) {
        String prompt = """
            You are a data analyst. Analyze the following dataset summary 
            and return:
            1. 3 key insights
            2. Any anomalies detected
            3. A one-line executive summary
            
            Dataset:
            %s
            """.formatted(datasetSummary);

        // Call OpenAI Chat Completions API
        var request = Map.of(
            "model", "gpt-4o-mini",
            "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        var response = restClient.post()
            .uri("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer " + apiKey)
            .body(request)
            .retrieve()
            .body(Map.class);

        // Extract response text
        return ((Map<?, ?>) ((List<?>) response.get("choices"))
            .get(0)).get("message").toString();
    }
}
```

### 1.5 application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smartinsight
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

openai:
  api:
    key: ${OPENAI_API_KEY}

server:
  port: 8080
```

---

## 🧪 Phase 2 — Testing with TDD

### Antigravity skills for this phase

```
Use @senior-fullstack with TDD mode — write unit tests for LLMService 
that mock the OpenAI API call and assert insight parsing.

Use @cc-skill-security-review to audit my file upload endpoint 
for security issues like path traversal or oversized payloads.
```

### Test structure

```
src/test/java/com/smartinsight/
├── service/
│   ├── DataParserServiceTest.java    # Unit test CSV parsing
│   ├── LLMServiceTest.java           # Mock RestClient, test prompt building
│   └── InsightServiceTest.java       # Integration of parse + LLM + save
└── controller/
    └── UploadControllerTest.java     # MockMvc integration tests
```

### Sample test

```java
@ExtendWith(MockitoExtension.class)
class LLMServiceTest {

    @Mock RestClient restClient;
    @InjectMocks LLMService llmService;

    @Test
    void shouldReturnInsightsForValidSummary() {
        // Arrange: mock RestClient chain
        var mockResponse = Map.of("choices", List.of(
            Map.of("message", Map.of("content", "Insight 1: Revenue grew 12%"))
        ));
        // ... mock setup

        // Act
        String result = llmService.generateInsights("Q3 sales data...");

        // Assert
        assertThat(result).contains("Insight");
    }
}
```

---

## 🐳 Phase 3 — Docker + PostgreSQL

```yaml
# docker-compose.yml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: smartinsight
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"

  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      OPENAI_API_KEY: ${OPENAI_API_KEY}
    depends_on:
      - postgres
```

```bash
docker-compose up -d
```

### Antigravity skill for this phase

```
Use @backend-dev-guidelines to write a production-ready Dockerfile 
for my Spring Boot 3 app with multi-stage build and non-root user.
```

---

## ⚛️ Phase 4 — React Frontend

### Setup

```bash
npm create vite@latest smartinsight-ui -- --template react
cd smartinsight-ui
npm install axios recharts react-dropzone tailwindcss
```

### Antigravity skills for this phase

```
Use @frontend-developer to build a React 19 dashboard with:
- A drag-and-drop CSV upload zone
- A loading state while AI analysis runs
- A card grid displaying AI insights returned from the API
- A recharts bar chart from the parsed dataset

Use @design-taste-frontend for polished typography and layout.
```

### Key components to build

```
src/
├── components/
│   ├── UploadZone.jsx          # react-dropzone CSV uploader
│   ├── InsightCard.jsx         # Displays one AI insight
│   ├── InsightGrid.jsx         # Maps over insights array
│   ├── DataChart.jsx           # Recharts bar/line chart
│   └── LoadingSpinner.jsx
├── pages/
│   └── Dashboard.jsx           # Main page — composes all components
├── api/
│   └── client.js               # Axios instance pointing to :8080
└── App.jsx
```

### Dashboard flow

```
User drops CSV
    → POST /api/upload  (multipart/form-data)
    ← { datasetId, preview: [...] }
    → POST /api/analyze  (body: { datasetId })
    ← { insights: [...], chartData: [...] }
    → Render InsightGrid + DataChart
```

---

## 🔐 Phase 5 — Security & Polish

### Antigravity skills

```
Use @cc-skill-security-review on my UploadController — check for:
file size limits, allowed MIME types, sanitization before LLM prompt injection.

Use @code-refactoring to clean up my LLMService and extract a 
PromptBuilder utility class.
```

### Checklist

- [ ] File size limit (`spring.servlet.multipart.max-file-size=5MB`)
- [ ] Allowed MIME type validation (only `text/csv`, `application/json`)
- [ ] Prompt injection guard — sanitize user data before embedding in LLM prompt
- [ ] API key stored in environment variable, never hardcoded
- [ ] CORS configured for frontend origin only
- [ ] Error responses return structured JSON, not stack traces

---

## 📦 Phase 6 — GitHub & README

### Antigravity skill

```
Use @senior-fullstack to write a production-quality README.md for 
SmartInsight that includes: project overview, architecture diagram 
(ASCII), setup steps, API reference, and screenshots placeholder.
```

### Suggested repo structure

```
smartinsight/
├── backend/              # Spring Boot Maven project
├── frontend/             # Vite + React project
├── docker-compose.yml
├── .env.example
└── README.md
```

### README must-haves for your resume

1. **Live demo GIF** — record a 20-second Loom, convert to GIF
2. **Architecture section** — show the Java ↔ LLM ↔ React flow
3. **API table** — document your 3-4 endpoints
4. **Tech badges** — Java, Spring Boot, React, OpenAI, Docker, PostgreSQL
5. **"Why I built this"** — 2-sentence product story

---

## 🚀 Phase 7 — Deployment (Bonus)

| Service | What to deploy | Free tier? |
|--------|----------------|------------|
| Railway | Spring Boot + PostgreSQL | ✅ Yes |
| Vercel | React frontend | ✅ Yes |
| Render | Spring Boot alternative | ✅ Yes |

```
Use @backend-dev-guidelines to configure my Spring Boot app 
for Railway deployment with environment variable injection.
```

---

## 📋 Full Antigravity Session Workflow

Here's the recommended session-by-session order to maximize skill productivity:

| Session | Skills to Invoke | Goal |
|---------|-----------------|------|
| 1 | `@brainstorming` + `@senior-fullstack` | Architecture + data model design |
| 2 | `@backend-dev-guidelines` | Scaffold controllers, services, entities |
| 3 | `@senior-fullstack` (TDD mode) | Write and run unit tests |
| 4 | `@cc-skill-security-review` | Security audit on upload + LLM endpoints |
| 5 | `@frontend-developer` + `@design-taste-frontend` | Build React dashboard |
| 6 | `@code-refactoring` | Final cleanup and SOLID pass |
| 7 | `@senior-fullstack` | Write README + deployment config |

---

## ✅ Resume Bullet Points (Ready to Copy)

Once built, you can use these on your CV:

> - Built **SmartInsight**, a full-stack AI analytics platform using **Java 21 + Spring Boot 3** and **React 19**, integrating the OpenAI API for LLM-powered CSV data analysis
> - Designed a REST microservice architecture with **Spring Data JPA**, **PostgreSQL**, and **Docker Compose** for local and cloud deployment
> - Implemented prompt engineering patterns in Java to safely embed user data into LLM calls with injection-safe sanitization
> - Achieved 80%+ test coverage with **JUnit 5 + Mockito** using a TDD-first workflow

---

## 🔗 Resources

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [OpenAI Java SDK (community)](https://github.com/TheoKanning/openai-java)
- [Recharts Docs](https://recharts.org)
- [Antigravity Skills Catalog](https://sickn33.github.io/antigravity-awesome-skills/)
- [antigravity-awesome-skills repo](https://github.com/sickn33/antigravity-awesome-skills)
