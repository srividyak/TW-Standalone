package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.EsConfigSettingsDao;
import com.tapwisdom.core.daos.documents.EsConfigSettings;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by srividyak on 12/07/15.
 */
@Component
public class EsConfigSettingsDaoImpl extends BaseDaoImpl<EsConfigSettings> implements EsConfigSettingsDao {
    
    @Override
    public EsConfigSettings getSettings() throws TapWisdomException {
        Query query = new Query();
        List<EsConfigSettings> settingsList = operations.find(query, EsConfigSettings.class);
        return settingsList != null && settingsList.size() > 0 ? settingsList.get(0) : null;
    }

}
