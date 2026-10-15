const studentSelect = document.getElementById("studentSelect");
const hintText = document.getElementById("hintText");
const attendanceContent = document.getElementById("attendanceContent");
const errorBanner = document.getElementById("errorBanner");

const totalClassesEl = document.getElementById("totalClasses");
const presentCountEl = document.getElementById("presentCount");
const percentageValueEl = document.getElementById("percentageValue");

const attendanceForm = document.getElementById("attendanceForm");
const fieldSubject = document.getElementById("fieldSubject");
const fieldDate = document.getElementById("fieldDate");
const fieldStatus = document.getElementById("fieldStatus");

const attendanceTableBody = document.getElementById("attendanceTableBody");
const noRecordsState = document.getElementById("noRecordsState");

function showError(message) {
  errorBanner.textContent = message;
  errorBanner.hidden = false;
}

function clearError() {
  errorBanner.hidden = true;
}

function escapeHtml(value) {
  const div = document.createElement("div");
  div.textContent = value;
  return div.innerHTML;
}

function percentageBadgeClass(pct) {
  if (pct >= 75) return "badge-green";
  if (pct >= 60) return "badge-gold";
  return "badge-rose";
}

async function loadStudentOptions() {
  try {
    const students = await apiRequest("/api/students", { method: "GET" });
    studentSelect.innerHTML = '<option value="">-- choose a student --</option>';
    for (const s of [...students].sort((a, b) => a.id - b.id)) {
      const opt = document.createElement("option");
      opt.value = s.id;
      opt.textContent = `#${s.id} - ${s.name}`;
      studentSelect.appendChild(opt);
    }
  } catch (err) {
    showError("Could not load students: " + err.message);
  }
}

async function loadAttendance(studentId) {
  try {
    clearError();
    const data = await apiRequest(`/api/attendance/student/${studentId}`, { method: "GET" });

    totalClassesEl.textContent = data.totalClasses;
    presentCountEl.textContent = data.present;
    percentageValueEl.textContent = data.percentage.toFixed(1) + "%";
    percentageValueEl.className = "value";

    attendanceTableBody.innerHTML = "";
    if (data.records.length === 0) {
      noRecordsState.hidden = false;
    } else {
      noRecordsState.hidden = true;
      for (const r of [...data.records].sort((a, b) => (a.date < b.date ? 1 : -1))) {
        const tr = document.createElement("tr");
        const badgeClass = r.status === "Present" ? "badge-green" : "badge-rose";
        tr.innerHTML = `
          <td>${escapeHtml(r.subject)}</td>
          <td>${r.date}</td>
          <td><span class="badge ${badgeClass}">${r.status}</span></td>
          <td class="col-actions">
            <div class="row-actions">
              <button class="icon-btn danger" data-id="${r.id}" title="Delete" type="button">${ICONS.trash}</button>
            </div>
          </td>
        `;
        attendanceTableBody.appendChild(tr);
      }
    }
  } catch (err) {
    showError(err.message);
  }
}

studentSelect.addEventListener("change", () => {
  const id = studentSelect.value;
  if (!id) {
    attendanceContent.hidden = true;
    hintText.hidden = false;
    return;
  }
  hintText.hidden = true;
  attendanceContent.hidden = false;
  fieldDate.value = new Date().toISOString().slice(0, 10);
  loadAttendance(id);
});

attendanceForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearError();
  const studentId = Number(studentSelect.value);

  try {
    await apiRequest("/api/attendance", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        studentId,
        subject: fieldSubject.value.trim(),
        date: fieldDate.value,
        status: fieldStatus.value,
      }),
    });
    fieldSubject.value = "";
    showToast("Attendance marked.", "success");
    await loadAttendance(studentId);
  } catch (err) {
    showError(err.message);
  }
});

attendanceTableBody.addEventListener("click", async (event) => {
  const btn = event.target.closest("button[data-id]");
  if (!btn) return;
  const ok = await showConfirm("Delete record?", "Delete this attendance record?", "Delete");
  if (!ok) return;

  try {
    clearError();
    await apiRequest(`/api/attendance/${btn.dataset.id}`, { method: "DELETE" });
    showToast("Record deleted.", "success");
    await loadAttendance(studentSelect.value);
  } catch (err) {
    showToast(err.message, "error");
  }
});

loadStudentOptions();
