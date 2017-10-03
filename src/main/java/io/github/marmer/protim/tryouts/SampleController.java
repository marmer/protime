package io.github.marmer.protim.tryouts;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.marmer.protim.model.SampleModel;

@RestController
@RequestMapping("rest/sample")
public class SampleController {
	@Autowired
	private SampleModelRepository sampleModelRepository;

	@GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
	public String someGet() {
		Random random = new Random();
		SampleModel entity = SampleModel.builder().niceProperty("Random " + random.nextInt()).build();
		sampleModelRepository.save(entity);
		return "It works without a teapot";
	}

	@GetMapping(path = "json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public SampleModel someGetJson() {
		Random random = new Random();
		SampleModel entity = SampleModel.builder().niceProperty("Random " + random.nextInt()).build();
		sampleModelRepository.save(entity);
		return entity;
	}

	@GetMapping(path = "xml", produces = MediaType.APPLICATION_XML_VALUE)
	public SampleModel someGetXml() {
		Random random = new Random();
		SampleModel entity = SampleModel.builder().niceProperty("Random " + random.nextInt()).build();
		sampleModelRepository.save(entity);
		return entity;
	}
}
