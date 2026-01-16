package com.hoangnm.cmsdemo.config;

import com.hoangnm.cmsdemo.entity.Order;
import com.hoangnm.cmsdemo.entity.OrderDetail;
import com.hoangnm.cmsdemo.entity.Product;
import com.hoangnm.cmsdemo.entity.User;
import com.hoangnm.cmsdemo.repository.OrderRepository;
import com.hoangnm.cmsdemo.repository.ProductRepository;
import com.hoangnm.cmsdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // ... create users ...
        }
        if (productRepository.count() == 0) {
            createProducts();
        }
        if (orderRepository.count() == 0) {
            // ... create sample orders ...
        }
    }

    private void createProducts() {
        List<Product> products = new ArrayList<>();
        // 10 Phones
        products.add(new Product(null, "iPhone 15 Pro Max 256GB", 32500000.0, "Titan tự nhiên, Chip A17 Pro.", "https://cdn.hoanghamobile.com/i/productlist/ts/1700471852.webp", "Phone"));
        products.add(new Product(null, "Samsung Galaxy S24 Ultra 512GB", 31990000.0, "Galaxy AI, Khung Titan, Bút S Pen.", "https://cdn.hoanghamobile.com/i/productlist/ts/1705559932.webp", "Phone"));
        products.add(new Product(null, "Google Pixel 8 Pro 128GB", 24500000.0, "Chip Google Tensor G3, Camera AI.", "https://cdn.hoanghamobile.com/i/productlist/ts/1696492584.webp", "Phone"));
        products.add(new Product(null, "iPhone 15 Plus 128GB", 24500000.0, "Màn hình lớn, Dynamic Island.", "https://cdn.hoanghamobile.com/i/productlist/ts/1694597334.webp", "Phone"));
        products.add(new Product(null, "Samsung Galaxy Z Fold5 256GB", 38990000.0, "Màn hình gập, đa nhiệm như PC.", "https://cdn.hoanghamobile.com/i/productlist/ts/1690364213.webp", "Phone"));
        products.add(new Product(null, "Xiaomi 14 Ultra", 29990000.0, "Hợp tác Leica, nhiếp ảnh di động.", "https://cdn.hoanghamobile.com/i/productlist/ts/1709264121.webp", "Phone"));
        products.add(new Product(null, "OPPO Find N3 Flip", 20990000.0, "Thiết kế gập vỏ sò, camera Hasselblad.", "https://cdn.hoanghamobile.com/i/productlist/ts/1697702829.webp", "Phone"));
        products.add(new Product(null, "iPhone 13 128GB", 15790000.0, "Chip A15 Bionic, giá tốt.", "https://cdn.hoanghamobile.com/i/productlist/ts/1631864220.webp", "Phone"));
        products.add(new Product(null, "Samsung Galaxy S23 FE", 12890000.0, "Hiệu năng mạnh mẽ, camera chất lượng.", "https://cdn.hoanghamobile.com/i/productlist/ts/1697253983.webp", "Phone"));
        products.add(new Product(null, "Realme GT5 Pro", 15500000.0, "Snapdragon 8 Gen 3, sạc siêu nhanh.", "https://cdn.hoanghamobile.com/i/productlist/ts/1702542987.webp", "Phone"));

        // 10 Headphones
        products.add(new Product(null, "AirPods Pro 2 (USB-C)", 5850000.0, "Chống ồn chủ động, Âm thanh không gian.", "https://cdn.hoanghamobile.com/i/productlist/ts/1694592535.webp", "Headphone"));
        products.add(new Product(null, "Sony WH-1000XM5", 7490000.0, "Chống ồn đỉnh cao, 8 microphones.", "https://cdn.hoanghamobile.com/i/productlist/ts/1653468826.webp", "Headphone"));
        products.add(new Product(null, "AirPods 3", 4390000.0, "Thiết kế mới, Spatial Audio.", "https://cdn.hoanghamobile.com/i/productlist/ts/1634631793.webp", "Headphone"));
        products.add(new Product(null, "Bose QuietComfort Ultra", 9990000.0, "Âm thanh Immersive, chống ồn đẳng cấp.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695027731.webp", "Headphone"));
        products.add(new Product(null, "Sennheiser Momentum 4", 8990000.0, "Thời lượng pin 60 giờ, âm thanh đặc trưng.", "https://cdn.hoanghamobile.com/i/productlist/ts/1661157989.webp", "Headphone"));
        products.add(new Product(null, "JBL Tour Pro 2", 5990000.0, "Hộp sạc thông minh, chống ồn thích ứng.", "https://cdn.hoanghamobile.com/i/productlist/ts/1684136829.webp", "Headphone"));
        products.add(new Product(null, "Beats Studio Buds +", 3990000.0, "Thiết kế trong suốt, tương thích đa nền tảng.", "https://cdn.hoanghamobile.com/i/productlist/ts/1686557829.webp", "Headphone"));
        products.add(new Product(null, "Nothing Ear (2)", 2990000.0, "Thiết kế độc đáo, chống ồn cá nhân hóa.", "https://cdn.hoanghamobile.com/i/productlist/ts/1679559836.webp", "Headphone"));
        products.add(new Product(null, "Galaxy Buds2 Pro", 3450000.0, "Âm thanh Hi-Fi 24bit, thiết kế công thái học.", "https://cdn.hoanghamobile.com/i/productlist/ts/1660123914.webp", "Headphone"));
        products.add(new Product(null, "Anker Soundcore Liberty 4 NC", 1990000.0, "Chống ồn hiệu quả, giá tốt.", "https://cdn.hoanghamobile.com/i/productlist/ts/1690184895.webp", "Headphone"));

        // 10 Watches
        products.add(new Product(null, "Apple Watch Series 9 45mm", 10490000.0, "Chip S9 SiP, Cảm biến nhiệt độ.", "https://cdn.hoanghamobile.com/i/productlist/ts/1694593324.webp", "Watch"));
        products.add(new Product(null, "Samsung Galaxy Watch6 Classic", 7490000.0, "Viền xoay vật lý, theo dõi sức khỏe.", "https://cdn.hoanghamobile.com/i/productlist/ts/1690363991.webp", "Watch"));
        products.add(new Product(null, "Apple Watch Ultra 2", 21490000.0, "Vỏ Titan, GPS tần số kép.", "https://cdn.hoanghamobile.com/i/productlist/ts/1694593560.webp", "Watch"));
        products.add(new Product(null, "Garmin Forerunner 265", 11690000.0, "Màn hình AMOLED, chuyên chạy bộ.", "https://cdn.hoanghamobile.com/i/productlist/ts/1677741362.webp", "Watch"));
        products.add(new Product(null, "Xiaomi Watch S3", 3690000.0, "Thay đổi viền bezel, HyperOS.", "https://cdn.hoanghamobile.com/i/productlist/ts/1709264213.webp", "Watch"));
        products.add(new Product(null, "Huawei Watch GT 4", 5490000.0, "Thiết kế bát giác, theo dõi calo.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695630327.webp", "Watch"));
        products.add(new Product(null, "Amazfit Balance", 5490000.0, "Phân tích thành phần cơ thể, Zepp OS 3.0.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695199066.webp", "Watch"));
        products.add(new Product(null, "Apple Watch SE 2023", 6390000.0, "Giá phải chăng, tính năng cần thiết.", "https://cdn.hoanghamobile.com/i/productlist/ts/1694593443.webp", "Watch"));
        products.add(new Product(null, "Samsung Galaxy Watch5 Pro", 6990000.0, "Pin lớn, khung Titan, GPS chuyên nghiệp.", "https://cdn.hoanghamobile.com/i/productlist/ts/1660123833.webp", "Watch"));
        products.add(new Product(null, "Garmin Instinct 2X Solar", 11290000.0, "Sạc năng lượng mặt trời, siêu bền.", "https://cdn.hoanghamobile.com/i/productlist/ts/1681201584.webp", "Watch"));

        // 10 Cases
        products.add(new Product(null, "Ốp lưng Spigen Ultra Hybrid S", 750000.0, "Trong suốt, chống sốc, có chân dựng.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695636052.webp", "Case"));
        products.add(new Product(null, "Ốp lưng UAG Monarch Pro", 2100000.0, "5 lớp bảo vệ, siêu bền, MagSafe.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695111160.webp", "Case"));
        products.add(new Product(null, "Ốp lưng Apple Silicone Case", 1450000.0, "Chất liệu mềm mại, chính hãng.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695094263.webp", "Case"));
        products.add(new Product(null, "Ốp lưng ZAGG Crystal Palace", 850000.0, "Chống sốc 4m, trong suốt không ố vàng.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695635882.webp", "Case"));
        products.add(new Product(null, "Ốp lưng LAUT Crystal Matter", 990000.0, "Thiết kế độc đáo, chống sốc tốt.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695635957.webp", "Case"));
        products.add(new Product(null, "Ốp lưng Spigen Thin Fit", 550000.0, "Thiết kế siêu mỏng, gọn nhẹ.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695636034.webp", "Case"));
        products.add(new Product(null, "Ốp lưng Samsung Smart View", 1290000.0, "Cửa sổ thông minh, xem thông báo.", "https://cdn.hoanghamobile.com/i/productlist/ts/1676364377.webp", "Case"));
        products.add(new Product(null, "Ốp lưng UAG Plyo", 1100000.0, "Chống sốc, trong suốt, viền dẻo.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695111142.webp", "Case"));
        products.add(new Product(null, "Ốp lưng Apple FineWoven", 1750000.0, "Chất liệu vải dệt mới, thân thiện môi trường.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695094247.webp", "Case"));
        products.add(new Product(null, "Ốp lưng ESR Classic Hybrid", 450000.0, "Trong suốt, giá tốt, có MagSafe.", "https://cdn.hoanghamobile.com/i/productlist/ts/1695635920.webp", "Case"));

        // 10 Chargers
        products.add(new Product(null, "Sạc Anker 30W Nano 3 (A2147)", 450000.0, "Siêu nhỏ gọn, sạc nhanh.", "https://cdn.hoanghamobile.com/i/productlist/ts/1663321522.webp", "Charger"));
        products.add(new Product(null, "Sạc Apple 20W USB-C", 550000.0, "Sạc nhanh chính hãng cho iPhone.", "https://cdn.hoanghamobile.com/i/productlist/ts/1603856942.webp", "Charger"));
        products.add(new Product(null, "Cáp Apple USB-C to Lightning", 490000.0, "Cáp chính hãng, độ dài 1m.", "https://cdn.hoanghamobile.com/i/productlist/ts/1603856863.webp", "Charger"));
        products.add(new Product(null, "Pin dự phòng Anker PowerCore 10000", 850000.0, "Dung lượng 10000mAh, nhỏ gọn.", "https://cdn.hoanghamobile.com/i/productlist/ts/1618386999.webp", "Charger"));
        products.add(new Product(null, "Sạc không dây Belkin 15W", 1290000.0, "Hỗ trợ sạc nhanh MagSafe.", "https://cdn.hoanghamobile.com/i/productlist/ts/1632731115.webp", "Charger"));
        products.add(new Product(null, "Sạc Anker 65W 3 cổng (735)", 1100000.0, "Sạc cùng lúc laptop, điện thoại.", "https://cdn.hoanghamobile.com/i/productlist/ts/1652345512.webp", "Charger"));
        products.add(new Product(null, "Sạc Samsung 45W", 990000.0, "Sạc siêu nhanh 2.0 cho điện thoại Samsung.", "https://cdn.hoanghamobile.com/i/productlist/ts/1644828033.webp", "Charger"));
        products.add(new Product(null, "Cáp Anker PowerLine III USB-C", 350000.0, "Bền bỉ, tốc độ cao.", "https://cdn.hoanghamobile.com/i/productlist/ts/1618386962.webp", "Charger"));
        products.add(new Product(null, "Pin Magsafe Apple Battery Pack", 2490000.0, "Pin dự phòng không dây chính hãng.", "https://cdn.hoanghamobile.com/i/productlist/ts/1626333983.webp", "Charger"));
        products.add(new Product(null, "Sạc Innostyle Minigo 20W", 290000.0, "Giá rẻ, sạc nhanh, nhỏ gọn.", "https://cdn.hoanghamobile.com/i/productlist/ts/1632731086.webp", "Charger"));
        
        productRepository.saveAll(products);
    }

    private void createSampleOrder(User user, List<Product> products, List<Integer> quantities, LocalDateTime orderTime, String status) {
        // ...
    }
}
