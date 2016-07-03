package com.arvernistudio.wolfengine.mapper;

import com.arvernistudio.wolfengine.component.Component;

public interface ComponentMapper {
    Integer getComponentIndex(Class<? extends Component> clazz);
}
