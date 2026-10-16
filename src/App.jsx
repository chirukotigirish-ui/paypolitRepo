
import { useState } from "react";
import "./App.css";

export default function App() {
  const [register, setRegister] = useState(false);
  const [otpStep, setOtpStep] = useState(false);
  const [loggedIn, setLoggedIn] = useState(false);

  const [forgotMode, setForgotMode] = useState(false);
  const [resetMode, setResetMode] = useState(false);

  const [name, setName] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [otp, setOtp] = useState("");

  const [resetEmail, setResetEmail] = useState("");
  const [resetUsername, setResetUsername] = useState("");
  const [resetOtp, setResetOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmNewPassword, setConfirmNewPassword] = useState("");

  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [activePage, setActivePage] = useState("Overview");

  async function apiRequest(url, body) {
    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });

    const data = await res.json().catch(() => ({}));

    if (!res.ok) {
      throw new Error(
        data.message || data.error || "Request failed. Please try again."
      );
    }

    return data;
  }

  async function handleRegister(e) {
    e.preventDefault();
    setLoading(true);
    setMessage("Submitting registration...");

    try {
      const data = await apiRequest("/api/users/register", {
        fullName: name,
        username,
        email,
        password,
      });

      setMessage(
        data.message || "Registration request submitted for approval."
      );
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleLogin(e) {
    e.preventDefault();
    setLoading(true);
    setMessage("Signing in...");

    try {
      const data = await apiRequest("/api/auth/login", {
        username,
        password,
      });

      setOtpStep(true);
      setOtp("");
      setMessage(
        data.message || "OTP sent to your registered email address."
      );
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleVerifyOtp(e) {
    e.preventDefault();

    if (!/^\d{6}$/.test(otp)) {
      setMessage("Please enter a valid 6-digit OTP.");
      return;
    }

    setLoading(true);
    setMessage("Verifying OTP...");

    try {
      const data = await apiRequest("/api/auth/verify-otp", {
        username,
        otp,
        purpose: "LOGIN",
      });

      setName(data.fullName || name || username);
      setLoggedIn(true);
      setOtpStep(false);
      setActivePage("Overview");
      setMessage("");
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleForgotPassword(e) {
    e.preventDefault();
    setLoading(true);
    setMessage("Requesting password reset...");

    try {
      const data = await apiRequest("/api/auth/forgot-password", {
        email: resetEmail.trim(),
      });

      setResetMode(true);
      setForgotMode(false);
      setMessage(
        data.message ||
          "Password reset OTP has been sent to your registered email."
      );
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleResetPassword(e) {
    e.preventDefault();

    if (!/^\d{6}$/.test(resetOtp)) {
      setMessage("Please enter a valid 6-digit password reset OTP.");
      return;
    }

    if (newPassword.length < 8 || newPassword.length > 100) {
      setMessage("Your new password must be between 8 and 100 characters.");
      return;
    }

    if (newPassword !== confirmNewPassword) {
      setMessage("The new passwords do not match.");
      return;
    }

    setLoading(true);
    setMessage("Resetting your password...");

    try {
      const data = await apiRequest("/api/auth/reset-password", {
        username: resetUsername.trim(),
        otp: resetOtp,
        newPassword,
      });

      setUsername(resetUsername.trim());
      setPassword("");
      setOtp("");
      setOtpStep(false);
      setRegister(false);
      setForgotMode(false);
      setResetMode(false);

      setResetOtp("");
      setNewPassword("");
      setConfirmNewPassword("");
      setResetEmail("");
      setResetUsername("");

      setMessage(
        data.message ||
          "Password reset successfully. You can now sign in."
      );
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  function logout() {
    setLoggedIn(false);
    setOtpStep(false);
    setPassword("");
    setOtp("");
    setMessage("");
    setActivePage("Overview");
  }

  function switchMode() {
    setRegister(!register);
    setOtpStep(false);
    setForgotMode(false);
    setResetMode(false);
    setMessage("");
    setOtp("");
  }

  function openForgotPassword() {
    setForgotMode(true);
    setResetMode(false);
    setOtpStep(false);
    setRegister(false);
    setResetEmail(email);
    setMessage("");
  }

  function backToLogin() {
    setForgotMode(false);
    setResetMode(false);
    setOtpStep(false);
    setRegister(false);
    setPassword("");
    setOtp("");
    setResetOtp("");
    setNewPassword("");
    setConfirmNewPassword("");
    setMessage("");
  }

  if (loggedIn) {
    return (
      <div className="app dashboard-app">
        <header className="navbar">
          <a
            className="logo"
            href="/"
            onClick={(e) => {
              e.preventDefault();
              setActivePage("Overview");
            }}
          >
            PayPilot.
          </a>

          <button className="register-button" onClick={logout}>
            Log out
          </button>
        </header>

        <main className="dashboard">
          <aside className="dashboard-sidebar">
            <p className="eyebrow">MAIN MENU</p>

            {["Overview", "Transactions", "Payments", "Settings"].map(
              (item) => (
                <button
                  key={item}
                  className={`nav-item ${
                    activePage === item ? "active" : ""
                  }`}
                  onClick={() => setActivePage(item)}
                >
                  {item}
                </button>
              )
            )}

            <div className="sidebar-user">
              <div className="avatar">
                {(name || username || "U").charAt(0).toUpperCase()}
              </div>
              <div>
                <strong>{name || username}</strong>
                <p>PayPilot user</p>
              </div>
            </div>
          </aside>

          <section className="dashboard-content">
            <p className="eyebrow">YOUR FINANCES, IN CONTROL</p>
            <h1>
              {activePage === "Overview"
                ? `Welcome, ${name || username}!`
                : activePage}
            </h1>

            <p className="dashboard-subtitle">
              {activePage === "Overview"
                ? "Here's your financial overview."
                : `Your ${activePage.toLowerCase()} section.`}
            </p>

            {activePage === "Overview" && (
              <>
                <div className="dashboard-notice">
                  Demo dashboard — balances and transactions are not connected
                  to real financial data.
                </div>

                <div className="dashboard-grid">
                  <article className="dashboard-card balance-card">
                    <p>Available balance</p>
                    <h2>₹ 0.00</h2>
                    <span>Demo balance</span>
                  </article>

                  <article className="dashboard-card">
                    <p>Total income</p>
                    <h2>₹ 0.00</h2>
                    <span>Demo data</span>
                  </article>

                  <article className="dashboard-card">
                    <p>Total expenses</p>
                    <h2>₹ 0.00</h2>
                    <span>Demo data</span>
                  </article>
                </div>

                <h2 className="section-heading">Quick actions</h2>

                <div className="quick-actions">
                  {[
                    ["↗", "Transfer money", "Payments"],
                    ["＋", "Make a payment", "Payments"],
                    ["▤", "View transactions", "Transactions"],
                  ].map(([symbol, title, page]) => (
                    <button
                      type="button"
                      className="action-card"
                      key={title}
                      onClick={() => setActivePage(page)}
                    >
                      <span className="action-symbol">{symbol}</span>
                      <strong>{title}</strong>
                      <span>Open →</span>
                    </button>
                  ))}
                </div>

                <h2 className="section-heading">Recent transactions</h2>

                <div className="transactions-card">
                  <div className="transaction-row">
                    <div>
                      <strong>No transactions yet</strong>
                      <p>Your transaction history will appear here.</p>
                    </div>
                    <span>—</span>
                  </div>
                </div>
              </>
            )}

            {activePage === "Transactions" && (
              <div className="transactions-card">
                <h2>Transaction history</h2>
                <p>
                  Transaction data will appear here when the backend endpoint
                  is available.
                </p>
              </div>
            )}

            {activePage === "Payments" && (
              <div className="transactions-card">
                <h2>Payments and transfers</h2>
                <p>
                  Payment processing is not connected yet. No money can be
                  transferred from this demo screen.
                </p>
              </div>
            )}

            {activePage === "Settings" && (
              <div className="transactions-card">
                <h2>Account settings</h2>
                <p>
                  <strong>Username:</strong> {username}
                </p>
                <p>
                  <strong>Full name:</strong> {name || "Not provided"}
                </p>
                <button className="login-button" onClick={logout}>
                  Log out
                </button>
              </div>
            )}
          </section>
        </main>

        <footer className="footer">
          <span>© 2026 PayPilot</span>
          <span>Simple. Secure. Smarter.</span>
        </footer>
      </div>
    );
  }

  const heading = otpStep
    ? "Verify your email"
    : forgotMode
    ? "Forgot password?"
    : resetMode
    ? "Reset your password"
    : register
    ? "Create your account"
    : "Sign in to PayPilot";

  const eyebrow = otpStep
    ? "EMAIL VERIFICATION"
    : forgotMode || resetMode
    ? "PASSWORD RECOVERY"
    : register
    ? "GET STARTED"
    : "WELCOME BACK";

  return (
    <div className="app">
      <header className="navbar">
        <a
          className="logo"
          href="/"
          onClick={(e) => {
            e.preventDefault();
            backToLogin();
          }}
        >
          PayPilot.
        </a>
        <span>Secure account access</span>
      </header>

      <main className="main-layout">
        <section className="intro">
          <p className="eyebrow">YOUR FINANCES, IN CONTROL</p>

          <h1>
            A smarter way
            <br />
            to manage your
            <br />
            <span>money.</span>
          </h1>

          <p>Welcome to a simpler financial experience.</p>

          <h3>✓ Secure verification</h3>
          <p>Verify your identity with email OTP.</p>
        </section>

        <section className="login-section">
          <div className="login-card">
            <p className="eyebrow">{eyebrow}</p>
            <h2>{heading}</h2>

            {otpStep ? (
              <form onSubmit={handleVerifyOtp}>
                <p>Enter the 6-digit OTP sent to your registered email.</p>

                <label htmlFor="otp">Email OTP</label>
                <input
                  id="otp"
                  required
                  type="text"
                  inputMode="numeric"
                  maxLength={6}
                  pattern="[0-9]{6}"
                  value={otp}
                  onChange={(e) =>
                    setOtp(e.target.value.replace(/\D/g, "").slice(0, 6))
                  }
                  placeholder="Enter 6-digit OTP"
                  autoComplete="one-time-code"
                />

                <button
                  className="login-button"
                  type="submit"
                  disabled={loading}
                >
                  {loading ? "Verifying..." : "Verify OTP →"}
                </button>

                <button
                  type="button"
                  className="register-button"
                  onClick={backToLogin}
                >
                  Back to sign in
                </button>
              </form>
            ) : forgotMode ? (
              <form onSubmit={handleForgotPassword}>
                <p>
                  Enter the email address linked to your approved PayPilot
                  account. We will send you a password-reset OTP.
                </p>

                <label htmlFor="resetEmail">Registered email</label>
                <input
                  id="resetEmail"
                  required
                  type="email"
                  autoComplete="email"
                  value={resetEmail}
                  onChange={(e) => setResetEmail(e.target.value)}
                  placeholder="Enter your registered email"
                />

                <button
                  className="login-button"
                  type="submit"
                  disabled={loading}
                >
                  {loading ? "Sending OTP..." : "Send reset OTP →"}
                </button>

                <button
                  type="button"
                  className="register-button"
                  onClick={backToLogin}
                >
                  Back to sign in
                </button>
              </form>
            ) : resetMode ? (
              <form onSubmit={handleResetPassword}>
                <p>
                  Enter your username, the six-digit OTP sent to your email,
                  and choose a new password.
                </p>

                <label htmlFor="resetUsername">Username</label>
                <input
                  id="resetUsername"
                  required
                  minLength={3}
                  maxLength={100}
                  autoComplete="username"
                  value={resetUsername}
                  onChange={(e) => setResetUsername(e.target.value)}
                  placeholder="Enter your username"
                />

                <label htmlFor="resetOtp">Password reset OTP</label>
                <input
                  id="resetOtp"
                  required
                  type="text"
                  inputMode="numeric"
                  pattern="[0-9]{6}"
                  maxLength={6}
                  autoComplete="one-time-code"
                  value={resetOtp}
                  onChange={(e) =>
                    setResetOtp(e.target.value.replace(/\D/g, "").slice(0, 6))
                  }
                  placeholder="Enter 6-digit OTP"
                />

                <label htmlFor="newPassword">New password</label>
                <input
                  id="newPassword"
                  required
                  type="password"
                  minLength={8}
                  maxLength={100}
                  autoComplete="new-password"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  placeholder="Enter new password"
                />

                <label htmlFor="confirmNewPassword">
                  Confirm new password
                </label>
                <input
                  id="confirmNewPassword"
                  required
                  type="password"
                  minLength={8}
                  maxLength={100}
                  autoComplete="new-password"
                  value={confirmNewPassword}
                  onChange={(e) => setConfirmNewPassword(e.target.value)}
                  placeholder="Re-enter new password"
                />

                <button
                  className="login-button"
                  type="submit"
                  disabled={loading}
                >
                  {loading ? "Resetting password..." : "Reset password →"}
                </button>

                <button
                  type="button"
                  className="register-button"
                  onClick={() => {
                    setResetMode(false);
                    setForgotMode(true);
                    setMessage("");
                  }}
                >
                  Back to forgot password
                </button>

                <button
                  type="button"
                  className="register-button"
                  onClick={backToLogin}
                >
                  Back to sign in
                </button>
              </form>
            ) : (
              <form onSubmit={register ? handleRegister : handleLogin}>
                {register && (
                  <>
                    <label htmlFor="fullName">Full name</label>
                    <input
                      id="fullName"
                      required
                      autoComplete="name"
                      value={name}
                      onChange={(e) => setName(e.target.value)}
                      placeholder="Enter your full name"
                    />

                    <label htmlFor="email">Email</label>
                    <input
                      id="email"
                      required
                      type="email"
                      autoComplete="email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      placeholder="Enter your email"
                    />
                  </>
                )}

                <label htmlFor="username">Username</label>
                <input
                  id="username"
                  required
                  minLength={3}
                  maxLength={100}
                  autoComplete="username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Enter your username"
                />

                <label htmlFor="password">Password</label>
                <input
                  id="password"
                  required
                  type="password"
                  minLength={8}
                  maxLength={100}
                  autoComplete={register ? "new-password" : "current-password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Enter your password"
                />

                {!register && (
                  <p className="forgot-password-link">
                    <button
                      type="button"
                      className="register-button"
                      onClick={openForgotPassword}
                    >
                      Forgot password?
                    </button>
                  </p>
                )}

                <button
                  className="login-button"
                  type="submit"
                  disabled={loading}
                >
                  {loading
                    ? "Please wait..."
                    : register
                    ? "Create account →"
                    : "Sign in →"}
                </button>
              </form>
            )}

            {message && (
              <p role="status" aria-live="polite">
                {message}
              </p>
            )}

            {!otpStep && !forgotMode && !resetMode && (
              <p>
                {register
                  ? "Already have an account? "
                  : "New to PayPilot? "}

                <button
                  type="button"
                  className="register-button"
                  onClick={switchMode}
                >
                  {register ? "Sign in" : "Create an account"}
                </button>
              </p>
            )}
          </div>
        </section>
      </main>

      <footer className="footer">
        <span>© 2026 PayPilot</span>
        <span>Simple. Secure. Smarter.</span>
      </footer>
    </div>
  );
}
