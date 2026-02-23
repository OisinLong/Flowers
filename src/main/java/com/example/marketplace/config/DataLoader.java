package com.example.marketplace.config;

import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final ProductRepository productRepo;

    public DataLoader(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    /*
     Runs automatically when Spring finishes starting up.
     */
    @PostConstruct
    public void loadInitialData() {

        // Prevent duplicate inserts on restart
        if (productRepo.count() > 0) {
            return;
        }

        productRepo.save(new Product(
                "Rose",
                "This elegant rose features velvety petals in deep crimson red. Perfect for romantic occasions or home decor, each stem is carefully selected for quality and freshness. The classic beauty of roses makes them ideal for bouquets, centerpieces, or standalone arrangements. These blooms typically last 7-10 days with proper care, including regular water changes and stem trimming for optimal longevity.",
                4.99,
                "/images/Rose.png"
        ));

        productRepo.save(new Product(
                "Tulip",
                "These vibrant tulips bring a touch of spring to any setting with their smooth, cup-shaped blooms. Available in a rainbow of colors including red, yellow, pink, and purple, they're perfect for brightening up homes and offices. Tulips are known for their graceful stems and ability to continue growing in the vase. With minimal care requirements, they offer lasting beauty for approximately one week.",
                3.49,
                "/images/Tulip.png"
        ));

        productRepo.save(new Product(
                "Sunflower",
                "Bold and cheerful sunflowers feature large golden-yellow petals surrounding a dark center disc packed with seeds. These impressive blooms can reach up to 12 inches in diameter and symbolize happiness and vitality. Ideal for creating dramatic displays or adding warmth to casual arrangements, sunflowers are surprisingly long-lasting and bring instant sunshine to any room with their towering presence and radiant color.",
                5.99,
                "/images/Sunflower.png"
        ));

        productRepo.save(new Product(
                "Lily",
                "Elegant white lilies showcase pristine petals and a captivating sweet fragrance that fills any room. These sophisticated blooms are perfect for weddings, sympathy arrangements, or adding elegance to home decor. Each stem produces multiple large flowers that open gradually, providing extended enjoyment. With proper care including removing pollen-heavy stamens, lilies can last up to two weeks, making them excellent value.",
                6.49,
                "/images/Lily.png"
        ));

        productRepo.save(new Product(
                "Orchid",
                "This exotic purple orchid plant arrives in an elegant decorative pot, ready to enhance any space with its sophisticated beauty. Known for their long-lasting blooms that can persist for months, orchids require minimal maintenance with weekly watering. The graceful arching stems display multiple delicate flowers with intricate patterns. Perfect for homes or offices, this living gift continues to reward with stunning blooms season after season.",
                24.99,
                "/images/Orchid.png"
        ));

        productRepo.save(new Product(
                "Daisy",
                "Simple yet charming white daisies feature pristine petals radiating from bright yellow centers, embodying natural innocence and purity. These cheerful flowers are versatile additions to casual bouquets or wildflower arrangements. Hardy and easy to care for, daisies maintain their fresh appearance for up to ten days. Their unpretentious beauty makes them perfect for everyday enjoyment or as cheerful gifts that bring smiles to recipients.",
                2.99,
                "/images/Daisy.png"
        ));

        productRepo.save(new Product(
                "Carnation",
                "Ruffled pink carnations display layers of delicate, frilled petals with a subtle spicy-sweet fragrance. These long-lasting blooms are among the most durable cut flowers available, often remaining fresh for two to three weeks with proper care. Perfect for mixed arrangements or standalone displays, carnations symbolize love and admiration. Their sturdy stems and resilient nature make them ideal for both home enjoyment and special events.",
                3.99,
                "/images/Canation.png"
        ));

        productRepo.save(new Product(
                "Peony",
                "Luxurious pink peonies feature incredibly full, rounded blooms with dozens of soft, ruffled petals and an intoxicating sweet fragrance. These highly sought-after flowers symbolize romance, prosperity, and good fortune. Available seasonally, peonies are prized for their lush appearance and impressive size. Though their vase life is approximately one week, their spectacular beauty and enchanting scent make them worth every moment of enjoyment.",
                7.99,
                "/images/Peony.png"
        ));

        productRepo.save(new Product(
                "Lavender",
                "Fragrant purple lavender stems offer both visual beauty and therapeutic aromatherapy benefits with their calming, distinctive scent. These versatile flowers can be enjoyed fresh in vases or dried for long-lasting use in sachets and crafts. Known for promoting relaxation and stress relief, lavender adds a touch of Provence to any setting. The slender stems topped with tiny purple florets create elegant, textured arrangements perfect for rustic or cottage-style decor.",
                4.49,
                "/images/Lavender.png"
        ));

        productRepo.save(new Product(
                "Hydrangea",
                "Magnificent blue hydrangea blooms consist of clusters of smaller florets forming large, showy flower heads up to 8 inches across. These impressive flowers make bold statements in arrangements and are perfect as standalone displays. Hydrangeas are surprisingly versatile, transitioning beautifully from fresh to dried flowers. With proper hydration and care, they provide approximately one week of stunning beauty, adding volume and color to any floral design.",
                8.99,
                "/images/Hydagea.png"
        ));

        productRepo.save(new Product(
                "Dahlia",
                "Striking orange dahlias feature intricate layers of pointed petals arranged in perfect geometric patterns, creating mesmerizing depth and dimension. These show-stopping blooms range from compact pompoms to dinner-plate sized flowers. Dahlias symbolize elegance and dignity while their bold colors add dramatic flair to arrangements. Though somewhat delicate, with attentive care including daily water changes, they reward with approximately five to seven days of spectacular beauty.",
                6.99,
                "/images/Dahlia.png"
        ));

        productRepo.save(new Product(
                "Chrysanthemum",
                "Cheerful yellow chrysanthemums display pompom-style blooms composed of numerous tightly-packed petals creating full, rounded flowers. These hardy blooms are celebrated for exceptional longevity, often lasting two to three weeks in proper conditions. Chrysanthemums symbolize optimism and joy across many cultures. Available in various sizes from button mums to large exhibition varieties, they're perfect for adding lasting color to both casual and formal arrangements throughout autumn seasons.",
                5.49,
                "/images/Chrysanthumm.png"
        ));
    }
}
