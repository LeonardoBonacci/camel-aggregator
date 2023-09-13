package guru.bonacci.camel.aggregator;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import guru.bonacci.camel.aggregator.beans.BodyAppender;
import guru.bonacci.camel.aggregator.beans.MyAggregateController;
import guru.bonacci.camel.aggregator.beans.Randomizer;

@Component
public class SimpleRouteBuilder extends RouteBuilder {

	
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
			.setHeader("flush", simple("${random(1,5)}"))
			.aggregate(header("id"), AggregationStrategies.bean(BodyAppender.class, "append"))
				.completionSize(3)
				.completionTimeout(8000)
				.completionPredicate(header("flush").isEqualTo("1"))
				.aggregateController(controller)
			.to("direct:printer");
		
	  from("direct:printer")
		  .log("Message received : ${headers.id}<>${body}");
	}
}