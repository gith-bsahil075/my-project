const studentSelect = document.getElementById("studentSelect");
const hintText = document.getElementById("hintText");
const marksContent = document.getElementById("marksContent");
const errorBanner = document.getElementById("errorBanner");

const averageValueEl = document.getElementById("averageValue");
const gradeValueEl = document.getElementById("gradeValue");

const marksForm = document.getElementById("marksForm");
const fieldSubject = document.getElementById("fieldSubject");
const fieldMarksObtained = document.getElementById("fieldMarksObtained");
const fieldMaxMarks = document.getElementById("fieldMaxMarks");

const marksTableBody = document.getElementById("marksTableBody");
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

function gradeBadgeClass(grade) {
  if (grade === "A+" || grade === "A") return "badge-green";
  if (grade === "B+" || grade === "B" || grade === "C") return "badge-gold";
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

async function loadMarks(studentId) {
  try {
    clearError();
    const data = await apiRequest(`/api/marks/student/${studentId}`, { method: "GET" });

    averageValueEl.textContent = data.average.toFixed(1) + "%";
    gradeValueEl.textContent = data.grade;

    marksTableBody.innerHTML = "";
    if (data.records.length === 0) {
      noRecordsState.hidden = false;
    } else {
      noRecordsState.hidden = true;
      for (const r of data.records) {
        const pct = (r.marksObtained / r.maxMarks) * 100;
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${escapeHtml(r.subject)}</td>
          <td>${r.marksObtained}</td>
          <td>${r.maxMarks}</td>
          <td><span class="badge ${gradeBadgeClass(data.grade)}">${pct.toFixed(1)}%</span></td>
          <td class="col-actions">
            <div class="row-actions">
              <button class="icon-btn danger" data-id="${r.id}" title="Delete" type="button">${ICONS.trash}</button>
            </div>
          </td>
        `;
        marksTableBody.appendChild(tr);
      }
    }
  } catch (err) {
    showError(err.message);
  }
}

studentSelect.addEventListener("change", () => {
  const id = studentSelect.value;
  if (!id) {
    marksContent.hidden = true;
    hintText.hidden = false;
    return;
  }
  hintText.hidden = true;
  marksContent.hidden = false;
  loadMarks(id);
});

marksForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearError();
  const studentId = Number(studentSelect.value);

  try {
    await apiRequest("/api/marks", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        studentId,
        subject: fieldSubject.value.trim(),
        marksObtained: Number(fieldMarksObtained.value),
        maxMarks: Number(fieldMaxMarks.value),
      }),
    });
    fieldSubject.value = "";
    fieldMarksObtained.value = "";
    fieldMaxMarks.value = "100";
    showToast("Marks added.", "success");
    await loadMarks(studentId);
  } catch (err) {
    showError(err.message);
  }
});

marksTableBody.addEventListener("click", async (event) => {
  const btn = event.target.closest("button[data-id]");
  if (!btn) return;
  const ok = await showConfirm("Delete record?", "Delete this marks record?", "Delete");
  if (!ok) return;

  try {
    clearError();
    await apiRequest(`/api/marks/${btn.dataset.id}`, { method: "DELETE" });
    showToast("Record deleted.", "success");
    await loadMarks(studentSelect.value);
  } catch (err) {
    showToast(err.message, "error");
  }
});

loadStudentOptions();
