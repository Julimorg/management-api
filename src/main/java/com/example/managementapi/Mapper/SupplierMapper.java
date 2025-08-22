package com.example.managementapi.Mapper;


import com.example.managementapi.Dto.Request.Supplier.CreateSupplierReq;
import com.example.managementapi.Dto.Request.Supplier.UpdateSupplierReq;
import com.example.managementapi.Dto.Response.Supplier.CreateSupplierRes;
import com.example.managementapi.Dto.Response.Supplier.GetSupplierDetailRes;
import com.example.managementapi.Dto.Response.Supplier.GetSupplierRes;
import com.example.managementapi.Dto.Response.Supplier.UpdateSupplierRes;
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

    GetSupplierDetailRes toGetSupplierDetailRes(Supplier supplier);


}
