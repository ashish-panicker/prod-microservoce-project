# Product Service

| Operation         | Method | Endpoint      |
|-------------------|--------|---------------|
| Create Product    | POST   | /api/products |
| Read all Products | GET    | /api/products |

**MongoDB docker container**
```dockerfile
version: '4'
services:
  mongo:
    image: mongo:7.0.5
    container_name: mongo
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: password
      MONGO_INITDB_DATABASE: product-service
    volumes:
      - ./docker/mongodb/data:/data/db
```

**Spring Data MongoDB**
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://root:password@localhost:27017/product-service?authSource=admin
```

**Model**
```java
@Document(value = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
```

**Service**
```java
public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    ProductResponse mapToProductResponse(Product product);
}
```

**Repository**
```java
public interface ProductRepository extends MongoRepository<Product, String> {
}
```

**DTO**
```java
public record ProductRequest(String name, String description, BigDecimal price) {
}

public record ProductResponse(String id, String name, String description, BigDecimal price) {
}
```

**Rest Controller**
```java
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
    }
}
```