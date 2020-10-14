package com.whysly.alimseolap1.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private Integer confirm_passwordError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer confirm_passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.confirm_passwordError = confirm_passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.confirm_passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getConfirm_passwordError() {
        return confirm_passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}