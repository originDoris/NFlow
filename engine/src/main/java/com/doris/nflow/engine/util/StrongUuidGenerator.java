package com.doris.nflow.engine.util;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

/**
 * @author: xhz
 * @Title: StrongUuidGenerator
 * @Description:
 * @date: 2022/9/30 11:33
 */
public class StrongUuidGenerator implements IdGenerator{
    private static volatile TimeBasedGenerator timeBasedGenerator;

    public StrongUuidGenerator() {
        initGenerator();
    }

    private void initGenerator() {
        if (timeBasedGenerator == null) {
            synchronized (StrongUuidGenerator.class) {
                if (timeBasedGenerator == null) {
                    timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
                }
            }
        }
    }

    @Override
    public String getNextId() {
        return timeBasedGenerator.generate().toString();
    }
}
