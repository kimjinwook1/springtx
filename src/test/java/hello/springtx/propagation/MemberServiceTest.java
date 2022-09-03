package hello.springtx.propagation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class MemberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	LogRepository logRepository;

	/**
	 * memberService	@Transactional:OFF
	 * memberRepository	@Transactional:ON
	 * logRepository	@Transactional:ON
	 */
	@Test
	void outerTxOff_success() {
		//given
		String username = "outerTxOff_suceess";

		//when
		memberService.joinV1(username);

		//then: 모든 데이터가 정상 저장된다.
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isPresent());
	}

	/**
	 * memberService	@Transactional:OFF
	 * memberRepository	@Transactional:ON
	 * logRepository	@Transactional:ON Exception
	 */
	@Test
	void outerTxOff_fail() {
		//given
		String username = "로그예외_outerTxOff_fail";

		//when
		assertThatThrownBy(() -> memberService.joinV1(username))
				.isInstanceOf(RuntimeException.class);

		//then: 모든 데이터가 정상 저장된다.
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isEmpty());
	}
}
