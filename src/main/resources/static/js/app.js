function getCookie(name) {
    const m = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return m ? decodeURIComponent(m[2]) : null;
}

let csrfInitPromise = null;

async function ensureCsrfCookie() {
    if (getCookie('XSRF-TOKEN')) return;

    // voorkom race: maar 1 init-call tegelijk
    if (!csrfInitPromise) {
        csrfInitPromise = fetch('/api/csrf', {
            credentials: 'same-origin',
            cache: 'no-store'
        }).finally(() => {
            // laat de promise staan; cookie blijft toch geldig voor de sessie
        });
    }

    await csrfInitPromise;
}

async function csrfFetch(url, options = {}) {
    await ensureCsrfCookie();

    const token = getCookie('XSRF-TOKEN');
    const headers = options.headers ? { ...options.headers } : {};
    if (token) headers['X-XSRF-TOKEN'] = token;

    return fetch(url, { ...options, headers, credentials: 'same-origin' });
}

async function logout() {
    await csrfFetch('/api/auth/logout', { method: 'POST' });
    window.location.href = '/login';
}

document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('logoutBtn');
    if (btn) {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            logout();
        });
    }
});

// Seed meteen bij pageload (zodat eerste klik nooit 403 is)
ensureCsrfCookie();