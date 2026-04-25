# SmartInsight — AI-Powered Analytics Dashboard

SmartInsight is a full-stack analytics platform built with **Java 21, Spring Boot 3, and React 19**. It allows users to upload datasets (CSV/JSON), which are then analyzed by the **Gemini API (Google AI)** to extract key insights, detect anomalies, and summarize the data for an interactive dashboard.

## 🚀 Architecture

```text
[ React Dashboard (Vite + Tailwind) ]
       ↓ REST API (Axios)
[ Spring Boot 3 Backend (Java 21) ]
   ├── /api/upload  → Parses data (Commons CSV)
   ├── /api/analyze → Calls Gemini API for insight generation
   └── /api/insights → Retrieves AI-generated results
       ↓
[ PostgreSQL ] & [ Gemini API ]
```

## 🛠️ Tech Stack

- **Frontend:** React 19, Vite, Tailwind CSS, Recharts, React Dropzone
- **Backend:** Java 21, Spring Boot 3 (Web, Data JPA, Validation), Lombok
- **Database:** PostgreSQL 15
- **AI Integration:** Google Gemini 1.5 Flash API
- **Infrastructure:** Docker, Docker Compose

## 📦 Local Setup

### 1. Database (Docker)
Start the PostgreSQL database:
```bash
docker-compose up -d
```

### 2. Backend (Spring Boot)
Requires Maven and Java 21. Set your Gemini API key in `application.yml` or export it:
```bash
export GEMINI_API_KEY="your-api-key-here"
cd backend
mvn spring-boot:run
```

### 3. Frontend (React)
Requires Node.js 20+.
```bash
cd frontend
npm install
npm run dev
```

## 🌐 API Reference

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/upload` | POST | Upload a dataset (Multipart `file`). Returns `datasetId`. |
| `/api/analyze` | POST | Analyze dataset via LLM. Body: `{ "datasetId": 1 }`. Returns insights. |
| `/api/insights/{id}` | GET | Fetch saved insights for a specific dataset. |

## 🛡️ Security

- File size limits (5MB Max)
- Allowed MIME types (CSV and JSON)
- Prompt injection mitigation strategies
