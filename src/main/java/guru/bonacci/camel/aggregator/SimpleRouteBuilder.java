package guru.bonacci.camel.aggregator;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.AggregationRepository;
import org.springframework.stereotype.Component;

import guru.bonacci.camel.aggregator.beans.BodyAppender;
import guru.bonacci.camel.aggregator.beans.MyAggregateController;
import guru.bonacci.camel.aggregator.beans.Randomizer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SimpleRouteBuilder extends RouteBuilder {

	private final AggregationRepository repo;
	
	@Override
	public void configure() throws Exception {
		var controller = new MyAggregateController();

		var task = new TimerTask() {
	    public void run() {
	    		System.out.println("force flush");
	    		controller.forceCompletionOfAllGroups();
	    }
		};
		var timer = new Timer();
		long delay = 15000L;
		timer.schedule(task, delay);
	

		from("timer://foo?fixedRate=true&period=500")
			.bean(Randomizer.class)
			.setHeader("id", simple("${random(1,5)}"))
			.to("direct:aggregate");

		from("timer://foo?fixedRate=true&period=5000")
			.setHeader("flush", simple("${random(1,5)}"))
			.setHeader("id", header("flush"))
			.setBody(constant("flushed!"))
			.to("direct:aggregate");

	  from("direct:aggregate")
			.aggregate(header("id"), AggregationStrategies.bean(BodyAppender.class, "append"))
				.completionSize(3)
				.completionTimeout(8000)
				.completionPredicate(header("flush").isNotNull())
				.aggregateController(controller)	
				.aggregationRepository(repo)
				.to("direct:print");

	  from("direct:print")
	  	.choice()
		  	.when(body().isInstanceOf(List.class))	
			  	.log("Message received : ${headers.id}<>${body}")
	  		.otherwise()
	  			.log("Houston : ${headers.id}<>${body}");
	}
}