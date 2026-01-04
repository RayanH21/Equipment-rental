function getCookie(name) {
    const m = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return m ? decodeURIComponent(m[2]) : null;
}

let csrfInitPromise = null;

async function ensureCsrfCookie() {
    if (getCookie('XSRF-TOKEN')) return;

    if (!csrfInitPromise) {
        csrfInitPromise = fetch('/api/csrf', {
            credentials: 'same-origin',
            cache: 'no-store'
        }).finally(() => {
            // reset zodat je later opnieuw kan proberen als het ooit faalt
            csrfInitPromise = null;
        });
    }

    await csrfInitPromise;
}

async function csrfFetch(url, options = {}) {
    await ensureCsrfCookie();

    const token = getCookie('XSRF-TOKEN');
    const headers = options.headers ? { ...options.headers } : {};
    if (token) headers['X-XSRF-TOKEN'] = token;

    let resp = await fetch(url, { ...options, headers, credentials: 'same-origin' });

    // Als 403: forceer cookie-init opnieuw en retry 1x
    if (resp.status === 403) {
        await ensureCsrfCookie();

        const token2 = getCookie('XSRF-TOKEN');
        const headers2 = options.headers ? { ...options.headers } : {};
        if (token2) headers2['X-XSRF-TOKEN'] = token2;

        resp = await fetch(url, { ...options, headers: headers2, credentials: 'same-origin' });
    }

    return resp;
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