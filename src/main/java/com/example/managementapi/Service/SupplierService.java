package com.example.managementapi.Service;


import com.example.managementapi.Dto.Request.CreateSupplierReq;
import com.example.managementapi.Dto.Request.UpdateSupplierReq;
import com.example.managementapi.Dto.Response.CreateSupplierRes;
import com.example.managementapi.Dto.Response.GetSupplierRes;
import com.example.managementapi.Dto.Response.UpdateSupplierRes;
import com.example.managementapi.Entity.Supplier;
import com.example.managementapi.Mapper.SupplierMapper;
import com.example.managementapi.Repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierService {

    @Autowired
    private final SupplierRepository supplierRepository;

    @Autowired
    private final SupplierMapper supplierMapper;

    public List<GetSupplierRes> getSuppliers(){
        return supplierRepository.findAll().stream()
                .map(supplier -> supplierMapper.toGetSuppliers(supplier)).toList();
    }

    public CreateSupplierRes createSupplier(CreateSupplierReq request){

       var supplier = supplierMapper.toCreateSupplierReq(request);

       supplier = supplierRepository.save(supplier);

        return supplierMapper.toCreateSupplierRes(supplier);
    }

    public UpdateSupplierRes updateSupplier(String supplier_id, UpdateSupplierReq request){

        Supplier supplier = supplierRepository.findById(supplier_id).orElseThrow(()
                -> new RuntimeException("Supplier not found"));

        supplierMapper.toUpdateSupplierReq(supplier, request);

        return supplierMapper.toUpdateSupplierRes(supplierRepository.save(supplier));

    }

    public void deleteSupplier(String supplier_id){
        supplierRepository.deleteById(supplier_id);
    }

}
