/**
 * Lightweight toast notifications. Usage:
 *   showToast("Student added successfully.", "success");
 *   showToast("Something went wrong.", "error");
 * Types: "success" | "error" | "warning" | "info" (default "info").
 */
function ensureToastStack() {
  let stack = document.querySelector(".toast-stack");
  if (!stack) {
    stack = document.createElement("div");
    stack.className = "toast-stack";
    document.body.appendChild(stack);
  }
  return stack;
}

const TOAST_ICONS = {
  success: typeof ICONS !== "undefined" ? ICONS.checkCircle : "",
  error: typeof ICONS !== "undefined" ? ICONS.xCircle : "",
  warning: typeof ICONS !== "undefined" ? ICONS.alertTriangle : "",
  info: typeof ICONS !== "undefined" ? ICONS.info : "",
};

function showToast(message, type = "info", durationMs = 3800) {
  const stack = ensureToastStack();
  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.innerHTML = `${TOAST_ICONS[type] || ""}<span>${message}</span>`;
  stack.appendChild(toast);

  const remove = () => {
    toast.classList.add("closing");
    setTimeout(() => toast.remove(), 180);
  };
  setTimeout(remove, durationMs);
  toast.addEventListener("click", remove);
}
