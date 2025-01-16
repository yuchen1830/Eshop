package com.eshop.enums;

public enum CategoryEnums {
    ELECTRONICS(0, "Electronics"),
    CLOTHING(1, "Clothing"),
    FOOD(2, "Food"),
    HOME(3, "Home"),
    TOYS(4, "Toys");

    private final Integer type;
    private final String description;

    CategoryEnums(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    // Method to get a ProductCategory from its ID
    public static CategoryEnums fromType(Integer type) {
        for (CategoryEnums category : CategoryEnums.values()) {
            if (category.type.equals(type)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category found for type: " + type);
    }
}
