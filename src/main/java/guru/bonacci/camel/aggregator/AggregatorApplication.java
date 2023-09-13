package guru.bonacci.camel.aggregator;

import javax.sql.DataSource;

import org.apache.camel.processor.aggregate.jdbc.JdbcAggregationRepository;
import org.apache.camel.spi.AggregationRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;


@SpringBootApplication
public class AggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggregatorApplication.class, args);
	}

	
	@Bean
	public AggregationRepository myAggregationRepository(
	    final PlatformTransactionManager platformTransactionManager, final DataSource dataSource) {

	  final var repo = new JdbcAggregationRepository();
	  repo.setRepositoryName("aggregation");
	  repo.setTransactionManager(platformTransactionManager);
	  repo.setDataSource(dataSource);

	  return repo;
	}
}
