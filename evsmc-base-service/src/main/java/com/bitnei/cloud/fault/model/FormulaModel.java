package com.bitnei.cloud.fault.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xuzhijie
 */
@Getter
@Setter
public class FormulaModel {

    /**
     * 公式
     */
    private String formula;

    /**
     * 公式描述
     */
    private String formulaDesc;

    /**
     * @param excludeFormulaModel
     */
    public void appendExcludeFormula(FormulaModel excludeFormulaModel) {
        if (excludeFormulaModel == null) {
            return;
        }
        if (this.formula != null && StringUtils.isNotBlank(excludeFormulaModel.getFormula())) {
            this.formula += "&&(" + excludeFormulaModel.getFormula() + ")";
            this.formulaDesc += "且(" + excludeFormulaModel.getFormulaDesc() + ")";
        }
    }

}
