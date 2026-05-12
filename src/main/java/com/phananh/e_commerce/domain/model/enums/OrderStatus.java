package com.phananh.e_commerce.domain.model.enums;

public enum OrderStatus {
    PENDING,     // Chá» xÃ¡c nháº­n (KhÃ¡ch vá»«a báº¥m Ä‘áº·t hÃ ng)
    CONFIRMED,   // ÄÃ£ xÃ¡c nháº­n (Shop Ä‘Ã£ kiá»ƒm tra kho vÃ  cháº¥p nháº­n Ä‘Æ¡n)
    SHIPPING,    // Äang giao hÃ ng (ÄÃ£ giao cho Ä‘Æ¡n vá»‹ váº­n chuyá»ƒn)
    DELIVERED,   // ÄÃ£ giao hÃ ng thÃ nh cÃ´ng
    CANCELLED,   // ÄÃ£ há»§y (Bá»Ÿi khÃ¡ch hoáº·c bá»Ÿi Shop)
    RETURNED     // Tráº£ hÃ ng/HoÃ n tiá»n
}

