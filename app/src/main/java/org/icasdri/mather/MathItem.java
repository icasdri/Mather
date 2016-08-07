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
        parser.eval(this.input, new MathParser.Callback() {
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
