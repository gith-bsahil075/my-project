const errorBanner = document.getElementById("errorBanner");

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

function isDark() {
  return document.documentElement.getAttribute("data-theme") === "dark";
}

function chartTextColor() {
  return isDark() ? "#9298B0" : "#6B7280";
}

async function loadFees() {
  if (typeof Chart === "undefined") {
    showError("Chart could not load (no internet connection to fetch Chart.js). Stats below still work.");
  }

  try {
    const fee = await apiRequest("/api/me/fees", { method: "GET" });

    document.getElementById("feeTotal").textContent = money(fee.totalFee);
    document.getElementById("feePaid").textContent = money(fee.paidAmount);
    document.getElementById("feeDue").textContent = money(fee.dueAmount);
    document.getElementById("feeStatus").innerHTML =
      `<span class="badge ${statusBadgeClass(fee.status)}">${fee.status}</span>`;

    const percentPaid = fee.totalFee > 0 ? Math.min(100, (fee.paidAmount / fee.totalFee) * 100) : 0;
    document.getElementById("feeProgressFill").style.width = percentPaid.toFixed(1) + "%";
    document.getElementById("feeProgressPercent").textContent = percentPaid.toFixed(1) + "% paid";
    document.getElementById("feeProgressDue").textContent = money(fee.dueAmount) + " due";
    document.getElementById("feeProgressSub").textContent =
      fee.status === "Paid"
        ? "All dues cleared. Thank you!"
        : `${money(fee.paidAmount)} paid out of ${money(fee.totalFee)}`;

    if (fee.status === "Paid" && fee.totalFee > 0) {
      document.getElementById("paidBanner").hidden = false;
    }

    if (typeof Chart !== "undefined") {
      new Chart(document.getElementById("chartFees"), {
        type: "doughnut",
        data: {
          labels: ["Paid", "Due"],
          datasets: [{
            data: [fee.paidAmount, fee.dueAmount],
            backgroundColor: ["#4F46E5", "#F59E0B"],
            borderWidth: 0,
          }],
        },
        options: { responsive: true, plugins: { legend: { labels: { color: chartTextColor() } } } },
      });
    }
  } catch (err) {
    showError(err.message);
  }
}

loadFees();
