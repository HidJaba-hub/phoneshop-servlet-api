package com.es.phoneshop.service;

import com.es.phoneshop.security.CustomDosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomDosProtectionServiceTest {

    private String ip;
    @InjectMocks
    private CustomDosProtectionService dosProtectionService = CustomDosProtectionService.getInstance();

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(CustomDosProtectionService.class);
        dosProtectionService.getCountMap().clear();
        dosProtectionService.getTimeMap().clear();
        ip = "111";
    }

    @Test
    public void givenRequest_whenIsAllowed_thenGetTrue() {
        boolean protectionServiceAllowed = dosProtectionService.isAllowed(ip);

        assertTrue(protectionServiceAllowed);
        assertEquals(1, dosProtectionService.getCountMap().size());
    }

    @Test
    public void givenMoreThanAllowedReq_whenIsAllowed_thenGetFalse() {
        dosProtectionService.getCountMap().put(ip, dosProtectionService.getMaxRequestCount() + 1);
        boolean protectionServiceAllowed = dosProtectionService.isAllowed(ip);

        assertFalse(protectionServiceAllowed);
    }

    @Test
    public void givenMoreThanAllowedTime_whenIsAllowed_thenGetTrue() {
        dosProtectionService.getTimeMap().put(ip, System.currentTimeMillis() - 60000);
        boolean protectionServiceAllowed = dosProtectionService.isAllowed(ip);

        assertTrue(protectionServiceAllowed);
        assertEquals(1, dosProtectionService.getCountMap().size());
    }
}
