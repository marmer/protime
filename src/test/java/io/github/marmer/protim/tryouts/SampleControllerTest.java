package io.github.marmer.protim.tryouts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class SampleControllerTest {
	@Rule
	public final MockitoRule mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

	private MockMvc mockMvc;

	@InjectMocks
	private SampleController sampleController;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(sampleController).build();
	}

	@Test
	public void testMockMvcIntegration() throws Exception {
		// Preparation

		// Execution
		ResultActions request = mockMvc.perform(get("/sample"));

		// Assertion
		request.andExpect(status().isOk()).andExpect(content().string(is(equalTo("It works without a teapot"))));
	}

}
