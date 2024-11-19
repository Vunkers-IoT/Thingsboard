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
import org.thingsboard.server.common.data.cf.CalculatedFieldConfiguration;
import org.thingsboard.server.common.data.cf.CalculatedFieldType;

import java.util.HashMap;
import java.util.Map;

@Data
public class SimpleCalculatedFieldState implements CalculatedFieldState {

    // TODO: use value object(TsKv) instead of string
    Map<String, String> arguments = new HashMap<>();
    String result;

    @Override
    public CalculatedFieldType getType() {
        return CalculatedFieldType.SIMPLE;
    }

    @Override
    public void performCalculation(Map<String, String> argumentValues, CalculatedFieldConfiguration calculatedFieldConfiguration, boolean initialCalculation) {
        if (initialCalculation) {
            // todo: perform initial calculation
            this.arguments = argumentValues;
        } else {
            // todo: perform calculation based on previous data
            this.arguments.putAll(argumentValues);
        }
        this.result = "result";
    }

}
