const errorBanner = document.getElementById("errorBanner");

function showError(message) {
  errorBanner.textContent = message;
  errorBanner.hidden = false;
}

function escapeHtml(value) {
  const div = document.createElement("div");
  div.textContent = value;
  return div.innerHTML;
}

function isDark() {
  return document.documentElement.getAttribute("data-theme") === "dark";
}

function chartTextColor() {
  return isDark() ? "#9298B0" : "#6B7280";
}

async function loadAttendance() {
  if (typeof Chart === "undefined") {
    showError("Chart could not load (no internet connection to fetch Chart.js). Stats below still work.");
  }

  try {
    const data = await apiRequest("/api/me/attendance", { method: "GET" });
    const absent = Math.max(0, data.totalClasses - data.present);

    animateCounter(document.getElementById("totalClasses"), data.totalClasses, { decimals: 0 });
    animateCounter(document.getElementById("presentCount"), data.present, { decimals: 0 });
    animateCounter(document.getElementById("absentCount"), absent, { decimals: 0 });
    document.getElementById("attendancePercentage").textContent = data.percentage.toFixed(1) + "%";

    if (typeof Chart !== "undefined") {
      new Chart(document.getElementById("chartAttendance"), {
        type: "doughnut",
        data: {
          labels: ["Present", "Absent"],
          datasets: [{
            data: [data.present, absent],
            backgroundColor: ["#10B981", "#F43F5E"],
            borderWidth: 0,
          }],
        },
        options: { responsive: true, plugins: { legend: { labels: { color: chartTextColor() } } } },
      });
    }

    const tbody = document.getElementById("attendanceTableBody");
    tbody.innerHTML = "";
    const empty = document.getElementById("attendanceEmpty");

    if (data.records.length === 0) {
      empty.hidden = false;
      return;
    }
    empty.hidden = true;

    for (const r of [...data.records].sort((a, b) => (a.date < b.date ? 1 : -1))) {
      const tr = document.createElement("tr");
      const badgeClass = r.status === "Present" ? "badge-green" : "badge-rose";
      tr.innerHTML = `
        <td>${escapeHtml(r.subject)}</td>
        <td>${r.date}</td>
        <td><span class="badge ${badgeClass}">${r.status}</span></td>
      `;
      tbody.appendChild(tr);
    }
  } catch (err) {
    showError(err.message);
  }
}

loadAttendance();
