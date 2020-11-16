package com.bitnei.cloud.rule.parameter;

import com.bitnei.cloud.fault.domain.ParameterRule;
import com.bitnei.cloud.fault.enums.VehicleModelRuleTypeEnum;
import com.bitnei.cloud.fault.model.FormulaModel;
import com.bitnei.cloud.fault.service.impl.ParameterRuleService;
import com.bitnei.cloud.sys.model.VehModelAlarmModel;
import com.google.common.collect.ImmutableMap;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个是变更前的处理，保留一下，后面
 */
@Slf4j
@Disabled
public class ParameterRuleServiceTest {

    /**
     * 测试规则生成
     */
    @Test
    public void t1() {
        String vehModelId = "d3ccdba1127b4fb59a9fd7b03acb87d3";
        VehModelAlarmModel newModel = new VehModelAlarmModel();
        newModel.setId(vehModelId);
        newModel.setTemperatureDifferenceAlarm("2,2;3,4;4,4");
        newModel.setBatteryConsistencyAlarm("2,;,;,");
        newModel.setSocJumpAlarm("2,;,;,");
        newModel.setBeginThreshold(20);
        newModel.setEndThreshold(30);
        newModel.setResponseMode("1,3");

        List<ParameterRule> levelRules = new ArrayList<>(20);

        ParameterRuleService service = new ParameterRuleService();
        service.generateRule(VehicleModelRuleTypeEnum.TEMPERATURE_DIFFERENCE_ALARM, newModel, levelRules);

        Assert.assertEquals(3, levelRules.size());

        service.generateRule(VehicleModelRuleTypeEnum.BATTERY_CONSISTENCY_ALARM, newModel, levelRules);
        Assert.assertEquals(4, levelRules.size());

        //service.generateRule(VehicleModelRuleTypeEnum.DC_DC_TEMPERATURE_ALARM, newModel, levelRules);
        //Assert.assertEquals(5, levelRules.size());

        service.generateRule(VehicleModelRuleTypeEnum.SOC_JUMP_ALARM, newModel, levelRules);
        System.out.println("===");
    }

    /**
     * 测试编辑
     */
    //@SimTest
    //public void 车辆型号通用报警表达式测试() {
    //    ParameterRuleService service = new ParameterRuleService();
    //
    //    {
    //        System.out.println("==================== 测试配置有三级， 二级， 一级 ===================");
    //        String levelValue3 = "30,40";
    //        String levelValue2 = "20,50";
    //        String levelValue1 = "10,60";
    //        FormulaModel formula3 = service.generateFormula(levelValue3, null, null, 3, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula2 = service.generateFormula(levelValue2, null, levelValue3, 2, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula1 = service.generateFormula(levelValue1, levelValue2, levelValue3, 1, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //
    //        System.out.println("三级报警表达式[ " + levelValue3 + " ]：" + formula3.getFormula() + ", 中文描述：" + formula3.getFormulaDesc());
    //        System.out.println("二级报警表达式[ " + levelValue2 + " ]：" + formula2.getFormula() + ", 中文描述：" + formula2.getFormulaDesc());
    //        System.out.println("一级报警表达式[ " + levelValue1 + " ]：" + formula1.getFormula() + ", 中文描述：" + formula1.getFormulaDesc());
    //
    //        ImmutableMap<String, BigDecimal> env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("39"))
    //            .build();
    //
    //        boolean formula3_flag = compute(formula3.getFormula(), env);
    //        boolean formula2_flag = compute(formula2.getFormula(), env);
    //        boolean formula1_flag = compute(formula1.getFormula(), env);
    //
    //        System.out.println("表达式三计算结果：" + formula3_flag + "\td2609为39");
    //        System.out.println("表达式二计算结果：" + formula2_flag + "\td2609为39");
    //        System.out.println("表达式一计算结果：" + formula1_flag + "\td2609为39");
    //
    //        Assert.assertEquals(true, formula3_flag);
    //        Assert.assertEquals(false, formula2_flag);
    //        Assert.assertEquals(false, formula1_flag);
    //    }
    //    {
    //        System.out.println("\n==================== 测试配置有二级， 一级 ===================");
    //        String levelValue3 = ",";
    //        String levelValue2 = "20,50";
    //        String levelValue1 = "10,60";
    //        FormulaModel formula3 = service.generateFormula(levelValue3, null, null, 3, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula2 = service.generateFormula(levelValue2, null, levelValue3, 2, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula1 = service.generateFormula(levelValue1, levelValue2, levelValue3, 1, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //
    //        System.out.println("三级报警表达式[ " + levelValue3 + " ]：" + formula3.getFormula() + ", 中文描述：" + formula3.getFormulaDesc());
    //        System.out.println("二级报警表达式[ " + levelValue2 + " ]：" + formula2.getFormula() + ", 中文描述：" + formula2.getFormulaDesc());
    //        System.out.println("一级报警表达式[ " + levelValue1 + " ]：" + formula1.getFormula() + ", 中文描述：" + formula1.getFormulaDesc());
    //
    //        ImmutableMap<String, BigDecimal> env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("40"))
    //            .build();
    //
    //        boolean formula3_flag = compute(formula3.getFormula(), env);
    //        boolean formula2_flag = compute(formula2.getFormula(), env);
    //        boolean formula1_flag = compute(formula1.getFormula(), env);
    //
    //        System.out.println("表达式三计算结果：" + formula3_flag + "\td2609为40");
    //        System.out.println("表达式二计算结果：" + formula2_flag + "\td2609为40");
    //        System.out.println("表达式一计算结果：" + formula1_flag + "\td2609为40");
    //
    //        Assert.assertEquals(false, formula3_flag);
    //        Assert.assertEquals(true, formula2_flag);
    //        Assert.assertEquals(false, formula1_flag);
    //    }
    //    {
    //        System.out.println("\n==================== 测试配置有一级 ===================");
    //        String levelValue3 = ",";
    //        String levelValue2 = ",";
    //        String levelValue1 = "10,60";
    //        FormulaModel formula3 = service.generateFormula(levelValue3, null, null, 3, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula2 = service.generateFormula(levelValue2, null, levelValue3, 2, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula1 = service.generateFormula(levelValue1, levelValue2, levelValue3, 1, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //
    //        System.out.println("三级报警表达式[ " + levelValue3 + " ]：" + formula3.getFormula() + ", 中文描述：" + formula3.getFormulaDesc());
    //        System.out.println("二级报警表达式[ " + levelValue2 + " ]：" + formula2.getFormula() + ", 中文描述：" + formula2.getFormulaDesc());
    //        System.out.println("一级报警表达式[ " + levelValue1 + " ]：" + formula1.getFormula() + ", 中文描述：" + formula1.getFormulaDesc());
    //
    //        ImmutableMap<String, BigDecimal> env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("40"))
    //            .build();
    //
    //        boolean formula3_flag = compute(formula3.getFormula(), env);
    //        boolean formula2_flag = compute(formula2.getFormula(), env);
    //        boolean formula1_flag = compute(formula1.getFormula(), env);
    //
    //        System.out.println("表达式三计算结果：" + formula3_flag + "\td2609为40");
    //        System.out.println("表达式二计算结果：" + formula2_flag + "\td2609为40");
    //        System.out.println("表达式一计算结果：" + formula1_flag + "\td2609为40");
    //
    //        Assert.assertEquals(false, formula3_flag);
    //        Assert.assertEquals(false, formula2_flag);
    //        Assert.assertEquals(true, formula1_flag);
    //    }
    //    {
    //        System.out.println("\n==================== 测试三种级别都符合 ===================");
    //        String levelValue3 = "30,40";
    //        String levelValue2 = "20,50";
    //        String levelValue1 = "10,60";
    //        FormulaModel formula3 = service.generateFormula(levelValue3, null, null, 3, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula2 = service.generateFormula(levelValue2, null, levelValue3, 2, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula1 = service.generateFormula(levelValue1, levelValue2, levelValue3, 1, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //
    //        System.out.println("三级报警表达式[ " + levelValue3 + " ]：" + formula3.getFormula() + ", 中文描述：" + formula3.getFormulaDesc());
    //        System.out.println("二级报警表达式[ " + levelValue2 + " ]：" + formula2.getFormula() + ", 中文描述：" + formula2.getFormulaDesc());
    //        System.out.println("一级报警表达式[ " + levelValue1 + " ]：" + formula1.getFormula() + ", 中文描述：" + formula1.getFormulaDesc());
    //
    //        ImmutableMap<String, BigDecimal> env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("39"))
    //            .build();
    //
    //        boolean formula3_flag = compute(formula3.getFormula(), env);
    //
    //        env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("25"))
    //            .build();
    //        boolean formula2_flag = compute(formula2.getFormula(), env);
    //
    //        env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("10"))
    //            .build();
    //        boolean formula1_flag = compute(formula1.getFormula(), env);
    //
    //        System.out.println("表达式三计算结果：" + formula3_flag + "\td2609为39");
    //        System.out.println("表达式二计算结果：" + formula2_flag + "\td2609为25");
    //        System.out.println("表达式一计算结果：" + formula1_flag + "\td2609为10");
    //
    //        Assert.assertEquals(true, formula3_flag);
    //        Assert.assertEquals(true, formula2_flag);
    //        Assert.assertEquals(true, formula1_flag);
    //    }
    //    {
    //        System.out.println("\n==================== 测试三种级别都不符合 ===================");
    //        String levelValue3 = "30,40";
    //        String levelValue2 = "20,50";
    //        String levelValue1 = "10,60";
    //        FormulaModel formula3 = service.generateFormula(levelValue3, null, null, 3, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula2 = service.generateFormula(levelValue2, null, levelValue3, 2, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula1 = service.generateFormula(levelValue1, levelValue2, levelValue3, 1, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //
    //        System.out.println("三级报警表达式[ " + levelValue3 + " ]：" + formula3.getFormula() + ", 中文描述：" + formula3.getFormulaDesc());
    //        System.out.println("二级报警表达式[ " + levelValue2 + " ]：" + formula2.getFormula() + ", 中文描述：" + formula2.getFormulaDesc());
    //        System.out.println("一级报警表达式[ " + levelValue1 + " ]：" + formula1.getFormula() + ", 中文描述：" + formula1.getFormulaDesc());
    //
    //        ImmutableMap<String, BigDecimal> env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("70"))
    //            .build();
    //
    //        boolean formula3_flag = compute(formula3.getFormula(), env);
    //        boolean formula2_flag = compute(formula2.getFormula(), env);
    //
    //        env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("70"))
    //            .put("d2612", new BigDecimal("5"))
    //            .build();
    //        boolean formula1_flag = compute(formula1.getFormula(), env);
    //
    //        System.out.println("表达式三计算结果：" + formula3_flag + "\td2609为70");
    //        System.out.println("表达式二计算结果：" + formula2_flag + "\td2609为70");
    //        System.out.println("表达式一计算结果：" + formula1_flag + "\td2609为5");
    //
    //        Assert.assertEquals(false, formula3_flag);
    //        Assert.assertEquals(false, formula2_flag);
    //        Assert.assertEquals(false, formula1_flag);
    //    }
    //
    //}

    //@SimTest
    //public void t5(){
    //    ParameterRuleService service = new ParameterRuleService();
    //    {
    //        System.out.println("\n==================== 分段区间测试 ===================");
    //        String levelValue3 = "10,20";
    //        String levelValue2 = "20,30";
    //        String levelValue1 = "30,40";
    //        FormulaModel formula3 = service.generateFormula(levelValue3, null, null, 3, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula2 = service.generateFormula(levelValue2, null, levelValue3, 2, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula1 = service.generateFormula(levelValue1, levelValue2, levelValue3, 1, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //
    //        System.out.println("三级报警表达式[ " + levelValue3 + " ]：" + formula3.getFormula() + ", 中文描述：" + formula3.getFormulaDesc());
    //        System.out.println("二级报警表达式[ " + levelValue2 + " ]：" + formula2.getFormula() + ", 中文描述：" + formula2.getFormulaDesc());
    //        System.out.println("一级报警表达式[ " + levelValue1 + " ]：" + formula1.getFormula() + ", 中文描述：" + formula1.getFormulaDesc());
    //
    //        ImmutableMap<String, BigDecimal> env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("15"))
    //            .build();
    //        boolean formula3_flag = compute(formula3.getFormula(), env);
    //
    //
    //        env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("25"))
    //            .build();
    //        boolean formula2_flag = compute(formula2.getFormula(), env);
    //
    //        env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("35"))
    //            .build();
    //        boolean formula1_flag = compute(formula1.getFormula(), env);
    //
    //        System.out.println("表达式三计算结果：" + formula3_flag + "\td2609为15");
    //        System.out.println("表达式二计算结果：" + formula2_flag + "\td2609为25");
    //        System.out.println("表达式一计算结果：" + formula1_flag + "\td2609为35");
    //
    //        Assert.assertEquals(true, formula3_flag);
    //        Assert.assertEquals(true, formula2_flag);
    //        Assert.assertEquals(true, formula1_flag);
    //    }
    //
    //    {
    //        System.out.println("\n==================== 分段区间测试2 ===================");
    //        String levelValue1 = "10,20";
    //        String levelValue2 = "20,30";
    //        String levelValue3 = "30,40";
    //        FormulaModel formula3 = service.generateFormula(levelValue3, null, null, 3, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula2 = service.generateFormula(levelValue2, null, levelValue3, 2, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //        FormulaModel formula1 = service.generateFormula(levelValue1, levelValue2, levelValue3, 1, VehicleModelRuleTypeEnum.BATTERY_HIGH_TEMPERATUR_ALARM);
    //
    //        System.out.println("三级报警表达式[ " + levelValue3 + " ]：" + formula3.getFormula() + ", 中文描述：" + formula3.getFormulaDesc());
    //        System.out.println("二级报警表达式[ " + levelValue2 + " ]：" + formula2.getFormula() + ", 中文描述：" + formula2.getFormulaDesc());
    //        System.out.println("一级报警表达式[ " + levelValue1 + " ]：" + formula1.getFormula() + ", 中文描述：" + formula1.getFormulaDesc());
    //
    //        ImmutableMap<String, BigDecimal> env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("35"))
    //            .build();
    //        boolean formula3_flag = compute(formula3.getFormula(), env);
    //
    //
    //        env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("25"))
    //            .build();
    //        boolean formula2_flag = compute(formula2.getFormula(), env);
    //
    //        env = ImmutableMap.<String, BigDecimal>builder()
    //            .put("d2609", new BigDecimal("15"))
    //            .build();
    //        boolean formula1_flag = compute(formula1.getFormula(), env);
    //
    //        System.out.println("表达式三计算结果：" + formula3_flag + "\td2609为35");
    //        System.out.println("表达式二计算结果：" + formula2_flag + "\td2609为25");
    //        System.out.println("表达式一计算结果：" + formula1_flag + "\td2609为15");
    //
    //        Assert.assertEquals(true, formula3_flag);
    //        Assert.assertEquals(true, formula2_flag);
    //        Assert.assertEquals(true, formula1_flag);
    //    }
    //}

    private boolean compute(String formula, ImmutableMap<String, BigDecimal> env) {
        try {
            AviatorEvaluatorInstance aviator = AviatorEvaluator.newInstance();
            Expression expression = aviator.compile(formula);

            final Object result = expression.execute(ImmutableMap.copyOf(env));
            if (result instanceof Boolean) {
                return (Boolean) result;
            }
        } catch (final Exception e) {
            log.trace("表达式计算异常:{}", e.getLocalizedMessage(), e);
        }
        return false;
    }

}
