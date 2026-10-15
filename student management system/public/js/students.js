const API_URL = "/api/students";
const PAGE_SIZE = 8;

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

const fieldPhoto = document.getElementById("fieldPhoto");
const photoPreview = document.getElementById("photoPreview");
const photoPlaceholder = document.getElementById("photoPlaceholder");

const searchBox = document.getElementById("searchBox");
const courseFilter = document.getElementById("courseFilter");
const paginationEl = document.getElementById("pagination");
const paginationInfo = document.getElementById("paginationInfo");
const paginationControls = document.getElementById("paginationControls");

let editingId = null;
let pendingPhotoDataUrl = null;
let allStudents = [];
let sortKey = "id";
let sortDir = 1; // 1 = asc, -1 = desc
let currentPage = 1;

// ---------- Helpers ----------

function cgpaTier(cgpa) {
  if (cgpa >= 8) return "cgpa-high";
  if (cgpa >= 6) return "cgpa-mid";
  return "cgpa-low";
}

function initials(name) {
  return name.trim().split(/\s+/).slice(0, 2).map(w => w[0].toUpperCase()).join("");
}

function escapeHtml(value) {
  const div = document.createElement("div");
  div.textContent = value;
  return div.innerHTML;
}

function showError(message) {
  errorBanner.textContent = message;
  errorBanner.hidden = false;
}

function clearError() {
  errorBanner.hidden = true;
}

// ---------- Data pipeline: filter -> sort -> paginate -> render ----------

function getFilteredSorted() {
  const query = searchBox.value.trim().toLowerCase();
  const course = courseFilter.value;

  let list = allStudents.filter(s => {
    const matchesQuery = !query || s.name.toLowerCase().includes(query) || String(s.id).includes(query);
    const matchesCourse = !course || s.course === course;
    return matchesQuery && matchesCourse;
  });

  list.sort((a, b) => {
    let av = a[sortKey], bv = b[sortKey];
    if (typeof av === "string") { av = av.toLowerCase(); bv = bv.toLowerCase(); }
    if (av < bv) return -1 * sortDir;
    if (av > bv) return 1 * sortDir;
    return 0;
  });

  return list;
}

function populateCourseFilter() {
  const courses = [...new Set(allStudents.map(s => s.course))].sort();
  const currentValue = courseFilter.value;
  courseFilter.innerHTML = '<option value="">All Courses</option>' +
    courses.map(c => `<option value="${escapeHtml(c)}">${escapeHtml(c)}</option>`).join("");
  courseFilter.value = currentValue;
}

function updateSortArrows() {
  document.querySelectorAll(".sort-arrow").forEach(el => {
    el.classList.remove("active");
    el.textContent = "";
  });
  const activeArrow = document.querySelector(`.sort-arrow[data-arrow="${sortKey}"]`);
  if (activeArrow) {
    activeArrow.classList.add("active");
    activeArrow.textContent = sortDir === 1 ? "\u25B2" : "\u25BC";
  }
}

function render() {
  populateCourseFilter();
  const filtered = getFilteredSorted();
  studentCountEl.textContent = allStudents.length;
  updateSortArrows();

  if (filtered.length === 0) {
    tableBody.innerHTML = "";
    emptyState.hidden = false;
    paginationEl.hidden = true;
    return;
  }
  emptyState.hidden = true;

  const totalPages = Math.max(1, Math.ceil(filtered.length / PAGE_SIZE));
  currentPage = Math.min(currentPage, totalPages);
  const start = (currentPage - 1) * PAGE_SIZE;
  const pageItems = filtered.slice(start, start + PAGE_SIZE);

  tableBody.innerHTML = "";
  for (const s of pageItems) {
    const tr = document.createElement("tr");
    const avatarCell = s.hasPhoto
      ? `<img class="avatar" src="/api/students/${s.id}/photo?v=${Date.now()}" alt="${escapeHtml(s.name)}">`
      : `<div class="avatar-placeholder">${initials(s.name)}</div>`;
    tr.innerHTML = `
      <td class="col-photo">${avatarCell}</td>
      <td class="col-id">${s.id}</td>
      <td><a href="student-profile.html?id=${s.id}" style="color:var(--ink);font-weight:600;text-decoration:none;">${escapeHtml(s.name)}</a></td>
      <td class="col-age">${s.age}</td>
      <td>${escapeHtml(s.course)}</td>
      <td class="col-cgpa"><span class="cgpa-badge ${cgpaTier(s.cgpa)}">${s.cgpa.toFixed(2)}</span></td>
      <td class="col-portal">
        ${s.hasLogin ? '<span class="badge badge-green">Active</span>' : '<span class="badge badge-neutral">None</span>'}
        <button class="btn btn-small btn-ghost" data-action="generate-login" data-id="${s.id}" type="button">
          ${s.hasLogin ? "Reset" : "Generate"}
        </button>
      </td>
      <td class="col-actions">
        <div class="row-actions">
          <a class="icon-btn" href="student-profile.html?id=${s.id}" title="View profile">${ICONS.eye}</a>
          <button class="icon-btn" data-action="edit" data-id="${s.id}" title="Edit" type="button">${ICONS.edit}</button>
          <button class="icon-btn danger" data-action="delete" data-id="${s.id}" title="Delete" type="button">${ICONS.trash}</button>
        </div>
      </td>
    `;
    tableBody.appendChild(tr);
  }

  renderPagination(filtered.length, totalPages);
}

function renderPagination(totalItems, totalPages) {
  paginationEl.hidden = totalPages <= 1 && totalItems <= PAGE_SIZE;
  const start = (currentPage - 1) * PAGE_SIZE + 1;
  const end = Math.min(currentPage * PAGE_SIZE, totalItems);
  paginationInfo.textContent = `Showing ${start}-${end} of ${totalItems}`;

  let buttons = "";
  for (let p = 1; p <= totalPages; p++) {
    buttons += `<button class="${p === currentPage ? "active" : ""}" data-page="${p}" type="button">${p}</button>`;
  }
  paginationControls.innerHTML = buttons;
}

paginationControls.addEventListener("click", (e) => {
  const btn = e.target.closest("button[data-page]");
  if (!btn) return;
  currentPage = Number(btn.dataset.page);
  render();
});

searchBox.addEventListener("input", () => { currentPage = 1; render(); });
courseFilter.addEventListener("change", () => { currentPage = 1; render(); });

document.querySelectorAll("th.sortable").forEach(th => {
  th.addEventListener("click", () => {
    const key = th.dataset.sort;
    if (sortKey === key) {
      sortDir *= -1;
    } else {
      sortKey = key;
      sortDir = 1;
    }
    render();
  });
});

async function loadStudents() {
  try {
    clearError();
    allStudents = (await apiRequest(API_URL, { method: "GET" })) || [];
    render();
    maybeOpenEditFromQueryParam();
  } catch (err) {
    showError("Could not load students: " + err.message);
  }
}

function maybeOpenEditFromQueryParam() {
  const params = new URLSearchParams(window.location.search);
  const editId = params.get("edit");
  if (!editId) return;
  const student = allStudents.find(s => String(s.id) === editId);
  if (student) {
    openForm("edit", student);
  }
  window.history.replaceState({}, "", "dashboard.html");
}

// ---------- Form handling ----------

function setPhotoPreview(src) {
  if (src) {
    photoPreview.src = src;
    photoPreview.hidden = false;
    photoPlaceholder.hidden = true;
  } else {
    photoPreview.hidden = true;
    photoPlaceholder.hidden = false;
  }
}

function openForm(mode, student) {
  clearError();
  studentForm.reset();
  pendingPhotoDataUrl = null;

  if (mode === "edit" && student) {
    editingId = student.id;
    formTitle.textContent = `Edit Student #${student.id}`;
    submitBtn.textContent = "Update Student";
    fieldName.value = student.name;
    fieldAge.value = student.age;
    fieldCourse.value = student.course;
    fieldCgpa.value = student.cgpa;
    setPhotoPreview(student.hasPhoto ? `/api/students/${student.id}/photo?v=${Date.now()}` : null);
  } else {
    editingId = null;
    formTitle.textContent = "Add Student";
    submitBtn.textContent = "Save Student";
    setPhotoPreview(null);
  }

  formPanel.hidden = false;
  fieldName.focus();
}

function closeForm() {
  formPanel.hidden = true;
  studentForm.reset();
  editingId = null;
  pendingPhotoDataUrl = null;
}

fieldPhoto.addEventListener("change", () => {
  const file = fieldPhoto.files[0];
  if (!file) return;
  if (file.size > 3 * 1024 * 1024) {
    showToast("Photo is too large (max 3 MB).", "error");
    fieldPhoto.value = "";
    return;
  }
  const reader = new FileReader();
  reader.onload = () => {
    pendingPhotoDataUrl = reader.result;
    setPhotoPreview(pendingPhotoDataUrl);
  };
  reader.readAsDataURL(file);
});

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
    const savedStudent = await apiRequest(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (pendingPhotoDataUrl) {
      const targetId = isEdit ? editingId : savedStudent.id;
      await apiRequest(`${API_URL}/${targetId}/photo`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ photo: pendingPhotoDataUrl }),
      });
    }

    closeForm();
    showToast(isEdit ? "Student updated successfully." : "Student added successfully.", "success");
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

  if (action === "generate-login") {
    const ok = await showConfirm(
      "Generate portal login?",
      `This creates a new password for student #${id}. Any existing password will stop working.`,
      "Generate"
    );
    if (!ok) return;
    try {
      clearError();
      const result = await apiRequest(`${API_URL}/${id}/generate-login`, { method: "POST" });
      await showConfirm(
        `Login ready for student #${id}`,
        `Student ID: ${id}<br>Password: <strong>${result.password}</strong><br><br>Share these with the student -- this password won't be shown again.`,
        "Got it"
      );
      await loadStudents();
    } catch (err) {
      showToast(err.message, "error");
    }
  }

  if (action === "edit") {
    try {
      const student = await apiRequest(`${API_URL}/${id}`, { method: "GET" });
      openForm("edit", student);
    } catch (err) {
      showToast(err.message, "error");
    }
  }

  if (action === "delete") {
    const ok = await showConfirm("Delete student?", `Delete student #${id}? This cannot be undone.`, "Delete");
    if (!ok) return;
    try {
      clearError();
      await apiRequest(`${API_URL}/${id}`, { method: "DELETE" });
      showToast("Student deleted.", "success");
      await loadStudents();
    } catch (err) {
      showToast(err.message, "error");
    }
  }
});

// ---------- Export: PDF and Excel ----------

document.getElementById("exportPdfBtn").addEventListener("click", () => {
  try {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    doc.setFontSize(16);
    doc.text("Student Management System", 14, 16);
    doc.setFontSize(10);
    doc.setTextColor(120);
    doc.text(`Exported ${new Date().toLocaleString()}`, 14, 22);

    const rows = getFilteredSorted().map(s => [s.id, s.name, s.age, s.course, s.cgpa.toFixed(2)]);
    doc.autoTable({
      startY: 28,
      head: [["ID", "Name", "Age", "Course", "CGPA"]],
      body: rows,
      headStyles: { fillColor: [79, 70, 229] },
      styles: { fontSize: 10 },
    });

    doc.save("students.pdf");
    showToast("PDF exported.", "success");
  } catch (err) {
    showToast("Could not export PDF: " + err.message, "error");
  }
});

document.getElementById("exportExcelBtn").addEventListener("click", () => {
  try {
    const rows = getFilteredSorted().map(s => ({
      ID: s.id, Name: s.name, Age: s.age, Course: s.course, CGPA: s.cgpa,
    }));
    const worksheet = XLSX.utils.json_to_sheet(rows);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Students");
    XLSX.writeFile(workbook, "students.xlsx");
    showToast("Excel file exported.", "success");
  } catch (err) {
    showToast("Could not export Excel: " + err.message, "error");
  }
});

// ---------- Init ----------

loadStudents();
