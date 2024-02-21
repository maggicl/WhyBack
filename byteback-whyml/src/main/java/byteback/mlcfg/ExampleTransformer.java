package byteback.mlcfg;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.vimp.AssertionStmt;
import byteback.analysis.vimp.AssumptionStmt;
import byteback.analysis.vimp.InvariantStmt;
import soot.Body;
import soot.Unit;
import soot.jimple.*;

import java.util.ArrayList;
import java.util.List;

public class ExampleTransformer extends JimpleStmtSwitch<Body> {

    private final List<Object> visited = new ArrayList<>();

    @Override
    public void defaultCase(Object o) {
        super.defaultCase(o);
        visited.add(o);
    }

    @Override
    public Body visit(Unit unit) {
        visited.add(unit);
        return super.visit(unit);
    }

    @Override
    public Body visit(Body body) {
        visited.add(body);
        return super.visit(body);
    }

    @Override
    public void caseAssertionStmt(AssertionStmt s) {
        visited.add(s);
        super.caseAssertionStmt(s);
    }

    @Override
    public void caseAssumptionStmt(AssumptionStmt s) {
        visited.add(s);
        super.caseAssumptionStmt(s);
    }

    @Override
    public void caseInvariantStmt(InvariantStmt s) {
        visited.add(s);
        super.caseInvariantStmt(s);
    }

    @Override
    public void caseDefault(Unit o) {
        visited.add(o);
        super.caseDefault(o);
    }

    @Override
    public void caseBreakpointStmt(BreakpointStmt stmt) {
        visited.add(stmt);
        super.caseBreakpointStmt(stmt);
    }

    @Override
    public void caseInvokeStmt(InvokeStmt stmt) {
        visited.add(stmt);
        super.caseInvokeStmt(stmt);
    }

    @Override
    public void caseAssignStmt(AssignStmt stmt) {
        visited.add(stmt);
        super.caseAssignStmt(stmt);
    }

    @Override
    public void caseIdentityStmt(IdentityStmt stmt) {
        visited.add(stmt);
        super.caseIdentityStmt(stmt);
    }

    @Override
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
        visited.add(stmt);
        super.caseEnterMonitorStmt(stmt);
    }

    @Override
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
        visited.add(stmt);
        super.caseExitMonitorStmt(stmt);
    }

    @Override
    public void caseNopStmt(NopStmt stmt) {
        visited.add(stmt);
        super.caseNopStmt(stmt);
    }

    @Override
    public void caseReturnStmt(ReturnStmt stmt) {
        visited.add(stmt);
        super.caseReturnStmt(stmt);
    }

    @Override
    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
        visited.add(stmt);
        super.caseReturnVoidStmt(stmt);
    }

    @Override
    public void caseTableSwitchStmt(TableSwitchStmt stmt) {
        visited.add(stmt);
        super.caseTableSwitchStmt(stmt);
    }

    @Override
    public void caseThrowStmt(ThrowStmt stmt) {
        visited.add(stmt);
        super.caseThrowStmt(stmt);
    }

    @Override
    public void caseGotoStmt(GotoStmt stmt) {
        visited.add(stmt);
        super.caseGotoStmt(stmt);
    }

    @Override
    public void caseIfStmt(IfStmt stmt) {
        visited.add(stmt);
        super.caseIfStmt(stmt);
    }

    @Override
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
        visited.add(stmt);
        super.caseLookupSwitchStmt(stmt);
    }

    @Override
    public void caseRetStmt(RetStmt stmt) {
        visited.add(stmt);
        super.caseRetStmt(stmt);
    }
}
