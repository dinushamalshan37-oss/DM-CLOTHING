const API_URL = "http://localhost:8080/api";

let allProducts = [];
let selectedProduct = null;

loadProducts();

function loadProducts() {
    fetch(`${API_URL}/products`)
        .then(res => res.json())
        .then(products => {
            allProducts = products;
            displayProducts(products);
        })
        .catch(err => {
            console.error(err);
            alert("Products load error");
        });
}

function displayProducts(products) {
    const box = document.getElementById("products");
    const count = document.getElementById("productCount");

    box.innerHTML = "";
    count.innerText = `${products.length} products`;

    if (products.length === 0) {
        box.innerHTML = `<p class="empty-text">No products available.</p>`;
        return;
    }

    products.forEach(product => {
        const colors = product.colors ? product.colors.split(",") : ["Black"];
        const firstColor = colors[0].trim();

        box.innerHTML += `
            <div class="product-card" onclick="openProduct(${product.productId})">
                <div class="image-wrap">
                    <img src="${product.image}" 
                         alt="${product.productName}"
                         onerror="this.src='https://placehold.co/800x1000/f2f2f2/111?text=DM+CLOTHING'">
                </div>

                <div class="swatches">
                    ${colors.map(c => `<span style="background:${getColor(c.trim())}"></span>`).join("")}
                </div>

                <h3>${product.productName}</h3>
                <p>${firstColor}</p>
                <h4>LKR ${Number(product.price).toLocaleString()}.00</h4>
            </div>
        `;
    });
}

function getColor(color) {
    const c = color.toLowerCase();

    if (c.includes("black")) return "#000";
    if (c.includes("white")) return "#fff";
    if (c.includes("blue")) return "#2563eb";
    if (c.includes("gray") || c.includes("grey")) return "#9ca3af";
    if (c.includes("pink")) return "#ec4899";
    if (c.includes("cream")) return "#f5ead5";
    if (c.includes("red")) return "#dc2626";
    if (c.includes("green")) return "#16a34a";

    return "#ddd";
}

function filterProducts(category) {
    if (category === "all") {
        displayProducts(allProducts);
        return;
    }

    const filtered = allProducts.filter(p =>
        p.category && p.category.toLowerCase() === category.toLowerCase()
    );

    displayProducts(filtered);
}

function sortProducts(type) {
    let sorted = [...allProducts];

    if (type === "low") {
        sorted.sort((a, b) => a.price - b.price);
    }

    if (type === "high") {
        sorted.sort((a, b) => b.price - a.price);
    }

    displayProducts(sorted);
}

function searchProducts() {
    const keyword = prompt("Search product");

    if (!keyword) return;

    const result = allProducts.filter(p =>
        p.productName.toLowerCase().includes(keyword.toLowerCase()) ||
        p.category.toLowerCase().includes(keyword.toLowerCase())
    );

    displayProducts(result);
}

function openProduct(productId) {
    selectedProduct = allProducts.find(p => p.productId === productId);

    if (!selectedProduct) return;

    document.getElementById("modalImage").src = selectedProduct.image;
    document.getElementById("modalName").innerText = selectedProduct.productName;
    document.getElementById("modalCategory").innerText = selectedProduct.category;
    document.getElementById("modalPrice").innerText = `LKR ${Number(selectedProduct.price).toLocaleString()}.00`;

    const colors = selectedProduct.colors ? selectedProduct.colors.split(",") : ["Black"];
    const sizes = selectedProduct.size ? selectedProduct.size.split(",") : ["M"];

    document.getElementById("modalColor").innerHTML =
        colors.map(c => `<option value="${c.trim()}">${c.trim()}</option>`).join("");

    document.getElementById("modalSize").innerHTML =
        sizes.map(s => `<option value="${s.trim()}">${s.trim()}</option>`).join("");

    document.getElementById("modalQty").value = 1;
    document.getElementById("productModal").classList.remove("hidden");
}

function closeProduct() {
    document.getElementById("productModal").classList.add("hidden");
}

function addModalToCart() {
    const customerId = localStorage.getItem("customerId");

    if (!customerId) {
        alert("Please login first");
        window.location.href = "login.html";
        return;
    }

    const data = {
        customerId: Number(customerId),
        productId: selectedProduct.productId,
        quantity: Number(document.getElementById("modalQty").value),
        selectedColor: document.getElementById("modalColor").value,
        selectedSize: document.getElementById("modalSize").value
    };

    fetch(`${API_URL}/cart/add`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    })
    .then(res => res.text())
    .then(msg => {
        alert(msg);
        closeProduct();
    });
}

function logout() {
    localStorage.removeItem("customerId");
    alert("Logout successful");
    window.location.href = "login.html";
}