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

function chartGridColor() {
  return isDark() ? "#2A2E42" : "#E7E9F3";
}

async function loadDashboard() {
  if (typeof Chart === "undefined") {
    showError("Charts could not load (no internet connection to fetch Chart.js). Stats below still work.");
  }

  try {
    const stats = await apiRequest("/api/dashboard/stats", { method: "GET" });

    animateCounter(document.getElementById("statTotalStudents"), stats.totalStudents, { decimals: 0 });
    animateCounter(document.getElementById("statPresentToday"), stats.presentToday, { decimals: 0 });
    animateCounter(document.getElementById("statAverageMarks"), stats.averageMarks, { decimals: 1, suffix: "%" });

    const feesEl = document.getElementById("statFeesCollected");
    animateCounter(feesEl, stats.feesCollected, { decimals: 0, prefix: "Rs " });

    if (typeof Chart !== "undefined") {
      renderGrowthChart(stats.studentGrowth);
      renderAttendanceChart(stats.attendanceOverview);
      renderSubjectsChart(stats.subjectMarks);
      renderFeesChart(stats.feesCollected, stats.feesDue);
    }
  } catch (err) {
    showError(err.message);
  }
}

function baseOptions(extra = {}) {
  return Object.assign({
    responsive: true,
    plugins: { legend: { labels: { color: chartTextColor() } } },
    scales: {
      x: { ticks: { color: chartTextColor() }, grid: { color: chartGridColor() } },
      y: { ticks: { color: chartTextColor() }, grid: { color: chartGridColor() } },
    },
  }, extra);
}

function renderGrowthChart(growth) {
  const labels = Object.keys(growth);
  const data = Object.values(growth);
  new Chart(document.getElementById("chartGrowth"), {
    type: "line",
    data: {
      labels,
      datasets: [{
        label: "New students",
        data,
        borderColor: CHART_COLORS.blue,
        backgroundColor: "rgba(79,70,229,0.15)",
        fill: true,
        tension: 0.35,
        pointRadius: 4,
      }],
    },
    options: baseOptions({ plugins: { legend: { display: false } } }),
  });
}

function renderAttendanceChart(overview) {
  new Chart(document.getElementById("chartAttendance"), {
    type: "doughnut",
    data: {
      labels: Object.keys(overview),
      datasets: [{
        data: Object.values(overview),
        backgroundColor: [CHART_COLORS.emerald, CHART_COLORS.rose],
        borderWidth: 0,
      }],
    },
    options: { responsive: true, plugins: { legend: { labels: { color: chartTextColor() } } } },
  });
}

function renderSubjectsChart(subjectMarks) {
  const labels = Object.keys(subjectMarks);
  const data = Object.values(subjectMarks);
  new Chart(document.getElementById("chartSubjects"), {
    type: "bar",
    data: {
      labels,
      datasets: [{
        label: "Average %",
        data,
        backgroundColor: CHART_COLORS.purple,
        borderRadius: 6,
      }],
    },
    options: baseOptions({ plugins: { legend: { display: false } } }),
  });
}

function renderFeesChart(collected, due) {
  new Chart(document.getElementById("chartFees"), {
    type: "doughnut",
    data: {
      labels: ["Collected", "Due"],
      datasets: [{
        data: [collected, due],
        backgroundColor: [CHART_COLORS.emerald, CHART_COLORS.amber],
        borderWidth: 0,
      }],
    },
    options: { responsive: true, plugins: { legend: { labels: { color: chartTextColor() } } } },
  });
}

loadDashboard();
