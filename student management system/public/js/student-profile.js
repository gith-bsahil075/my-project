const errorBanner = document.getElementById("errorBanner");
const params = new URLSearchParams(window.location.search);
const studentId = params.get("id");

function showError(message) {
  errorBanner.textContent = message;
  errorBanner.hidden = false;
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

async function loadProfile() {
  const s = await apiRequest(`/api/students/${studentId}`, { method: "GET" });
  document.getElementById("profileName").textContent = s.name;
  document.getElementById("profileId").textContent = `Student ID: ${s.id}`;
  document.getElementById("profileCourse").textContent = s.course;
  document.getElementById("profileAgeCgpa").textContent = `Age ${s.age} | CGPA ${s.cgpa.toFixed(2)}`;

  const photoEl = document.getElementById("profilePhoto");
  const placeholderEl = document.getElementById("profilePlaceholder");
  if (s.hasPhoto) {
    photoEl.src = `/api/students/${s.id}/photo?v=${Date.now()}`;
    photoEl.hidden = false;
    placeholderEl.hidden = true;
  } else {
    placeholderEl.textContent = s.name.trim().split(/\s+/).slice(0, 2).map(w => w[0].toUpperCase()).join("");
    photoEl.hidden = true;
    placeholderEl.hidden = false;
  }
}

async function loadAttendance() {
  const data = await apiRequest(`/api/attendance/student/${studentId}`, { method: "GET" });
  document.getElementById("totalClasses").textContent = data.totalClasses;
  document.getElementById("presentCount").textContent = data.present;
  document.getElementById("attendancePercentage").textContent = data.percentage.toFixed(1) + "%";
}

async function loadMarks() {
  const data = await apiRequest(`/api/marks/student/${studentId}`, { method: "GET" });
  document.getElementById("marksAverage").textContent = data.average.toFixed(1) + "%";
  document.getElementById("marksGrade").textContent = data.grade;
}

async function loadFees() {
  const fees = await apiRequest("/api/fees", { method: "GET" });
  const fee = fees.find(f => String(f.studentId) === String(studentId));
  if (!fee) return;
  document.getElementById("feeTotal").textContent = money(fee.totalFee);
  document.getElementById("feePaid").textContent = money(fee.paidAmount);
  document.getElementById("feeDue").textContent = money(fee.dueAmount);
  document.getElementById("feeStatus").innerHTML =
    `<span class="badge ${statusBadgeClass(fee.status)}">${fee.status}</span>`;
}

// ---------- ID Card + QR code ----------

let idCardBuilt = false;

function buildIdCard(s) {
  document.getElementById("idCardName").textContent = s.name;
  document.getElementById("idCardId").textContent = s.id;
  document.getElementById("idCardCourse").textContent = s.course;
  document.getElementById("idCardAge").textContent = s.age;

  const photoEl = document.getElementById("idCardPhoto");
  const placeholderEl = document.getElementById("idCardPlaceholder");
  if (s.hasPhoto) {
    photoEl.src = `/api/students/${s.id}/photo?v=${Date.now()}`;
    photoEl.hidden = false;
    placeholderEl.hidden = true;
  } else {
    placeholderEl.textContent = s.name.trim().split(/\s+/).slice(0, 2).map(w => w[0].toUpperCase()).join("");
    photoEl.hidden = true;
    placeholderEl.hidden = false;
  }

  const qrContainer = document.getElementById("idCardQr");
  qrContainer.innerHTML = "";
  if (typeof QRCode !== "undefined") {
    new QRCode(qrContainer, {
      text: `SMS-STUDENT-${s.id}-${s.name}`,
      width: 64,
      height: 64,
      colorDark: "#161A2B",
      colorLight: "#ffffff",
    });
  }
  idCardBuilt = true;
}

document.getElementById("idCardBtn").addEventListener("click", async () => {
  const section = document.getElementById("idCardSection");
  section.hidden = !section.hidden;
  if (!section.hidden && !idCardBuilt) {
    try {
      const s = await apiRequest(`/api/students/${studentId}`, { method: "GET" });
      buildIdCard(s);
    } catch (err) {
      showToast(err.message, "error");
    }
  }
});

document.getElementById("printIdCardBtn").addEventListener("click", () => {
  window.print();
});

// ---------- Edit / Delete ----------

document.getElementById("editBtn").addEventListener("click", () => {
  window.location.href = `dashboard.html?edit=${studentId}`;
});

document.getElementById("deleteBtn").addEventListener("click", async () => {
  const ok = await showConfirm("Delete student?", "This will remove the student and all their attendance, marks, fee and photo records. This cannot be undone.", "Delete");
  if (!ok) return;
  try {
    await apiRequest(`/api/students/${studentId}`, { method: "DELETE" });
    showToast("Student deleted.", "success");
    setTimeout(() => { window.location.href = "dashboard.html"; }, 600);
  } catch (err) {
    showToast(err.message, "error");
  }
});

async function loadAll() {
  if (!studentId) {
    showError("No student selected.");
    return;
  }
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
