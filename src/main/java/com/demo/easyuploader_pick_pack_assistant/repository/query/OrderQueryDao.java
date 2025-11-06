package com.demo.easyuploader_pick_pack_assistant.repository.query;

import com.demo.easyuploader_pick_pack_assistant.dto.OrderItemDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class OrderQueryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Integer> findOrderId(String trackingNumber) {
        String sql = """
            SELECT FIRST 1 t.ID_TRANS
            FROM TRANS_WYSYLKA t
            WHERE (',' || REPLACE(t.NR_NADANIA, ' ', '') || ',') LIKE '%,' || :trackingNumber || ',%'
              AND t.ID_TRANS IN (SELECT ID FROM TRANSAKCJE WHERE GRUPA_UKRYJ = 0)
            ORDER BY t.DATA_WYSYLKI DESC
        """;
        try {
            Integer result = (Integer) entityManager
                    .createNativeQuery(sql)
                    .setParameter("trackingNumber", trackingNumber)
                    .getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public OrderItemDto findOrderItemsFromEU(long orderId) {
        String sql = """
            SELECT ID, KOD, TYTUL_AUKCJI, EAN, ILOSC
            FROM TRANSAKCJE
            WHERE ID = :orderId
        """;
        Object[] row = (Object[]) entityManager
                .createNativeQuery(sql)
                .setParameter("orderId", orderId)
                .getSingleResult();

        return OrderItemDto.builder()
                .orderId(((Number) row[0]).longValue())
                .model((String) row[1])
                .name((String) row[2])
                .barcode((String) row[3])
                .quantity(((Number) row[4]).intValue())
                .build();

    }

    public String findOrderItemWarehouseLocation(String model) {
        String sql = """
            SELECT FIRST 1 NOTATKI
            FROM AUK_OPISY
            WHERE ID_AUKCJI IN (SELECT ID FROM AUKCJE WHERE KOD = :model)
        """;
        return (String) entityManager
                .createNativeQuery(sql)
                .setParameter("model", model)
                .getSingleResult();
    }

    public Integer findOrderItemQuantity(String trackingNumber, String model) {
        String sql = """
            SELECT SUM(t.ILOSC)
            FROM TRANS_WYSYLKA w
            LEFT JOIN TRANSAKCJE t ON w.ID_TRANS = t.ID
            WHERE (',' || REPLACE(w.NR_NADANIA, ' ', '') || ',') LIKE '%,' || :trackingNumber || ',%'
              AND t.KOD = :model
        """;
        return ((Number) entityManager
                .createNativeQuery(sql)
                .setParameter("trackingNumber", trackingNumber)
                .setParameter("model", model)
                .getSingleResult()).intValue();
    }

    public String findBuyerLoginByOrderId(long orderId) {
        String sql = """
            SELECT FIRST 1 k.KL_LOGIN
            FROM TRANSAKCJE t
            LEFT JOIN TRANS_KLIENCI k ON t.ID_KLIENT = k.ID_KLIENT
            WHERE t.ID = :orderId
        """;
        return (String) entityManager
                .createNativeQuery(sql)
                .setParameter("orderId", orderId)
                .getSingleResult();
    }

    public String findOrderNotesByOrderId(long orderId) {
        String sql = """
            SELECT w.UWAGI
            FROM TRANSAKCJE t
            LEFT JOIN TRANS_WIADOM w ON t.ID = w.ID_TRANS
            WHERE t.ID = :orderId
        """;
        return (String) entityManager
                .createNativeQuery(sql)
                .setParameter("orderId", orderId)
                .getSingleResult();
    }

    public String findGiftWrappingByOrderId(long orderId) {
        String sql = """
            SELECT USLUGA
            FROM TRANSAKCJE
            WHERE ID = :orderId
        """;
        return (String) entityManager
                .createNativeQuery(sql)
                .setParameter("orderId", orderId)
                .getSingleResult();
    }

    public String findOrderItemName(String trackingNumber, String model) {
        String sql = """
            SELECT FIRST 1 t.TYTUL_AUKCJI
            FROM TRANS_WYSYLKA w
            LEFT JOIN TRANSAKCJE t ON w.ID_TRANS = t.ID
            WHERE (',' || REPLACE(w.NR_NADANIA, ' ', '') || ',') LIKE '%,' || :trackingNumber || ',%'
              AND t.KOD = :model
        """;
        return (String) entityManager
                .createNativeQuery(sql)
                .setParameter("trackingNumber", trackingNumber)
                .setParameter("model", model)
                .getSingleResult();
    }

    public String findOrderItemBarcode(String model) {
        String sql = """
            SELECT FIRST 1 Z_EANY
            FROM AUKCJE WHERE KOD = :model
        """;
        return (String) entityManager
                .createNativeQuery(sql)
                .setParameter("model", model)
                .getSingleResult();
    }

    public String findFirstTrackingNumberByOrderId(Long orderId) {
        String sql = """
                SELECT
                  CASE
                   WHEN POSITION(',' IN NR_NADANIA) > 0 THEN SUBSTRING(NR_NADANIA FROM 1 FOR POSITION(',' IN NR_NADANIA) - 1)
                   ELSE NR_NADANIA
                 END AS first_value
                FROM TRANS_WYSYLKA WHERE ID_TRANS = :orderId
                """;
        return (String) entityManager
                .createNativeQuery(sql)
                .setParameter("orderId", orderId)
                .getSingleResult();
    }

    public Optional<byte[]> findImage(String model) {
        String sql = """
                SELECT ZD_MIN
                FROM AUK_ZDJECIA
                WHERE ID_AUKCJI=(
                  SELECT FIRST 1 ID FROM AUKCJE WHERE KOD = :model
                )
                """;
        try {
            byte[] result = (byte[]) entityManager
                    .createNativeQuery(sql)
                    .setParameter("model", model)
                    .getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }


    }
}
