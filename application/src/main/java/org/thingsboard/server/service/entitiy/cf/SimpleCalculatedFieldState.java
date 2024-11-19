/**
 * Copyright © 2016-2024 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.service.entitiy.cf;

import lombok.Data;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.thingsboard.server.common.data.cf.CalculatedFieldConfiguration;
import org.thingsboard.server.common.data.cf.CalculatedFieldType;

import java.util.HashMap;
import java.util.Map;

@Data
public class SimpleCalculatedFieldState implements CalculatedFieldState {

    // TODO: use value object(TsKv) instead of string
    private Map<String, String> arguments = new HashMap<>();
    private String outputResult;

    @Override
    public CalculatedFieldType getType() {
        return CalculatedFieldType.SIMPLE;
    }

    @Override
    public void initState(Map<String, String> argumentValues) {
        this.arguments = argumentValues;
    }

    @Override
    public CalculatedFieldResult performCalculation(CalculatedFieldConfiguration calculatedFieldConfiguration) {
        if (isValid(arguments, calculatedFieldConfiguration)) {
            String expression = calculatedFieldConfiguration.getOutput().getExpression();
            ThreadLocal<Expression> customExpression = new ThreadLocal<>();
            var expr = customExpression.get();
            if (expr == null) {
                expr = new ExpressionBuilder(expression)
                        .implicitMultiplication(true)
                        .variables(arguments.keySet())
                        .build();
                customExpression.set(expr);
            }
            Map<String, Double> variables = new HashMap<>();
            arguments.forEach((k, v) -> variables.put(k, Double.parseDouble(v)));
            expr.setVariables(variables);
            double result = expr.evaluate();
            this.outputResult = Double.toString(result);
        }
        return null;
    }

}
