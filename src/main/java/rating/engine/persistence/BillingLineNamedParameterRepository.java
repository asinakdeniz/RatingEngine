package rating.engine.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BillingLineNamedParameterRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = """
            INSERT INTO rating_engine.billing_line_gold
                (contract_id, start_date, end_date, product_id, price, created_date)
            VALUES
                (:contractId, :startDate, :endDate, :productId, :price, :createdDate)
            """;

    @Transactional
    public void saveAllBatch(List<BillingLineEntity> entities) {
        SqlParameterSource[] batch = entities.stream()
                .map(this::toParams)
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(INSERT_SQL, batch);
    }

    private MapSqlParameterSource toParams(BillingLineEntity e) {
        return new MapSqlParameterSource()
                .addValue("contractId", e.getContractId())
                .addValue("startDate", toOffsetDateTime(e.getStartDate()))
                .addValue("endDate", toOffsetDateTime(e.getEndDate()))
                .addValue("productId", e.getProductId())
                .addValue("price", e.getPrice())
                .addValue("createdDate", toOffsetDateTime(
                        e.getCreatedDate() != null ? e.getCreatedDate() : Instant.now()
                ));
    }

    private OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant != null ? OffsetDateTime.ofInstant(instant, ZoneOffset.UTC) : null;
    }
}