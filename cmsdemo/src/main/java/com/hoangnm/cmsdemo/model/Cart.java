package com.hoangnm.cmsdemo.model;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class Cart {
    private Map<Long, CartItem> items = new HashMap<>();

    // Thêm sản phẩm vào giỏ hoặc tăng số lượng
    public void addItem(CartItem item) {
        CartItem existingItem = items.get(item.getProduct().getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            items.put(item.getProduct().getId(), item);
        }
    }

    // Xóa sản phẩm khỏi giỏ
    public void removeItem(Long productId) {
        items.remove(productId);
    }

    // Cập nhật số lượng
    public void updateItem(Long productId, int quantity) {
        CartItem item = items.get(productId);
        if (item != null) {
            if (quantity > 0) {
                item.setQuantity(quantity);
            } else {
                items.remove(productId);
            }
        }
    }

    // Lấy tổng số lượng sản phẩm
    public int getTotalItems() {
        return items.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    // Lấy tổng tiền
    public double getTotalPrice() {
        return items.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    // Xóa toàn bộ giỏ hàng
    public void clear() {
        items.clear();
    }
}
