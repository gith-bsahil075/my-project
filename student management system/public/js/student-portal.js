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

function money(n) {
  return "Rs " + Number(n).toLocaleString("en-IN", { maximumFractionDigits: 2 });
}

function statusBadgeClass(status) {
  if (status === "Paid") return "badge-green";
  if (status === "Partial") return "badge-gold";
  if (status === "Due") return "badge-rose";
  return "badge-neutral";
}

function gradeBadgeClass(grade) {
  if (grade === "A+" || grade === "A") return "badge-green";
  if (grade === "B+" || grade === "B" || grade === "C") return "badge-gold";
  return "badge-rose";
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

async function loadAttendance() {
  const data = await apiRequest("/api/me/attendance", { method: "GET" });
  document.getElementById("totalClasses").textContent = data.totalClasses;
  document.getElementById("presentCount").textContent = data.present;
  document.getElementById("attendancePercentage").textContent = data.percentage.toFixed(1) + "%";

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
}

async function loadMarks() {
  const data = await apiRequest("/api/me/marks", { method: "GET" });
  document.getElementById("marksAverage").textContent = data.average.toFixed(1) + "%";
  document.getElementById("marksGrade").textContent = data.grade;

  const tbody = document.getElementById("marksTableBody");
  tbody.innerHTML = "";
  const empty = document.getElementById("marksEmpty");

  if (data.records.length === 0) {
    empty.hidden = false;
    return;
  }
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

async function loadFees() {
  const fee = await apiRequest("/api/me/fees", { method: "GET" });
  document.getElementById("feeTotal").textContent = money(fee.totalFee);
  document.getElementById("feePaid").textContent = money(fee.paidAmount);
  document.getElementById("feeDue").textContent = money(fee.dueAmount);
  document.getElementById("feeStatus").innerHTML =
    `<span class="badge ${statusBadgeClass(fee.status)}">${fee.status}</span>`;
}

async function loadAll() {
  try {
    await loadProfile();
    await loadAttendance();
    await loadMarks();
    await loadFees();
  } catch (err) {
    showError(err.message);
  }
}

loadAll();
