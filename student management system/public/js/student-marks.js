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

function gradeBadgeClass(grade) {
  if (grade === "A+" || grade === "A") return "badge-green";
  if (grade === "B+" || grade === "B" || grade === "C") return "badge-gold";
  return "badge-rose";
}

function isDark() {
  return document.documentElement.getAttribute("data-theme") === "dark";
}

function chartTextColor() {
  return isDark() ? "#9298B0" : "#6B7280";
}

function chartGridColor() {
  return isDark() ? "#2A2E42" : "#E7E9F3";
}

async function loadMarks() {
  if (typeof Chart === "undefined") {
    showError("Chart could not load (no internet connection to fetch Chart.js). Stats below still work.");
  }

  try {
    const data = await apiRequest("/api/me/marks", { method: "GET" });
    document.getElementById("marksAverage").textContent = data.average.toFixed(1) + "%";
    document.getElementById("marksGrade").textContent = data.grade;
    document.getElementById("marksSubjectCount").textContent = data.records.length;

    let best = null;
    let bestPct = -1;
    for (const r of data.records) {
      const pct = (r.marksObtained / r.maxMarks) * 100;
      if (pct > bestPct) {
        bestPct = pct;
        best = r.subject;
      }
    }
    document.getElementById("marksHighest").textContent = best || "-";

    const tbody = document.getElementById("marksTableBody");
    tbody.innerHTML = "";
    const empty = document.getElementById("marksEmpty");

    if (data.records.length === 0) {
      empty.hidden = false;
    } else {
      empty.hidden = true;
      for (const r of data.records) {
        const pct = (r.marksObtained / r.maxMarks) * 100;
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${escapeHtml(r.subject)}</td>
          <td>${r.marksObtained}</td>
          <td>${r.maxMarks}</td>
          <td><span class="badge ${gradeBadgeClass(data.grade)}">${pct.toFixed(1)}%</span></td>
        `;
        tbody.appendChild(tr);
      }
    }

    if (typeof Chart !== "undefined" && data.records.length > 0) {
      new Chart(document.getElementById("chartMarks"), {
        type: "bar",
        data: {
          labels: data.records.map(r => r.subject),
          datasets: [{
            label: "Percentage",
            data: data.records.map(r => (r.marksObtained / r.maxMarks) * 100),
            backgroundColor: "#7C3AED",
            borderRadius: 6,
          }],
        },
        options: {
          responsive: true,
          plugins: { legend: { display: false } },
          scales: {
            x: { ticks: { color: chartTextColor() }, grid: { color: chartGridColor() } },
            y: { ticks: { color: chartTextColor() }, grid: { color: chartGridColor() }, max: 100 },
          },
        },
      });
    }
  } catch (err) {
    showError(err.message);
  }
}

loadMarks();
