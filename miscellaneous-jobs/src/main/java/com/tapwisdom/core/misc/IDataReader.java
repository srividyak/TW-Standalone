package com.tapwisdom.core.misc;

import java.util.List;

public interface IDataReader<O> {

    public void loadData();

    public List<O> getDataList();
    
}
