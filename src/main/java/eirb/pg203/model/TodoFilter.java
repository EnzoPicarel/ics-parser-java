package eirb.pg203.model;

import java.util.List;
import java.util.stream.Collectors;

public abstract class TodoFilter {

  public abstract List<Todo> filter(List<Todo> todos);

  public static class IncompleteFilter extends TodoFilter {
    @Override
    public List<Todo> filter(List<Todo> todos) {
      return todos.stream().filter(t -> !"COMPLETED".equals(t.status)).collect(Collectors.toList());
    }
  }

  public static class AllTodosFilter extends TodoFilter {
    @Override
    public List<Todo> filter(List<Todo> todos) {
      return todos;
    }
  }

  public static class CompletedFilter extends TodoFilter {
    @Override
    public List<Todo> filter(List<Todo> todos) {
      return todos.stream().filter(t -> "COMPLETED".equals(t.status)).collect(Collectors.toList());
    }
  }

  public static class InProcessFilter extends TodoFilter {
    @Override
    public List<Todo> filter(List<Todo> todos) {
      return todos.stream().filter(t -> "IN-PROCESS".equals(t.status)).collect(Collectors.toList());
    }
  }

  public static class NeedsActionFilter extends TodoFilter {
    @Override
    public List<Todo> filter(List<Todo> todos) {
      return todos.stream()
          .filter(t -> "NEEDS-ACTION".equals(t.status))
          .collect(Collectors.toList());
    }
  }
}
