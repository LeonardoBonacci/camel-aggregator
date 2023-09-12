package guru.bonacci.camel.aggregator.beans;

public class BodyAppender {

	 public String append(String existing, String next) {
     return next + "|" + existing;
 }}