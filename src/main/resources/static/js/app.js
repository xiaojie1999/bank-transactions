// 全局变量
let currentPage = 0;
let pageSize = 10;
let totalItems = 0;
let searchQuery = '';

// DOM元素
const transactionsBody = document.getElementById('transactionsBody');
const prevPageBtn = document.getElementById('prevPage');
const nextPageBtn = document.getElementById('nextPage');
const pageInfoSpan = document.getElementById('pageInfo');
const pageSizeSelect = document.getElementById('pageSize');
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const transactionForm = document.getElementById('transactionForm');
const editTransactionModal = new bootstrap.Modal(document.getElementById('editTransactionModal'));
const editTransactionForm = document.getElementById('editTransactionForm');
const saveTransactionBtn = document.getElementById('saveTransactionBtn');

// 事件监听器
document.addEventListener('DOMContentLoaded', loadTransactions);
transactionForm.addEventListener('submit', createTransaction);
prevPageBtn.addEventListener('click', () => {
    if (currentPage > 0) {
        currentPage--;
        loadTransactions();
    }
});
nextPageBtn.addEventListener('click', () => {
    if ((currentPage + 1) * pageSize < totalItems) {
        currentPage++;
        loadTransactions();
    }
});
pageSizeSelect.addEventListener('change', () => {
    pageSize = parseInt(pageSizeSelect.value);
    currentPage = 0;
    loadTransactions();
});
searchBtn.addEventListener('click', () => {
    searchQuery = searchInput.value.trim();
    currentPage = 0;
    loadTransactions();
});
searchInput.addEventListener('keyup', (event) => {
    if (event.key === 'Enter') {
        searchBtn.click();
    }
});
saveTransactionBtn.addEventListener('click', updateTransaction);

// 函数
async function loadTransactions() {
    try {
        const params = new URLSearchParams({
            page: currentPage,
            size: pageSize
        }).toString();

        let url = `/api/transactions?${params}`;
        if (searchQuery) {
            url = `/api/transactions/search?query=${encodeURIComponent(searchQuery)}&${params}`;
        }

        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('加载交易失败');
        }

        const transactions = await response.json();
        totalItems = await getTotalTransactions();

        renderTransactions(transactions);
        updatePagination();
    } catch (error) {
        console.error('加载交易时出错:', error);
        alert('加载交易失败。请重试。');
    }
}

async function getTotalTransactions() {
    try {
        const response = await fetch('/api/transactions/count');
        if (!response.ok) {
            throw new Error('获取交易总数失败');
        }
        return await response.json();
    } catch (error) {
        console.error('获取交易总数时出错:', error);
        return 0;
    }
}

function renderTransactions(transactions) {
    transactionsBody.innerHTML = '';

    transactions.forEach(transaction => {
        const row = document.createElement('tr');

        // 格式化金额，保留两位小数
        const amount = parseFloat(transaction.amount).toFixed(2);

        row.innerHTML = `
            <td>${transaction.id}</td>
            <td>${transaction.accountId}</td>
            <td>$${amount}</td>
            <td>
                ${transaction.type === 'DEPOSIT' ? '存款' :
                 transaction.type === 'WITHDRAWAL' ? '取款' :
                 '转账'}
            </td>

            <td>${transaction.description || '-'}</td>
            <td>${new Date(transaction.timestamp).toLocaleString('zh-CN')}</td>
            <td>
                <button class="btn btn-sm btn-outline-primary edit-btn" data-id="${transaction.id}">编辑</button>
                <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${transaction.id}">删除</button>
            </td>
        `;

        transactionsBody.appendChild(row);
    });

    // 为编辑和删除按钮添加事件监听器
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const id = e.target.dataset.id;
            editTransaction(id);
        });
    });

    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const id = e.target.dataset.id;
            deleteTransaction(id);
        });
    });
}

function updatePagination() {
    const totalPages = Math.ceil(totalItems / pageSize);
    const currentPageNum = currentPage + 1;

    pageInfoSpan.textContent = `第 ${currentPageNum} 页，共 ${totalPages} 页`;

    prevPageBtn.disabled = currentPage === 0;
    nextPageBtn.disabled = (currentPage + 1) * pageSize >= totalItems;
}

async function createTransaction(e) {
    e.preventDefault();

    const transaction = {
        accountId: document.getElementById('accountId').value,
        amount: parseFloat(document.getElementById('amount').value),
        type: document.getElementById('type').value,
        description: document.getElementById('description').value
    };

    try {
        const response = await fetch('/api/transactions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(transaction)
        });

        if (!response.ok) {
            if (response.status === 409) {
                alert('交易已存在！');
            } else {
                alert('创建交易失败。请重试。');
            }
            return;
        }

        transactionForm.reset();
        loadTransactions();
        alert('交易创建成功！');
    } catch (error) {
        console.error('创建交易时出错:', error);
        alert('创建交易失败。请重试。');
    }
}

async function editTransaction(id) {
    try {
        const response = await fetch(`/api/transactions/${id}`);
        if (!response.ok) {
            throw new Error('加载交易失败');
        }

        const transaction = await response.json();

        // 填充表单字段
        document.getElementById('editTransactionId').value = transaction.id;
        document.getElementById('editAccountId').value = transaction.accountId;
        document.getElementById('editAmount').value = transaction.amount;
        document.getElementById('editType').value = transaction.type;
        document.getElementById('editDescription').value = transaction.description || '';

        // 显示模态框
        editTransactionModal.show();
    } catch (error) {
        console.error('加载交易编辑信息时出错:', error);
        alert('加载交易失败。请重试。');
    }
}

async function updateTransaction() {
    const id = document.getElementById('editTransactionId').value;
    const transaction = {
        accountId: document.getElementById('editAccountId').value,
        amount: parseFloat(document.getElementById('editAmount').value),
        type: document.getElementById('editType').value,
        description: document.getElementById('editDescription').value
    };

    try {
        const response = await fetch(`/api/transactions/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(transaction)
        });

        if (!response.ok) {
            alert('更新交易失败。请重试。');
            return;
        }

        editTransactionModal.hide();
        loadTransactions();
        alert('交易更新成功！');
    } catch (error) {
        console.error('更新交易时出错:', error);
        alert('更新交易失败。请重试。');
    }
}

async function deleteTransaction(id) {
    if (!confirm('您确定要删除此交易吗？')) {
        return;
    }

    try {
        const response = await fetch(`/api/transactions/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            alert('删除交易失败。请重试。');
            return;
        }

        loadTransactions();
        alert('交易删除成功！');
    } catch (error) {
        console.error('删除交易时出错:', error);
        alert('删除交易失败。请重试。');
    }
}