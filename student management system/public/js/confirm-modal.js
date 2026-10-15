/**
 * Styled confirm dialog, replacing window.confirm().
 * Usage: const ok = await showConfirm("Delete student?", "This can't be undone.");
 */
function showConfirm(title, message, confirmLabel = "Confirm") {
  return new Promise((resolve) => {
    const overlay = document.createElement("div");
    overlay.className = "modal-overlay";
    overlay.innerHTML = `
      <div class="modal-card">
        <h3>${title}</h3>
        <p>${message}</p>
        <div class="modal-actions">
          <button class="btn btn-ghost" data-choice="cancel" type="button">Cancel</button>
          <button class="btn btn-primary" data-choice="ok" type="button">${confirmLabel}</button>
        </div>
      </div>
    `;
    document.body.appendChild(overlay);

    function close(result) {
      overlay.remove();
      resolve(result);
    }

    overlay.addEventListener("click", (e) => {
      if (e.target === overlay) close(false);
      const btn = e.target.closest("button[data-choice]");
      if (btn) close(btn.dataset.choice === "ok");
    });

    document.addEventListener("keydown", function escHandler(e) {
      if (e.key === "Escape") {
        document.removeEventListener("keydown", escHandler);
        close(false);
      }
    });
  });
}
