package repast.simphony.statecharts;

public class SelfTransition<T> extends Transition<T> {

  protected SelfTransition(Trigger trigger, AbstractState<T> state, double priority) {
    super(trigger, state, state, priority);
  }

  protected SelfTransition(String id, Trigger trigger, AbstractState<T> state, double priority) {
    super(id, trigger, state, state, priority);
  }

}
