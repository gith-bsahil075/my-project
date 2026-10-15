const errorBanner = document.getElementById("errorBanner");

function showError(message) {
  errorBanner.textContent = message;
  errorBanner.hidden = false;
}

function money(n) {
  return "Rs " + Number(n).toLocaleString("en-IN", { maximumFractionDigits: 0 });
}

const CHART_COLORS = {
  blue: "#4F46E5",
  purple: "#7C3AED",
  emerald: "#10B981",
  amber: "#F59E0B",
  rose: "#F43F5E",
};

function isDark() {
  return document.documentElement.getAttribute("data-theme") === "dark";
}

function chartTextColor() {
  return isDark() ? "#9298B0" : "#6B7280";
}

async function loadProfile() {
  const s = await apiRequest("/api/me", { method: "GET" });
  document.getElementById("profileName").textContent = s.name;
  document.getElementById("profileId").textContent = `Student ID: ${s.id}`;
  document.getElementById("profileCourse").textContent = s.course;
  document.getElementById("profileAgeCgpa").textContent = `Age ${s.age} | CGPA ${s.cgpa.toFixed(2)}`;

  const photoEl = document.getElementById("profilePhoto");
  const placeholderEl = document.getElementById("profilePlaceholder");
  if (s.hasPhoto) {
    photoEl.src = `/api/me/photo?v=${Date.now()}`;
    photoEl.hidden = false;
    placeholderEl.hidden = true;
  } else {
    placeholderEl.textContent = s.name.trim().split(/\s+/).slice(0, 2).map(w => w[0].toUpperCase()).join("");
    photoEl.hidden = true;
    placeholderEl.hidden = false;
  }
}

async function loadDashboard() {
  if (typeof Chart === "undefined") {
    showError("Charts could not load (no internet connection to fetch Chart.js). Stats below still work.");
  }

  try {
    await loadProfile();

    const attendance = await apiRequest("/api/me/attendance", { method: "GET" });
    animateCounter(document.getElementById("statAttendance"), attendance.percentage, { decimals: 1, suffix: "%" });

    const marks = await apiRequest("/api/me/marks", { method: "GET" });
    animateCounter(document.getElementById("statMarks"), marks.average, { decimals: 1, suffix: "%" });
    document.getElementById("statGrade").textContent = marks.grade;

    const fee = await apiRequest("/api/me/fees", { method: "GET" });
    animateCounter(document.getElementById("statFeePaid"), fee.paidAmount, { decimals: 0, prefix: "Rs " });
    animateCounter(document.getElementById("statFeeDue"), fee.dueAmount, { decimals: 0, prefix: "Rs " });

    if (typeof Chart !== "undefined") {
      const present = attendance.present;
      const absent = Math.max(0, attendance.totalClasses - present);
      renderAttendanceChart(present, absent);
      renderFeesChart(fee.paidAmount, fee.dueAmount);
    }
  } catch (err) {
    showError(err.message);
  }
}

function renderAttendanceChart(present, absent) {
  new Chart(document.getElementById("chartAttendance"), {
    type: "doughnut",
    data: {
      labels: ["Present", "Absent"],
      datasets: [{
        data: [present, absent],
        backgroundColor: [CHART_COLORS.emerald, CHART_COLORS.rose],
        borderWidth: 0,
      }],
    },
    options: { responsive: true, plugins: { legend: { labels: { color: chartTextColor() } } } },
  });
}

function renderFeesChart(paid, due) {
  new Chart(document.getElementById("chartFees"), {
    type: "doughnut",
    data: {
      labels: ["Paid", "Due"],
      datasets: [{
        data: [paid, due],
        backgroundColor: [CHART_COLORS.blue, CHART_COLORS.amber],
        borderWidth: 0,
      }],
    },
    options: { responsive: true, plugins: { legend: { labels: { color: chartTextColor() } } } },
  });
}

loadDashboard();
