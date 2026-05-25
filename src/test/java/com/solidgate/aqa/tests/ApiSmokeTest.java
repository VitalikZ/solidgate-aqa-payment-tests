package com.solidgate.aqa.tests;

import com.solidgate.aqa.api.SolidgateApiClient;
import com.solidgate.aqa.api.dto.OrderStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ApiSmokeTest {

    @Test
    public void shouldQueryStatusOfExampleOrder() {
        OrderStatusResponse status = new SolidgateApiClient().getOrderStatus("order_example");
        assertThat(status.getOrder()).isNotNull();
        log.info("order_example -> status={}, {} {}",
                status.getOrder().getStatus(),
                status.getOrder().getAmount(),
                status.getOrder().getCurrency());
    }
}
