package eirb.pg203;

public class Client {
  public static void main(String[] args) {
    CalendarApplication app = new CalendarApplication();
    int exitCode = app.run(args);
    System.exit(exitCode);
  }
}
