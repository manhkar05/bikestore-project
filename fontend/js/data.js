// DashboardData helpers used by dashboard.html
// These helpers read from window.dashboardApp.data when available
const DashboardData = {
    getRevenueStats() {
        const orders = (window.dashboardApp && window.dashboardApp.data && window.dashboardApp.data.orders) || [];
        const delivered = orders.filter(o => o.status === 'delivered');
        const totalRevenue = delivered.reduce((s, o) => s + (o.totalPrice || 0), 0);
        const avgOrderValue = delivered.length ? totalRevenue / delivered.length : 0;
        const conversionRate = orders.length ? (delivered.length / orders.length) * 100 : 0;
        return {
            totalRevenue,
            avgOrderValue,
            conversionRate
        };
    },

    getTopProducts() {
        const products = (window.dashboardApp && window.dashboardApp.data && window.dashboardApp.data.products) || [];
        // naive top by sold count if present, otherwise by lowest stock
        const top = products.slice().sort((a,b) => (b.sold || 0) - (a.sold || 0)).slice(0,10);
        return top;
    },

    getLowStockProducts() {
        const products = (window.dashboardApp && window.dashboardApp.data && window.dashboardApp.data.products) || [];
        return products.filter(p => (p.stock || 0) <= 5).slice(0, 20);
    }
};

window.DashboardData = DashboardData;
