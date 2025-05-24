document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registration-form');

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        clearErrors();

        const userData = {
            username: document.getElementById('username').value.trim(),
            password: document.getElementById('password').value.trim(),
            name: document.getElementById('name').value.trim(),
            lastName: document.getElementById('lastName').value.trim(),
            email: document.getElementById('email').value.trim(),
            age: parseInt(document.getElementById('age').value, 10) || 0,
            roles: ["ROLE_USER"]
        };

        try {
            const response = await fetch('/api/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData)
            });

            if (response.ok) {

                window.location.href = '/login';
            } else if (response.status === 400) {

                const errorResponse = await response.json();


                if (Array.isArray(errorResponse)) {
                    errorResponse.forEach(err => {

                        const [field, ...msgParts] = err.split(':');
                        const message = msgParts.join(':').trim();
                        showFieldError(field.trim(), message);
                    });
                } else if (typeof errorResponse === 'object' && errorResponse !== null) {

                    Object.entries(errorResponse).forEach(([field, message]) => {
                        showFieldError(field, message);
                    });
                } else {
                    // Общая ошибка
                    showGeneralError('Registration error. Try again later');
                }
            } else {
                showGeneralError('Unknown server error');
            }
        } catch (error) {
            showGeneralError('Server connection error');
            console.error('Registration error', error);
        }
    });
});

function clearErrors() {
    document.getElementById('error-message').style.display = 'none';
    document.getElementById('error-message').textContent = '';
    ['username', 'password', 'name', 'lastName', 'email', 'age'].forEach(field => {
        const el = document.getElementById(`${field}-error`);
        if (el) el.textContent = '';
    });
}

function showFieldError(field, message) {
    const el = document.getElementById(`${field}-error`);
    if (el) {
        el.textContent = message;
    } else {
        // Если поле не найдено, показываем общее сообщение
        showGeneralError(message);
    }
}

function showGeneralError(message) {
    const errorDiv = document.getElementById('error-message');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
}

