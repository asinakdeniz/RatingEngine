package rating.engine.persistence;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;


@Data
@Builder
@Table(name = "billing_line_gold")
public class BillingLineEntity {

    @Id
    private Long id;

    private String contractId;

    private Instant startDate;

    private Instant endDate;

    private String productId;

    private BigDecimal price;

    private Instant createdDate;

}
