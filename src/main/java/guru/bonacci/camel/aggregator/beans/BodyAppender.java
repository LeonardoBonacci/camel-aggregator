package guru.bonacci.camel.aggregator.beans;

import java.util.List;

public class BodyAppender {

	 public List<String> append(List<String> existing, String next) {
     existing.add(next);
     return existing;
 }
}