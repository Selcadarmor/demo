// Глобальная переменная ролей
window.allRoles = window.allRoles || [];
let allRoles = window.allRoles;

async function loadAllRoles(selected = []) {
    try {
        const res = await fetch('/api/users/roles');
        if (!res.ok) {
            alert('Failed to load roles');
            return [];
        }
        const roles = await res.json();

        const roleSelect = document.getElementById('editRoles');
        roleSelect.innerHTML = '';

        roles.forEach(role => {
            const option = document.createElement('option');
            option.value = String(role.id);
            option.textContent = role.name.replace('ROLE_', '');
            if (selected.some(sel => sel.id === role.id)) {
                option.selected = true;
            }
            roleSelect.appendChild(option);
        });

        // Обновляем глобальный список ролей
        window.allRoles.splice(0, window.allRoles.length, ...roles);

        return roles;
    } catch (error) {
        alert('Error loading roles: ' + error.message);
        return [];
    }
}

async function openEditModal(id) {
    try {
        const res = await fetch(`/api/users/${id}`);
        if (!res.ok) {
            alert('Failed to load user data');
            return;
        }
        const user = await res.json();

        document.getElementById('editUserId').value = user.id;
        document.getElementById('editId').value = user.id;
        document.getElementById('editName').value = user.name;
        document.getElementById('editLastName').value = user.lastName;
        document.getElementById('editEmail').value = user.email;
        document.getElementById('editAge').value = user.age;

        await loadAllRoles(user.roles);

        const modal = new bootstrap.Modal(document.getElementById('editUserModal'));
        modal.show();
    } catch (error) {
        alert('Error opening modal: ' + error.message);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('editUserForm');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const id = document.getElementById('editUserId').value;

        const selectedRoleIds = Array.from(document.getElementById('editRoles').selectedOptions)
            .map(option => Number(option.value));

        const selectedRoles = allRoles.filter(role => selectedRoleIds.includes(role.id))
            .map(role => ({ id: role.id, name: role.name }));

        const updatedUser = {
            id: Number(id),
            name: document.getElementById('editName').value.trim(),
            lastName: document.getElementById('editLastName').value.trim(),
            email: document.getElementById('editEmail').value.trim(),
            age: Number(document.getElementById('editAge').value),
            roles: selectedRoles
        };

        try {
            const res = await fetch(`/api/users/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedUser)
            });

            if (!res.ok) {
                const errorText = await res.text();
                throw new Error(`Failed to update user: ${res.status} ${res.statusText}\n${errorText}`);
            }

            const modal = bootstrap.Modal.getInstance(document.getElementById('editUserModal'));
            modal.hide();

            if (typeof loadUsers === 'function') {
                loadUsers();
            }
        } catch (error) {
            alert('Error: ' + error.message);
            console.error(error);
        }
    });
});
