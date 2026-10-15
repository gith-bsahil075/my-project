const feeTableBody = document.getElementById("feeTableBody");
const emptyState = document.getElementById("emptyState");
const errorBanner = document.getElementById("errorBanner");

const formPanel = document.getElementById("formPanel");
const formTitle = document.getElementById("formTitle");
const feeForm = document.getElementById("feeForm");
const fieldTotalFee = document.getElementById("fieldTotalFee");
const fieldPaidAmount = document.getElementById("fieldPaidAmount");
const cancelFormBtn = document.getElementById("cancelFormBtn");

let editingStudentId = null;

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

function statusBadgeClass(status) {
  if (status === "Paid") return "badge-green";
  if (status === "Partial") return "badge-gold";
  if (status === "Due") return "badge-rose";
  return "badge-neutral";
}

function money(n) {
  return "Rs " + Number(n).toLocaleString("en-IN", { maximumFractionDigits: 2 });
}

async function loadFees() {
  try {
    clearError();
    const fees = await apiRequest("/api/fees", { method: "GET" });

    feeTableBody.innerHTML = "";
    if (fees.length === 0) {
      emptyState.hidden = false;
      return;
    }
    emptyState.hidden = true;

    for (const f of [...fees].sort((a, b) => a.studentId - b.studentId)) {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td class="col-id">${f.studentId}</td>
        <td>${escapeHtml(f.name)}</td>
        <td>${escapeHtml(f.course)}</td>
        <td>${money(f.totalFee)}</td>
        <td>${money(f.paidAmount)}</td>
        <td>${money(f.dueAmount)}</td>
        <td><span class="badge ${statusBadgeClass(f.status)}">${f.status}</span></td>
        <td class="col-actions">
          <div class="row-actions">
            <button class="icon-btn" data-id="${f.studentId}"
              data-total="${f.totalFee}" data-paid="${f.paidAmount}" title="Edit" type="button">${ICONS.edit}</button>
          </div>
        </td>
      `;
      feeTableBody.appendChild(tr);
    }
  } catch (err) {
    showError("Could not load fees: " + err.message);
  }
}

function openForm(studentId, totalFee, paidAmount) {
  clearError();
  editingStudentId = studentId;
  formTitle.textContent = `Update Fee - Student #${studentId}`;
  fieldTotalFee.value = totalFee;
  fieldPaidAmount.value = paidAmount;
  formPanel.hidden = false;
  fieldTotalFee.focus();
}

function closeForm() {
  formPanel.hidden = true;
  feeForm.reset();
  editingStudentId = null;
}

cancelFormBtn.addEventListener("click", closeForm);

feeTableBody.addEventListener("click", (event) => {
  const btn = event.target.closest("button[data-id]");
  if (!btn) return;
  openForm(Number(btn.dataset.id), Number(btn.dataset.total), Number(btn.dataset.paid));
});

feeForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearError();

  try {
    await apiRequest(`/api/fees/${editingStudentId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        totalFee: Number(fieldTotalFee.value),
        paidAmount: Number(fieldPaidAmount.value),
      }),
    });
    closeForm();
    showToast("Fee updated.", "success");
    await loadFees();
  } catch (err) {
    showError(err.message);
  }
});

loadFees();
