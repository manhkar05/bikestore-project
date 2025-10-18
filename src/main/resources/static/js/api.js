// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// API Helper Functions
class ApiService {
    static async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        try {
            const response = await fetch(url, config);
            const data = await response.json().catch(() => ({})); // Get JSON or empty object on parsing error

            if (!response.ok) {
                const message = data.message || `HTTP error! status: ${response.status}`;
                const error = new Error(message);
                error.status = response.status;
                throw error;
            }

            return data;
        } catch (error) {
            console.error('API request failed:', error);
            throw error;
        }
    }

    // Product APIs
    static async getProducts() {
        return this.request('/products');
    }

    static async getProductById(id) {
        return this.request(`/products/${id}`);
    }

    static async getProductsByCategory(category) {
        return this.request(`/products/category/${category}`);
    }

    static async getProductsByBrand(brand) {
        return this.request(`/products/brand/${brand}`);
    }

    static async searchProducts(query) {
        return this.request(`/products/search?name=${encodeURIComponent(query)}`);
    }

    static async getProductsByPriceRange(minPrice, maxPrice) {
        return this.request(`/products/price?minPrice=${minPrice}&maxPrice=${maxPrice}`);
    }

    // User APIs
    static async registerUser(userData) {
        return this.request('/users/register', {
            method: 'POST',
            body: JSON.stringify(userData)
        });
    }

    static async loginUser(email, password) {
        return this.request('/users/login', {
            method: 'POST',
            body: JSON.stringify({
                email,
                password
            })
        });
    }

    static async getUserById(id) {
        return this.request(`/users/${id}`);
    }

    static async updateUser(id, userData) {
        return this.request(`/users/${id}`, {
            method: 'PUT',
            body: JSON.stringify(userData)
        });
    }

    static async getUserOrders(userId) {
        return this.request(`/users/${userId}/orders`);
    }

    static async getUserTransactions(userId) {
        return this.request(`/users/${userId}/transactions`);
    }

    static async addToWishlist(userId, productId) {
        return this.request(`/users/${userId}/wishlist/${productId}`, {
            method: 'POST'
        });
    }

    static async removeFromWishlist(userId, productId) {
        return this.request(`/users/${userId}/wishlist/${productId}`, {
            method: 'DELETE'
        });
    }

    // Order APIs
    static async createOrder(orderData) {
        return this.request('/orders', {
            method: 'POST',
            body: JSON.stringify(orderData)
        });
    }

    static async getOrdersByUserId(userId) {
        return this.request(`/orders/user/${userId}`);
    }

    static async getAllOrders() {
        return this.request('/orders');
    }

    static async getOrderById(id) {
        return this.request(`/orders/${id}`);
    }

    static async updateOrderStatus(id, status) {
        return this.request(`/orders/${id}/status?status=${status}`, {
            method: 'PUT'
        });
    }

    static async getOrdersByStatus(status) {
        return this.request(`/orders/status/${status}`);
    }

    // Cart APIs (if needed)
    // Cart APIs (server-backed helpers)
    // Note: backend exposes /api/carts/{userId} (GET) and POST /api/carts to save
    static async getCart(userId) {
        return this.request(`/carts/${encodeURIComponent(userId)}`);
    }

    static async saveCart(cart) {
        // cart should include userId, items
        return this.request('/carts', {
            method: 'POST',
            body: JSON.stringify(cart)
        });
    }

    // Helper: determine current user or guest id via cookie
    static getCookie(name) {
        const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
        if (match) return decodeURIComponent(match[2]);
        return null;
    }

    static setCookie(name, value, days = 7) {
        const expires = new Date(Date.now() + days * 864e5).toUTCString();
        document.cookie = `${name}=${encodeURIComponent(value)}; expires=${expires}; path=/`;
    }

    static getCurrentUserId() {
        return this.getCookie('currentUserId');
    }

    static setCurrentUserId(id) {
        this.setCookie('currentUserId', id, 7);
    }

    static getGuestId() {
        let guest = this.getCookie('guestId');
        if (!guest) {
            guest = 'GUEST_' + Date.now();
            this.setCookie('guestId', guest, 7);
        }
        return guest;
    }

    static async getCartServer() {
        const userId = this.getCurrentUserId() || this.getGuestId();
        try {
            const cart = await this.getCart(userId);
            return cart;
        } catch (e) {
            // If no cart exists, return empty structure
            return { id: null, userId: userId, items: [] };
        }
    }

    static async saveCartServer(cart) {
        // Ensure cart has userId
        if (!cart.userId) {
            cart.userId = this.getCurrentUserId() || this.getGuestId();
        }

        // Ensure cart items have both id and productId for compatibility
        if (cart.items) {
            cart.items = cart.items.map(item => {
                if (item.id && !item.productId) {
                    item.productId = item.id;
                } else if (item.productId && !item.id) {
                    item.id = item.productId;
                }
                return item;
            });
        }

        return this.saveCart(cart);
    }

    // Dashboard APIs
    static async getOrderDashboard() {
        return this.request('/dashboard/orders');
    }

    static async getCustomerDashboard() {
        return this.request('/dashboard/customers');
    }

    static async getRevenueDashboard() {
        return this.request('/dashboard/revenue');
    }

    static async getSalesPerformanceDashboard() {
        return this.request('/dashboard/sales-performance');
    }

    static async getMarketingDashboard() {
        return this.request('/dashboard/marketing');
    }

    static async getFinanceDashboard() {
        return this.request('/dashboard/finance');
    }

    static async getProductManagementDashboard() {
        return this.request('/dashboard/products');
    }
}

// Utility Functions
class Utils {
    static formatPrice(price) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(price);
    }

    static formatDate(date) {
        return new Date(date).toLocaleDateString('vi-VN');
    }

    static showNotification(message, type = 'success') {
        // Provide a safe default when message is missing/empty to avoid showing "undefined"
        if (message === undefined || message === null || message === '') {
            message = 'Cảm ơn đã gửi';
        }

        // Debug: log caller and message so we can confirm this implementation is used at runtime
        try { console.debug('Utils.showNotification (api.js) called', { message, type }); } catch (e) { /* ignore */ }

        // Create notification element
        const notification = document.createElement('div');
        const bsType = (type === 'success') ? 'success' : 'danger';
        notification.className = `alert alert-${bsType} alert-dismissible fade show position-fixed`;
        notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
        notification.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;

        document.body.appendChild(notification);

        // Auto remove after 2 seconds
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 2000);
    }

    static showLoading(element) {
        if (element) {
            element.innerHTML = '<div class="text-center"><div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div></div>';
        }
    }

    static hideLoading(element, content) {
        if (element) {
            element.innerHTML = content;
        }
    }
}

// Export for use in other files
window.ApiService = ApiService;
window.Utils = Utils;

// Admin helper for frontend (server-backed)
class AdminAuth {
    // Returns true if an admin is logged in (checks cookie and server)
    static async checkAdminAccess() {
        const adminId = ApiService.getCookie('currentAdminId');
        if (!adminId) return false;
        try {
            const user = await ApiService.getUserById(adminId);
            return user && user.role === 'admin';
        } catch (e) {
            console.error('Admin access check failed:', e);
            return false;
        }
    }

    // Returns admin user object or null
    static async getCurrentAdmin() {
        const adminId = ApiService.getCookie('currentAdminId');
        if (!adminId) return null;
        try {
            const user = await ApiService.getUserById(adminId);
            return user;
        } catch (e) {
            console.error('Could not load current admin:', e);
            return null;
        }
    }

    // Store admin info in cookie (ui-level only)
    static setCurrentAdmin(user) {
        if (!user) return;
        if (user.id) ApiService.setCookie('currentAdminId', user.id, 1);
        try { ApiService.setCookie('currentAdmin', JSON.stringify(user), 1); } catch (e) { /* ignore */ }
    }

    static clear() {
        ApiService.setCookie('currentAdminId', '', -1);
        ApiService.setCookie('currentAdmin', '', -1);
    }
}

window.AdminAuth = AdminAuth;

// Additional API wrappers used by dashboard
ApiService.getAllUsers = async function() {
    return ApiService.request('/users');
};

// Return all orders (raw list)
ApiService.getAllOrders = async function() {
    return ApiService.request('/orders');
};

// Return all products
ApiService.getProducts = async function() {
    return ApiService.request('/products');
};

ApiService.addProduct = async function(productData) {
    return ApiService.request('/products', {
        method: 'POST',
        body: JSON.stringify(productData)
    });
};

ApiService.updateProduct = async function(id, productData) {
    return ApiService.request(`/products/${id}`, {
        method: 'PUT',
        body: JSON.stringify(productData)
    });
};

ApiService.deleteProduct = async function(id) {
    return ApiService.request(`/products/${id}`, {
        method: 'DELETE'
    });
};