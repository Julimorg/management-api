package com.example.managementapi.Service;

import com.cloudinary.Cloudinary;
import com.example.managementapi.Dto.Request.Color.CreateColorReq;
import com.example.managementapi.Dto.Request.Color.UpdateColorReq;
import com.example.managementapi.Dto.Response.Cloudinary.CloudinaryRes;
import com.example.managementapi.Dto.Response.Color.CreateColorRes;
import com.example.managementapi.Dto.Response.Color.GetColorDetailRes;
import com.example.managementapi.Dto.Response.Color.GetColorRes;
import com.example.managementapi.Dto.Response.Color.UpdateColorRes;
import com.example.managementapi.Entity.Color;
import com.example.managementapi.Mapper.ColorMapper;
import com.example.managementapi.Repository.ColorRepository;
import com.example.managementapi.Util.FileUpLoadUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ColorService {
    @Autowired
    private final ColorRepository colorRepository;

    @Autowired
    private final CloudinaryService cloudinaryService;

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
        log.info("Processing createColor request for colorName: {}", request.getColorName());

        MultipartFile image = request.getColorImg();
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            log.info("Uploading image for color: {}", request.getColorName());
            FileUpLoadUtil.assertAllowed(image, FileUpLoadUtil.IMAGE_PATTERN);

            String fileName = FileUpLoadUtil.getFileName(request.getColorName());
            log.info("Generated fileName: {}", fileName);

            CloudinaryRes cloudinaryRes = cloudinaryService.uploadFile(image, fileName);
            imageUrl = cloudinaryRes.getUrl();
            log.info("Uploaded image URL: {}", imageUrl);
        } else {
            log.info("No image provided for color: {}", request.getColorName());
        }

        Color color = colorMapper.toCreateColorReq(request);
        color.setColorImg(imageUrl);
        log.info("Set colorImg URL: {}", imageUrl);

        color = colorRepository.save(color);
        log.info("Saved color with ID: {}", color.getColorId());

        return colorMapper.toCreateColorRes(color);
    }


    public UpdateColorRes updateColor(String colorId, UpdateColorReq request){

        MultipartFile image = request.getColorImg();

        String imageUrl = null;

        if(image != null && !image.isEmpty()) {

        }

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
