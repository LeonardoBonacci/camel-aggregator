package guru.bonacci.camel.aggregator.beans;

import java.util.UUID;

public class Randomizer {

  public String saySomething(String input) {
      return UUID.randomUUID().toString().substring(0, 6);
  }
}
