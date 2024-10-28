package semantic;

import gen.ImperativeCompConstParser;

public class Optimizer {
    public void optimize(ImperativeCompConstParser.InputContext parseTree) {
        ReductionOptimizerVisitor reductionOptimizerVisitor = new ReductionOptimizerVisitor();
        reductionOptimizerVisitor.visit(parseTree);

        BranchOptimizerVisitor branchOptimizerVisitor = new BranchOptimizerVisitor();
        branchOptimizerVisitor.visit(parseTree);
    }
}
