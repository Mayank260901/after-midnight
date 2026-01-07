# 🌙 After Midnight

**After Midnight** is a private, emotion-driven full-stack web application designed to express feelings, thoughts, poetry, and music in a secure and meaningful way.  
The platform blends **modern web technologies** with a **minimal, dark, intimate user experience**, accessible only via a private token.

This project was built as a **production-ready full-stack application**, deployed end-to-end using modern cloud platforms.

---

## ✨ Key Features

- 🔐 **Private Token-Based Access**  
  Only users with a valid access token can unlock the content.

- 🖤 **Dark, Minimal UI**  
  Designed for calm, emotional readability — especially late-night use.

- 📝 **Poems & Thoughts**  
  Handwritten poems, reflections, and “3 AM thoughts” stored securely.

- 🎵 **Music Sharing**  
  Music composed and written using AI, integrated into the experience.

- ⚡ **Smooth Animations & Loading States**  
  Clean transitions with graceful loaders for a premium feel.

---

## 🏗️ Tech Stack

### Frontend
- Next.js (App Router)
- React
- Axios
- Tailwind CSS
- Dark Theme UI
- Deployed on Vercel

### Backend
- Java Spring Boot
- Spring Data JPA
- REST APIs
- Token validation logic
- Deployed on Render

### Database
- PostgreSQL
- Hosted on Render
- Production-ready schema

---

## 🔐 Access Flow

1. User opens the frontend application  
2. A **private access token** is required  
3. Token is validated via backend API  
4. If valid → content is unlocked  
5. If invalid → access is denied  

This ensures the platform remains **personal and intentional**.

---

## 🌍 Live URLs

- **Frontend (Vercel)**  
  https://after-midnight.vercel.app  

- **Direct Access Link (With Token)**  
  https://after-midnight.vercel.app/?token=midnight-only  

- **Backend (Render API)**  
  https://after-midnight-backend.onrender.com  

---

## 🧪 Sample API Endpoint

    GET /api/access/validate?token=midnight-only

Response:

    true

---

## ⚙️ Environment Variables

### Frontend (.env.production)

    NEXT_PUBLIC_API_BASE_URL=https://after-midnight-backend.onrender.com

### Backend (Render Environment Variables)

    DB_URL=jdbc:postgresql://<host>:5432/<database>
    DB_USERNAME=<username>
    DB_PASSWORD=<password>
    PORT=8080

---

## 🚀 Deployment Summary

- Frontend deployed via **Vercel** using GitHub integration  
- Backend deployed via **Render** as a Docker-based web service  
- PostgreSQL database provisioned on **Render**  
- Secure environment variables configured for production  
- External database connections handled using production credentials  

---

## 🧠 Why This Project Matters

This project demonstrates:

- Real-world **full-stack architecture**
- Production-level deployment experience
- Secure backend and database integration
- Clean separation between frontend and backend
- Thoughtful UI/UX design with emotional intent  

It serves as both a **technical portfolio project** and a **creative expression built with purpose**.

---

## 👨‍💻 Author

**Mayank Bhargava**  
Full-Stack Java Developer  

- Java · Spring Boot · PostgreSQL  
- React · Next.js · REST APIs  
- Passionate about building meaningful, real-world applications  

---

## 📌 Final Note

> Some projects are built to scale.  
> Some are built to express.  
>  
> **After Midnight** was built to do both.

