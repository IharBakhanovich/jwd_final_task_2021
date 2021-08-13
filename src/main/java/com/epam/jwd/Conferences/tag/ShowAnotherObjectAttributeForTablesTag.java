package com.epam.jwd.Conferences.tag;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class ShowAnotherObjectAttributeForTablesTag extends TagSupport {

    private String valueWithWhichCompare;
    private String valueToCompare;
    private String valueToShow;

    public String getValueWithWhichCompare() {
        return valueWithWhichCompare;
    }

    public String getValueToCompare() {
        return valueToCompare;
    }

    public String getValueToShow() {
        return valueToShow;
    }

    public void setValueWithWhichCompare(String valueWithWhichCompare) {
        this.valueWithWhichCompare = valueWithWhichCompare;
    }

    public void setValueToCompare(String valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    public void setValueToShow(String valueToShow) {
        this.valueToShow = valueToShow;
    }

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

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}