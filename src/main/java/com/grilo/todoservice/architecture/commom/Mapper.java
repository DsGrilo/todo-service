package com.grilo.todoservice.architecture.commom;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final ModelMapper modelMapper;

    public <D> D convert(Object source, Class<D> target) {
        if (source == null) {
            return null;
        }
        return modelMapper.map(source, target);
    }

    public <D> D convertStrict(Object source, Class<D> target) {
        if (source == null) {
            return null;
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(source, target);
    }

    public <D> D convertStrict(Object source, Type target) {
        if (source == null) {
            return null;
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(source, target);

    }

    public <D> D convertLazyLoading(Object source, Class<D> target) {
        if (source == null) {
            return null;
        }
        return modelMapper.map(source, target);
    }

    public <D> D convertStrictLazyLoading(Object source, Class<D> target) {
        if (source == null) {
            return null;
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(source, target);
    }

    public <D> D convertListStrictLazyLoading(Object source, Type target) {
        if (source == null) {
            return null;
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(source, target);
    }

}
