package com.solidgate.aqa.tests;

import com.solidgate.aqa.BaseTest;
import com.solidgate.aqa.api.dto.OrderRequest;
import com.solidgate.aqa.api.dto.OrderStatusResponse;
import com.solidgate.aqa.api.dto.PaymentInitResponse;
import com.solidgate.aqa.fixtures.PaymentScenario;
import com.solidgate.aqa.fixtures.PaymentScenarios;
import com.solidgate.aqa.listeners.RequiresRetry;
import com.solidgate.aqa.util.OrderIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class PaymentFlowTest extends BaseTest {

    private static final int STATUS_POLL_TIMEOUT_SECONDS = 30;

    @Test(dataProvider = "successScenarios", dataProviderClass = PaymentScenarios.class)
    @RequiresRetry
    public void shouldPayOrderViaPaymentPage(PaymentScenario scenario) {
        String orderId = OrderIdGenerator.generate();
        log.info("Scenario '{}' orderId={}", scenario.label(), orderId);

        PaymentInitResponse init = api.initPaymentPage(
                OrderRequest.sampleSuccessOrder(orderId, scenario.amount(), scenario.currency()));

        assertThat(init.url()).isNotBlank().startsWith("http");

        paymentPage
                .openUrl(init.url())
                .fillCard(scenario.card())
                .submit()
                .assertVisible();

        OrderStatusResponse status = api.waitForOrderStatus(orderId, STATUS_POLL_TIMEOUT_SECONDS);

        assertThat(status.getOrder()).isNotNull();
        assertThat(status.getOrder().getOrderId()).isEqualTo(orderId);
        assertThat(status.getOrder().getAmount()).isEqualTo(scenario.amount());
        assertThat(status.getOrder().getCurrency()).isEqualToIgnoringCase(scenario.currency());
        assertThat(status.getOrder().getStatus())
                .isNotBlank()
                .containsAnyOf(scenario.expectedOrderStatuses());

        assertThat(status.getTransactions()).isNotEmpty();
        boolean anySuccess = status.getTransactions().values().stream()
                .map(OrderStatusResponse.Transaction::getStatus)
                .filter(st -> st != null)
                .anyMatch(st -> {
                    for (String ok : scenario.expectedTxStatuses()) {
                        if (st.equalsIgnoreCase(ok)) return true;
                    }
                    return false;
                });
        assertThat(anySuccess).as("at least one transaction must be in success state").isTrue();
    }
}
