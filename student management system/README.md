# Student Management System (Java SE 21)

Two front ends, one shared backend. Built with plain Java SE 21 — no
external libraries, no database driver, no Maven/Gradle required for
either version.

- **Console version** (`Main.java`) — simple text-menu app, students only.
- **Web version** (`ServerMain.java`) — a full REST API + browser UI with
  login and four modules, using only `com.sun.net.httpserver`
  (built into the JDK).

## Features (web version)
- **Dashboard** — stat cards (students, present today, fees collected, average marks) and charts (student growth, attendance overview, subject-wise marks, fees collection)
- **Admin panel** — login, manage Students / Attendance / Marks / Fees
- **Student portal** — a separate, read-only login where each student can
  view only their own profile, attendance, marks, and fee status
- **Login** — admin account, PBKDF2-hashed password, session cookie
- **Students** — Add / View / Search / Update / Delete, with an optional profile photo per student, plus a dedicated profile page, search/filter/sort/pagination
- **Dark / light mode**, toast notifications, and a custom confirm dialog throughout
- **Export** the Students table to PDF or Excel (respects current search/filter/sort)
- **Student ID Card** with a QR code, printable / saveable as PDF from the profile page
- **Attendance** — mark Present/Absent per subject & date, auto-calculated
  attendance percentage
- **Marks / Results** — subject-wise marks entry, average % and letter grade
- **Fees** — total fee / paid / due / status per student

All data is stored as plain text files under `data/` (CSV-style, human
readable) — no database server to install or configure.

### Default login
```
username: admin
password: admin123
```
Created automatically the first time you run the web version. To reset it,
stop the server, delete `data/admin.txt`, and start it again.

## Project Structure
```
StudentManagementSystem/
├── src/
│   ├── Main.java                  -> console entry point (students only)
│   ├── ServerMain.java            -> web entry point (starts the server)
│   ├── model/                     -> Student, AttendanceRecord, MarkRecord, Fee
│   ├── dao/                       -> file-based storage for each module
│   ├── service/                   -> validation + business logic per module
│   ├── server/
│   │   ├── ApiServer.java         -> wires routes, filters, shared services
│   │   ├── AuthFilter.java        -> blocks requests without a valid session
│   │   ├── AuthHandler.java       -> /api/login, /api/logout, /api/session
│   │   ├── StudentHandler.java    -> /api/students
│   │   ├── AttendanceHandler.java -> /api/attendance
│   │   ├── MarksHandler.java      -> /api/marks
│   │   └── FeeHandler.java        -> /api/fees
│   └── util/                      -> JsonUtil, PasswordUtil, SessionManager
├── public/                        -> the web frontend
│   ├── index.html                 -> login page
│   ├── dashboard.html             -> Students
│   ├── attendance.html
│   ├── marks.html
│   ├── fees.html
│   ├── style.css
│   └── js/                        -> one script per page + shared common.js
├── data/                          -> auto-created on first save
└── .vscode/                       -> run configs for both versions
```

## How to Run in VS Code
1. Install the **Extension Pack for Java** (by Microsoft), if you don't
   already have it.
2. Open this folder in VS Code: `File -> Open Folder`.
3. Open the **Run and Debug** panel (`Ctrl+Shift+D`) and pick a
   configuration:
   - **Launch Main (Console version)** -> runs in the Terminal panel.
   - **Launch ServerMain (Web version)** -> starts the server; then open
     **http://localhost:8080** in your browser and log in.

## How to Run from a Terminal (no VS Code needed)
Requires JDK 21+ (`java -version` / `javac -version` to check). Run these
from inside the `StudentManagementSystem` folder:

```bash
# compile everything once
javac -d bin src/Main.java src/ServerMain.java src/model/*.java src/dao/*.java src/service/*.java src/server/*.java src/util/*.java

# then run whichever version you want
java -cp bin Main          # console version
java -cp bin ServerMain    # web version -> open http://localhost:8080
```
(On Windows PowerShell, `src/model/*.java` style wildcards work the same way.
If your shell doesn't expand them, list every `.java` file explicitly instead.)

## REST API (web version)
All routes below except `/api/login` require a valid session cookie
(returned automatically by your browser after logging in).

| Method | URL                         | Body (JSON)                             | Description              |
|--------|------------------------------|-------------------------------------------|----------------------------|
| POST   | `/api/login`                 | `{username, password}`                    | Log in, sets session cookie |
| POST   | `/api/logout`                | –                                          | Clears session               |
| GET    | `/api/students`              | –                                          | List all students            |
| POST   | `/api/students`               | `{name, age, course, cgpa}`               | Create (auto ID)             |
| PUT    | `/api/students/{id}`          | `{name, age, course, cgpa}`               | Update                       |
| DELETE | `/api/students/{id}`          | –                                          | Delete                        |
| GET    | `/api/students/{id}/photo`    | –                                          | View a student's photo         |
| POST   | `/api/students/{id}/photo`    | `{photo: "data:image/...;base64,..."}`    | Upload/replace a student's photo |
| POST   | `/api/attendance`             | `{studentId, subject, date, status}`      | Mark attendance               |
| GET    | `/api/attendance/student/{id}`| –                                          | Records + percentage          |
| DELETE | `/api/attendance/{id}`        | –                                          | Delete a record               |
| POST   | `/api/marks`                  | `{studentId, subject, marksObtained, maxMarks}` | Add marks             |
| GET    | `/api/marks/student/{id}`     | –                                          | Records + average + grade     |
| DELETE | `/api/marks/{id}`             | –                                          | Delete a record                |
| GET    | `/api/fees`                   | –                                          | Every student's fee status     |
| PUT    | `/api/fees/{studentId}`       | `{totalFee, paidAmount}`                  | Set fee for a student           |
| GET    | `/api/dashboard/stats`        | –                                          | Aggregated stats + chart data for the Dashboard page |

## Deploying for Free (so others can open it online)

This project can be deployed for free on **Render.com** (no credit card
needed). A `Dockerfile` is already included, since Render doesn't run
plain Java directly — it builds and runs the Docker image instead.

**Important limitation:** Render's free tier has no persistent storage.
Any students/attendance/marks/fees added on the live site are lost
whenever the service restarts, redeploys, or goes to sleep after ~15
minutes of no traffic (it wakes back up automatically on the next
visit, just with empty data again). This is fine for **sharing a working
demo/portfolio link** — not for data you need to keep long-term. (True
persistence would mean adding a real database, which is a bigger change.)

### Steps
1. **Push this project to GitHub** (skip if it's already on GitHub):
   - Create a new repository at github.com (Public or Private, either works).
   - In this project folder, run:
     ```bash
     git init
     git add .
     git commit -m "Student Management System"
     git branch -M main
     git remote add origin https://github.com/<your-username>/<repo-name>.git
     git push -u origin main
     ```
2. **Create a free Render account** at render.com (sign in with GitHub — fastest).
3. Click **New -> Web Service**, connect your GitHub repo.
4. Render will detect the `Dockerfile` automatically — leave Runtime as **Docker**.
5. Instance type: **Free**. Click **Create Web Service**.
6. Wait for the build to finish (a few minutes the first time). Render
   gives you a public URL like `https://your-app-name.onrender.com`.
7. Open that URL, log in with `admin` / `admin123` (same default as local),
   and you have a link you can send to anyone.

That's the whole thing — after step 7 you have a shareable link.

## Notes

- The backend has zero dependencies (plain JDK only). The Dashboard
  charts, PDF/Excel export, and QR code features use small third-party
  JS libraries (Chart.js, jsPDF, SheetJS, QRCode.js) -- these are bundled
  locally under `public/vendor/`, not loaded from a CDN, so the whole
  app (including these features) works fully offline.
- The UI uses a modern Blue/Purple/Emerald palette, Inter/Poppins fonts
  (Google Fonts, loaded by the browser), a collapsible sidebar, dark/light
  mode, toast notifications, a custom confirm dialog, and Chart.js for the
  dashboard -- all still backed by the same plain-JDK Java backend.
- Everything (HTTP server, JSON parsing, password hashing, sessions) is
  hand-written on top of the plain JDK, on purpose — nothing to install,
  nothing that can fail to download, and it's easy to explain line-by-line
  in an interview.
- Password hashing uses **PBKDF2WithHmacSHA256** (built into
  `javax.crypto`) with a random salt per account and a constant-time
  comparison — a legitimate hashing approach, not just plain SHA-256.
- Sessions live in memory, so restarting the server logs everyone out —
  a reasonable tradeoff for a college project, and avoids needing a
  database just for login state.
- Because storage sits behind DAO classes, swapping any module's storage
  for a real database later only means rewriting that one DAO — the
  services, handlers, and frontend would not need to change.
