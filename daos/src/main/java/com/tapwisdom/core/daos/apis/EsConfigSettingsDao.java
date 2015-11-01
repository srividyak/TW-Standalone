package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.EsConfigSettings;

/**
 * Created by srividyak on 12/07/15.
 */
public interface EsConfigSettingsDao extends BaseDao<EsConfigSettings> {
    
    public EsConfigSettings getSettings() throws TapWisdomException;

}
