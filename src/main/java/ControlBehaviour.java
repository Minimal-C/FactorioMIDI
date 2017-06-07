public class ControlBehaviour {
	private Filter[] filters;
	private DeciderConditions decider_conditions;
	private CircuitCondition circuit_condition;
	private CircuitParameters circuit_parameters;

	public ControlBehaviour(Filter[] filters) {
		this.filters = filters;
	}

	public ControlBehaviour(DeciderConditions decider_conditions) {
		this.decider_conditions = decider_conditions;
	}

	public ControlBehaviour(CircuitCondition circuit_condition, CircuitParameters circuit_parameters) {
		this.circuit_condition = circuit_condition;
		this.circuit_parameters = circuit_parameters;
	}
}
