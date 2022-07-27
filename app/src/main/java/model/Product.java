package model;

public class Product {
    private Double Price;
    private String Product;

    public Product(Double price, String product) {
        Price = price;
        Product = product;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }
}
