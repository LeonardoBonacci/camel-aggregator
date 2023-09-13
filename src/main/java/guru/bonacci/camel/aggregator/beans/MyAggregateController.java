package guru.bonacci.camel.aggregator.beans;

import org.apache.camel.processor.aggregate.AggregateController;
import org.apache.camel.processor.aggregate.AggregateProcessor;

public class MyAggregateController implements AggregateController {

  private AggregateProcessor processor;

  @Override
  public void onStart(AggregateProcessor processor) {
      this.processor = processor;
  }

  @Override
  public void onStop(AggregateProcessor processor) {
      this.processor = null;
  }

  @Override
  public int forceCompletionOfGroup(String key) {
      if (processor != null) {
          return processor.forceCompletionOfGroup(key);
      } else {
          return 0;
      }
  }

  @Override
  public int forceCompletionOfAllGroups() {
      if (processor != null) {
          return processor.forceCompletionOfAllGroups();
      } else {
          return 0;
      }
  }

  @Override
  public int forceDiscardingOfGroup(String key) {
      if (processor != null) {
          return processor.forceDiscardingOfGroup(key);
      } else {
          return 0;
      }
  }

  @Override
  public int forceDiscardingOfAllGroups() {
      if (processor != null) {
          return processor.forceDiscardingOfAllGroups();
      } else {
          return 0;
      }
  }
}
