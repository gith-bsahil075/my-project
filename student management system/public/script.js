const API_URL = "/api/students";

const tableBody = document.getElementById("studentTableBody");
const emptyState = document.getElementById("emptyState");
const studentCountEl = document.getElementById("studentCount");
const errorBanner = document.getElementById("errorBanner");

const formPanel = document.getElementById("formPanel");
const formTitle = document.getElementById("formTitle");
const studentForm = document.getElementById("studentForm");
const submitBtn = document.getElementById("submitBtn");

const fieldName = document.getElementById("fieldName");
const fieldAge = document.getElementById("fieldAge");
const fieldCourse = document.getElementById("fieldCourse");
const fieldCgpa = document.getElementById("fieldCgpa");

const openAddFormBtn = document.getElementById("openAddFormBtn");
const cancelFormBtn = document.getElementById("cancelFormBtn");

let editingId = null; // null => "add" mode, otherwise the id being edited

// ---------- Rendering ----------

function cgpaTier(cgpa) {
  if (cgpa >= 8) return "cgpa-high";
  if (cgpa >= 6) return "cgpa-mid";
  return "cgpa-low";
}

function escapeHtml(value) {
  const div = document.createElement("div");
  div.textContent = value;
  return div.innerHTML;
}

function renderStudents(students) {
  tableBody.innerHTML = "";
  studentCountEl.textContent = students.length;

  if (students.length === 0) {
    emptyState.hidden = false;
    return;
  }
  emptyState.hidden = true;

  const sorted = [...students].sort((a, b) => a.id - b.id);

  for (const s of sorted) {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td class="col-id">${s.id}</td>
      <td>${escapeHtml(s.name)}</td>
      <td class="col-age">${s.age}</td>
      <td>${escapeHtml(s.course)}</td>
      <td class="col-cgpa"><span class="cgpa-badge ${cgpaTier(s.cgpa)}">${s.cgpa.toFixed(2)}</span></td>
      <td class="col-actions">
        <div class="row-actions">
          <button class="btn btn-small btn-edit" data-action="edit" data-id="${s.id}" type="button">Edit</button>
          <button class="btn btn-small btn-delete" data-action="delete" data-id="${s.id}" type="button">Delete</button>
        </div>
      </td>
    `;
    tableBody.appendChild(tr);
  }
}

function showError(message) {
  errorBanner.textContent = message;
  errorBanner.hidden = false;
}

function clearError() {
  errorBanner.hidden = true;
  errorBanner.textContent = "";
}

// ---------- API calls ----------

async function apiRequest(url, options) {
  const response = await fetch(url, options);
  let data = null;
  try {
    data = await response.json();
  } catch (_) {
    // no JSON body (fine for some responses)
  }
  if (!response.ok) {
    const message = (data && data.error) ? data.error : `Request failed (${response.status})`;
    throw new Error(message);
  }
  return data;
}

async function loadStudents() {
  try {
    clearError();
    const students = await apiRequest(API_URL, { method: "GET" });
    renderStudents(students || []);
  } catch (err) {
    showError("Could not load students: " + err.message);
  }
}

// ---------- Form handling ----------

function openForm(mode, student) {
  clearError();
  studentForm.reset();

  if (mode === "edit" && student) {
    editingId = student.id;
    formTitle.textContent = `Edit Student #${student.id}`;
    submitBtn.textContent = "Update Student";
    fieldName.value = student.name;
    fieldAge.value = student.age;
    fieldCourse.value = student.course;
    fieldCgpa.value = student.cgpa;
  } else {
    editingId = null;
    formTitle.textContent = "Add Student";
    submitBtn.textContent = "Save Student";
  }

  formPanel.hidden = false;
  fieldName.focus();
}

function closeForm() {
  formPanel.hidden = true;
  studentForm.reset();
  editingId = null;
}

openAddFormBtn.addEventListener("click", () => openForm("add"));
cancelFormBtn.addEventListener("click", closeForm);

studentForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearError();

  const payload = {
    name: fieldName.value.trim(),
    age: Number(fieldAge.value),
    course: fieldCourse.value.trim(),
    cgpa: Number(fieldCgpa.value),
  };

  const isEdit = editingId !== null;
  const url = isEdit ? `${API_URL}/${editingId}` : API_URL;
  const method = isEdit ? "PUT" : "POST";

  submitBtn.disabled = true;
  try {
    await apiRequest(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    closeForm();
    await loadStudents();
  } catch (err) {
    showError(err.message);
  } finally {
    submitBtn.disabled = false;
  }
});

// ---------- Row actions (event delegation) ----------

tableBody.addEventListener("click", async (event) => {
  const btn = event.target.closest("button[data-action]");
  if (!btn) return;

  const id = Number(btn.dataset.id);
  const action = btn.dataset.action;

  if (action === "edit") {
    try {
      const student = await apiRequest(`${API_URL}/${id}`, { method: "GET" });
      openForm("edit", student);
    } catch (err) {
      showError(err.message);
    }
  }

  if (action === "delete") {
    const confirmed = confirm(`Delete student #${id}? This cannot be undone.`);
    if (!confirmed) return;
    try {
      clearError();
      await apiRequest(`${API_URL}/${id}`, { method: "DELETE" });
      await loadStudents();
    } catch (err) {
      showError(err.message);
    }
  }
});

// ---------- Init ----------

loadStudents();
