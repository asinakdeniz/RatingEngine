package rating.engine.persistence;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;


@Data
@Builder
@Table(name = "product")
public class ProductEntity {

    @Id
    private Long id;

    private String productId;

    private BigDecimal price;

    private String type;

    private BigDecimal coefficient;

    private String formula;

    private BigDecimal monthlyFee;

}
