/**
 * Dark/light theme toggle. Applies data-theme on <html> and remembers the
 * choice in localStorage so it persists across pages and reloads.
 */
(function () {
  const saved = localStorage.getItem("sms-theme");
  if (saved === "dark") {
    document.documentElement.setAttribute("data-theme", "dark");
  }
})();

function currentTheme() {
  return document.documentElement.getAttribute("data-theme") === "dark" ? "dark" : "light";
}

function setTheme(theme) {
  if (theme === "dark") {
    document.documentElement.setAttribute("data-theme", "dark");
  } else {
    document.documentElement.removeAttribute("data-theme");
  }
  localStorage.setItem("sms-theme", theme);
  updateThemeToggleIcon();
}

function toggleTheme() {
  setTheme(currentTheme() === "dark" ? "light" : "dark");
}

function updateThemeToggleIcon() {
  const btn = document.getElementById("themeToggleBtn");
  if (!btn || typeof ICONS === "undefined") return;
  btn.innerHTML = currentTheme() === "dark" ? ICONS.sun : ICONS.moon;
}

function wireThemeToggle() {
  const btn = document.getElementById("themeToggleBtn");
  if (!btn) return;
  updateThemeToggleIcon();
  btn.addEventListener("click", toggleTheme);
}

document.addEventListener("DOMContentLoaded", wireThemeToggle);
