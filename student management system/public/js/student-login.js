const loginForm = document.getElementById("loginForm");
const errorBanner = document.getElementById("errorBanner");
const loginBtn = document.getElementById("loginBtn");
const studentIdField = document.getElementById("studentId");
const passwordField = document.getElementById("password");
const togglePasswordBtn = document.getElementById("togglePassword");
const rememberMeBox = document.getElementById("rememberMe");

function updateToggleIcon() {
  togglePasswordBtn.innerHTML = passwordField.type === "password" ? ICONS.eye : ICONS.eyeOff;
}
updateToggleIcon();
togglePasswordBtn.addEventListener("click", () => {
  passwordField.type = passwordField.type === "password" ? "text" : "password";
  updateToggleIcon();
});

const rememberedId = localStorage.getItem("sms-remembered-student");
if (rememberedId) {
  studentIdField.value = rememberedId;
  rememberMeBox.checked = true;
  passwordField.focus();
}

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  errorBanner.hidden = true;
  loginBtn.disabled = true;
  loginBtn.textContent = "Logging in...";

  const studentId = Number(studentIdField.value);
  const password = passwordField.value;

  try {
    const response = await fetch("/api/student-login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ studentId, password }),
    });
    const data = await response.json().catch(() => null);

    if (!response.ok) {
      throw new Error((data && data.error) ? data.error : "Login failed.");
    }

    if (rememberMeBox.checked) {
      localStorage.setItem("sms-remembered-student", String(studentId));
    } else {
      localStorage.removeItem("sms-remembered-student");
    }

    window.location.href = "student-dashboard.html";
  } catch (err) {
    errorBanner.textContent = err.message;
    errorBanner.hidden = false;
    loginBtn.disabled = false;
    loginBtn.textContent = "Log In";
  }
});
