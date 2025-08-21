package com.example.managementapi.Mapper;


import com.example.managementapi.Dto.Request.CreateSupplierReq;
import com.example.managementapi.Dto.Request.UpdateSupplierReq;
import com.example.managementapi.Dto.Response.CreateSupplierRes;
import com.example.managementapi.Dto.Response.GetSupplierRes;
import com.example.managementapi.Dto.Response.UpdateSupplierRes;
import com.example.managementapi.Entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {


    Supplier toCreateSupplierReq(CreateSupplierReq request);

    CreateSupplierRes toCreateSupplierRes(Supplier supplier);

    void toUpdateSupplierReq(@MappingTarget Supplier supplier, UpdateSupplierReq request);

    UpdateSupplierRes toUpdateSupplierRes(Supplier supplier);

    GetSupplierRes toGetSuppliers(Supplier supplier);


}
