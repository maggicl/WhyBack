package byteback.analysis.transformer;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.Vimp;
import byteback.analysis.vimp.LogicConstant;
import byteback.util.Lazy;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.Type;
import soot.TypeSwitch;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.jimple.AndExpr;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.EqExpr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LtExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.OrExpr;
import soot.jimple.UnopExpr;

public class LogicValueTransformer extends BodyTransformer implements UnitTransformer {

	private interface BinaryConstructor extends BiFunction<ValueBox, ValueBox, Value> {}

	private interface UnaryConstructor extends Function<ValueBox, Value> {}

	private static final Lazy<LogicValueTransformer> instance = Lazy.from(LogicValueTransformer::new);

	public static class LogicValueSwitch extends JimpleValueSwitch<Value> {

		public final Type expectedType;

		public final ValueBox resultBox;

		public LogicValueSwitch(final Type expectedType, final ValueBox resultBox) {
			this.expectedType = expectedType;
			this.resultBox = resultBox;
		}

		public void setValue(final Value value) {
			resultBox.setValue(value);
		}

		public void setUnaryValue(final UnaryConstructor constructor, final Type expectedType, final UnopExpr value) {
			final ValueBox operandBox = value.getOpBox();
			new LogicValueSwitch(expectedType, operandBox).visit(operandBox.getValue());
		}

		public void setBinaryValue(final BinaryConstructor constructor, final Type expectedType, final BinopExpr value) {
			final ValueBox leftBox = value.getOp1Box();
			final ValueBox rightBox = value.getOp2Box();
			setValue(constructor.apply(value.getOp1Box(), value.getOp2Box()));
			new LogicValueSwitch(expectedType, leftBox).visit(leftBox.getValue());
			new LogicValueSwitch(expectedType, rightBox).visit(rightBox.getValue());
		}
		
		@Override
		public void caseLocal(final Local local) {
			final Type actualType; 

			if (local.getType() != BooleanType.v()) {
				actualType = Type.toMachineType(local.getType());
			} else {
				actualType = local.getType();
			}

			if (expectedType != actualType) {
				setValue(Grimp.v().newCastExpr(local, expectedType));
			}
		}

		@Override
		public void caseIntConstant(final IntConstant constant) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType type) {
					setValue(LogicConstant.v(constant.value > 0));
				}
					
			});
		}

		@Override
		public void caseAndExpr(final AndExpr value) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType type) {
					setBinaryValue(Vimp.v()::newLogicAndExpr, type, value);
				}

			});
		}

		@Override
		public void caseOrExpr(final OrExpr value) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType type) {
					setBinaryValue(Vimp.v()::newLogicOrExpr, type, value);
				}

			});
		}

		@Override
		public void caseNegExpr(final NegExpr value) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType type) {
					setUnaryValue(Vimp.v()::newLogicNotExpr, type, value);
				}

			});
		}

		@Override
		public void caseGtExpr(final GtExpr value) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType $) {
					setBinaryValue(Vimp.v()::newGtExpr, IntType.v(), value);
				}

			});
		}

		@Override
		public void caseGeExpr(final GeExpr value) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType $) {
					setBinaryValue(Vimp.v()::newGeExpr, IntType.v(), value);
				}

			});
		}

		@Override
		public void caseLtExpr(final LtExpr value) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType $) {
					setBinaryValue(Vimp.v()::newLtExpr, IntType.v(), value);
				}

			});
		}

		@Override
		public void caseLeExpr(final LeExpr value) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType $) {
					setBinaryValue(Vimp.v()::newLeExpr, IntType.v(), value);
				}

			});
		}

		@Override
		public void caseEqExpr(final EqExpr value) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType $) {
					setBinaryValue(Vimp.v()::newEqExpr, IntType.v(), value);
				}

			});
		}

		@Override
		public void caseNeExpr(final NeExpr value) {
			expectedType.apply(new TypeSwitch<>() {

				@Override
				public void caseBooleanType(final BooleanType $) {
					System.out.println("=====================");
					setBinaryValue(Vimp.v()::newNeExpr, IntType.v(), value);
					System.out.println("?????????????????????");
				}

			});
		}

		@Override
		public void caseInvokeExpr(final InvokeExpr value) {
			for (int i = 0; i < value.getArgCount(); ++i) {
				final ValueBox argumentBox = value.getArgBox(i);
				new LogicValueSwitch(value.getMethod().getParameterType(i), argumentBox)
					.visit(argumentBox.getValue());
			}
		}

		@Override
		public void caseLengthExpr(final LengthExpr $) {
		}

		@Override
		public void caseDefault(final Value defaultValue) {
			if (defaultValue instanceof BinopExpr value) {
				final ValueBox leftBox = value.getOp1Box();
				final ValueBox rightBox = value.getOp2Box();
				new LogicValueSwitch(expectedType, leftBox).visit(leftBox.getValue());
				new LogicValueSwitch(expectedType, rightBox).visit(rightBox.getValue());
			}

			if (defaultValue instanceof UnopExpr value) {
				final ValueBox operandBox = value.getOpBox();
				new LogicValueSwitch(expectedType, operandBox).visit(operandBox.getValue());
			}
		}

		@Override
		public Value result() {
			return resultBox.getValue();
		}

	}

	public static LogicValueTransformer v() {
		return instance.get();
	}

	private LogicValueTransformer() {
	}

	@Override
	protected void internalTransform(final Body body, final String phaseName, final Map<String, String> options) {
		if (body instanceof GrimpBody grimpBody) {
			transformBody(grimpBody);
		} else {
			throw new IllegalArgumentException("Can only transform Jimple");
		}
	}

	public void transformValue(final ValueBox valueBox) {
		new LogicValueSwitch(BooleanType.v(), valueBox).visit(valueBox.getValue());
	}

	public void transformUnit(final Unit unit) {
		unit.apply(new JimpleStmtSwitch<>() {

			@Override
			public void caseAssignStmt(final AssignStmt unit) {
				final Type type = unit.getLeftOp().getType();
				final ValueBox rightBox = unit.getRightOpBox();
				new LogicValueSwitch(type, unit.getRightOpBox()).visit(rightBox.getValue());
			}

			@Override
			public void caseIfStmt(final IfStmt unit) {
				final ValueBox conditionBox = unit.getConditionBox();
				new LogicValueSwitch(BooleanType.v(), conditionBox).visit(conditionBox.getValue());
			}

		});
	}

	@Override
	public void transformUnit(final UnitBox unitBox) {
		transformUnit(unitBox.getUnit());
	}

}
