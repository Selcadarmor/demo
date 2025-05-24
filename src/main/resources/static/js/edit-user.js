// Инициализация глобального массива ролей, если еще не создан
window.allRoles = window.allRoles || [];

// Загрузка всех пользователей и заполнение таблицы
async function loadUsers() {
    try {
        const response = await fetch('/api/users');
        if (!response.ok) throw new Error('Failed to load users');
        const users = await response.json();

        const tbody = document.getElementById('userTableBody');
        tbody.innerHTML = '';

        users.forEach(user => {
            const rolesText = (user.roles || [])
                .map(role => role.name?.replace('ROLE_', '') || '')
                .join(', ');

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td>
                <td>${user.age}</td>
                <td>${rolesText}</td>
                <td>
                    <button class="btn btn-sm btn-primary me-1" onclick="openEditModal(${user.id})">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.id})">Delete</button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        alert(error.message);
        console.error(error);
    }
}

// Удаление пользователя
async function deleteUser(id) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    try {
        const response = await fetch(`/api/users/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Failed to delete user');
        await loadUsers();
    } catch (error) {
        alert(error.message);
        console.error(error);
    }
}

// Загрузка всех ролей и заполнение выпадающего списка
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
            const roleName = role.name?.replace('ROLE_', '') || '';
            const option = document.createElement('option');
            option.value = role.id;
            option.textContent = roleName;
            if (selected.some(sel => sel.id === role.id)) {
                option.selected = true;
            }
            roleSelect.appendChild(option);
        });

        // Обновляем глобальный массив ролей
        window.allRoles.splice(0, window.allRoles.length, ...roles);
        return roles;
    } catch (error) {
        alert('Error loading roles: ' + error.message);
        console.error(error);
        return [];
    }
}

// Открытие модального окна редактирования пользователя
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
        document.getElementById('editName').value = user.name || '';
        document.getElementById('editLastName').value = user.lastName || '';
        document.getElementById('editEmail').value = user.email || '';
        document.getElementById('editAge').value = user.age || '';

        await loadAllRoles(user.roles || []);

        const modal = new bootstrap.Modal(document.getElementById('editUserModal'));
        modal.show();
    } catch (error) {
        alert('Error opening modal: ' + error.message);
        console.error(error);
    }
}

// Обработка формы редактирования пользователя
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('editUserForm');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const id = Number(document.getElementById('editUserId').value);
        const selectedRoleIds = Array.from(document.getElementById('editRoles').selectedOptions)
            .map(option => Number(option.value));

        const selectedRoles = window.allRoles
            .filter(role => selectedRoleIds.includes(role.id))
            .map(role => ({ id: role.id, name: role.name }));

        const updatedUser = {
            id,
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

            await loadUsers();
        } catch (error) {
            alert('Network or unexpected error: ' + error.message);
            console.error(error);
        }
    });

    // Загружаем пользователей после загрузки страницы
    loadUsers();
});
