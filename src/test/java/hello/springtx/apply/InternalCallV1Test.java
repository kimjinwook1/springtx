package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

	@Autowired
	CallService callService;

	@Test
	void printProxy() {
		log.info("callService class={}", callService.getClass());
	}

	@Test
	void internalCall() {
		callService.internal();
	}

	@Test
	void externalCall() {
		callService.external();
	}

	@TestConfiguration
	static class InternalCallV1TestConfig{

		@Bean
		CallService callService() {
			return new CallService();
		}
	}

	@Slf4j
	static class CallService {

		public void external() {
			log.info("call external");
			printTxInfo();
			internal();
			//-> this.internal()을 호출하기 떄문에 프록시를 거치치 않는다.
			//그렇기 떄문에 트랜잭션이 적용되지 않는다.
		}

		@Transactional
		public void internal() {
			log.info("call internal");
			printTxInfo();
		}

		private void printTxInfo() {
			boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
			log.info("tx active={}", txActive);
		}
	}
}
