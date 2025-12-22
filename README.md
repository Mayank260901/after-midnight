🌙 After Midnight
After Midnight is a private, emotion-driven full-stack web application designed to express feelings, thoughts, poetry, and music in a secure and meaningful way.
The platform blends modern web technologies with a minimal, dark, intimate user experience, accessible only via a private token.

This project was built as a production-ready full-stack application, deployed end-to-end using modern cloud platforms.

✨ Key Features
🔐 Private Token-Based Access
Only users with a valid access token can unlock the content.

🖤 Dark, Minimal UI
Designed for calm, emotional readability — especially late-night use.

📝 Poems & Thoughts
Handwritten poems, reflections, and “3 AM thoughts” stored securely.

🎵 Music Sharing
Music composed and written using AI, integrated into the experience.

⚡ Smooth Animations & Loading States
Clean transitions with graceful loaders for a premium feel.

🏗️ Tech Stack
Frontend
Next.js (App Router)

React

Axios

Tailwind CSS

Dark Theme UI

Deployed on Vercel

Backend
Java Spring Boot

Spring Data JPA

REST APIs

Token validation logic

Deployed on Render

Database
PostgreSQL

Hosted on Render

Production-ready schema

🔐 Access Flow
User opens the frontend application

A private access token is required

Token is validated via backend API

If valid → content is unlocked

If invalid → access is denied

This ensures the platform remains personal and intentional.

🌍 Live URLs
Frontend (Vercel)
👉 Use the Vercel deployment URL

Backend (Render API)

https://after-midnight-backend.onrender.com
🧪 Sample API Endpoint
GET /api/access/validate?token=midnight-only
Response:

true
⚙️ Environment Variables
Frontend (.env.production)
NEXT_PUBLIC_API_BASE_URL=https://after-midnight-backend.onrender.com
Backend (Render Environment Variables)
DB_URL=jdbc:postgresql://<host>:5432/<database>
DB_USERNAME=<username>
DB_PASSWORD=<password>
PORT=8080
🚀 Deployment Summary
Frontend deployed via Vercel (GitHub integration)

Backend deployed via Render (Docker-based service)

Database provisioned using Render PostgreSQL

Secure environment variables configured for production

External DB connections handled professionally

🧠 Why This Project Matters
This project demonstrates:

Real-world full-stack architecture

Production deployment experience

Secure backend design

Clean frontend separation

Thoughtful UI/UX design

Emotional storytelling through technology

It is both a technical showcase and a personal creative expression.

👨‍💻 Author
Mayank Bhargava
Full-Stack Java Developer

Java · Spring Boot · PostgreSQL · React · Next.js

Passionate about building meaningful, real-world applications

📌 Final Note
Some projects are built to scale.
Some are built to express.

After Midnight was built to do both.
