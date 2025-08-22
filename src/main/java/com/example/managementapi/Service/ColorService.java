package com.example.managementapi.Service;

import com.example.managementapi.Dto.Request.Color.CreateColorReq;
import com.example.managementapi.Dto.Request.Color.UpdateColorReq;
import com.example.managementapi.Dto.Response.Color.CreateColorRes;
import com.example.managementapi.Dto.Response.Color.GetColorDetailRes;
import com.example.managementapi.Dto.Response.Color.GetColorRes;
import com.example.managementapi.Dto.Response.Color.UpdateColorRes;
import com.example.managementapi.Entity.Color;
import com.example.managementapi.Mapper.ColorMapper;
import com.example.managementapi.Repository.ColorRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ColorService {
    @Autowired
    private final ColorRepository colorRepository;

    @Autowired
    private final ColorMapper colorMapper;

    public List<GetColorRes> getColor(){
        return colorRepository
                .findAll()
                .stream()
                .map(color -> colorMapper.toGetColorRes(color))
                .toList();
    }
    public GetColorDetailRes getColorDetail(String colorId){
        return colorMapper
                .toGetColorDetailRes(colorRepository
                        .findById(colorId)
                        .orElseThrow(() -> new RuntimeException("Color not found!")));
    }
    public CreateColorRes createColor(CreateColorReq request){
        var color = colorMapper.toCreateColorReq(request);

        color = colorRepository.save(color);

        return colorMapper.toCreateColorRes(color);
    }

    public UpdateColorRes updateColor(String colorId, UpdateColorReq request){

        Color color = colorRepository
                .findById(colorId)
                .orElseThrow(() -> new RuntimeException("Color not Found!"));

        colorMapper.toUpdateColor(color, request);

        return colorMapper.toUpdateColorRes(colorRepository.save(color));
    }

    public void deleteColor(String colorId){
        if(!colorRepository.existsById(colorId)){
            throw new RuntimeException("Color not Found!");
        }
        colorRepository.deleteById(colorId);
    }
}
