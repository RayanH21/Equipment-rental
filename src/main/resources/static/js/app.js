function getCookie(name) {
    const m = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return m ? decodeURIComponent(m[2]) : null;
}

async function ensureCsrfCookie() {
    if (!getCookie('XSRF-TOKEN')) {
        await fetch('/api/csrf', { credentials: 'same-origin' });
    }
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

ensureCsrfCookie();