const loginForm = document.getElementById("loginForm");
const errorBanner = document.getElementById("errorBanner");
const loginBtn = document.getElementById("loginBtn");
const usernameField = document.getElementById("username");
const passwordField = document.getElementById("password");
const togglePasswordBtn = document.getElementById("togglePassword");
const rememberMeBox = document.getElementById("rememberMe");

// Show/hide password
function updateToggleIcon() {
  togglePasswordBtn.innerHTML = passwordField.type === "password" ? ICONS.eye : ICONS.eyeOff;
}
updateToggleIcon();
togglePasswordBtn.addEventListener("click", () => {
  passwordField.type = passwordField.type === "password" ? "text" : "password";
  updateToggleIcon();
});

// Remember me: only remembers the username locally, never the password
const rememberedUser = localStorage.getItem("sms-remembered-admin");
if (rememberedUser) {
  usernameField.value = rememberedUser;
  rememberMeBox.checked = true;
  passwordField.focus();
}

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  errorBanner.hidden = true;
  loginBtn.disabled = true;
  loginBtn.textContent = "Logging in...";

  const username = usernameField.value.trim();
  const password = passwordField.value;

  try {
    const response = await fetch("/api/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });
    const data = await response.json().catch(() => null);

    if (!response.ok) {
      throw new Error((data && data.error) ? data.error : "Login failed.");
    }

    if (rememberMeBox.checked) {
      localStorage.setItem("sms-remembered-admin", username);
    } else {
      localStorage.removeItem("sms-remembered-admin");
    }

    window.location.href = "home.html";
  } catch (err) {
    errorBanner.textContent = err.message;
    errorBanner.hidden = false;
    loginBtn.disabled = false;
    loginBtn.textContent = "Log In";
  }
});
