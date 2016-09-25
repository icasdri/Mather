/*
 * Copyright 2016 icasdri
 *
 * This file is part of Mather. The original source code for Mather can be
 * found at <https://github.com/icasdri/Mather>. See COPYING for licensing
 * details.
 */

package org.icasdri.mather;

public class MathItem {

    public interface ChangeListener {
        void handleChange();
    }

    private ChangeListener changeListener;

    private String input;
    private MathParser.Result result;

    public MathParser.Result getResult() {
        return result;
    }

    public String getInput() {
        return input;
    }

    /**
     * Constructor. Following construction, eval() should be called
     * so that the result field may be populated.
     *
     * @param input the user-given input string for this item
     */
    public MathItem(String input) {
        this.input = input;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void eval(MathParser parser) {
        if (this.input.startsWith("debug")) {
            for (String a : this.input.split("\\s+")) {
                switch (a) {
                    case "resultview":
                        this.result = new MathParser.Result(
                                            "Result View Here!",
                                            MathParser.ResultType.ANS);
                        break;
                    default: break;
                    case "debug": break;
                }
            }
        } else parser.eval(this.input, new MathParser.Callback() {
            @Override
            public void processResult(MathParser.Result result) {
                MathItem.this.result = result;

                if (MathItem.this.changeListener != null) {
                    MathItem.this.changeListener.handleChange();
                }
            }
        });
    }
}
