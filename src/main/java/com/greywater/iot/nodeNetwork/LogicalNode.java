package com.greywater.iot.nodeNetwork;

import java.util.List;

public class LogicalNode extends EvaluableNode<Boolean> {

    public LogicalNode() { super(); }

    public void eval() {
        state = super.evaluateScript();
    }
}
