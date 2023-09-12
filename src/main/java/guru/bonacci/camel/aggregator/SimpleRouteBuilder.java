package guru.bonacci.camel.aggregator;

import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import guru.bonacci.camel.aggregator.beans.BodyAppender;
import guru.bonacci.camel.aggregator.beans.Randomizer;

@Component
public class SimpleRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer://foo?fixedRate=true&period=500")
			.bean(Randomizer.class)
			.setHeader("id", simple("${random(1,5)}"))
			.aggregate(AggregationStrategies.bean(BodyAppender.class, "append"))
				.constant(true)
				.completionSize(3)
			.to("direct:printer");
		
	  from("direct:printer")
		  .log("Message received : ${headers.id}<>${body}");
	}
}