package com.tapwisdom.core.misc;

public interface DataTranslator<O, T> {
    
    public T translate(O o);
    
}
