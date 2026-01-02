package be.rayanhaddou.equipmentrental.dto;

public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int quantity;
    private boolean active;
    private CategoryDto category;

    public ProductDto() {}

    public ProductDto(Long id, String name, String description, int quantity, boolean active, CategoryDto category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.active = active;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public boolean isActive() { return active; }
    public CategoryDto getCategory() { return category; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setActive(boolean active) { this.active = active; }
    public void setCategory(CategoryDto category) { this.category = category; }
}
