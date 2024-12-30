package com.fx_data_api.connectors.openexchagerates.connectflow;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.support.GenericMessage;

@Configuration
public class OpenExchangesRatesConnectorFlow {

	private static final Logger logger = LoggerFactory.getLogger(OpenExchangesRatesConnectorFlow.class);

	@Value("${OPEN_EXCHANGE_RATES_API_URL}")
	private String API_URL;
	
	@Value("${OPEN_EXCHANGE_RATES_APP_ID}")
	private String APP_ID;
	
	@Bean
	public IntegrationFlow exchangeRatesPollingFlow() {
		// Validate that appId is provided
		if (APP_ID == null || APP_ID.isEmpty()) {
			throw new IllegalStateException(
					"Environment variable 'OPEN_EXCHANGE_RATES_APP_ID' or 'openexchangerates.app_id' is not set.");
		}

		String uri = API_URL + "?app_id=" + APP_ID;

		logger.debug("API Endpoint URI: {}", uri);

		return IntegrationFlow
				.from(() -> new GenericMessage<>(""), c -> c.poller(Pollers.fixedRate(Duration.ofHours(1))))
				.handle(Http.outboundGateway(uri).httpMethod(HttpMethod.GET).expectedResponseType(String.class))
				.handle(message -> logger.debug("Exchange Rates Response: {}", message.getPayload())).get();

	}
}
