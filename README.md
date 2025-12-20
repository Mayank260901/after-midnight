🖤 After Midnight
After Midnight is a private, full-stack web application designed to explore how technology can present emotional content with restraint, privacy, and intention.

The project focuses on calm UX, minimal design, and clean system architecture, combining a modern Java backend with a polished React frontend.

This is not a social platform.
It is a quiet, intentionally private digital space.

🌌 Key Features
🔐 Private Access (Token-Based)

No login, no accounts, no tracking

Access granted only via a secret link/token

✍️ Poems

Stored in MySQL

Rendered with soft typography and subtle animations

🌙 3 AM Thoughts

Scroll-based storytelling

One thought per screen for focus and calm pacing

🎧 Music Player

Audio streamed from external sources

Minimal UI with clean state handling

🧭 Global Navigation

Fixed, dark navbar

Active route highlighting

No visual noise

🧠 Design Philosophy
Less UI is more meaning

Silence is part of the experience

The user controls pace and attention

No analytics, no pressure, no obligation

Every animation, color, and interaction is intentionally restrained.

🏗️ System Architecture
Frontend (Next.js + Tailwind + Framer Motion)
        |
        |  REST APIs (Axios)
        v
Backend (Spring Boot)
        |
        |  JPA / Hibernate
        v
Database (MySQL)
🛠️ Tech Stack
Backend
Java 17

Spring Boot

Spring Data JPA

Hibernate

MySQL

RESTful APIs

Frontend
Next.js (App Router)

React

JavaScript (no TypeScript)

Tailwind CSS

Framer Motion

Axios

Other
Git & GitHub

MySQL Workbench

IntelliJ IDEA

VS Code

🔐 Private Access Flow
A single access token is stored in the database

Frontend checks token via backend API

If valid → site unlocks

If invalid → private message is shown

No authentication UI.
No user data stored.

📂 Project Structure
after-midnight/
├── backend/
│   ├── controller/
│   ├── model/
│   ├── repository/
│   └── config/
│
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   ├── components/
│   │   └── lib/
│
└── README.md
🚀 Running Locally
Backend
cd backend
mvn spring-boot:run
Runs on:

http://localhost:8080
Frontend
cd frontend
npm install
npm run dev
Runs on:

http://localhost:3000
🌑 Example Access
http://localhost:3000?token=midnight-only
(Replace token in production)

📌 Why This Project Matters
This project demonstrates:

Full-stack system design

Clean backend architecture

Thoughtful frontend engineering

Respect for user privacy

UI restraint and emotional intelligence

It intentionally avoids features that create pressure, dependency, or noise.

📖 Project Story (For Recruiters / Reviewers)
After Midnight was built as an exploration of how software can present deeply personal content without exploiting attention or data.

Instead of focusing on engagement metrics, the project prioritizes privacy, pacing, and minimalism.

Technically, it demonstrates a complete full-stack implementation using Spring Boot, MySQL, and Next.js, with secure access control and clean separation of concerns.

Design decisions were guided by one question:
Does this add clarity, or does it add noise?

🧑‍💻 Author
Mayank Bhargava
Java | Spring Boot | Full-Stack Developer

🖤 Closing Note
Some projects are meant to scale.
Some are meant to be understood quietly.

This one chooses the latter.