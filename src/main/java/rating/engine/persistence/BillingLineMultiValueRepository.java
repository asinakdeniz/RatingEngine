package rating.engine.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BillingLineMultiValueRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_PREFIX =
            "INSERT INTO rating_engine.billing_line_gold " +
                    "(contract_id, start_date, end_date, product_id, price, created_date) VALUES ";

    @Transactional
    public void saveAllBatch(List<BillingLineEntity> entities) {
        if (entities.isEmpty()) return;

        StringBuilder sql = new StringBuilder(INSERT_PREFIX);
        List<Object> params = new ArrayList<>(entities.size() * 6);
        Instant now = Instant.now();

        for (int i = 0; i < entities.size(); i++) {
            if (i > 0) sql.append(',');
            sql.append("(?,?,?,?,?,?)");

            BillingLineEntity e = entities.get(i);
            params.add(e.getContractId());
            params.add(toOffsetDateTime(e.getStartDate()));
            params.add(toOffsetDateTime(e.getEndDate()));
            params.add(e.getProductId());
            params.add(e.getPrice());
            params.add(toOffsetDateTime(e.getCreatedDate() != null ? e.getCreatedDate() : now));
        }

        jdbcTemplate.update(sql.toString(), params.toArray());
    }

    private OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? OffsetDateTime.ofInstant(instant, ZoneOffset.UTC) : null;
    }
}