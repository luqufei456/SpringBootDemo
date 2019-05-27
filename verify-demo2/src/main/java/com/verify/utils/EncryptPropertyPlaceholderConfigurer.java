package com.verify.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 这是c3p0的自动加密配置，应该...
 * 但是我这里是druid，所以也没有配置bean，直接把这个类当成一个中间实体来使用。无视也没事
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static Logger logger = LoggerFactory.getLogger(EncryptPropertyPlaceholderConfigurer.class);
    private String[] encryptPropNames = null;
    private String keySeed = null;

    public EncryptPropertyPlaceholderConfigurer() {
    }

    public void setEncryptPropNames(String[] encryptPropNames) {
        this.encryptPropNames = encryptPropNames;
    }

    public void setKeySeed(String keySeed) {
        this.keySeed = keySeed;
    }

    public String convertProperty(String propertyValue) {
        // logger.info("try decript: property:" + propertyName + ";propertyValue:" + propertyValue);
        return DESUtil.getDecryptString(propertyValue, this.keySeed);

        /*if (this.isEncryptProp(propertyName)) {
            try {
                logger.info("try decript: property:" + propertyName + ";propertyValue:" + propertyValue);
                String decryptValue = DESUtil.getDecryptString(propertyValue, this.keySeed);
                return decryptValue;
            } catch (Throwable var4) {
                logger.info("try decript: property:" + propertyName + ";propertyValue:" + propertyValue + ",keySeed is:" + this.keySeed);
                throw new RuntimeException(var4);
            }
        } else {
            return propertyValue;
        }*/
    }

    private boolean isEncryptProp(String propertyName) {
        String[] var5 = this.encryptPropNames;
        int var4 = this.encryptPropNames.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            String encryptpropertyName = var5[var3];
            if (encryptpropertyName.equals(propertyName)) {
                return true;
            }
        }

        return false;
    }
}