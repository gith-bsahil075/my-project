/**
 * Shared helpers for every page inside the app shell (dashboard, attendance,
 * marks, fees) AND the student portal. Include this before the page's own
 * script. Pages can set `window.LOGIN_PAGE` before this script runs to
 * control where a 401 / logout sends the user (defaults to the admin login).
 */

const LOGIN_PAGE = window.LOGIN_PAGE || "index.html";

/**
 * Wraps fetch(): on any 401 response it redirects to the login page instead
 * of letting each page handle "not logged in" separately.
 */
async function apiFetch(url, options) {
  const response = await fetch(url, options);
  if (response.status === 401) {
    window.location.href = LOGIN_PAGE;
    throw new Error("Not logged in.");
  }
  return response;
}

async function apiRequest(url, options) {
  const response = await apiFetch(url, options);
  let data = null;
  try {
    data = await response.json();
  } catch (_) {
    // some responses have no body
  }
  if (!response.ok) {
    const message = (data && data.error) ? data.error : `Request failed (${response.status})`;
    throw new Error(message);
  }
  return data;
}

function wireLogoutButton() {
  const btn = document.getElementById("logoutBtn");
  if (!btn) return;
  btn.addEventListener("click", async () => {
    try {
      await fetch("/api/logout", { method: "POST" });
    } finally {
      window.location.href = LOGIN_PAGE;
    }
  });
}

/** Collapsible sidebar -- remembers the choice across pages via localStorage. */
function wireSidebarToggle() {
  const sidebar = document.querySelector(".sidebar");
  const toggle = document.getElementById("sidebarToggle");
  if (!sidebar || !toggle) return;

  if (localStorage.getItem("sms-sidebar-collapsed") === "true") {
    sidebar.classList.add("collapsed");
  }
  toggle.addEventListener("click", () => {
    sidebar.classList.toggle("collapsed");
    localStorage.setItem("sms-sidebar-collapsed", sidebar.classList.contains("collapsed"));
  });
}

/** Animates a number counting up from 0 -- used for dashboard stat cards. */
function animateCounter(el, target, { duration = 900, decimals = 0, prefix = "", suffix = "" } = {}) {
  const start = performance.now();
  function tick(now) {
    const progress = Math.min((now - start) / duration, 1);
    const eased = 1 - Math.pow(1 - progress, 3);
    const value = target * eased;
    el.textContent = prefix + value.toFixed(decimals) + suffix;
    if (progress < 1) requestAnimationFrame(tick);
  }
  requestAnimationFrame(tick);
}

document.addEventListener("DOMContentLoaded", () => {
  wireLogoutButton();
  wireSidebarToggle();
});
