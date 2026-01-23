# Social Media Platform

A full-stack social media application built with modern technologies, supporting real-time messaging, audio calls, video calls, and bi-directional communication using WebSockets.

---

## Project Overview

This project consists of a Jakarta EE 10 backend and a React.js frontend. The backend handles authentication, data management, WebSocket signaling, and media coordination, while the frontend provides the user interface for chat, audio calls, and video calls. The entire application is containerized using Docker.

---

## Project Structure

.
├── backend/
│   └── java/corechat/          Jakarta EE 10 backend (REST, WebSocket, media handling)
├── frontend/                   React.js frontend
└── docker-compose.yaml         Docker orchestration

---

## Tech Stack

Backend:
- Jakarta EE 10
- WebSocket
- Apache Maven

Frontend:
- React.js

Infrastructure:
- Docker
- Docker Compose

---

## Prerequisites

Before running the project, make sure you have the following installed:

- Node.js
- Apache Maven
- Docker
- Docker Compose

---

## How to Run the Project

1. Clean the backend webapp directory by removing any existing static files:

   backend/java/corechat/src/main/webapp/*

2. Build the frontend:

   ```bash
   cd frontend
   npm install
   npm run build
   cp -r dist/* ../backend/java/corechat/src/main/webapp/

3. build backend 
   cd ../backend/java/corechat
   mvn clean package

4. cd to the project root and run the following:
   docker compose up --build -d

5. If everything runs correctly, you should see the corechat application deployed successfully in the GlassFish container logs.

    Open your browser and navigate to:

    http://localhost:8080/corechat/

    You should see the login page. Register a new user and log in using the newly created account.

6. Real-Time Communication Testing (Important)

    Do NOT open the application in two tabs of the same browser.
    Session-based authentication will conflict.

    To test real-time messaging and calls, use:

    Two different browsers on the same machine (e.g., Chrome and Firefox), OR

    Two different computers.

    Testing From Another PC (Recommended for Media)

    Find the IPv4 address of the machine running Docker.

    On the second PC, open the following URL:

    https://<ipv4_of_host>/corechat/

    The application works over both HTTP and HTTPS. HTTPS is recommended when testing across devices because browsers are more permissive with camera and microphone access over secure connections.

7. Using the Application

    Log in as a different user on the second browser or PC.

    Navigate to the Feed page.

    Click the 🔎 Search button.

    Search for users by name or email.

    Click the ➕ button to start a conversation.

    You can now:

    Send real-time text messages

    Initiate audio calls

    Initiate video calls

    Messages and calls should appear instantly on both clients.

8. Troubleshooting

    If the frontend does not load, ensure the dist files were copied correctly into the backend webapp directory.

    If audio or video permissions fail, try using HTTPS or a different browser.

    Check Docker logs if the application does not deploy correctly.



