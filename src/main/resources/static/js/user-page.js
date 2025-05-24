window.addEventListener('DOMContentLoaded', () => {
    fetch('/api/user')
        .then(response => response.json())
        .then(user => {

            const headerName = document.getElementById("headerName");
            if (headerName) headerName.textContent = user.name;

            const headerRoles = document.getElementById("headerRoles");
            if (headerRoles) {
                headerRoles.innerHTML = '';
                user.roles.forEach(role => {
                    const shortName = role.name.replace("ROLE_", "");
                    const span = document.createElement("span");
                    span.textContent = shortName;
                    span.className = "me-1";
                    headerRoles.appendChild(span);
                });
            }

            const idEl = document.getElementById("id");
            if (idEl) idEl.textContent = user.id;

            const nameEl = document.getElementById("name");
            if (nameEl) nameEl.textContent = user.name;

            const lastNameEl = document.getElementById("lastName");
            if (lastNameEl) lastNameEl.textContent = user.lastName;

            const emailEl = document.getElementById("email");
            if (emailEl) emailEl.textContent = user.email;

            const ageEl = document.getElementById("age");
            if (ageEl) ageEl.textContent = user.age;

            const roleEl = document.getElementById("role");
            if (roleEl) {
                roleEl.textContent = user.roles.map(role => role.name.replace("ROLE_", "")).join(', ');
            }

            const rolesEl = document.getElementById("roles");
            if (rolesEl) {
                rolesEl.innerHTML = '';
                user.roles.forEach(role => {
                    const span = document.createElement('span');
                    span.className = 'badge bg-secondary me-1';
                    span.textContent = role.name.replace('ROLE_', '');
                    rolesEl.appendChild(span);
                });
            }

            // Блок для администратора
            const isAdmin = user.roles.some(role => role.name === "ROLE_ADMIN");
            const adminInfo = document.getElementById("admin-info");
            if (isAdmin && adminInfo) {
                adminInfo.style.display = "block";
            }
        })
        .catch(error => console.error("User not found:", error));
});
