package be.rayanhaddou.equipmentrental.repository;

import be.rayanhaddou.equipmentrental.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Filter op categorie naam (bv. ?category=Kabels)
    List<Product> findByCategory_NameIgnoreCase(String categoryName);

    // Vaak handig: alleen actieve producten tonen
    List<Product> findByActiveTrue();

    // Actieve producten binnen een categorie
    List<Product> findByActiveTrueAndCategory_NameIgnoreCase(String categoryName);
}
