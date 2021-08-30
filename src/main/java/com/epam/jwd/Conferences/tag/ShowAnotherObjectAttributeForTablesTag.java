package com.epam.jwd.Conferences.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Creates a custom tag which receives three parameters, checks conditions
 * and returns the third parameter when the condition is true
 */
public class ShowAnotherObjectAttributeForTablesTag extends TagSupport {

    private String valueWithWhichCompare;
    private String valueToCompare;
    private String valueToShow;

    /**
     * A Getter for the valueWithWhichCompare.
     *
     * @return The valueWithWhichCompare.
     */
    public String getValueWithWhichCompare() {
        return valueWithWhichCompare;
    }

    /**
     * A Getter for the valueToCompare.
     *
     * @return The valueToCompare.
     */
    public String getValueToCompare() {
        return valueToCompare;
    }

    /**
     * A Getter for the valueToShow.
     *
     * @return The valueToShow.
     */
    public String getValueToShow() {
        return valueToShow;
    }

    /**
     * A Setter for the valueWithWhichCompare.
     *
     * @param valueWithWhichCompare The value set.
     */
    public void setValueWithWhichCompare(String valueWithWhichCompare) {
        this.valueWithWhichCompare = valueWithWhichCompare;
    }

    /**
     * A Setter for the valueToCompare.
     *
     * @param valueToCompare The value set.
     */
    public void setValueToCompare(String valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    /**
     * A Setter for the valueToShow.
     *
     * @param valueToShow The value set.
     */
    public void setValueToShow(String valueToShow) {
        this.valueToShow = valueToShow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int doStartTag() throws JspException {
        try {
            if (valueToCompare.equalsIgnoreCase(valueWithWhichCompare)) {
                pageContext.getOut().write("<td>" + valueToShow + "</td>");
            }
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}