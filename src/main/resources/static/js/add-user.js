async function loadRolesIntoForm() {
    const rolesSelect = document.getElementById('roles');

    try {
        const res = await fetch('/api/users/roles');
        if (!res.ok) throw new Error('Failed to fetch roles');
        const roles = await res.json();

        rolesSelect.innerHTML = ''; // Очищаю список перед добавлением

        roles.forEach(role => {
            const option = document.createElement('option');
            option.value = role.id;
            option.textContent = role.name.replace('ROLE_', '');
            rolesSelect.appendChild(option);
        });
    } catch (err) {
        console.error('Ошибка загрузки ролей:', err);
        rolesSelect.innerHTML = '<option disabled>Ошибка загрузки ролей</option>';
    }
}

async function loadAddUserForm() {
    const container = document.getElementById('add-user-container');

    const response = await fetch('fragments/userForm.html');
    const formHtml = await response.text();

    container.innerHTML = formHtml;

    //  DOM
    await loadRolesIntoForm();

    const form = document.getElementById('userForm');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const user = {
            name: form.name.value,
            lastName: form.lastName.value,
            email: form.email.value,
            age: parseInt(form.age.value, 10),
            username: form.username.value,
            password: form.password.value,
            roles: Array.from(form.roles.selectedOptions).map(opt => ({ id: parseInt(opt.value, 10) }))
        };


        const res = await fetch('/api/users', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(user)
        });

        if (res.ok) {
            alert('User added successfully!');
            form.reset();
            loadUsers();
        } else {
            alert('Error adding user.');
        }
    });
}
