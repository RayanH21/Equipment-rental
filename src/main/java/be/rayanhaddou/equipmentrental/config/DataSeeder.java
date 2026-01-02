package be.rayanhaddou.equipmentrental.config;

import be.rayanhaddou.equipmentrental.model.Category;
import be.rayanhaddou.equipmentrental.model.Product;
import be.rayanhaddou.equipmentrental.repository.CategoryRepository;
import be.rayanhaddou.equipmentrental.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod") // draait niet in productie
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataSeeder(CategoryRepository categoryRepository,
                      ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return; // data bestaat al → niets opnieuw seeden
        }

        // Categories
        Category lighting = categoryRepository.save(new Category("Belichting"));
        Category cables = categoryRepository.save(new Category("Kabels"));
        Category control = categoryRepository.save(new Category("Controlepanelen"));
        Category stage = categoryRepository.save(new Category("Podiumelementen"));

        // Products - Belichting
        productRepository.save(new Product(
                "LED PAR Spotlight",
                "Krachtige LED PAR voor podium- en scenebelichting.",
                lighting,
                10
        ));
        productRepository.save(new Product(
                "Moving Head Spot",
                "Beweegbare spot met DMX-ondersteuning.",
                lighting,
                4
        ));

        // Products - Kabels
        productRepository.save(new Product(
                "XLR Kabel 10m",
                "Professionele XLR-kabel voor audioverbindingen.",
                cables,
                25
        ));
        productRepository.save(new Product(
                "PowerCON Kabel",
                "Stroomkabel voor professionele lichtinstallaties.",
                cables,
                15
        ));

        // Products - Controlepanelen
        productRepository.save(new Product(
                "DMX Controller",
                "Lichtcontroller voor het aansturen van DMX-verlichting.",
                control,
                3
        ));
        productRepository.save(new Product(
                "Lighting Console",
                "Geavanceerde lichttafel voor podiumproducties.",
                control,
                2
        ));

        // Products - Podiumelementen
        productRepository.save(new Product(
                "Podiumplatform 2x1m",
                "Modulair podiumelement voor podia en installaties.",
                stage,
                6
        ));
        productRepository.save(new Product(
                "Truss Element",
                "Aluminium truss voor ophanging van licht en decor.",
                stage,
                8
        ));
    }
}